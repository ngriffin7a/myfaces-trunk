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
package net.sourceforge.myfaces.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesLogger
    extends Logger
{
    public static final String LOGGER_NAME = "MyFacesLogger";

    public MyFacesLogger()
    {
        super(LOGGER_NAME, Logger.global.getResourceBundleName());
    }


    public void exiting()
    {
        internalExiting(null, Level.FINER);
    }

    public void exiting(String msg)
    {
        internalExiting(msg, Level.FINER);
    }

    public void exiting(String msg, Level level)
    {
        internalExiting(msg, level);
    }

    private void internalExiting(String msg, Level level)
    {
        String methodName = "?";
        String className = "?";

        if (getLevel().intValue() <= level.intValue())
        {
            //only throw exception when logging will happen
            Throwable t = new Throwable();
            StackTraceElement ste = t.getStackTrace()[2];
            methodName = ste.getMethodName();
            className = ste.getClassName();
        }

        logp(level, className, methodName,
             msg != null ? ("RETURN (" + msg + ")") : "RETURN");
    }



    public void entering()
    {
        internalExiting(null, Level.FINER);
    }

    private void internalEntering(String msg, Level level)
    {
        String methodName = "?";
        String className = "?";

        if (getLevel().intValue() <= level.intValue())
        {
            //only throw exception when logging will happen
            Throwable t = new Throwable();
            StackTraceElement ste = t.getStackTrace()[2];
            methodName = ste.getMethodName();
            className = ste.getClassName();
        }

        logp(level, className, methodName,
             msg != null ? ("ENTRY (" + msg + ")") : "ENTRY");
    }

}
