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
package org.apache.myfaces.context.portlet;

import java.util.Enumeration;
import javax.portlet.PortletRequest;
import org.apache.myfaces.context.servlet.AbstractAttributeMap;



/**
 * PortletRequest headers as Map.
 * 
 * @author  Stan Silvert (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2005/01/26 17:03:09  matzew
 * MYFACES-86. portlet support provided by Stan Silver (JBoss Group)
 *
 */
public class RequestHeaderMap extends AbstractAttributeMap
{
    private final PortletRequest _portletRequest;

    RequestHeaderMap(PortletRequest portletRequest)
    {
        _portletRequest = portletRequest;
    }

    protected Object getAttribute(String key)
    {
        return _portletRequest.getProperty(key);
    }

    protected void setAttribute(String key, Object value)
    {
        throw new UnsupportedOperationException(
            "Cannot set PortletRequest property");
    }

    protected void removeAttribute(String key)
    {
        throw new UnsupportedOperationException(
            "Cannot remove PortletRequest property");
    }

    protected Enumeration getAttributeNames()
    {
        return _portletRequest.getPropertyNames();
    }
}
