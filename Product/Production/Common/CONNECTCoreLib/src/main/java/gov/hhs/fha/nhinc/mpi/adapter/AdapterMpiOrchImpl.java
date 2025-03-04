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
package gov.hhs.fha.nhinc.mpi.adapter;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import org.hl7.v3.PRPAIN201306UV02;

import org.hl7.v3.PRPAIN201305UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;

import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;

/**
 * 
 * This is the business logic for the AdapterMpi. This is a thin layer,
 * 
 * it simply takes the request and calls the AdapterComponentMpi.
 * 
 * 
 * 
 * @author Les Westberg
 */

public class AdapterMpiOrchImpl

{

    private static Log log = LogFactory.getLog(AdapterMpiOrchImpl.class);

    /**
     * 
     * Send the patient query request to the actual MPI that is implemented
     * 
     * 
     * 
     * @param findCandidatesRequest The request containing the query information.
     * 
     * @param assertion The assertion for this message.
     * 
     * @return The results of the query.
     */

    public PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)

    {

        log.debug("Entering AdapterMpiOrchImpl.query method...");

        AdapterComponentMpiProxy oMpiProxy = null;

        AdapterComponentMpiProxyObjectFactory oFactory = new AdapterComponentMpiProxyObjectFactory();

        oMpiProxy = oFactory.getAdapterComponentMpiProxy();

        PRPAIN201306UV02 oResponse = oMpiProxy.findCandidates(findCandidatesRequest, assertion);

        return oResponse;

    }

}
