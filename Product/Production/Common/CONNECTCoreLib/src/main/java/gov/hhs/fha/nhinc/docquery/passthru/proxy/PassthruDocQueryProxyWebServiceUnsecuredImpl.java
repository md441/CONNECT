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
package gov.hhs.fha.nhinc.docquery.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author JHOPPESC
 */
public class PassthruDocQueryProxyWebServiceUnsecuredImpl implements PassthruDocQueryProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocquery";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQuery";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQueryPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocquery:RespondingGateway_CrossGatewayQueryRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocQueryProxyWebServiceUnsecuredImpl() {
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
    protected NhincProxyDocQueryPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        NhincProxyDocQueryPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocQueryPortType.class);
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

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        log.debug("Begin respondingGatewayCrossGatewayQuery");
        AdhocQueryResponse response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINC_PROXY_DOC_QUERY_SERVICE_NAME);
            NhincProxyDocQueryPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if (body == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("AssertionType was null");
            } else if (target == null) {
                log.error("target was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
                request.setAdhocQueryRequest(body);
                request.setAssertion(assertion);
                request.setNhinTargetSystem(target);

                response = (AdhocQueryResponse) oProxyHelper.invokePort(port, NhincProxyDocQueryPortType.class,
                        "respondingGatewayCrossGatewayQuery", request);
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            throw ex;

        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
