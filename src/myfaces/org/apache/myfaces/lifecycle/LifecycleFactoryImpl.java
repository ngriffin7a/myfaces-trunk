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
package net.sourceforge.myfaces.lifecycle;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Collections;
import java.util.Iterator;

/**
 * Spec 1.0 EA - JSF.6.5 LifecycleFactory
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LifecycleFactoryImpl
        extends LifecycleFactory
{
    private static final Object _lock = new Object();
    private static Lifecycle _lifecycle = null;

    public void addLifecycle(String s, Lifecycle lifecycle)
    {
        //TODO
        throw new UnsupportedOperationException();
    }

    public Lifecycle getLifecycle(String s)
            throws FacesException
    {
        if (!s.equals(DEFAULT_LIFECYCLE))
        {
            throw new IllegalArgumentException("Only default lifecycle supported!");
        }
        synchronized (_lock)
        {
            if (_lifecycle == null)
            {
                _lifecycle = new LifecycleImpl();
            }
            return _lifecycle;
        }
    }

    public Iterator getLifecycleIds()
    {
        return Collections.singleton(getLifecycle(DEFAULT_LIFECYCLE)).iterator();
    }
}
