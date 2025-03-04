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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * 
 * @author jhoppesc
 */
public class PatientCorrelationServiceUnsecuredImpl
        implements
        PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(PatientCorrelationServiceUnsecuredImpl.class);

    private PatientCorrelationOrch orchestration;

    PatientCorrelationServiceUnsecuredImpl(PatientCorrelationOrch orchestration) {
        this.orchestration = orchestration;
    }

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(
            RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest, AssertionType assertionType) {

        log.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
        RetrievePatientCorrelationsResponseType response = orchestration.retrievePatientCorrelations(
                retrievePatientCorrelationsRequest.getPRPAIN201309UV02(),
                retrievePatientCorrelationsRequest.getAssertion());

        return response;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(
            AddPatientCorrelationRequestType addPatientCorrelationRequest, AssertionType assertionType) {

        log.info("Calling the Patient Correlation Add Correlations Orch Impl");
        AddPatientCorrelationResponseType response = orchestration.addPatientCorrelation(
                addPatientCorrelationRequest.getPRPAIN201301UV02(), addPatientCorrelationRequest.getAssertion());

        return response;
    }

}
