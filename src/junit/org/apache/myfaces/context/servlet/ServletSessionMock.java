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
package net.sourceforge.myfaces.context.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/10 05:30:15  dave0000
 * Fix issue with setting Managed Bean to a wrong scope
 *
 */
public class ServletSessionMock implements HttpSession
{
    private final Map _attributes = new HashMap();

    public ServletSessionMock()
    {
        super();
        // FIXME Auto-generated constructor stub
    }

    public long getCreationTime()
    {
        // FIXME Auto-generated method stub
        return 0;
    }

    public String getId()
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public long getLastAccessedTime()
    {
        // FIXME Auto-generated method stub
        return 0;
    }

    public ServletContext getServletContext()
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public void setMaxInactiveInterval(int arg0)
    {
        // FIXME Auto-generated method stub
        
    }

    public int getMaxInactiveInterval()
    {
        // FIXME Auto-generated method stub
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public Object getAttribute(String key)
    {
        return _attributes.get(key);
    }

    public Object getValue(String arg0)
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public Enumeration getAttributeNames()
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public String[] getValueNames()
    {
        // FIXME Auto-generated method stub
        return null;
    }

    public void setAttribute(String key, Object value)
    {
        _attributes.put(key, value);
    }

    public void putValue(String arg0, Object arg1)
    {
        // FIXME Auto-generated method stub
        
    }

    public void removeAttribute(String arg0)
    {
        // FIXME Auto-generated method stub
        
    }

    public void removeValue(String arg0)
    {
        // FIXME Auto-generated method stub
        
    }

    public void invalidate()
    {
        // FIXME Auto-generated method stub
        
    }

    public boolean isNew()
    {
        // FIXME Auto-generated method stub
        return false;
    }

}
