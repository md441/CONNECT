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
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.hiem.dte.marshallers.NotificationMessageMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.NotifyMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscriptionReferenceMarshaller;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class NotifyBuilder {

    /**
     * this is intended to be used to build an "outbound" notification message based on an "inbound" notify and matching
     * subscribe. This includes updating the notify with the subscribe's subscription reference and wrapping the
     * notification message in a notify
     * 
     * @param notificationMessage
     * @param subscribe
     * @return
     */
    public Element buildNotifyFromSubscribe(Element notificationMessageElement, Element subscriptionReferenceElement) {
        NotificationMessageMarshaller notificationMessageMarshaller = new NotificationMessageMarshaller();
        NotificationMessageHolderType notificationMessage = notificationMessageMarshaller
                .unmarshal(notificationMessageElement);
        notificationMessage.setSubscriptionReference(null);

        SubscriptionReferenceMarshaller subscriptionReferenceMarshaller = new SubscriptionReferenceMarshaller();
        EndpointReferenceType subscriptionReference = subscriptionReferenceMarshaller
                .unmarshal(subscriptionReferenceElement);
        notificationMessage.setSubscriptionReference(subscriptionReference);

        Notify notify = new Notify();
        notify.getNotificationMessage().add(notificationMessage);

        NotifyMarshaller notifyMarshaller = new NotifyMarshaller();
        Element notifyElement = notifyMarshaller.marshal(notify);

        return notifyElement;
    }
}
