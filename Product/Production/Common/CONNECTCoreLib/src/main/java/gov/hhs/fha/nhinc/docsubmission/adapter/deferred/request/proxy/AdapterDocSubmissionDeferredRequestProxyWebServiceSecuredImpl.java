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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Visu Patlolla
 */
public class AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl implements
        AdapterDocSubmissionDeferredRequestProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterxdrrequestsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterXDRRequestSecured_Service";
    private static final String PORT_LOCAL_PART = "AdapterXDRRequestSecured_Port_Soap";
    private static final String WSDL_FILE = "AdapterXDRRequestSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "tns:XDRRequestInput";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(this.getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    protected AdapterXDRRequestSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        AdapterXDRRequestSecuredPortType port = null;

        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterXDRRequestSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,
                    NhincConstants.XDR_REQUEST_ACTION, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
            ProvideAndRegisterDocumentSetRequestType request, String url, AssertionType assertion) {
        log.debug("Begin AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBRequest");
        XDRAcknowledgementType response = null;
        String serviceName = NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME;

        try {
            log.debug("Before target system destination URL look up.");
            String destURL = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + destURL);

            if (NullChecker.isNotNullish(destURL)) {
                AdapterProvideAndRegisterDocumentSetSecuredRequestType wsRequest = new AdapterProvideAndRegisterDocumentSetSecuredRequestType();
                wsRequest.setProvideAndRegisterDocumentSetRequest(request);
                wsRequest.setUrl(url);
                AdapterXDRRequestSecuredPortType port = getPort(destURL, WS_ADDRESSING_ACTION, assertion);
                response = (XDRAcknowledgementType) oProxyHelper.invokePort(port,
                        AdapterXDRRequestSecuredPortType.class, "provideAndRegisterDocumentSetBRequest", wsRequest);
            } else {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBRequest");
        return response;
    }
}
