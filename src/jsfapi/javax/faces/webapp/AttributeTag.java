/*
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
package javax.faces.webapp;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AttributeTag
        extends TagSupport
{
    private String _name;
    private String _value;

    public void setName(String name)
    {
        _name = name;
    }

    public void setValue(String value)
    {
        _value = value;
    }

    public int doStartTag() throws JspException
    {
        UIComponentTag componentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (componentTag == null)
        {
            throw new JspException("no parent UIComponentTag found");
        }
        UIComponent component = componentTag.getComponentInstance();
        if (component == null)
        {
            throw new JspException("parent UIComponentTag has no UIComponent");
        }
        String name = getName();
        if (component.getAttributes().get(name) == null)
        {
            component.getAttributes().put(name, getValue());
        }
        return Tag.SKIP_BODY;
    }

    public void release()
    {
        _name = null;
        _value = null;
    }


    private String getName()
    {
        if (UIComponentTag.isValueReference(_name))
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ValueBinding vb = facesContext.getApplication().createValueBinding(_name);
            return (String)vb.getValue(facesContext);
        }
        else
        {
            return _name;
        }
    }

    private String getValue()
    {
        if (UIComponentTag.isValueReference(_value))
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ValueBinding vb = facesContext.getApplication().createValueBinding(_value);
            return (String)vb.getValue(facesContext);
        }
        else
        {
            return _value;
        }
    }

}
