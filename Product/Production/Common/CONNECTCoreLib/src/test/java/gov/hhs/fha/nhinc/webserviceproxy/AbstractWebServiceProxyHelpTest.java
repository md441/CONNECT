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

import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.jmock.lib.legacy.ClassImposteriser;

@Ignore
public abstract class AbstractWebServiceProxyHelpTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final protected IPropertyAcessor mockPropertyAccessor = context.mock(IPropertyAcessor.class);

    protected void initializationExpectations() throws PropertyAccessException {
        retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

        retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("5"));

        timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("300"));

        exceptionExpectation(mockPropertyAccessor, Expectations.returnValue("PropertyAccessException"));
    }

    protected void exceptionExpectation(IPropertyAcessor mock, Action action) throws PropertyAccessException {
        propertyExpectation(mock, action, WebServiceProxyHelperProperties.CONFIG_KEY_EXCEPTION);
    }

    protected void timeoutExpectation(IPropertyAcessor mockPropertyAccessor, Action action)
            throws PropertyAccessException {
        propertyExpectation(mockPropertyAccessor, action, WebServiceProxyHelperProperties.CONFIG_KEY_TIMEOUT);

    }

    protected void retryAttemptsExpectation(final IPropertyAcessor mock, final Action action)
            throws PropertyAccessException {
        propertyExpectation(mock, action, WebServiceProxyHelperProperties.CONFIG_KEY_RETRYATTEMPTS);
    }

    protected void retryDelayExpectation(final IPropertyAcessor mock, final Action action)
            throws PropertyAccessException {
        propertyExpectation(mock, action, WebServiceProxyHelperProperties.CONFIG_KEY_RETRYDELAY);
    }

    protected void propertyExpectation(final IPropertyAcessor mock, final Action action, final String property)
            throws PropertyAccessException {
        context.checking(new Expectations() {

            {
                oneOf(mock).getProperty(property);
                will(action);
            }
        });
    }

}
