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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201305;
import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201306;
import gov.hhs.fha.nhinc.mpilib.*;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import org.hl7.v3.PRPAIN201305UV02;

/**
 * 
 * @author Jon Hoppesch
 */
public class PatientChecker implements AdapterComponentMpiChecker {

    private static Log log = LogFactory.getLog(PatientChecker.class);

    /**
     * Find matching patients from mpi
     * 
     * @param query request
     * @return response
     */
    public PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query) {
        log.debug("Entering PatientChecker.FindPatient method...");
        PRPAIN201306UV02 result = null;

        PRPAMT201306UV02ParameterList queryParams = HL7Parser201305.ExtractHL7QueryParamsFromMessage(query);

        Patients filteredPatients = new Patients();

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7Parser201305.ExtractMpiPatientFromQueryParams(queryParams);
            log.info("perform patient lookup in mpi");

            log.info("source patient check 1 [" + sourcePatient.toString() + "]");
            Patients searchResults = MpiDataAccess.LookupPatients(sourcePatient);
            if (searchResults != null) {
                log.debug("searchResults.size(): " + searchResults.size());
            } else {
                log.debug("No matching patient found");
            }

            List<String> dupOrgIds = new ArrayList<String>();
            for (Patient patient : searchResults) {
                if ((patient.getIdentifiers() != null) && (patient.getIdentifiers().size() > 0)
                        && (patient.getIdentifiers().get(0).getOrganizationId() != null)) {

                    for (Patient tempPatient : filteredPatients) {
                        if ((tempPatient.getIdentifiers().get(0).getOrganizationId()).equalsIgnoreCase(patient
                                .getIdentifiers().get(0).getOrganizationId())) {
                            dupOrgIds.add(patient.getIdentifiers().get(0).getOrganizationId());
                        }
                    }
                    filteredPatients.add(patient);
                }
            }

            if ((dupOrgIds != null) && (dupOrgIds.size() > 0)) {
                HashSet hashSet = new HashSet(dupOrgIds);
                dupOrgIds = new ArrayList(hashSet);
                log.debug("More than one matching patient found in some organizations. dupOrgIds.size(): "
                        + dupOrgIds.size());
            }

            for (Patient patient : searchResults) {
                if ((patient.getIdentifiers() != null) && (patient.getIdentifiers().size() > 0)
                        && (patient.getIdentifiers().get(0).getOrganizationId() != null)) {

                    for (String str : dupOrgIds) {
                        if ((patient.getIdentifiers().get(0).getOrganizationId()).equalsIgnoreCase(str)) {
                            filteredPatients.remove(patient);
                        }
                    }
                }
            }

            if (filteredPatients != null) {
                log.debug("After duplicates removed - filteredPatients.size(): " + filteredPatients.size());
            } else {
                log.debug("filteredPatients - null");
            }
        }

        result = HL7Parser201306.BuildMessageFromMpiPatient(filteredPatients, query);

        log.debug("Exiting PatientChecker.FindPatient method...");
        return result;
    }

    /**
     * 
     * @param query
     * @return true - minimum params found; false - not found
     */
    public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query) {
        boolean result = false;

        PRPAMT201306UV02ParameterList queryParams = HL7Parser201305.ExtractHL7QueryParamsFromMessage(query);
        Patient sourcePatient = HL7Parser201305.ExtractMpiPatientFromQueryParams(queryParams);

        if (sourcePatient != null && sourcePatient.getNames() != null && sourcePatient.getNames().size() > 0
                && sourcePatient.getNames().get(0) != null
                && NullChecker.isNotNullish(sourcePatient.getNames().get(0).getFirstName())
                && NullChecker.isNotNullish(sourcePatient.getNames().get(0).getLastName())
                && NullChecker.isNotNullish(sourcePatient.getGender()) && sourcePatient.getDateOfBirth() != null) {
            result = true;
        }

        return result;
    }
}
