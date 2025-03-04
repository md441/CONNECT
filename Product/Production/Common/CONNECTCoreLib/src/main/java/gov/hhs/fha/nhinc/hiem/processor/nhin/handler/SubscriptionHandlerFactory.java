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
package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;

/**
 * 
 * 
 * @author Neil Webb
 */
public class SubscriptionHandlerFactory {
    // Child adapter subscription mode
    private static Log log = LogFactory.getLog(SubscriptionHandlerFactory.class);

    public SubscriptionHandler getSubscriptionHandler() throws SubscribeCreationFailedFault, ConfigurationException {
        SubscriptionHandler subscriptionHandler = null;

        ConfigurationManager config = new ConfigurationManager();
        String childAdapterSubscriptionMode = config.getAdapterSubscriptionMode();
        log.debug("child adapter subscription mode = " + childAdapterSubscriptionMode);
        if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_SUBSCRIPTIONS
                .equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new ChildSubscriptionModeSubscriptionHandler();
        } else if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_FORWARD
                .equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new ForwardChildModeSubscriptionHandler();
        } else if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED
                .equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new DisabledChildModeSubscriptionHandler();
        } else {
            throw new SoapFaultFactory().getUnknownSubscriptionServiceAdapterModeFault(childAdapterSubscriptionMode);
        }
        log.debug("subscriptionHandler = " + subscriptionHandler.toString());
        return subscriptionHandler;
    }
}
