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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Doc Query implementation of OutboundDelegate
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundDocQueryDelegate.class);

    public OutboundDocQueryDelegate() {
    }

    @Override
    public Orchestratable process(Orchestratable message) {
        getLogger().debug("NhinDocQueryDelegate::process Orchestratable");
        if (message == null) {
            getLogger().error("NhinDocQueryDelegate Orchestratable was null!!!");
            return null;
        }
        if (message instanceof OutboundDocQueryOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        if (message instanceof OutboundDocQueryOrchestratable) {
            return process((OutboundDocQueryOrchestratable) message);
        }
        getLogger().error("NhinDocQueryDelegate message is not an instance of EntityDocQueryOrchestratable!");
        return null;
    }

    public OutboundDocQueryOrchestratable process(OutboundDocQueryOrchestratable message) {
        getLogger().debug("NhinDocQueryDelegate::process EntityDocQueryOrchestratable");

        OutboundDocQueryOrchestrationContextBuilder contextBuilder = (OutboundDocQueryOrchestrationContextBuilder) OrchestrationContextFactory
                .getInstance().getBuilder(message.getTarget().getHomeCommunity(), NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY);

        contextBuilder.setAssertionType(message.getAssertion());
        contextBuilder.setRequest(message.getRequest());
        contextBuilder.setTarget(message.getTarget());
        contextBuilder.setServiceName(message.getServiceName());
        contextBuilder.setPolicyTransformer(message.getPolicyTransformer());
        contextBuilder.setAuditTransformer(message.getAuditTransformer());
        contextBuilder.setProcessor(message.getResponseProcessor());

        OutboundDocQueryOrchestratable response = (OutboundDocQueryOrchestratable) contextBuilder.build().execute();

        if (response instanceof OutboundDocQueryOrchestratable_a0) {
            getLogger().debug("NhinDocQueryDelegate::process returning a0 result");
        } else if (response instanceof OutboundDocQueryOrchestratable_a0) {
            getLogger().debug("NhinDocQueryDelegate::process returning a1 result");
        } else {
            getLogger().error("NhinDocQueryDelegate::process has unknown response!!!");
        }
        return response;
    }

    private Log getLogger() {
        return log;
    }

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.orchestration.OutboundDelegate#createErrorResponse(gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable, java.lang.String)
	 */
	@Override
	public void createErrorResponse(OutboundOrchestratable message, String error) {
		// TODO Auto-generated method stub
		
	}

}
