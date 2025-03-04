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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

/**
 * This class is the implementation of the AdapterComponentMpi. It performs any web service specific stuff necessary and
 * then calls the underlying java implementation for this service.
 * 
 * @author Les Westberg
 */
public class AdapterComponentMpiImpl {

    private static Log log = LogFactory.getLog(AdapterComponentMpiImpl.class);

    /**
     * Perform a look up on the MPI.
     * 
     * @param bIsSecure TRUE if this is being called from a secure web service.
     * @param findCandidatesRequest The information about the patient that is being used for the lookup.
     * @param context The web service context information.
     * @return The results of the lookup.
     */
    public PRPAIN201306UV02 query(boolean bIsSecure, org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest,
            WebServiceContext context) {
        log.debug("Entering AdapterComponentMpiImpl.query - secured");

        AssertionType assertion = null;
        if ((bIsSecure) && (context != null)) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = new AssertionType();
        }

        AdapterComponentMpiOrchImpl oOrchestrator = new AdapterComponentMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.findCandidates(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterComponentMpiImpl.query - secured");
        return response;
    }

    public PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertionFromBody) {
        log.debug("Entering AdapterComponentMpiImpl.query - unsecured");

        AssertionType assertion = null;
        if (assertionFromBody != null) {
            assertion = assertionFromBody;
        } else {
            assertion = new AssertionType();
        }

        AdapterComponentMpiOrchImpl oOrchestrator = new AdapterComponentMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.findCandidates(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterComponentMpiImpl.query - unsecured");
        return response;
    }
}
