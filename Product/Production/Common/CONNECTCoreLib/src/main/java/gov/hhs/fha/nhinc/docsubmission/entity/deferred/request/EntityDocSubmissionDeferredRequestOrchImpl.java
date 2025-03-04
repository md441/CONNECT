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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EntityDocSubmissionDeferredRequestOrchImpl {
    private static Log log = LogFactory.getLog(EntityDocSubmissionDeferredRequestOrchImpl.class);
    private XDRAuditLogger auditLogger = null;

    public EntityDocSubmissionDeferredRequestOrchImpl() {
        log = getLogger();
        auditLogger = getXDRAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(
            ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
            NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        XDRAcknowledgementType response = null;

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType internalRequest = createRequestForInternalProcessing(
                request, assertion, targets, urlInfo);

        auditRequestFromAdapter(internalRequest, assertion);

        if (isPolicyValid(internalRequest, assertion)) {
            log.info("Policy check successful");
            response = getResponseFromTarget(internalRequest, assertion);
        } else {
            log.error("Failed policy check.  Sending error response.");
            response = createFailedPolicyCheckResponse();
        }

        auditResponseToAdapter(response, assertion);

        return response;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
            ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, NhinTargetCommunitiesType targets,
            UrlInfoType urlInfo) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        request.setProvideAndRegisterDocumentSetRequest(msg);
        request.setNhinTargetCommunities(targets);
        request.setUrl(urlInfo);

        return request;
    }
    
    private boolean isPolicyValid(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion) {
        boolean isValid = false;

        if (hasNhinTargetHomeCommunityId(request)) {

            String senderHCID = getSubjectHelper().determineSendingHomeCommunityId(assertion.getHomeCommunity(),
                    assertion);
            String receiverHCID = getNhinTargetHomeCommunityId(request);
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;

            isValid = getXDRPolicyChecker().checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(),
                    assertion, senderHCID, receiverHCID, direction);
        } else {
            log.warn("Check on policy requires a non null target home community ID specified in the request");
        }
        log.debug("Check on policy returns: " + isValid);

        return isValid;
    }
    
    private XDRAcknowledgementType getResponseFromTarget(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {

        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest = createRequestForNhin(request);

        return sendToNhinProxy(nhinRequest, assertion);
    }

    private gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForNhin(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        
        NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
        targetSystemType.setHomeCommunity(getNhinTargetHomeCommunity(request));
        
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        nhinRequest.setNhinTargetSystem(targetSystemType);
        nhinRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());

        return nhinRequest;
    }
    
    private XDRAcknowledgementType sendToNhinProxy(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion) {

        OutboundDocSubmissionDeferredRequestDelegate delegate = getOutboundDocSubmissionDeferredRequestDelegate();
        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = createOrchestratable(delegate, request, assertion);             
        return ((OutboundDocSubmissionDeferredRequestOrchestratable) delegate.process(orchestratable)).getResponse();
    }

    private OutboundDocSubmissionDeferredRequestOrchestratable createOrchestratable(OutboundDocSubmissionDeferredRequestDelegate delegate, 
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        
        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = new OutboundDocSubmissionDeferredRequestOrchestratable(
                delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request.getProvideAndRegisterDocumentSetRequest());
        orchestratable.setTarget(request.getNhinTargetSystem());
        
        return orchestratable;
    }

    private XDRAcknowledgementType createFailedPolicyCheckResponse() {
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResp);

        return response;
    }
    
    private void auditRequestFromAdapter(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion) {
        auditLogger.auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToAdapter(XDRAcknowledgementType response, AssertionType assertion) {
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.XDR_REQUEST_ACTION);
    }

    private HomeCommunityType getNhinTargetHomeCommunity(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity();
    }
    
    private String getNhinTargetHomeCommunityId(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return getNhinTargetHomeCommunity(request).getHomeCommunityId();
    }

    protected boolean hasNhinTargetHomeCommunityId(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {

        if (request != null
                && request.getNhinTargetCommunities() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0)
                        .getHomeCommunity().getHomeCommunityId())) {
            return true;
        }

        return false;
    }

    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log getLogger() {
        return log;
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected SubjectHelper getSubjectHelper() {
        return new SubjectHelper();
    }

    protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
        return new OutboundDocSubmissionDeferredRequestDelegate();
    }
}
