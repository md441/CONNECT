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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author richard.ettema
 */
@WebService(serviceName = "DeferredQueueManager", portName = "DeferredQueueManagerPort", endpointInterface = "gov.hhs.fha.nhinc.deferredqueuemanager.DeferredQueueManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:deferredqueuemanager", wsdlLocation = "WEB-INF/wsdl/DeferredQueueManager/DeferredQueueManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true, required = true)
public class DeferredQueueManager {

    private static Log log = LogFactory.getLog(DeferredQueueManager.class);

    @Resource
    private WebServiceContext context;

    /**
     * Default constructor.
     */
    public DeferredQueueManager() {

        try {
            DeferredQueueTimer.startTimer();
        } catch (Exception e) {
            String sErrorMessage = "Failed to start DeferredQueueManager's timer.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    /**
     * Force the deferred queue process
     * 
     * @param deferredQueueManagerForceProcessRequest
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(
            DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        return new DeferredQueueManagerHelper().forceProcessOnDeferredQueue(deferredQueueManagerForceProcessRequest,
                context);
    }

    /**
     * Force the deferred queue process on a specific request
     * 
     * @param deferredQueueManagerForceProcessRequest
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredRequest(
            DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        return new DeferredQueueManagerHelper().forceProcessOnDeferredQueue(deferredQueueManagerForceProcessRequest,
                context);
    }

    /**
     * Query for deferred queue records based on passed criteria
     * 
     * @param queryDeferredQueueRequest
     * @return queryDeferredQueueResponse
     */
    public QueryDeferredQueueResponseType queryDeferredQueue(QueryDeferredQueueRequestType queryDeferredQueueRequest) {
        return new DeferredQueueManagerHelper().queryDeferredQueue(queryDeferredQueueRequest, context);
    }

    /**
     * Retrieve a deferred queue record based on passed criteria
     * 
     * @param retrieveDeferredQueueRequest
     * @return retrieveDeferredQueueResponse
     */
    public RetrieveDeferredQueueResponseType retrieveDeferredQueue(
            RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest) {
        return new DeferredQueueManagerHelper().retrieveDeferredQueue(retrieveDeferredQueueRequest, context);
    }

    /**
     * Retrieve deferred queue statistics based on passed criteria
     * 
     * @param deferredQueueStatisticsRequest
     * @return deferredQueueStatisticsResponse
     */
    public DeferredQueueStatisticsResponseType deferredQueueStatistics(
            DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest) {
        return new DeferredQueueManagerHelper().deferredQueueStatistics(deferredQueueStatisticsRequest, context);
    }

}
