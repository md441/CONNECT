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
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * 
 * Object factory to retrieve an AdapterPDPProxy instance.
 * 
 * 
 * 
 * @author Neil Webb
 */

public class AdapterPDPProxyObjectFactory extends ComponentProxyObjectFactory

{

    private static final String CONFIG_FILE_NAME = "AdapterPDPConfig.xml";

    private static final String BEAN_NAME = "adapterpdp";

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory#getConfigFileName()
     */

    @Override
    protected String getConfigFileName()

    {

        return CONFIG_FILE_NAME;

    }

    /**
     * 
     * Retrieve an adapter PDP implementation using the IOC framework.
     * 
     * This method retrieves the object from the framework that has an
     * 
     * identifier of "adapterpdp."
     * 
     * 
     * 
     * @return AdapterPDPProxy instance
     */

    public AdapterPDPProxy getAdapterPDPProxy()

    {

        return getBean(BEAN_NAME, AdapterPDPProxy.class);

    }

}
