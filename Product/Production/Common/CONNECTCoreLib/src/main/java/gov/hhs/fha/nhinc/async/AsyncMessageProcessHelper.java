/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 * This class provides methods to manage the async message record during its lifecycle.
 * 
 * @author richard.ettema
 */
public class AsyncMessageProcessHelper {

    private Log log = null;

    private static HashMap<String, String> statusToDirectionMap = new HashMap<String, String>();

    static {
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENT, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENTACK, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVD, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDACK, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);

        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENT, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSELECT, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
    }

    public AsyncMessageProcessHelper() {
        log = createLogger();
    }

    /**
     * Create a logger object.
     * 
     * @return The logger object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Creates a AsyncMsgRecordDao.
     * 
     * @return an instance of AsyncMsgRecordDao
     */
    protected AsyncMsgRecordDao createAsyncMsgRecordDao() {
        return new AsyncMsgRecordDao();
    }

    /**
     * Used to add the Deferred Patient Discovery Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     * 
     * @param request
     * @param assertion
     * @param direction
     * @return true - success; false - error
     */
    public boolean addPatientDiscoveryRequest(PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        log.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest(assertion)...");

        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(request);

        log.debug("End AsyncMessageProcessHelper.addPatientDiscoveryRequest(assertion)...");

        return addPatientDiscoveryRequest(newRequest, direction);
    }

    /**
     * Used to add the Deferred Patient Discovery Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     * 
     * @param request
     * @param direction
     * @return true - success; false - error
     */
    public boolean addPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, String direction) {
        log.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        boolean result = false;

        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            rec.setDirection(direction);
            rec.setCommunityId(getPatientDiscoveryMessageCommunityId(request, direction));
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
            rec.setResponseType(AsyncMsgRecordDao.QUEUE_RESPONSE_TYPE_AUTO);
            rec.setMsgData(getBlobFromPRPAIN201305UV02RequestType(request));

            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        log.debug("End AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        return result;
    }

    /**
     * Used to add the Deferred Query For Documents Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     * 
     * @param request
     * @param assertion
     * @param direction
     * @param communityId
     * @return true - success; false - error
     */
    public boolean addQueryForDocumentsRequest(AdhocQueryRequest request, AssertionType assertion, String direction,
            String communityId) {
        log.debug("Begin AsyncMessageProcessHelper.addQueryForDocumentsRequest(assertion)...");

        RespondingGatewayCrossGatewayQueryRequestType newRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        newRequest.setAssertion(assertion);
        newRequest.setAdhocQueryRequest(request);

        log.debug("End AsyncMessageProcessHelper.addQueryForDocumentsRequest(assertion)...");

        return addQueryForDocumentsRequest(newRequest, direction, communityId);
    }

    /**
     * Used to add the Deferred Query For Documents Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     * 
     * @param request
     * @param direction
     * @param communityId
     * @return true - success; false - error
     */
    public boolean addQueryForDocumentsRequest(RespondingGatewayCrossGatewayQueryRequestType request, String direction,
            String communityId) {
        log.debug("Begin AsyncMessageProcessHelper.addQueryForDocumentsRequest()...");

        boolean result = false;

        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.DOC_QUERY_SERVICE_NAME);
            rec.setDirection(direction);
            rec.setCommunityId(communityId);
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
            rec.setResponseType(AsyncMsgRecordDao.QUEUE_RESPONSE_TYPE_AUTO);
            rec.setMsgData(getBlobFromRespondingGatewayCrossGatewayQueryRequestType(request));

            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        log.debug("End AsyncMessageProcessHelper.addQueryForDocumentsRequest()...");

        return result;
    }

    /**
     * Used to add the Deferred Retrieve Documents Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     * 
     * @param request
     * @param direction
     * @param communityId
     * @return true - success; false - error
     */
    public boolean addRetrieveDocumentsRequest(RespondingGatewayCrossGatewayRetrieveRequestType request,
            String direction, String communityId) {
        log.debug("Begin AsyncMessageProcessHelper.addRetrieveDocumentsRequest()...");

        boolean result = false;

        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
            rec.setDirection(direction);
            rec.setCommunityId(communityId);
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
            rec.setResponseType(AsyncMsgRecordDao.QUEUE_RESPONSE_TYPE_AUTO);
            rec.setMsgData(getBlobFromRespondingGatewayCrossGatewayRetrieveRequestType(request));

            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        log.debug("End AsyncMessageProcessHelper.addRetrieveDocumentsRequest()...");

        return result;
    }

    /**
     * Process an acknowledgement for a Deferred Patient Discovery asyncmsgs record.
     * 
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processAck(String messageId, String newStatus, String errorStatus, MCCIIN000002UV01 ack) {
        log.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            String direction = getInitialDirectionFromStatus(newStatus);
            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromMCCIIN000002UV01(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processAck()...");

        return result;
    }

    /**
     * Process an acknowledgement for a Deferred Query For Documents asyncmsgs record
     * 
     * @param messageId
     * @param direction
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processAck(String messageId, String newStatus, String errorStatus, DocQueryAcknowledgementType ack) {
        log.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromDocQueryAcknowledgementType(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processAck()...");

        return result;
    }

    /**
     * Process an acknowledgement for a Deferred Retrieve Documents asyncmsgs record
     * 
     * @param messageId
     * @param direction
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processAck(String messageId, String newStatus, String errorStatus, DocRetrieveAcknowledgementType ack) {
        log.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromDocRetrieveAcknowledgementType(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processAck()...");

        return result;
    }

    /**
     * Process the new status for the asyncmsgs record
     * 
     * @param messageId
     * @param newStatus
     * @return true - success; false - error
     */
    public boolean processMessageStatus(String messageId, String newStatus) {
        log.debug("Begin AsyncMessageProcessHelper.processMessageStatus()...");

        boolean result = false;

        try {
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request status.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processMessageStatus()...");

        return result;
    }

    /**
     * Process an acknowledgement error for the Patient Discovery asyncmsgs record
     * 
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processPatientDiscoveryResponse(String messageId, String newStatus, String errorStatus,
            RespondingGatewayPRPAIN201306UV02RequestType response) {
        log.debug("Begin AsyncMessageProcessHelper.processPatientDiscoveryResponse()...");

        boolean result = false;

        try {
            if (response == null) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setResponseTime(new Date());

                // Calculate the duration in milliseconds
                Long duration = null;
                duration = records.get(0).getResponseTime().getTime() - records.get(0).getCreationTime().getTime();
                records.get(0).setDuration(duration);

                records.get(0).setStatus(newStatus);
                records.get(0).setRspData(getBlobFromPRPAIN201306UV02RequestType(response));
                instance.save(records.get(0));

                // Success if we got this far
                result = true;
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async response.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processPatientDiscoveryResponse()...");

        return result;
    }

    /**
     * Process an acknowledgement error for the Query For Documents asyncmsgs record
     * 
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processQueryForDocumentsResponse(String messageId, String newStatus, String errorStatus,
            RespondingGatewayCrossGatewayQueryResponseType response) {
        log.debug("Begin AsyncMessageProcessHelper.processQueryForDocumentsResponse()...");

        boolean result = false;

        try {
            if (response == null) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setResponseTime(new Date());

                // Calculate the duration in milliseconds
                Long duration = null;
                duration = records.get(0).getResponseTime().getTime() - records.get(0).getCreationTime().getTime();
                records.get(0).setDuration(duration);

                records.get(0).setStatus(newStatus);
                records.get(0).setRspData(getBlobFromRespondingGatewayCrossGatewayQueryResponseType(response));
                instance.save(records.get(0));

                // Success if we got this far
                result = true;
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async response.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processQueryForDocumentsResponse()...");

        return result;
    }

    /**
     * Process an acknowledgement error for the Retrieve Documents asyncmsgs record
     * 
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processRetrieveDocumentsResponse(String messageId, String newStatus, String errorStatus,
            RespondingGatewayCrossGatewayRetrieveResponseType response) {
        log.debug("Begin AsyncMessageProcessHelper.processQueryForDocumentsResponse()...");

        boolean result = false;

        try {
            if (response == null) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setResponseTime(new Date());

                // Calculate the duration in milliseconds
                Long duration = null;
                duration = records.get(0).getResponseTime().getTime() - records.get(0).getCreationTime().getTime();
                records.get(0).setDuration(duration);

                records.get(0).setStatus(newStatus);
                records.get(0).setRspData(getBlobFromRespondingGatewayCrossGatewayRetrieveResponseType(response));
                instance.save(records.get(0));

                // Success if we got this far
                result = true;
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async response.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processQueryForDocumentsResponse()...");

        return result;
    }

    /**
     * Copy the original RetrieveDocumentSetRequestType using JAXB
     * 
     * @param orig
     * @return copy of RetrieveDocumentSetRequestType
     */
    public RetrieveDocumentSetRequestType copyRetrieveDocumentSetRequestTypeObject(RetrieveDocumentSetRequestType orig) {
        RetrieveDocumentSetRequestType copy = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            JAXBElement<RetrieveDocumentSetRequestType> oJaxbElement = factory.createRetrieveDocumentSetRequest(orig);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ByteArrayInputStream baInStrm = new ByteArrayInputStream(buffer);
            JAXBElement<RetrieveDocumentSetRequestType> oJaxbElementCopy = (JAXBElement<RetrieveDocumentSetRequestType>) unmarshaller
                    .unmarshal(baInStrm);
            copy = oJaxbElementCopy.getValue();
            // asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during RetrieveDocumentSetRequestType conversion :" + e, e);
        }

        return copy;
    }

    /**
     * Copy the original RetrieveDocumentSetResponseType using JAXB
     * 
     * @param orig
     * @return copy of RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType copyRetrieveDocumentSetResponseTypeObject(
            RetrieveDocumentSetResponseType orig) {
        RetrieveDocumentSetResponseType copy = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            JAXBElement<RetrieveDocumentSetResponseType> oJaxbElement = factory.createRetrieveDocumentSetResponse(orig);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ByteArrayInputStream baInStrm = new ByteArrayInputStream(buffer);
            JAXBElement<RetrieveDocumentSetResponseType> oJaxbElementCopy = (JAXBElement<RetrieveDocumentSetResponseType>) unmarshaller
                    .unmarshal(baInStrm);
            copy = oJaxbElementCopy.getValue();
            // asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during copyRetrieveDocumentSetResponseTypeObject conversion :" + e, e);
        }

        return copy;
    }

    /**
     * Copy the original AssertionType using JAXB
     * 
     * @param orig
     * @return copy of AssertionType
     */
    public AssertionType copyAssertionTypeObject(AssertionType orig) {
        AssertionType copy = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(orig);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ByteArrayInputStream baInStrm = new ByteArrayInputStream(buffer);
            JAXBElement<AssertionType> oJaxbElementCopy = (JAXBElement<AssertionType>) unmarshaller.unmarshal(baInStrm);
            copy = oJaxbElementCopy.getValue();
            // asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during copyAssertionTypeObject conversion :" + e, e);
        }

        return copy;
    }

    /**
     * Marshal AssertionType using JAXB
     * 
     * @param orig
     * @return copy of AssertionType
     */
    public String marshalAssertionTypeObject(AssertionType assertion) {
        String returnValue = "";

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(assertion);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            returnValue = new String(buffer);
        } catch (Exception e) {
            log.error("Exception during marshalAssertionTypeObject conversion :" + e, e);
        }

        return returnValue;
    }

    private static String getInitialDirectionFromStatus(String status) {
        String direction = null;

        if (statusToDirectionMap.containsKey(status)) {
            direction = statusToDirectionMap.get(status);
        }

        return direction;
    }

    private Blob getBlobFromMCCIIN000002UV01(MCCIIN000002UV01 ack) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            PIXConsumerMCCIIN000002UV01RequestType request = factory.createPIXConsumerMCCIIN000002UV01RequestType();
            request.setMCCIIN000002UV01(ack);
            JAXBElement<PIXConsumerMCCIIN000002UV01RequestType> oJaxbElement = factory
                    .createPIXConsumerMCCIIN000002UV01Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201305UV02RequestType(RespondingGatewayPRPAIN201305UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201305UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201306UV02RequestType(RespondingGatewayPRPAIN201306UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            JAXBElement<RespondingGatewayPRPAIN201306UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201306UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromRespondingGatewayCrossGatewayQueryRequestType(
            RespondingGatewayCrossGatewayQueryRequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayQueryRequestType> oJaxbElement = factory
                    .createRespondingGatewayCrossGatewayQueryRequest(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromRespondingGatewayCrossGatewayQueryResponseType(
            RespondingGatewayCrossGatewayQueryResponseType response) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayQueryResponseType> oJaxbElement = factory
                    .createRespondingGatewayCrossGatewayQueryResponse(response);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromRespondingGatewayCrossGatewayRetrieveRequestType(
            RespondingGatewayCrossGatewayRetrieveRequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayRetrieveRequestType> oJaxbElement = factory
                    .createRespondingGatewayCrossGatewayRetrieveRequest(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromRespondingGatewayCrossGatewayRetrieveResponseType(
            RespondingGatewayCrossGatewayRetrieveResponseType response) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayRetrieveResponseType> oJaxbElement = factory
                    .createRespondingGatewayCrossGatewayRetrieveResponse(response);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromDocQueryAcknowledgementType(DocQueryAcknowledgementType ack) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.healthit.nhin.ObjectFactory factory = new gov.hhs.healthit.nhin.ObjectFactory();
            JAXBElement<DocQueryAcknowledgementType> oJaxbElement = factory.createDocQueryAcknowledgement(ack);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromDocRetrieveAcknowledgementType(DocRetrieveAcknowledgementType ack) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.healthit.nhin.ObjectFactory factory = new gov.hhs.healthit.nhin.ObjectFactory();
            JAXBElement<DocRetrieveAcknowledgementType> oJaxbElement = factory.createDocRetrieveAcknowledgement(ack);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private boolean isAckError(MCCIIN000002UV01 ack) {
        boolean result = false;

        if (ack != null && ack.getAcknowledgement() != null && ack.getAcknowledgement().size() > 0
                && ack.getAcknowledgement().get(0).getTypeCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode().equals(HL7AckTransforms.ACK_TYPE_CODE_ERROR)) {
            result = true;
        }

        return result;
    }

    private boolean isAckError(DocQueryAcknowledgementType ack) {
        boolean result = true;

        if (ack != null
                && ack.getMessage() != null
                && ack.getMessage().getStatus() != null
                && (ack.getMessage().getStatus().equals(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG) || ack
                        .getMessage().getStatus().equals(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG))) {
            result = false;
        }

        return result;
    }

    private boolean isAckError(DocRetrieveAcknowledgementType ack) {
        boolean result = true;

        if (ack != null
                && ack.getMessage() != null
                && ack.getMessage().getStatus() != null
                && (ack.getMessage().getStatus().equals(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG) || ack
                        .getMessage().getStatus().equals(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG))) {
            result = false;
        }

        return result;
    }

    /**
     * Get the home community id of the communicating gateway
     * 
     * @param requestMessage
     * @param direction
     * @return String
     */
    private String getPatientDiscoveryMessageCommunityId(RespondingGatewayPRPAIN201305UV02RequestType requestMessage,
            String direction) {
        String communityId = "";
        boolean useReceiver = false;

        if (requestMessage != null && direction != null) {
            if (direction.equals(AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND)) {
                useReceiver = true;
            }

            if (useReceiver) {
                if (requestMessage.getPRPAIN201305UV02() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                            .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.equals("")) {
                    if (requestMessage.getPRPAIN201305UV02().getReceiver() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0)
                                    .getRoot() != null) {
                        communityId = requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId()
                                .get(0).getRoot();
                    }
                }
            } else {
                if (requestMessage.getPRPAIN201305UV02().getSender() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                            .getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.equals("")) {
                    if (requestMessage.getPRPAIN201305UV02().getSender() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0).getRoot() != null) {
                        communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0)
                                .getRoot();
                    }
                }
            }
        }

        return communityId;
    }

}
