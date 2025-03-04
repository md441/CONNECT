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
package gov.hhs.fha.nhinc.auditrepository.nhinc;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mflynn02
 */
@WebService(serviceName = "AuditRepositoryManagerService", portName = "AuditRepositoryManagerPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository", wsdlLocation = "WEB-INF/wsdl/AuditRepositoryManagerService/NhincComponentAuditRepository.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AuditRepositoryUnsecured {
    @Resource
    private WebServiceContext context;

    protected AuditRepositoryUnsecuredImpl getAuditRepositoryUnsecuredImpl() {
        return new AuditRepositoryUnsecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(
            gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest) {
        // TODO implement this method
        return getAuditRepositoryUnsecuredImpl().queryAuditEvents(queryAuditEventsRequest, getWebServiceContext());
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEvent(
            gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType logEventRequest) {
        // TODO implement this method
        return getAuditRepositoryUnsecuredImpl().logEvent(logEventRequest, getWebServiceContext());
    }

}
