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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyfacesComponentTag
        extends UIComponentTag
{
    private static final Log log = LogFactory.getLog(MyfacesComponentTag.class);

    //UIComponent attributes
    private String _rendererType;
    private String _transient;
    private String _value;

    //user role attributes (Myfaces extension)
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;


    protected abstract String getDefaultRendererType();

    public final String getRendererType()
    {
        return _rendererType == null ? getDefaultRendererType() : _rendererType;
    }

    public void setRendererType(String rendererType)
    {
        _rendererType = rendererType;
    }


    public void setTransient(String aTransient)
    {
        _transient = aTransient;
    }

    public void setValue(String value)
    {
        _value = value;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.TRANSIENT_ATTR, _transient);
        setBooleanProperty(component, JSFAttr.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setBooleanProperty(component, JSFAttr.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);

        if (_value != null)
        {
            if (component instanceof ValueHolder)
            {
                if (_value instanceof String && isValueReference((String)_value))
                {
                    ValueBinding vb = context.getApplication().createValueBinding((String)_value);
                    component.setValueBinding("value", vb);
                }
                else
                {
                    ((ValueHolder)component).setValue(_value);
                }
            }
            else
            {
                log.error("Component " + component.getClass().getName() + " is no ValueHolder, cannot set value.");
            }
        }
    }


    protected void setStringProperty(UIComponent component, String propName, String value)
    {
        if (value != null)
        {
            if (isValueReference((String)value))
            {
                ValueBinding vb = context.getApplication().createValueBinding((String)value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                component.getAttributes().put(propName, value);
            }
        }
    }


    protected void setBooleanProperty(UIComponent component, String propName, String value)
    {
        if (value != null)
        {
            if (isValueReference((String)value))
            {
                ValueBinding vb = context.getApplication().createValueBinding((String)value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                //TODO: More sophisticated way to convert boolean value (yes/no, 1/0, on/off, etc.)
                component.getAttributes().put(propName, Boolean.valueOf((String)value));
            }
        }
    }


}
