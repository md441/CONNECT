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
package gov.hhs.fha.nhinc.admindistribution._20.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

import gov.hhs.fha.nhinc.admindistribution.entity.EntityAdminDistributionOrchImpl;

/**
 * 
 * @author dunnek
 */
@WebService(serviceName = "AdministrativeDistribution_Service", portName = "AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistribution/EntityAdminDist_g1.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityAdministrativeDistribution_g1 {

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType body) {
        // TODO implement this method
        getEntityImpl().sendAlertMessage(body, body.getAssertion(), body.getNhinTargetCommunities());
    }

    protected EntityAdminDistributionOrchImpl getEntityImpl() {
        return new EntityAdminDistributionOrchImpl();
    }
}
