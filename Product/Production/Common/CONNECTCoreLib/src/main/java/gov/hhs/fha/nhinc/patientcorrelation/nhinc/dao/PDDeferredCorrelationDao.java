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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.PDDeferredCorrelation;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hl7.v3.II;

/**
 * 
 * @author akong
 */
public class PDDeferredCorrelationDao {

    private static Log log = LogFactory.getLog(PDDeferredCorrelationDao.class);

    /**
     * Query by Message Id. This should return only one record.
     * 
     * @param messageId
     * @return matching records
     */
    public II queryByMessageId(String messageId) {
        log.debug("Performing database record retrieve using message id: " + messageId);

        List<PDDeferredCorrelation> pdCorrelations = null;
        Session sess = null;
        II patientId = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(PDDeferredCorrelation.class);
                    criteria.add(Restrictions.eq("MessageId", messageId));

                    pdCorrelations = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve by message id. Results found: "
                        + ((pdCorrelations == null) ? "0" : Integer.toString(pdCorrelations.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        if ((pdCorrelations == null) || (pdCorrelations.size() != 1)) {
            log.error("Failed to find a unique patient id with the given message id " + messageId);
        } else {
            PDDeferredCorrelation pdCorrelation = pdCorrelations.get(0);
            patientId = new II();
            patientId.setExtension(pdCorrelation.getPatientId());
            patientId.setRoot(pdCorrelation.getAssigningAuthorityId());
        }

        return patientId;
    }

    /**
     * Copies the field values from the source to the destination.
     * 
     * @param source
     * @param dest
     */
    public void copyValues(PDDeferredCorrelation source, PDDeferredCorrelation dest) {
        dest.setAssigningAuthorityId(source.getAssigningAuthorityId());
        dest.setCreationTime(source.getCreationTime());
        dest.setPatientId(source.getPatientId());
    }

    /**
     * Saves a record to the database. Updates if the message id already exists in the database.
     * 
     * @param messageId
     * @param patientId
     */
    public void saveOrUpdate(String messageId, II patientId) {
        PDDeferredCorrelation pdCorrelation = new PDDeferredCorrelation();
        pdCorrelation.setMessageId(messageId);
        pdCorrelation.setPatientId(patientId.getExtension());
        pdCorrelation.setAssigningAuthorityId(patientId.getRoot());
        pdCorrelation.setCreationTime(new Date());
        saveOrUpdate(pdCorrelation);
    }

    /**
     * Save a record to the database. Updates if the message id already exists in the database.
     * 
     * @param object to save.
     */
    public void saveOrUpdate(PDDeferredCorrelation pdCorrelation) {
        log.debug("PDDeferredCorrelationDao.save() - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();

                    Criteria criteria = sess.createCriteria(PDDeferredCorrelation.class);
                    criteria.add(Restrictions.eq("MessageId", pdCorrelation.getMessageId()));
                    List<PDDeferredCorrelation> pdCorrelations = criteria.list();
                    if ((pdCorrelations != null) && (pdCorrelations.size() == 1)) {
                        PDDeferredCorrelation updatedPdCorrelation = pdCorrelations.get(0);
                        copyValues(pdCorrelation, updatedPdCorrelation);
                        sess.saveOrUpdate(updatedPdCorrelation);
                    } else {
                        sess.saveOrUpdate(pdCorrelation);
                    }
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        log.debug("PDDeferredCorrelationDao.save() - End");
    }

}
