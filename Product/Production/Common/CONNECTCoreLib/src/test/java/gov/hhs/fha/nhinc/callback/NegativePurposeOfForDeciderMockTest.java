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
package gov.hhs.fha.nhinc.callback;

import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author mweaver
 *
 */
public class NegativePurposeOfForDeciderMockTest extends AbstractPurposeOfForDeciderMockBaseTest {
   
    /*-----------------Setup Methods---------------*/
    
    @Override
    protected Map<Object,Object> createTokenValues() {
        HashMap<Object, Object> tokenVals = new HashMap<Object, Object>();
        tokenVals.put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID, "1.1");
        tokenVals.put(NhincConstants.ACTION_PROP, "auditrepositorysecured");
        return tokenVals;
    }
    
    /*-----------------Test Methods---------------*/
    
    /**
     * Mock decider returns false, meaning we'll send PurposeOf
     */
    @Override
    @Test
    public void testIsPurposeOfg0() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValuesg0()));
        context.assertIsSatisfied();
    }
    
    /**
     * Mock decider returns true, meaning we'll send PurposeFor
     */
    @Override
    @Test
    public void testIsPurposeForg0() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValuesg0()));
        context.assertIsSatisfied();
    }
 
    /**
     * g1 (2011 specs), meaning we'll always send PurposeOf
     */
    @Override
    @Test
    public void testIsPurposeOfg1() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValuesg1()));
        context.assertIsSatisfied();
    }
    
    @Override
    @Test
    public void testIsPurposeOfg0APILookup() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValues()));
        context.assertIsSatisfied();
    }
    
    @Override
    @Test
    public void testIsPurposeForg0APILookup() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValues()));
        context.assertIsSatisfied();
    }
    
    @Override
    @Test
    public void testIsPurposeOfg1APILookup() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();
        
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(createTokenValues()));
        context.assertIsSatisfied();
    }
}
