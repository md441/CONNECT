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
package gov.hhs.fha.nhinc.patientdb.model;

import java.util.List;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.ArrayList;

/**
 * 
 * 
 * 
 * @author richard.ettema
 */

public class Patient implements Serializable {

    /**
     * 
     * Attribute patientId.
     */

    private Long patientId;

    /**
     * 
     * Attribute dateOfBirth.
     */

    private Timestamp dateOfBirth;

    /**
     * 
     * Attribute gender.
     */

    private String gender;

    /**
     * 
     * Attribute ssn.
     */

    private String ssn;

    /**
     * 
     * List of Addresses
     */

    private List<Address> addresses = null;

    /**
     * 
     * List of Identifiers
     */

    private List<Identifier> identifiers = null;

    /**
     * 
     * List of Personnames
     */

    private List<Personname> personnames = null;

    /**
     * 
     * List of Phonenumbers
     */

    private List<Phonenumber> phonenumbers = null;

    /**
     * 
     * @return patientId
     */

    public Long getPatientId() {

        return patientId;

    }

    /**
     * 
     * @param patientId new value for patientId
     */

    public void setPatientId(Long patientId) {

        this.patientId = patientId;

    }

    /**
     * 
     * @return dateOfBirth
     */

    public Timestamp getDateOfBirth() {

        return dateOfBirth;

    }

    /**
     * 
     * @param dateOfBirth new value for dateOfBirth
     */

    public void setDateOfBirth(Timestamp dateOfBirth) {

        this.dateOfBirth = dateOfBirth;

    }

    /**
     * 
     * @return gender
     */

    public String getGender() {

        return gender;

    }

    /**
     * 
     * @param gender new value for gender
     */

    public void setGender(String gender) {

        this.gender = gender;

    }

    /**
     * 
     * @return ssn
     */

    public String getSsn() {

        return ssn;

    }

    /**
     * 
     * @param ssn new value for ssn
     */

    public void setSsn(String ssn) {

        this.ssn = ssn;

    }

    /**
     * 
     * Get the list of Addresses
     */

    public List<Address> getAddresses() {

        if (this.addresses == null) {

            this.addresses = new ArrayList<Address>();

        }

        return this.addresses;

    }

    /**
     * 
     * Set the list of Addresses
     */

    public void setAddresses(List<Address> addresses) {

        this.addresses = addresses;

    }

    /**
     * 
     * Get the list of Identifiers
     */

    public List<Identifier> getIdentifiers() {

        if (this.identifiers == null) {

            this.identifiers = new ArrayList<Identifier>();

        }

        return this.identifiers;

    }

    /**
     * 
     * Set the list of Identifiers
     */

    public void setIdentifiers(List<Identifier> identifiers) {

        this.identifiers = identifiers;

    }

    /**
     * 
     * Get the list of Personnames
     */

    public List<Personname> getPersonnames() {

        if (this.personnames == null) {

            this.personnames = new ArrayList<Personname>();

        }

        return this.personnames;

    }

    /**
     * 
     * Set the list of Personnames
     */

    public void setPersonnames(List<Personname> personnames) {

        this.personnames = personnames;

    }

    /**
     * 
     * Get the list of Phonenumbers
     */

    public List<Phonenumber> getPhonenumbers() {

        if (this.phonenumbers == null) {

            this.phonenumbers = new ArrayList<Phonenumber>();

        }

        return this.phonenumbers;

    }

    /**
     * 
     * Set the list of Phonenumbers
     */

    public void setPhonenumbers(List<Phonenumber> phonenumbers) {

        this.phonenumbers = phonenumbers;

    }

    @Override
    public String toString() {

        StringBuffer output = new StringBuffer("");

        int counter = 0;

        for (Identifier identifier : this.getIdentifiers()) {

            output.append("Identifer[").append(counter).append("] = '").append(identifier.getId()).append("^^^&")
                    .append(identifier.getOrganizationId()).append("&ISO'; ");

        }

        if (this.getPersonnames().size() > 0) {

            output.append("Personname = '").append(this.getPersonnames().get(0).getLastName()).append(", ")
                    .append(this.getPersonnames().get(0).getFirstName()).append("'; ");

        }

        output.append("Gender = '").append(this.gender).append("'; ");

        output.append("DateOfBirth = '").append(this.dateOfBirth);

        return output.toString();

    }

}
