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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class AdapterPIPServiceImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);
    final AdapterPIPImpl mockAdapterPIPImpl = context.mock(AdapterPIPImpl.class);

    @Test
    public void testCreateLogger() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }
            };

            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterPIPImpl() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }
            };

            AdapterPIPImpl pipImpl = sut.getAdapterPIPImpl();
            assertNotNull("AdapterPIPImpl was null", pipImpl);
        } catch (Throwable t) {
            System.out.println("Error running testGetAdapterPIPImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPIPImpl: " + t.getMessage());
        }
    }

    @Test
    public void testLoadAssertion() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                }
            };

            final AssertionType mockAssertion = context.mock(AssertionType.class);
            sut.loadAssertion(mockAssertion, mockWebServiceContext);
            assertTrue("Passed loadAssertion", true);
        } catch (Throwable t) {
            System.out.println("Error running testLoadAssertion: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testLoadAssertion: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtIdHappy() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAdapterPIPImpl).retrievePtConsentByPtId(
                            with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            RetrievePtConsentByPtIdRequestType request = new RetrievePtConsentByPtIdRequestType();

            RetrievePtConsentByPtIdResponseType response = sut.retrievePtConsentByPtId(request, mockWebServiceContext);
            assertNotNull("RetrievePtConsentByPtIdResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtIdHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtIdHappy: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtIdException() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                    throw new IllegalArgumentException("Forced error.");
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });

            RetrievePtConsentByPtIdRequestType request = new RetrievePtConsentByPtIdRequestType();

            sut.retrievePtConsentByPtId(request, mockWebServiceContext);
            fail("Should have had exception.");
        } catch (RuntimeException e) {
            assertEquals("Exception message",
                    "Error occurred calling AdapterPIPImpl.retrievePtConsentByPtId.  Error: Forced error.",
                    e.getMessage());
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtIdException: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtIdException: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtDocIdHappy() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAdapterPIPImpl).retrievePtConsentByPtDocId(
                            with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });

            RetrievePtConsentByPtDocIdRequestType request = new RetrievePtConsentByPtDocIdRequestType();

            RetrievePtConsentByPtDocIdResponseType response = sut.retrievePtConsentByPtDocId(request,
                    mockWebServiceContext);
            assertNotNull("RetrievePtConsentByPtDocIdResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtDocIdHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtDocIdHappy: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtDocIdException() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                    throw new IllegalArgumentException("Forced error.");
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });

            RetrievePtConsentByPtDocIdRequestType request = new RetrievePtConsentByPtDocIdRequestType();

            sut.retrievePtConsentByPtDocId(request, mockWebServiceContext);
            fail("Should have had exception.");
        } catch (RuntimeException e) {
            assertEquals("Exception message",
                    "Error occurred calling AdapterPIPImpl.retrievePtConsentByPtDocId.  Error: Forced error.",
                    e.getMessage());
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtDocIdException: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtDocIdException: " + t.getMessage());
        }
    }

    @Test
    public void testStorePtConsentHappy() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAdapterPIPImpl).storePtConsent(with(aNonNull(StorePtConsentRequestType.class)));
                }
            });

            StorePtConsentRequestType request = new StorePtConsentRequestType();

            StorePtConsentResponseType response = sut.storePtConsent(request, mockWebServiceContext);
            assertNotNull("StorePtConsentResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testStorePtConsentHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStorePtConsentHappy: " + t.getMessage());
        }
    }

    @Test
    public void testStorePtConsentException() {
        try {
            AdapterPIPServiceImpl sut = new AdapterPIPServiceImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIPImpl() {
                    return mockAdapterPIPImpl;
                }

                @Override
                protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
                    throw new IllegalArgumentException("Forced error.");
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });

            StorePtConsentRequestType request = new StorePtConsentRequestType();

            sut.storePtConsent(request, mockWebServiceContext);
            fail("Should have had exception.");
        } catch (RuntimeException e) {
            assertEquals("Exception message",
                    "Error occurred calling AdapterPIPImpl.storePtConsent.  Error: Forced error.", e.getMessage());
        } catch (Throwable t) {
            System.out.println("Error running testStorePtConsentException: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStorePtConsentException: " + t.getMessage());
        }
    }

}