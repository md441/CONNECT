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
package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;

/**
 * CallableRequest is basically what is executed (i.e. the Runnable) Uses generics for Response (which represents object
 * that is returned)
 * 
 * Constructs with a OutboundOrchestratableMessage to be used, which contains the OutboundDelegate to execute request
 * and NhinProcessor to processErrorResponse (in the case of error/exception)
 * 
 * @author paul.eftis
 */
public class NhinCallableRequest<Response extends OutboundOrchestratableMessage> implements Callable<Response> {

    private OutboundDelegate client = null;
    private OutboundResponseProcessor processor = null;
    private OutboundOrchestratableMessage entityRequest = null;
    
    public NhinCallableRequest(OutboundOrchestratableMessage orch) {
        this.client = orch.getDelegate();
        this.processor = orch.getResponseProcessor();
        this.entityRequest = orch;
    }
    
    protected Log getLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Initiates web service client call to target with request
     * 
     * @return Response is web service response from client call
     */
    @Override
    public Response call() {
        Response response = null;
        try {
            if (client != null) {
                // make web service call using nhindelegate::process
                response = (Response) client.process(entityRequest);
                if (response == null) {
                    throw new Exception("Response received is null!!!");
                }
            } else {
                throw new Exception("NhinDelegate is null!!!");
            }
        } catch (Exception e) {
            getLogger().error("Failed to process callable request.", e);
            response = (Response) processor.processErrorResponse(entityRequest, e.getMessage());
        }
        return response;
    }

}
