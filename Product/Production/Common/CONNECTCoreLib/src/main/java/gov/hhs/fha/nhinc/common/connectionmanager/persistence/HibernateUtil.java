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
package gov.hhs.fha.nhinc.common.connectionmanager.persistence;

import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;

/**
 * This class will be used as a Utility Class to access the Data Object using Hibernate SessionFactory
 * 
 * @author svalluripalli
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);

    private static final String HIBERNATE_ASSIGNING_AUTHORITY = "assignauthority.hibernate.cfg.xml";
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Method returns an instance of Hibernate SessionFactory
     * 
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getInstance().getHibernateFile(HIBERNATE_ASSIGNING_AUTHORITY);
        } catch (Exception ex) {
            log.error("Unable to load " + HIBERNATE_ASSIGNING_AUTHORITY + " " + ex.getMessage(), ex);
        }

        return result;

    }
}
