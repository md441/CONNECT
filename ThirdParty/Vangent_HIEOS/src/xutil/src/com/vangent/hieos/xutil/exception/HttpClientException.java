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

package com.vangent.hieos.xutil.exception;

public class HttpClientException extends XdsException {
	
	public HttpClientException(String msg) {
		super(msg);
	}

	public HttpClientException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
