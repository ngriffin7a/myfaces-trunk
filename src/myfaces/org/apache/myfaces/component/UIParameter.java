/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.component;

import javax.faces.component.UIComponentBase;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class UIParameter
    extends UIComponentBase //TODO: javax.faces.component.UIParameter
{
    public static final String NAME_ATTR = "name";

    //public static final String TYPE = UIParameter.class.getName();
    public static final String TYPE = "javax.faces.component.UIParameter";

    public String getComponentType()
    {
        return TYPE;
    }


    public String getName()
    {
        return (String)getAttribute(NAME_ATTR);
    }

    public void setName(String name)
    {
        setAttribute(NAME_ATTR, name);
    }
}
