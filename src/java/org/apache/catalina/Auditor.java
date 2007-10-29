/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * Portions Copyright Apache Software Foundation.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
 

package org.apache.catalina;


/**
 * Defines interface of classes which implement audit functionality.
 *
 * <P>An <b>Auditor</b> class can be registered with a Context and
 * will receive notification of all auditable events processed by the
 * Authenticator of that context.
 *
 * <P> IASRI 4823322
 *
 * @author Jyri J. Virkki
 * @version $Revision: 1.2 $
 *
 */

public interface Auditor
{
    
    /**
     * Notify auditor of an authentication event.
     *
     * <P>This method will get invoked on every login attempt whether
     * it was approved or denied by the authentication infrastructure.
     *
     * @param user the user for whom authentication was processed
     * @param realm the realm which handled the authentication
     * @param succcess true if the authentication succeeded, false if denied
     *
     */
    public void authentication(String user, String realm, boolean success);

    
    /**
     * Notify auditor of a servlet container invocation.
     *
     * <P>This method will get invoked on every request whether it
     * was permitted or not by the authorization infrastructure.
     *     
     * @param req the HttpRequest
     * @param success true if the invocation was allowed, false if denied.
     *
     */
    public void webInvocation(HttpRequest req, boolean success);

    
}
