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
package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 * Entity subscribe message handler.
 * 
 * @author Neil Webb
 */
public interface EntitySubscribeHandler {
    /**
     * Set an optional patient identifier from the subscription
     * 
     * @param patientIdentifier Optional patient identifier
     */
    public void setPatientIdentifier(QualifiedSubjectIdentifierType patientIdentifier);

    /**
     * Set an optional XPath location to the patient identifier
     * 
     * @param xpathToPatientId Optional XPath location to patient identifier
     */
    public void setPatientIdentiferLocation(String xpathToPatientId);

    /**
     * Handle an entity subscribe message.
     * 
     * @param topicConfig Topic configuration for the subscribe message
     * @param subscribe Subscribe message received on the entity interface
     * @param subscribeElement XML element for the subscription as received in the SOAP message
     * @param assertion Assertion received from the adapter
     * @param targetCommunitites Optional target communities
     * @return Subscribe response
     * @throws org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault
     * @throws org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault
     * @throws org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault
     */
    public SubscribeResponse handleSubscribe(TopicConfigurationEntry topicConfig, Subscribe subscribe,
            Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites)
            throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault;
}
