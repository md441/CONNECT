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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;

import gov.hhs.fha.nhinc.patientdiscovery._10.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl;

@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncReq", portName = "NhincProxyPatientDiscoveryAsyncReqPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncreq.NhincProxyPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryDeferredRequestUnsecured/NhincProxyPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class NhincProxyPatientDiscoveryDeferredRequestUnsecured extends PatientDiscoveryBase {

    @Resource
    private WebServiceContext context;

    public NhincProxyPatientDiscoveryDeferredRequestUnsecured() {
        super();
    }

    public NhincProxyPatientDiscoveryDeferredRequestUnsecured(PatientDiscoveryServiceFactory serviceFactory) {
        super(serviceFactory);
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType request) {
        MCCIIN000002UV01 response = null;

        NhincProxyPatientDiscoveryDeferredRequestImpl serviceImpl = getServiceFactory()
                .getNhincProxyPatientDiscoveryDeferredRequestImpl();
        if (serviceImpl != null) {
            response = serviceImpl.processPatientDiscoveryAsyncRequestUnsecured(request, getWebServiceContext());
        }
        return response;
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }
}
