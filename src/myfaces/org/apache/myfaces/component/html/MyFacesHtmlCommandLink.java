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
package net.sourceforge.myfaces.component.html;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class MyFacesHtmlCommandLink
    extends HtmlCommandLink
{
    private ValueBinding _actionUpdateProperty;

    public ValueBinding getActionUpdateProperty()
    {
        return _actionUpdateProperty;
    }

    public void setActionUpdateProperty(ValueBinding actionUpdateProperty)
    {
        _actionUpdateProperty = actionUpdateProperty;
    }


    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof ActionEvent)
        {
            ValueBinding vb = getActionUpdateProperty();
            if (vb != null)
            {
                FacesContext context = getFacesContext();
                Object updateValue;
                if (_actionUpdateValue != null)
                {
                    Class type = vb.getType(context);
                    updateValue = ClassUtils.convertToType(_actionUpdateValue, type);
                }
                else
                {
                    updateValue = getActionUpdateValue();
                }
                vb.setValue(context, updateValue);
            }
        }

        super.broadcast(event);
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlCommandLink";
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Link";

    private String _target = null;
    private String _actionUpdateValue = null;

    public MyFacesHtmlCommandLink()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setTarget(String target)
    {
        _target = target;
    }

    public String getTarget()
    {
        if (_target != null) return _target;
        ValueBinding vb = getValueBinding("target");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActionUpdateValue(String actionUpdateValue)
    {
        _actionUpdateValue = actionUpdateValue;
    }

    public String getActionUpdateValue()
    {
        if (_actionUpdateValue != null) return _actionUpdateValue;
        ValueBinding vb = getValueBinding("actionUpdateValue");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _target;
        values[2] = saveAttachedState(context, _actionUpdateProperty);
        values[3] = _actionUpdateValue;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _target = (String)values[1];
        _actionUpdateProperty = (ValueBinding)restoreAttachedState(context, values[2]);
        _actionUpdateValue = (String)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
