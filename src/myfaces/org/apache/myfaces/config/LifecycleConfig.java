/*
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
package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.PhaseListener;
import java.util.*;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @author Thomas Spiegl*
 * @version $Revision$ $Date$
 */
public class LifecycleConfig
    implements Config
{
    private static final Log log = LogFactory.getLog(LifecycleConfig.class);
    //~ Instance fields ----------------------------------------------------------------------------

    private ArrayList _phaseListeners;

    //~ Methods ------------------------------------------------------------------------------------

    public void addPhaseListenerClasses(Iterator phaseListenerClasses)
    {
        while (phaseListenerClasses.hasNext())
        {
            _phaseListeners.add(phaseListenerClasses.next());
        }
    }

    public void addPhaseListener(String phaseListener)
    {
        try
        {
            Class clazz = Class.forName(phaseListener, true, Thread.currentThread().getContextClassLoader());
            if (PhaseListener.class.isAssignableFrom(clazz))
            {
                if (_phaseListeners == null)
                {
                    _phaseListeners = new ArrayList();
                }
                _phaseListeners.add(clazz);
            }
            else
            {
                log.error("Error in faces-config.xml - Class " + phaseListener +
                    " must be an instance of " + PhaseListener.class.getName());
            }
        }
        catch (ClassNotFoundException e)
        {
            log.error("Error in faces-config.xml - Class not found: " + phaseListener);
        }
    }

    public Iterator getPhaseListenerClasses()
    {
        if (_phaseListeners == null)
        {
            return Collections.EMPTY_LIST.iterator();
        }
        return _phaseListeners.iterator();
    }
}
