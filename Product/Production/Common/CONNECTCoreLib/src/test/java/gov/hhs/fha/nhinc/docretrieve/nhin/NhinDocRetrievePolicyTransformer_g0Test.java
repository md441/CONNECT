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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author mweaver
 */
public class NhinDocRetrievePolicyTransformer_g0Test {

    public NhinDocRetrievePolicyTransformer_g0Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of tranform method, of class NhinDocRetrievePolicyTransformer_g0.
     */
    @Test
    public void testTranform() {
        /*
         * NhinDocRetrievePolicyTransformer_g0 instance = new NhinDocRetrievePolicyTransformer_g0();
         * 
         * RetrieveDocumentSetRequestType req = new RetrieveDocumentSetRequestType(); DocumentRequest dreq = new
         * DocumentRequest(); dreq.setDocumentUniqueId("1"); dreq.setHomeCommunityId("1");
         * dreq.setRepositoryUniqueId("1"); req.getDocumentRequest().add(dreq); Orchestratable message = new
         * NhinDocRetrieveOrchestratableImpl_g0(req, null, instance, null, null); Direction direction =
         * PolicyTransformer.Direction.INBOUND; CheckPolicyRequestType expResult = null; CheckPolicyRequestType result =
         * instance.tranform(message, direction); assertEquals(expResult, result);
         */
        // TODO: this test requires mocking of the CheckPolicyRequestType and NhinDocRetrieveOrchestratableImpl_g0
        // classes.
    }

}