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
package net.sourceforge.myfaces.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class VariableResolverMockImpl
    extends VariableResolver
{
    private static final Log log = LogFactory.getLog(VariableResolverMockImpl.class);
    private Map _map = new HashMap();

    public Object resolveVariable(FacesContext facesContext, String name)
    {
        Object o = _map.get(name);
        if (o == null)
        {
            log.info("Variable '" + name + "' could not be resolved.");
        }
        return o;
    }

    public void addVariable(String name, Object obj)
    {
        _map.put(name, obj);
    }

}
