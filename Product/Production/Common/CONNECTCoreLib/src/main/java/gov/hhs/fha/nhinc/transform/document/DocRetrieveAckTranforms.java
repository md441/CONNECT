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
package gov.hhs.fha.nhinc.transform.document;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * 
 * @author richard.ettema
 */
public class DocRetrieveAckTranforms {

    /**
     * Create acknowledgement message based on specific data values.
     * 
     * @param status
     * @param errCode
     * @param errText
     * @return
     */
    public static DocRetrieveAcknowledgementType createAckMessage(String status, String errCode, String errText) {
        DocRetrieveAcknowledgementType ackMsg = new DocRetrieveAcknowledgementType();

        RegistryResponseType regResp = new RegistryResponseType();
        ackMsg.setMessage(regResp);

        // Assign the Ack status
        regResp.setStatus(status);

        // If an error is defined, create a registry error
        if (errCode != null && errText != null) {
            RegistryError regError = new RegistryError();
            regError.setErrorCode(errCode);
            regError.setCodeContext(errText);
            regError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            RegistryErrorList errorList = new RegistryErrorList();
            errorList.getRegistryError().add(regError);
            regResp.setRegistryErrorList(errorList);
        }

        return ackMsg;
    }

}
