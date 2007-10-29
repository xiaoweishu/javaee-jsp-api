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
package org.apache.catalina.ssi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.catalina.util.IOTools;
/**
 * Implements the Server-side #exec command
 * 
 * @author Bip Thelin
 * @author Amy Roh
 * @author Paul Speed
 * @author Dan Sandberg
 * @author David Becker
 * @version $Revision: 1.3 $, $Date: 2007/02/13 19:16:21 $
 */
public class SSIExec implements SSICommand {
    protected SSIInclude ssiInclude = new SSIInclude();
    protected final static int BUFFER_SIZE = 1024;


    /**
     * @see SSICommand
     */
    public long process(SSIMediator ssiMediator, String commandName,
            String[] paramNames, String[] paramValues, PrintWriter writer) {
        long lastModified = 0;
        String configErrMsg = ssiMediator.getConfigErrMsg();
        String paramName = paramNames[0];
        String paramValue = paramValues[0];
        String substitutedValue = ssiMediator.substituteVariables(paramValue);
        if (paramName.equalsIgnoreCase("cgi")) {
            lastModified = ssiInclude.process(ssiMediator, "include",
                    			new String[]{"virtual"}, new String[]{substitutedValue},
								writer);
        } else if (paramName.equalsIgnoreCase("cmd")) {
            boolean foundProgram = false;
            try {
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(substitutedValue);
                foundProgram = true;
                BufferedReader stdOutReader = new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
                BufferedReader stdErrReader = new BufferedReader(
                        new InputStreamReader(proc.getErrorStream()));
                char[] buf = new char[BUFFER_SIZE];
                IOTools.flow(stdErrReader, writer, buf);
                IOTools.flow(stdOutReader, writer, buf);
                proc.waitFor();
                lastModified = System.currentTimeMillis();                
            } catch (InterruptedException e) {
                ssiMediator.log("Couldn't exec file: " + substitutedValue, e);
                writer.write(configErrMsg);
            } catch (IOException e) {
                if (!foundProgram) {
                    //apache doesn't output an error message if it can't find
                    // a program
                }
                ssiMediator.log("Couldn't exec file: " + substitutedValue, e);
            }
        }
        return lastModified;
    }
}