/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.callback;

/**
 * 
 * @author Neil Webb
 */
public class SamlConstants {
    // SAML Constants
    public static final String ACTION_PROP = "action";
    public static final String RESOURCE_PROP = "resource";
    public static final String PURPOSE_CODE_PROP = "purposeOfUseRoleCode";
    public static final String PURPOSE_SYST_PROP = "purposeOfUseCodeSystem";
    public static final String PURPOSE_SYST_NAME_PROP = "purposeOfUseCodeSystemName";
    public static final String PURPOSE_DISPLAY_PROP = "purposeOfUseDisplayName";
    public static final String USER_FIRST_PROP = "userFirstName";
    public static final String USER_MIDDLE_PROP = "userMiddleName";
    public static final String USER_LAST_PROP = "userLastName";
    public static final String USER_FULL_NAME_PROP = "userFillName";
    public static final String USER_NAME_PROP = "userName";
    public static final String USER_ORG_PROP = "userOrganization";
    public static final String USER_ORG_ID_PROP = "userOrganizationID";
    public static final String USER_ORG_X509_NAME = "userOrganizationX509Name";
    public static final String HOME_COMMUNITY_ID_PROP = "localHomeCommunityId";
    public static final String HOME_COMMUNITY_DESCRIPTION_PROP = "localHomeCommunityDescription";
    public static final String HOME_COM_PROP = "homeCommunity";
    public static final String PATIENT_ID_PROP = "patientID";
    public static final String USER_CODE_PROP = "userRoleCode";
    public static final String USER_SYST_PROP = "userRoleCodeSystem";
    public static final String USER_SYST_NAME_PROP = "userRoleCodeSystemName";
    public static final String USER_DISPLAY_PROP = "userRoleCodeDisplayName";
    public static final String AUTHN_INSTANT_PROP = "authnInstant";
    public static final String AUTHN_SESSION_INDEX_PROP = "authnSessionIndex";
    public static final String AUTHN_CONTEXT_CLASS_PROP = "authnContextClass";
    public static final String SUBJECT_LOCALITY_ADDR_PROP = "subjectLocalityAddress";
    public static final String SUBJECT_LOCALITY_DNS_PROP = "subjectLocalityDNS";
    public static final String AUTHZ_DECISION_PROP = "authzDecision";
    public static final String AUTHZ_STATEMENT_EXISTS_PROP = "authzStatementExists";
    public static final String ASSERTION_ISSUER_PROP = "assertionIssuer";
    public static final String ASSERTION_ISSUER_FORMAT_PROP = "assertionIssuerFormat";
    public static final String EVIDENCE_ID_PROP = "evidenceAssertionId";
    public static final String EVIDENCE_INSTANT_PROP = "evidenceAssertionInstant";
    public static final String EVIDENCE_VERSION_PROP = "evidenceAssertionVersion";
    public static final String EVIDENCE_ISSUER_PROP = "evidenceAssertionIssuer";
    public static final String EVIDENCE_ISSUER_FORMAT_PROP = "evidenceAssertionIssuerFormat";
    public static final String EVIDENCE_CONDITION_NOT_BEFORE_PROP = "evidenceConditionNotBefore";
    public static final String EVIDENCE_CONDITION_NOT_AFTER_PROP = "evidenceConditionNotAfter";
    public static final String EVIDENCE_ACCESS_CONSENT_PROP = "evidenceAccessConsent";
    public static final String EVIDENCE_INST_ACCESS_CONSENT_PROP = "evidenceInstanceAccessConsent";
    public static final String EVIDENCE_RESOURCE_PROP = "evidenceResourcecProp";
    public static final String EVIDENCE_DECISION_PROP = "evidenceDecisionProp";
    public static final String SAML_VALID_INTERVAL_PROP = "samlValidInterval";
    public static final String AUDIT_QUERY_ACTION = "queryAuditLog";
    public static final String NOTIFY_ACTION = "notify";
    public static final String SUBSCRIBE_ACTION = "subscribe";
    public static final String UNSUBSCRIBE_ACTION = "unsubscribe";
    public static final String DOC_QUERY_ACTION = "queryDocuments";
    public static final String DOC_RETRIEVE_ACTION = "retrieveDocuments";
    public static final String SUBJECT_DISCOVERY_ACTION = "subjectDiscovery";
    public static final String PATIENT_DISCOVERY_ACTION = "patientDiscovery";
    public static final String ADAPTER_XDR_ACTION = "adapterXDRSecured";
    public static final String ADAPTER_XDRREQUEST_SECURED_ACTION = "adapterXDRRequestSecured";
    public static final String ADAPTER_XDRRESPONSE_SECURED_ACTION = "adapterXDRResponseSecured";
    public static final String ENTITY_XDR_SECURED_RESPONSE_ACTION = "entityXDRSecuredResponse";
    public static final String AUDIT_REPO_ACTION = "auditrepository";
    public static final String POLICY_ENGINE_ACTION = "policyengine";
    public static final String PAT_CORR_ACTION = "patientcorrelation";
    public static final String ADAPTER_MPI_ACTION = "mpi";
    public static final String XDR_ACTION = "xdr";
    public static final String XDR_REQUEST_ACTION = "xdrrequest";
    public static final String XDR_RESPONSE_ACTION = "xdrresponse";
    public static final String USERNAME_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:subject-id";
    public static final String USER_ORG_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:organization";
    public static final String USER_ORG_ID_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String HOME_COM_ID_ATTR = "urn:nhin:names:saml:homeCommunityId";
    public static final String USER_ROLE_ATTR = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String PURPOSE_ROLE_ATTR = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String PATIENT_ID_ATTR = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    public static final String ACCESS_CONSENT_ATTR = "AccessConsentPolicy";
    public static final String INST_ACCESS_CONSENT_ATTR = "InstanceAccessConsentPolicy";
    public static final String CE_CODE_ID = "code";
    public static final String CE_CODESYS_ID = "codeSystem";
    public static final String CE_CODESYSNAME_ID = "codeSystemName";
    public static final String CE_DISPLAYNAME_ID = "displayName";
    public static final String OID_TAG ="urn:oid:";
}
