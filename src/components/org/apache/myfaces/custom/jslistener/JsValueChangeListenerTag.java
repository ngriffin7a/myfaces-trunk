/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.jslistener;

import org.apache.myfaces.custom.updateactionlistener.UpdateActionListener;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.component.html.util.AddResource;
import org.apache.commons.logging.impl.Jdk14Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/12/17 13:19:10  mmarinschek
 * new component jsValueChangeListener
 *
 *
 */
public class JsValueChangeListenerTag
        extends TagSupport
{
    //private static final Log log = LogFactory.getLog(UpdateActionListenerTag.class);

    private String _for;
    private String _property;
    private String _expressionValue;
    private static Log log = LogFactory.getLog(JsValueChangeListenerTag.class);

    public JsValueChangeListenerTag()
    {
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setExpressionValue(String expressionValue)
    {
        _expressionValue = expressionValue;
    }

    public void setProperty(String property)
    {
        _property = property;
    }


    public int doStartTag() throws JspException
    {
        if (_for == null) throw new JspException("for attribute not set");

        //Find parent UIComponentTag
        UIComponentTag componentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (componentTag == null)
        {
            throw new JspException("ValueChangeListenerTag has no UIComponentTag ancestor");
        }

        if (componentTag.getCreated())
        {

            AddResource.addJavaScriptToHeader(
                    JsValueChangeListenerTag.class, "JSListener.js", true, getFacesContext());

            //Component was just created, so we add the Listener
            UIComponent component = componentTag.getComponentInstance();

            if(_for!=null)
            {
                UIComponent forComponent = component.findComponent(_for);

                String forComponentId = null;

                if (forComponent == null)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("Unable to find component '" + _for + "' (calling findComponent on component '" + component.getClientId(getFacesContext()) + "') - will try to render component id based on the parent-id (on same level)");
                    }
                    if (_for.length() > 0 && _for.charAt(0) == UINamingContainer.SEPARATOR_CHAR)
                    {
                        //absolute id path
                        forComponentId = _for.substring(1);
                    }
                    else
                    {
                        //relative id path, we assume a component on the same level as the label component
                        String labelClientId = component.getClientId(getFacesContext());
                        int colon = labelClientId.lastIndexOf(UINamingContainer.SEPARATOR_CHAR);
                        if (colon == -1)
                        {
                            forComponentId = _for;
                        }
                        else
                        {
                            forComponentId = labelClientId.substring(0, colon + 1) + _for;
                        }
                    }
                }
                else
                {
                    forComponentId = forComponent.getClientId(getFacesContext());
                }

                String expressionValue = _expressionValue.replaceAll("\\'","\\\\'");
                expressionValue = expressionValue.replaceAll("\"","\\\"");


                String methodCall = "orgApacheMyfacesJsListenerSetExpressionProperty('"+
                        component.getClientId(getFacesContext())+"','"+
                        forComponentId+"',"+
                        (_property==null?"null":"'"+_property+"'")+
                        ",'"+expressionValue+"');";


                callMethod(component, "onchange",methodCall);

            }
        }

        return Tag.SKIP_BODY;
    }

    private void callMethod(UIComponent uiComponent, String propName, String value)
    {
        Object oldValue = uiComponent.getAttributes().get(propName);

        if(oldValue != null)
        {
            String oldValueStr = oldValue.toString().trim();

            //check if method call has already been added...
            if(oldValueStr.indexOf(value)!=-1)
                return;

            if(oldValueStr.length()>0 && !oldValueStr.endsWith(";"))
                oldValueStr +=";";

            value = oldValueStr + value;

        }

        uiComponent.getAttributes().put(propName, value);
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }
}


