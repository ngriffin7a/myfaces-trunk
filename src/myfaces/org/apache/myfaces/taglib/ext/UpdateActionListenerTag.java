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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.event.UpdateActionListener;
import net.sourceforge.myfaces.taglib.core.ActionListenerTag;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UpdateActionListenerTag
        extends ActionListenerTag
{
    //private static final Log log = LogFactory.getLog(UpdateActionListenerTag.class);

    private String _property;
    private String _value;
    private String _converter;

    public UpdateActionListenerTag()
    {
    }

    public void setProperty(String property)
    {
        _property = property;
    }

    public void setValue(String value)
    {
        _value = value;
    }

    public void setConverter(String converter)
    {
        _converter = converter;
    }

    public int doStartTag() throws JspException
    {
        if (_property == null) throw new JspException("property attribute not set");
        if (_value == null) throw new JspException("value attribute not set");
        if (!UIComponentTag.isValueReference(_property)) throw new JspException("property attribute is no valid value reference: " + _property);

        //Find parent UIComponentTag
        UIComponentTag componentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (componentTag == null)
        {
            throw new JspException("UpdateActionListenerTag has no UIComponentTag ancestor");
        }

        if (componentTag.getCreated())
        {
            //Component was just created, so we add the Listener
            UIComponent component = componentTag.getComponentInstance();
            if (component instanceof ActionSource)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Application application = facesContext.getApplication();
                UpdateActionListener al = new UpdateActionListener();
                al.setPropertyBinding(application.createValueBinding(_property));
                if (UIComponentTag.isValueReference(_value))
                {
                    al.setValueBinding(application.createValueBinding(_value));
                }
                else
                {
                    al.setValue(_value);
                }
                if (_converter != null)
                {
                    Converter converter = application.createConverter(_converter);
                    al.setConverter(converter);
                }
                ((ActionSource)component).addActionListener(al);
            }
            else
            {
                throw new JspException("Component " + component.getId() + " is no ActionSource");
            }
        }

        return Tag.SKIP_BODY;
    }
}
