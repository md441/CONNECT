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
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterComponentRedactionEngineService", portName = "AdapterComponentRedactionEnginePortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction", wsdlLocation = "WEB-INF/wsdl/AdapterComponentRedactionEngine/AdapterComponentRedactionEngine.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterComponentRedactionEngine {
    @Resource
    private WebServiceContext context;

    public FilterDocQueryResultsResponseType filterDocQueryResults(
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
        FilterDocQueryResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if (redactionEngineImpl != null) {
            response = redactionEngineImpl.filterDocQueryResults(filterDocQueryResultsRequest, context);
        }

        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
        FilterDocRetrieveResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if (redactionEngineImpl != null) {
            response = redactionEngineImpl.filterDocRetrieveResults(filterDocRetrieveResultsRequest, context);
        }

        return response;
    }

    protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl() {
        return new AdapterComponentRedactionEngineImpl();
    }
}
