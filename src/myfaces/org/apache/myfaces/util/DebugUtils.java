/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.util;

import net.sourceforge.myfaces.tree.TreeUtils;
import org.apache.commons.logging.Log;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Utilities for logging.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DebugUtils
{
    private DebugUtils() 
    {
        // hide from public access
    }

    public static void logTree(Log log, String additionalMsg)
    {
        logTree(log, null, additionalMsg);
    }

    public static void logTree(Log log, UIViewRoot viewRoot, String additionalMsg)
    {
        if (log.isTraceEnabled())
        {
            if (viewRoot == null)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext == null)
                {
                    log.error("Cannot not print view to console (no FacesContext).");
                    return;
                }
                viewRoot = facesContext.getViewRoot();
            }

            log.trace("========================================");
            if (additionalMsg != null)
            {
                log.trace(additionalMsg);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TreeUtils.printTree(viewRoot, new PrintStream(baos));
            log.trace(baos.toString());
            log.trace("========================================");
        }
    }


    public static void assertError(boolean condition, Log log, String msg)
            throws FacesException
    {
        if (!condition)
        {
            log.error(msg);
            throw new FacesException(msg);
        }
    }

    public static void assertFatal(boolean condition, Log log, String msg)
        throws FacesException
    {
        if (!condition)
        {
            log.fatal(msg);
            throw new FacesException(msg);
        }
    }
}
