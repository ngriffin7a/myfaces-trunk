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

import org.apache.myfaces.util.NullEnumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * HttpSession attibutes as Map.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.13  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.12  2004/07/01 22:05:04  mwessendorf
 * ASF switch
 *
 * Revision 1.11  2004/04/16 15:35:59  manolito
 * Log
 *
 */
public class SessionMap extends AbstractAttributeMap
{
    private final HttpServletRequest _httpRequest;

    SessionMap(HttpServletRequest httpRequest)
    {
        _httpRequest = httpRequest;
    }

    protected Object getAttribute(String key)
    {
        HttpSession httpSession = getSession();
        return (httpSession == null) 
            ? null : httpSession.getAttribute(key.toString());
    }

    protected void setAttribute(String key, Object value)
    {
        _httpRequest.getSession(true).setAttribute(key, value);
    }

    protected void removeAttribute(String key)
    {
        HttpSession httpSession = getSession();
        if (httpSession != null)
        {
            httpSession.removeAttribute(key);
        }
    }

    protected Enumeration getAttributeNames()
    {
        HttpSession httpSession = getSession();
        return (httpSession == null)
            ? NullEnumeration.instance()
            : httpSession.getAttributeNames();
    }
    
    private HttpSession getSession()
    {
        return _httpRequest.getSession(false);
    }
}