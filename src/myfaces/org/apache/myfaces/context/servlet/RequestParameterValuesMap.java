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

import javax.servlet.ServletRequest;
import java.util.Enumeration;

/**
 * Helper class for {@link net.sourceforge.myfaces.context.servlet.ServletExternalContextImpl}
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RequestParameterValuesMap
    extends AbstractAttributeMap
{
    private ServletRequest _request;

    RequestParameterValuesMap(ServletRequest request)
    {
        _request = request;
    }

    protected Object getAttribute(String name)
    {
        return _request.getParameterValues(name);
    }

    protected void setAttribute(String name, Object newVal)
    {
        throw new UnsupportedOperationException();
    }

    protected void removeAttribute(String name)
    {
        throw new UnsupportedOperationException();
    }

    protected Enumeration getAttributeNames()
    {
        return _request.getParameterNames();
    }

}
