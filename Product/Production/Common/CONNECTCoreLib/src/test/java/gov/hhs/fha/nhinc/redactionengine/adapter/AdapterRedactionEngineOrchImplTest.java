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

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
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
public class AdapterRedactionEngineOrchImplTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final RedactionEngine mockRedactionEngine = context.mock(RedactionEngine.class);
    final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
    final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);
    final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
    final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);

    @Test
    public void testCreateLogger() {
        try {
            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            Log log = javaProxy.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetRedactionEngine() {
        try {
            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            RedactionEngine redactionEngine = javaProxy.getRedactionEngine();
            assertNotNull("RedactionEngine was null", redactionEngine);
        } catch (Throwable t) {
            System.out.println("Error running testGetRedactionEngine test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngine test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsHappy() {
        try {
            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockRedactionEngine).filterAdhocQueryResults(with(aNonNull(AdhocQueryRequest.class)),
                            with(aNonNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = javaProxy.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullInputs() {
        try {
            final AdhocQueryRequest adhocQueryRequest = null;
            final AdhocQueryResponse adhocQueryResponse = null;

            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockRedactionEngine).filterAdhocQueryResults(with(aNull(AdhocQueryRequest.class)),
                            with(aNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = javaProxy.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullRedactionEngine() {
        try {

            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return null;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });

            AdhocQueryResponse response = javaProxy.filterAdhocQueryResults(mockAdhocQueryRequest,
                    mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterAdhocQueryResultsNullRedactionEngine test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResultsNullRedactionEngine test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsHappy() {
        try {
            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockRedactionEngine).filterRetrieveDocumentSetResults(
                            with(aNonNull(RetrieveDocumentSetRequestType.class)),
                            with(aNonNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = javaProxy.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullInputs() {
        try {
            final RetrieveDocumentSetRequestType retrieveRequest = null;
            final RetrieveDocumentSetResponseType retrieveResponse = null;

            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockRedactionEngine).filterRetrieveDocumentSetResults(
                            with(aNull(RetrieveDocumentSetRequestType.class)),
                            with(aNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = javaProxy.filterRetrieveDocumentSetResults(retrieveRequest,
                    retrieveResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullRedactionEngine() {
        try {
            AdapterRedactionEngineOrchImpl javaProxy = new AdapterRedactionEngineOrchImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected RedactionEngine getRedactionEngine() {
                    return null;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });

            RetrieveDocumentSetResponseType response = javaProxy.filterRetrieveDocumentSetResults(mockRequest,
                    mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetResultsNullRedactionEngine test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResultsNullRedactionEngine test: " + t.getMessage());
        }
    }

}