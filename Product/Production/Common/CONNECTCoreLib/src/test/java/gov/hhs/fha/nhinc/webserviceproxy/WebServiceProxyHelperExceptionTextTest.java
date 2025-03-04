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
package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperExceptionTextTest extends AbstractWebServiceProxyHelpTest {

    /**
     * Test the GetExceptionText method happy path.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testGetExceptionTextHappyPath() throws PropertyAccessException {
        retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        exceptionExpectation(mockPropertyAccessor, Expectations.returnValue("PropertyAccessException"));

        WebServiceProxyHelperProperties properties = new WebServiceProxyHelperProperties(mockPropertyAccessor);

        String sExceptionText = properties.getExceptionText();
        assertEquals("getExceptionText failed.", "PropertyAccessException", sExceptionText);

    }

    /**
     * Test the GetExceptionText method with PropertyAccessException.
     * 
     * @throws PropertyAccessException
     */
    @Ignore
    @Test
    public void testGetExceptionTextPropertyException() throws PropertyAccessException {

        retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        exceptionExpectation(mockPropertyAccessor,
                Expectations.throwException(new PropertyAccessException("Failed to retrieve property.")));

        WebServiceProxyHelperProperties properties = new WebServiceProxyHelperProperties(mockPropertyAccessor);

        String sExceptionText = properties.getExceptionText();

        assertEquals("getExceptionText failed: ", "", sExceptionText);

    }

}
