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
package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.PassthruAdminDistributionHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author dunnek
 */
public class NhincAdminDistSecuredWebserviceImplTest {

    private Mockery context;

    public NhincAdminDistSecuredWebserviceImplTest() {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Test
    public void testSendAlertMessage() throws Exception {
        System.out.println("sendAlertMessage");
        final PassthruAdminDistributionHelper mockHelper = context.mock(PassthruAdminDistributionHelper.class);

        final WebServiceProxyHelper mockWebServiceProxyHelper = context.mock(WebServiceProxyHelper.class);

        EDXLDistribution body = null;
        AssertionType assertion = null;
        final NhinTargetSystemType target = null;

        PassthruAdminDistributionProxyWebServiceSecuredImpl instance = new PassthruAdminDistributionProxyWebServiceSecuredImpl(
                mockWebServiceProxyHelper, mockHelper);

        context.checking(new Expectations() {

            {

                allowing(mockHelper).getLocalCommunityId();
                allowing(mockHelper).getUrl(with(any(String.class)), with(any(String.class)),
                        with(any(NhincConstants.GATEWAY_API_LEVEL.class)));
                will(returnValue("http://someurl/"));

                allowing(mockHelper).getSecuredPort(with(any(String.class)), with(any(String.class)),
                        with(any(String.class)), with(any(AssertionType.class)),
                        with(any(NhincConstants.GATEWAY_API_LEVEL.class)));

            }
        });

        instance.sendAlertMessage(body, assertion, target, NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

        context.assertIsSatisfied();

    }

}