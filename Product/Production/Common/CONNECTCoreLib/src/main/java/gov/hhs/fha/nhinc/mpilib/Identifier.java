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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * 
 * @author rayj
 */
public class Identifier implements java.io.Serializable {
    static final long serialVersionUID = -4713959967816233116L;
        
    private String id = "";
    private String organizationId = "";
    
    public Identifier() {
    }

    public Identifier(String id, String organizationId) {
        this.id = id;
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String id) {
        organizationId = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override 
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Identifier))
            return false;

        return equals(this, (Identifier) obj);

    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (NullChecker.isNotNullish(id)) {
            hashCode = id.hashCode();
            if (NullChecker.isNotNullish(organizationId)) {
                hashCode += organizationId.hashCode();
            }
        }
        return hashCode;
    }
    
    private boolean equals(Identifier a, Identifier b) {        
        if ((NullChecker.isNullish(a.getId())) || (a.getOrganizationId() == null) ||
                (NullChecker.isNullish(b.getId())) || (b.getOrganizationId() == null) ) {
            return false;
        }

        return a.getId().contentEquals(b.getId()) && a.getOrganizationId().contentEquals(b.getOrganizationId());
    }


}
