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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy20;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xdr._2007.XDRDeferredResponse20PortType;
import java.util.HashMap;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements NhinDocSubmissionDeferredResponseProxy {
    private Log log = null;
    private static HashMap<String, Service> cachedServiceMap = new HashMap<String, Service>();
    private static final String NAMESPACE_URI = "urn:ihe:iti:xdr:2007";
    private static final String SERVICE_LOCAL_PART = "XDRDeferredResponse20_Service";
    private static final String PORT_LOCAL_PART = "XDRDeferredResponse20_Port_Soap";
    private static final String WSDL_FILE_G1 = "NhinXDRDeferredResponse20.wsdl";
    private static final String WS_ADDRESSING_ACTION_G1 = "urn:nhin:Deferred:ProvideAndRegisterDocumentSet-bResponse";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
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
    protected XDRDeferredResponse20PortType getPort20(String url, AssertionType assertion)
    {
        WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();

        XDRDeferredResponse20PortType port = null;

        Service service = getService(WSDL_FILE_G1, NAMESPACE_URI, SERVICE_LOCAL_PART);
        String wsAction = WS_ADDRESSING_ACTION_G1;

        if (service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), XDRDeferredResponse20PortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME, wsAction, assertion);
         }
        else
        {
            log.error("Unable to obtain service - no port created.");
        }
        return port;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService(String wsdl, String uri, String service)
    {
        Service cachedService = cachedServiceMap.get(wsdl);
        if (cachedService == null)
        {
            try
            {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
                cachedServiceMap.put(wsdl, cachedService);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public RegistryResponseType provideAndRegisterDocumentSetBDeferredResponse20(RegistryResponseType request, AssertionType assertion,
            NhinTargetSystemType target) {
        log.debug("Begin provideAndRegisterDocumentSetBDeferredResponse");
        RegistryResponseType response = null;

        try
        {
            String url = oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g1);
            XDRDeferredResponse20PortType port = getPort20(url, assertion);
            WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
            wsHelper.addTargetCommunity(((BindingProvider)port), target);

            if(request == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
            	Holder<RegistryResponseType> respHolder = new Holder<RegistryResponseType>();
            	respHolder.value = request;
                oProxyHelper.invokePort(port, XDRDeferredResponse20PortType.class, "provideAndRegisterDocumentSetBDeferredResponse", respHolder);
                response = respHolder.value;
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetBDeferredResponse: " + ex.getMessage(), ex);
            response.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            RegistryError re = new RegistryError();
            re.setCodeContext(ex.getMessage());
            re.setErrorCode("XDSRegistryError");
            re.setLocation("NhinDocSubmissionDeferredRequestWebServiceProxySecuredImpl");
            re.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            response.getRegistryErrorList().getRegistryError().add(re);
        }

        log.debug("End provideAndRegisterDocumentSetBDeferredResponse");
        return response;
    }

}
