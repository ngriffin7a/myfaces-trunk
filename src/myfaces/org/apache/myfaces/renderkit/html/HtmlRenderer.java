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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIRoot;
import net.sourceforge.myfaces.convert.ConversionErrorMessage;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.convert.MyFacesConverterException;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class HtmlRenderer
extends Renderer
{
    //~ Static fields/initializers -----------------------------------------------------------------
    private static final Log log = LogFactory.getLog(HtmlRenderer.class);

    private static final String CLIENT_ID_ATTR          =
        HtmlRenderer.class.getName() + ".CLIENT_ID";
    public static final String  LOCAL_STRING_VALUE_ATTR = "LOCAL_STRING_VALUE";

    //~ Methods ------------------------------------------------------------------------------------

    public abstract String getRendererType();

    public static boolean isComponentVisible(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!uiComponent.isRendered())
        {
            return false;
        }

        String userRole =
            (String) uiComponent.getAttribute(JSFAttr.VISIBLE_ON_USER_ROLE_ATTR);

        if (userRole == null)
        {
            //no restriction
            return true;
        }

        //is user in role?
        HttpServletRequest httpServletRequest =
            (HttpServletRequest) facesContext.getExternalContext().getRequest();

        return httpServletRequest.isUserInRole(userRole);
    }

    public static boolean isEnabledOnUserRole(FacesContext facesContext, UIComponent uiComponent)
    {
        String userRole =
            (String) uiComponent.getAttribute(JSFAttr.ENABLED_ON_USER_ROLE_ATTR);

        if (userRole == null)
        {
            //no restriction
            return true;
        }

        //is user in role?
        HttpServletRequest httpServletRequest =
            (HttpServletRequest) facesContext.getExternalContext().getRequest();

        return httpServletRequest.isUserInRole(userRole);
    }

    public void decode(FacesContext facescontext, UIComponent uicomponent)
    throws IOException
    {
        if (uicomponent instanceof UIOutput)
        {
            decodeValue(facescontext, (UIOutput) uicomponent);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
    throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
    }

    /**
     * Finds a proper Converter for the given attribute.
     */
    public static Converter findConverterForAttribute(
        FacesContext facesContext, UIComponent uiComponent, String attrName)
    {
        if (attrName.equals(LOCAL_STRING_VALUE_ATTR) || attrName.equals(CLIENT_ID_ATTR))
        {
            return ConverterUtils.getConverter(String.class);
        }
        else
        {
            return ConverterUtils.findAttributeConverter(facesContext, uiComponent, attrName);
        }
    }

    public static String urlEncode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8"); //always UTF-8 according to W3C
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

//        return URLEncoder.encode(s);
    }

    public String getClientId(FacesContext facesContext, UIComponent uiComponent)
    {
        return getComponentClientId(facesContext, uiComponent);
    }

    public static String getComponentClientId(FacesContext facesContext, UIComponent uiComponent)
    {
        String clientId = (String) uiComponent.getAttribute(CLIENT_ID_ATTR);

        if (clientId != null)
        {
            return clientId;
        }

        //Find namingContainer
        UIComponent find = uiComponent.getParent();

        if (find == null)
        {
            //we have got the root component
            if (!(uiComponent instanceof NamingContainer))
            {
                throw new FacesException("Root is no naming container?!");
            }

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(UIRoot.ROOT_COMPONENT_ID);
            }

            clientId = uiComponent.getComponentId();
        }
        else
        {
            while (!(find instanceof NamingContainer))
            {
                find = find.getParent();

                if (find == null)
                {
                    throw new FacesException("Root is no naming container?!");
                }
            }

            NamingContainer namingContainer = (NamingContainer) find;

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(namingContainer.generateClientId());
            }

            if (find.getParent() == null)
            {
                //NamingContainer is root, so nothing to be prepended
                clientId = uiComponent.getComponentId();
            }
            else
            {
                clientId =
                    find.getClientId(facesContext) + UIComponent.SEPARATOR_CHAR
                    + uiComponent.getComponentId();
            }
        }

        uiComponent.setAttribute(CLIENT_ID_ATTR, clientId);

        return clientId;
    }

    /**
     * Helper for derived renderers.
     */
    protected Application getApplication()
    {
        ApplicationFactory af =
            (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        return af.getApplication();
    }

    protected BodyContent getBodyContent(FacesContext facesContext, UIComponent uiComponent)
    {
        BodyContent bodyContent =
            (BodyContent) uiComponent.getAttribute(MyFacesBodyTag.BODY_CONTENT_ATTR);

        if (bodyContent == null)
        {
            log.fatal("No BodyContent found for component " + uiComponent.getClientId(facesContext)
                      + ": corresponding Tag not a MyFacesBodyTag?");
        }

        return bodyContent;
    }

    /**
     * @param facescontext
     * @param uiOutput
     * @return the current value as a String
     */
    protected String getStringValue(FacesContext facescontext, UIOutput uiOutput)
    {
        String strValue = (String) uiOutput.getAttribute(LOCAL_STRING_VALUE_ATTR);

        if (strValue != null)
        {
            return strValue;
        }

        Object objValue = null;

        try
        {
            objValue = uiOutput.currentValue(facescontext);
        }
        catch (Exception e)
        {
            log.error("Error getting current value of component "
                      + UIComponentUtils.toString(uiOutput) + ".", e);
            return "";
        }

        if (objValue != null)
        {
            try
            {
                return ConverterUtils.getComponentValueAsString(facescontext, uiOutput, objValue);
            }
            catch (ConverterException e)
            {
                log.error("Could not convert value of component " + UIComponentUtils.toString(uiOutput)
                          + " to String.", e);
                return "";
            }
        }

        return "";
    }

    protected void setUIOutputValue(UIOutput uiOutput, Object newValue)
    {
        uiOutput.setValue(newValue);
        uiOutput.setAttribute(LOCAL_STRING_VALUE_ATTR, null);
        uiOutput.setValid(true);
    }

    protected void addConversionErrorMessage(
        FacesContext facesContext, UIComponent comp, ConverterException e)
    {
        if (e instanceof MyFacesConverterException)
        {
            facesContext.addMessage(
                comp,
                ((MyFacesConverterException) e).getFacesMessage());
        }
        else
        {
            facesContext.addMessage(
                comp,
                new ConversionErrorMessage(e.getMessage()));
        }
    }

    protected void convertAndSetUIOutputValue(
        FacesContext facesContext, UIOutput uiOutput, String[] newValues)
    {
        Converter conv = ConverterUtils.findValueConverter(facesContext, uiOutput);

        if (conv == null)
        {
            //default to StringConverter
            conv = ConverterUtils.getConverter(String.class);
        }

        if (conv instanceof StringArrayConverter)
        {
            setUIOutputValue(uiOutput, newValues);
        }
        else
        {
            String s = StringArrayConverter.getAsString(newValues, false);

            try
            {
                Object objValue = conv.getAsObject(facesContext, uiOutput, s);
                setUIOutputValue(uiOutput, objValue);
            }
            catch (ConverterException e)
            {
                uiOutput.setValue(null);
                uiOutput.setAttribute(LOCAL_STRING_VALUE_ATTR, s);
                uiOutput.setValid(false);
                addConversionErrorMessage(facesContext, uiOutput, e);
            }
        }
    }

    /**
     * @return true, if new value was set
     */
    protected boolean decodeValue(FacesContext facescontext, UIOutput uiComponent)
    {
        String   clientId  = uiComponent.getClientId(facescontext);
        String[] newValues =
            ((ServletRequest) facescontext.getExternalContext().getRequest()).getParameterValues(
                clientId);

        if (newValues != null)
        {
            convertAndSetUIOutputValue(facescontext, uiComponent, newValues); //add error message on fail

            return true;
        }
        else
        {
            if (uiComponent instanceof UIInput)
            {
                //component was not sent with this request, so our task is to
                //maintain the component's state
                if (uiComponent.isValid())
                {
                    if ((uiComponent.getValueRef() != null) && (uiComponent.getValue() == null))
                    {
                        //HACK: since EA3 release of API the updateModel does also update null values
                        //      when isValid is true. Therefore we get the current model
                        //      value and set the value here.
                        ValueBinding vb =
                            getApplication().getValueBinding(uiComponent.getValueRef());
                        uiComponent.setValue(vb.getValue(facescontext));
                    }
                }
                else
                {
                    //HACK: We must set the component valid, because otherwise the
                    //updateModel phase would knock off our request and step to the
                    //render phase.
                    uiComponent.setValid(true);

                    if (uiComponent.getValueRef() != null)
                    {
                        //If there is a model reference, we must beware the model
                        //from being changed later in the update model phase.
                        //Since we cannot avoid the model beeing set, we simply
                        //get the current model value and overwrite the component's
                        //value.
                        ValueBinding vb =
                            getApplication().getValueBinding(uiComponent.getValueRef());
                        uiComponent.setValue(vb.getValue(facescontext));
                    }
                }
            }

            return false;
        }
    }
}
