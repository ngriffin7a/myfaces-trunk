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

import javax.servlet.ServletContext;


/**
 * ServletContext attributes as a Map.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.10  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.9  2004/07/01 22:05:04  mwessendorf
 * ASF switch
 *
 * Revision 1.8  2004/03/30 07:43:33  dave0000
 * add $Log
 *
 */
public class ApplicationMap extends AbstractAttributeMap
{
    final ServletContext _servletContext;

    ApplicationMap(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    protected Object getAttribute(String key)
    {
        return _servletContext.getAttribute(key);
    }

    protected void setAttribute(String key, Object value)
    {
        _servletContext.setAttribute(key, value);
    }

    protected void removeAttribute(String key)
    {
        _servletContext.removeAttribute(key);
    }

    protected Enumeration getAttributeNames()
    {
        return _servletContext.getAttributeNames();
    }
}
