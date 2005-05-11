/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.context.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
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
