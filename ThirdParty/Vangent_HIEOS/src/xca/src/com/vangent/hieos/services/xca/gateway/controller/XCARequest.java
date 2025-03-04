/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.services.xca.gateway.controller;

import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
public class XCARequest {

    OMElement request;

    /**
     *
     * @param requestNode
     */
    public XCARequest(OMElement request) {
        this.request = request;
    }

    /**
     *
     * @return
     */
    public OMElement getRequest()
    {
        return this.request;
    }
}
