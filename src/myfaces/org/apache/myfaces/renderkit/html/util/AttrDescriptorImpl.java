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
package net.sourceforge.myfaces.renderkit.html.util;

import javax.faces.component.AttributeDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @deprecated
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AttrDescriptorImpl
    extends AttributeDescriptor
{
    private static final String RESOURCE = "net.sourceforge.myfaces.renderkit.html.util.AttrDescriptor";
    private static final String DESCRIPTION_NAME_SUFFIX = ".descr";
    private static final String DISPLAY_NAME_SUFFIX     = ".displ";

    private String _name;
    private Class _type;

    public AttrDescriptorImpl(String name)
    {
        this(name, String.class);
    }

    public AttrDescriptorImpl(String name, Class type)
    {
        _name = name;
        _type = type;
    }

    public String getDescription()
    {
        return getDescription(Locale.getDefault());
    }

    public String getDescription(Locale locale)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, locale);
        String s = bundle.getString(_name + DESCRIPTION_NAME_SUFFIX);
        return s != null ? s : getDisplayName(locale);
    }

    public String getDisplayName()
    {
        return null;
    }

    public String getDisplayName(Locale locale)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, locale);
        String s = bundle.getString(_name + DISPLAY_NAME_SUFFIX);
        return s != null ? s : _name;
    }

    public String getName()
    {
        return _name;
    }

    public Class getType()
    {
        return _type;
    }


    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AttrDescriptorImpl)) return false;

        final AttrDescriptorImpl attrDescr = (AttrDescriptorImpl)o;

        if (!_name.equals(attrDescr._name)) return false;
        if (!_type.equals(attrDescr._type)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = _name.hashCode();
        result = 29 * result + _type.hashCode();
        return result;
    }
}
