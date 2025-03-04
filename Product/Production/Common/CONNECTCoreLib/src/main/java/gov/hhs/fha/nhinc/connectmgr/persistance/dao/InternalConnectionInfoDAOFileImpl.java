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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.File;

import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 * 
 * @author kshtabnoy
 * 
 *         Implementation of ConnectionManagerDAO that stores connection information in local file
 * 
 */
public class InternalConnectionInfoDAOFileImpl extends ConnectionManagerDAOBase implements ConnectionManagerDAO {

    private static InternalConnectionInfoDAOFileImpl instance = null;
    private File file = null;
    private Log log = null;
    private static String fileLocation = null;
    private static final String INTERNAL_XML_FILE_NAME = "internalConnectionInfo.xml";
    private static boolean failedToLoadEnvVar = false;

    public static InternalConnectionInfoDAOFileImpl getInstance() {
        if (instance == null) {
            instance = new InternalConnectionInfoDAOFileImpl();
        }

        return instance;
    }

    InternalConnectionInfoDAOFileImpl() {
        log = getLogger();

        String fileName = getInternalConnectionFileLocation();
        log.debug("Reading InternalConnectionInfo from file: " + fileName);
        file = new File(fileName);
    }

    public String getInternalConnectionFileLocation() {
        if (fileLocation == null) {
            String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();
            if ((sValue != null) && (sValue.length() > 0)) {
                if (sValue.endsWith(File.separator)) {
                    fileLocation = sValue + INTERNAL_XML_FILE_NAME;
                } else {
                    fileLocation = sValue + File.separator + INTERNAL_XML_FILE_NAME;
                }
            } else {
                failedToLoadEnvVar = true;
            }
        }

        return fileLocation;
    }

    @Override
    public BusinessDetail loadBusinessDetail() throws Exception {
        if (failedToLoadEnvVar) {
            throw new ConnectionManagerException("Unable to access system variable: nhinc.properties.dir.");
        }

        BusinessDetail resp = null;
        try {
            resp = super.loadBusinessDetail(file);
        } catch (JAXBException ex) {
            getLogger().error("unable to load business entities from " + file.getName(), ex);
            resp = new BusinessDetail();
            throw new Exception("unable to load business entities from " + file.getName(), ex);
        }
        return resp;
    }

    @Override
    public void saveBusinessDetail(BusinessDetail BusinessDetail) {
        super.saveBusinessDetail(BusinessDetail, file);
    }

    public long getLastModified() {
        if (file.exists()) {
            return file.lastModified();
        } else {
            return 0;
        }
    }

    @Override
    protected Log getLogger() {
        if (log == null) {
            setLogger(LogFactory.getLog(this.getClass()));
        }
        return log;
    }

    @Override
    protected void setLogger(Log log) {
        this.log = log;
    }

    public void setFileName(String fileName) {
        file = new File(fileName);
    }
}
