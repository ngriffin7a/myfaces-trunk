/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.webapp.filter;

import javax.servlet.ServletContext;

/**
 * The Factory provides a generic approach to providing
 * a string array of names of suitable welcome files for
 * a particular web application context.
 *
 * @author R.J. Lebowitz
 * @version $Revision$ $Date${DATE} ${TIME} $
 */
public class WelcomeFileFactory
{
    private WelcomeFileFactory()
    {
    }

    /**
     * Since the manner in which the welcome-file attribute is
     * JSP container-specific, it will be necessary to modify
     * this method to support as many containers as possible.
     *
     * At present, this factory class supports the following
     * JSP containers:
     *
     * - Tomcat 4.1
     *
     * @param context
     * @return String[]
     */
    public static String[] getWelcomeFiles(ServletContext context)
    {
        String[] welcomeFiles = null;
        Object attr = context.getAttribute("org.apache.catalina.WELCOME_FILES");
        if (attr != null)
        {
            welcomeFiles = (String[])attr;
        }
        return welcomeFiles;
    }

}

