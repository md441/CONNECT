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
package gov.hhs.fha.nhinc.admindistribution.nhin;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * 
 * @author dunnek
 */
public class NhinAdminDistributionOrchImpl {
    private Log log = null;

    public NhinAdminDistributionOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        log.info("begin sendAlert");
        // With the one-way service in a one-machine setup,
        // we were hanging on the next webservice call.
        // sleep allows Glassfish to catch up. Only applies to one box (dev)
        // setups. Please refer to the CONNECT 3.1 Release Notes for more information.
        this.checkSleep();

        auditMessage(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (isServiceEnabled()) {
            if (this.isInPassThroughMode() || checkPolicy(body, assertion)) {
                sendToAgency(body, assertion);
            }
        } else {
            log.warn("Service is disabled");
        }

        log.info("End sendAlert");
    }

    protected boolean isInPassThroughMode() {
        return new AdminDistributionHelper().isInPassThroughMode();
    }

    protected boolean isServiceEnabled() {
        return new AdminDistributionHelper().isServiceEnabled();
    }

    protected void sendToAgency(EDXLDistribution body, AssertionType assertion) {
        auditMessage(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        log.debug("begin send to agency");
        this.getAdapterAdminDistProxy().sendAlertMessage(body, assertion);

    }

    protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
        return this.getAdminFactory().getAdapterAdminDistProxy();
    }

    protected AdapterAdminDistributionProxyObjectFactory getAdminFactory() {
        return new AdapterAdminDistributionProxyObjectFactory();
    }

    protected AdminDistributionAuditLogger getLogger() {
        return new AdminDistributionAuditLogger();
    }

    protected boolean checkPolicy(EDXLDistribution body, AssertionType assertion) {
        boolean result = false;

        log.debug("begin checkPolicy");
        if (body != null) {
            result = this.getPolicyChecker().checkIncomingPolicy(body, assertion);
        } else {
            log.warn("EDXLDistribution was null");
        }

        log.debug("End Check Policy");
        return result;
    }

    protected AdminDistributionPolicyChecker getPolicyChecker() {
        return new AdminDistributionPolicyChecker();
    }

    private void checkSleep() {
        long sleep = 0;

        try {
            sleep = this.getSleepPeriod();
            if (sleep > 0) {
                log.debug("Admindistribution is sleeping...");
                Thread.sleep(sleep);
            }
        } catch (Exception ex) {
            log.error("Unable to sleep thread: " + ex);
        }

        log.debug("End checkSleep()");
    }

    protected long getSleepPeriod() {
        String result = "0";
        try {
            result = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    "administrativeDistributionSleepValue");
            log.debug("administrativeDistributionSleepValue = " + result);
        } catch (Exception ex) {
            log.warn("Unable to retrieve local home community id from Gateway.properties");
            log.warn(ex);
        }
        return Long.parseLong(result);
    }

    protected void auditMessage(EDXLDistribution body, AssertionType assertion, String direction,
            String logInterface) {
        AcknowledgementType ack = getLogger().auditNhinAdminDist(body, assertion, direction, logInterface);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }
    }
}
