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
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyfacesComponentBodyTag
        extends UIComponentBodyTag
{
    private static final Log log = LogFactory.getLog(MyfacesComponentBodyTag.class);

    public int doEndTag() throws JspException
    {
        if (log.isWarnEnabled())
        {
            UIComponent component = getComponentInstance();
            if (component != null &&
                component.getRendersChildren() &&
                !isBodyContentEmpty())
            {
                log.warn("Component with id '" + component.getClientId(getFacesContext()) + "' (" + getClass().getName() + " tag) renders it's children, but has embedded JSP or HTML code. Use the <f:verbatim> tag for nested JSP or HTML code!");
            }
        }
        return super.doEndTag();
    }

    private boolean isBodyContentEmpty()
    {
        BodyContent bodyContent = getBodyContent();
        if (bodyContent == null)
        {
            return true;
        }
        try
        {
            Reader reader = bodyContent.getReader();
            int c;
            while ((c = reader.read()) != -1)
            {
                if (!Character.isWhitespace((char)c))
                {
                    return false;
                }
            }
            return true;
        }
        catch (IOException e)
        {
            log.error("Error inspecting BodyContent", e);
            return false;
        }
    }

    //-------- rest is identical to MyfacesComponentTag ------------------

    /**
     * Must be implemented by sub classes.
     */
    protected abstract String getDefaultRendererType();

    //UIComponent attributes
    private String _transient;

    //user role attributes (Myfaces extension)
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    //Special UIComponent attributes (ValueHolder, ConvertibleValueHolder)
    private String _rendererType;
    private String _value;
    private String _converterId;
    //attributes id, rendered and binding are handled by UIComponentTag

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.TRANSIENT_ATTR, _transient);

        setBooleanProperty(component, JSFAttr.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setBooleanProperty(component, JSFAttr.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);

        //rendererType already handled by UIComponentTag

        setValueProperty(component, _value);
        setConverterIdProperty(component, _converterId);
    }

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

    public void setConverterId(String converterId)
    {
        _converterId = converterId;
    }



    // sub class helpers

    protected void setIntegerProperty(UIComponent component, String propName, String value)
    {
        MyfacesTagUtils.setIntegerProperty(getFacesContext(), component, propName, value);
    }

    protected void setStringProperty(UIComponent component, String propName, String value)
    {
        MyfacesTagUtils.setStringProperty(getFacesContext(), component, propName, value);
    }

    protected void setBooleanProperty(UIComponent component, String propName, String value)
    {
        MyfacesTagUtils.setBooleanProperty(getFacesContext(), component, propName, value);
    }

    protected void setValueProperty(UIComponent component, String value)
    {
        MyfacesTagUtils.setValueProperty(getFacesContext(), component, value);
    }

    private void setConverterIdProperty(UIComponent component, String value)
    {
        MyfacesTagUtils.setConverterIdProperty(getFacesContext(), component, value);
    }

    protected void setActionProperty(UIComponent component, String action)
    {
        MyfacesTagUtils.setActionProperty(getFacesContext(), component, action);
    }

    protected void setActionListenerProperty(UIComponent component, String actionListener)
    {
        MyfacesTagUtils.setActionListenerProperty(getFacesContext(), component, actionListener);
    }

    protected void setValueChangedListenerProperty(UIComponent component, String valueChangedListener)
    {
        MyfacesTagUtils.setValueChangedListenerProperty(getFacesContext(), component, valueChangedListener);
    }

    protected void setValueBinding(UIComponent component,
                                   String propName,
                                   String value)
    {
        MyfacesTagUtils.setValueBinding(getFacesContext(), component, propName, value);
    }

}
