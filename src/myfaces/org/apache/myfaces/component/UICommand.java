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

/**
 * TODO: description
 * @author Manfred Geiler 
 * @version $Revision$ $Date$
 */
public class UICommand
    extends javax.faces.component.UICommand
    implements MyFacesComponent
{
    public static final String COMMAND_NAME_ATTR = "commandName";
    public static final String HREF_ATTR = "href";
    public static final String IMAGE_SRC_ATTR = "imageSrc";

    //MyFaces extensions
    public static final String COMMAND_REFERENCE_ATTR = "commandReference";


    public void setAttribute(String name, Object value)
    {
        if (name.equals(COMMAND_NAME_ATTR))
        {
            setAttribute(VALUE_ATTR, value);
        }
        else
        {
            super.setAttribute(name, value);
        }
    }

    public Object getAttribute(String name)
    {
        if (name.equals(COMMAND_NAME_ATTR))
        {
            return getAttribute(VALUE_ATTR);
        }
        else
        {
            return super.getAttribute(name);
        }
    }


    public String getCommandReference()
    {
        return (String)getAttribute(COMMAND_REFERENCE_ATTR);
    }

    public void setCommandReference(String commandReference)
    {
        setAttribute(COMMAND_REFERENCE_ATTR, commandReference);
    }


    //MyFacesComponentDelegate
    private MyFacesComponentDelegate _delegate = new MyFacesComponentDelegate(this);

    public boolean isTransient()
    {
        return _delegate.isTransient();
    }

    public void setTransient(boolean b)
    {
        _delegate.setTransient(b);
    }
}
