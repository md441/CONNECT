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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Doc Query implementation of OutboundOrchestratableMessage
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestratable implements OutboundOrchestratableMessage {

    private OutboundDelegate delegate = null;
    private OutboundResponseProcessor processor = null;
    private AuditTransformer auditTransformer = null;
    private PolicyTransformer policyTransformer = null;
    private AssertionType assertion = null;
    private String serviceName = null;

    private NhinTargetSystemType target = null;
    private AdhocQueryRequest request = null;

    private AdhocQueryResponse response = null;

    public OutboundDocQueryOrchestratable() {
    }

    public OutboundDocQueryOrchestratable(OutboundDelegate delegate) {
        this.delegate = delegate;
    }

    public OutboundDocQueryOrchestratable(OutboundDelegate d, OutboundResponseProcessor p, AuditTransformer at,
            PolicyTransformer pt, AssertionType a, String name, NhinTargetSystemType t, AdhocQueryRequest r) {

        this.delegate = d;
        this.processor = p;
        this.auditTransformer = at;
        this.policyTransformer = pt;
        this.assertion = a;
        this.serviceName = name;

        this.target = t;
        this.request = r;
    }

    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    public void setRequest(AdhocQueryRequest request) {
        this.request = request;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    public AdhocQueryResponse getResponse() {
        return response;
    }

    public void setResponse(AdhocQueryResponse r) {
        response = r;
    }

    public OutboundDelegate getDelegate() {
        return delegate;
    }

    // NOT USED.......use getResponseProcessor instead
    public NhinAggregator getAggregator() {
        return null;
    }

    public OutboundResponseProcessor getResponseProcessor() {
        return processor;
    }

    public AuditTransformer getAuditTransformer() {
        return auditTransformer;
    }

    public PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    public AssertionType getAssertion() {
        return assertion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public AdhocQueryRequest getRequest() {
        return request;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isPassthru() {
        return false;
    }

}
