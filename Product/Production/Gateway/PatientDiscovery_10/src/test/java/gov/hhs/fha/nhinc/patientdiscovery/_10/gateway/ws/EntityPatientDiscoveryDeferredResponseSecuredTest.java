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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery._10.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredResponseSecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.PatientDiscoveryServiceFactory;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class EntityPatientDiscoveryDeferredResponseSecuredTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testDefaultConstructor() {
        EntityPatientDiscoveryDeferredResponseSecured ws = new EntityPatientDiscoveryDeferredResponseSecured();
        assertNotNull(ws);
    }

    @Test
    public void testMockService() {
        final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
        final RespondingGatewayPRPAIN201306UV02SecuredRequestType mockRequest = context
                .mock(RespondingGatewayPRPAIN201306UV02SecuredRequestType.class);
        final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
        final EntityPatientDiscoveryDeferredResponseImpl mockService = context
                .mock(EntityPatientDiscoveryDeferredResponseImpl.class);

        context.checking(new Expectations() {
            {
                oneOf(mockService).processPatientDiscoveryAsyncResp(with(same(mockRequest)),
                        with(any(WebServiceContext.class)));
                will(returnValue(expectedResponse));

                oneOf(mockFactory).getEntityPatientDiscoveryDeferredResponseImpl();
                will(returnValue(mockService));
            }
        });

        EntityPatientDiscoveryDeferredResponseSecured ws = new EntityPatientDiscoveryDeferredResponseSecured(
                mockFactory);

        MCCIIN000002UV01 actualResponse = ws.processPatientDiscoveryAsyncResp(mockRequest);

        assertSame(expectedResponse, actualResponse);
    }

}
