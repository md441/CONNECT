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
package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxyxdr.async.request.ProxyXDRAsyncRequestPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl implements
        PassthruDocSubmissionDeferredRequestProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:request";
    private static final String SERVICE_LOCAL_PART = "ProxyXDRAsyncRequest_Service";
    private static final String PORT_LOCAL_PART = "ProxyXDRAsyncRequest_Port";
    private static final String WSDL_FILE = "NhincProxyXDRRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:request:ProvideAndRegisterDocumentSet-bAsyncRequest_Request";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected ProxyXDRAsyncRequestPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        ProxyXDRAsyncRequestPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), ProxyXDRAsyncRequestPortType.class);
            oProxyHelper
                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     * 
     * @return The service class for this web service.
     */
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

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
            ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin provideAndRegisterDocumentSetBRequest");
        XDRAcknowledgementType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINC_PROXY_XDR_REQUEST_SERVICE_NAME);
            ProxyXDRAsyncRequestPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if (request == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("assertion was null");
            } else if (targetSystem == null) {
                log.error("targets was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayProvideAndRegisterDocumentSetRequestType msg = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
                msg.setProvideAndRegisterDocumentSetRequest(request);
                msg.setAssertion(assertion);
                msg.setNhinTargetSystem(targetSystem);

                response = (XDRAcknowledgementType) oProxyHelper.invokePort(port, ProxyXDRAsyncRequestPortType.class,
                        "provideAndRegisterDocumentSetBAsyncRequest", msg);
            }
        } catch (Exception ex) {
            log.error("Error calling provideAndRegisterDocumentSetBAsyncRequest: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBRequest");
        return response;
    }

}
