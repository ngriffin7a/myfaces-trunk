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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Spec 1.0 EA - JSF.6.5 LifecycleFactory
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LifecycleFactoryImpl
        extends LifecycleFactory
{
    private static final Object _lock = new Object();
    private static Map _lifecycles = new HashMap();

    public LifecycleFactoryImpl()
    {
        _lifecycles.put(DEFAULT_LIFECYCLE, new LifecycleImpl());
    }

    public void addLifecycle(String id, Lifecycle lifecycle)
    {
        synchronized (_lock)
        {
            if (_lifecycles.get(id) != null)
            {
                throw new IllegalArgumentException("Lifecycle with id '" + id + "' already exists.");
            }
            _lifecycles.put(id, lifecycle);
        }
    }

    public Lifecycle getLifecycle(String id)
            throws FacesException
    {
        synchronized (_lock)
        {
            Lifecycle lifecycle = (Lifecycle)_lifecycles.get(id);
            if (lifecycle == null)
            {
                throw new IllegalArgumentException("Unknown lifecycle '" + id + "'.");
            }
            return lifecycle;
        }
    }

    public Iterator getLifecycleIds()
    {
        return _lifecycles.keySet().iterator();
    }
}
