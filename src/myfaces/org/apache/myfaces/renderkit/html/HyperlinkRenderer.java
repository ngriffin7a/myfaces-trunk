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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIParameter;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.ConverterFactory;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.CommandEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * HyperlinkRenderer-Extension:
 * MyFaces HyperlinkRenderer decodes nested UIParameters and stores the
 * values as attributes in the UICommand.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HyperlinkRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLAnchorAttributes,
               HyperlinkRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Hyperlink";

    private static final String TYPE_SUFFIX = ".TYPE";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UICommand.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UICommand;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_hyperlink", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_hyperlink", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_hyperlink", HTML_ANCHOR_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_hyperlink", COMMAND_HYPERLINK_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_hyperlink", USER_ROLE_ATTRIBUTES);
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value never comes from request

        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        if (paramValue != null)
        {
            //link was clicked
            String commandName = paramValue;

            uiComponent.setValue(paramValue);
            uiComponent.setValid(true);

            //nested parameters
            Iterator children = uiComponent.getChildren();
            while (children.hasNext())
            {
                UIComponent child = (UIComponent)children.next();
                if (child instanceof UIParameter)
                {
                    decodeNestedParameter(facesContext, (UICommand)uiComponent, (UIParameter)child);
                }
            }

            //Old event processing:
            ApplicationEvent event = new CommandEvent(uiComponent, commandName);
            facesContext.addApplicationEvent(event);

            //New event processing:
            if (uiComponent instanceof javax.faces.component.UICommand)
            {
                ((javax.faces.component.UICommand)uiComponent).fireActionEvent(facesContext);
            }
            else
            {
                LogUtil.getLogger().warning("Component " + UIComponentUtils.toString(uiComponent) + "is no UICommand.");
            }
        }
        else
        {
            uiComponent.setValid(true);
        }
    }


    protected void decodeNestedParameter(FacesContext facesContext,
                                         UICommand uiCommand,
                                         UIParameter uiParameter) throws IOException
    {
        String name = uiParameter.getName();
        if (name == null)
        {
            name = uiParameter.getClientId(facesContext);
        }
        String strV = facesContext.getServletRequest().getParameter(name);
        if (strV != null)
        {
            Object objValue;
            //find converter
            Converter conv = null;
            ConverterFactory convFactory = (ConverterFactory)FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
            String type = facesContext.getServletRequest().getParameter(name + TYPE_SUFFIX);
            if (type != null)
            {
                try
                {
                    conv = convFactory.getConverter(Class.forName(type));
                }
                catch (ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                Object converterAttr = uiParameter.getAttribute(CONVERTER_ATTR);
                if (converterAttr != null)
                {
                    if (converterAttr instanceof Converter)
                    {
                        conv = (Converter)converterAttr;
                    }
                    else
                    {
                        conv = convFactory.getConverter((String)converterAttr);
                    }
                }
            }

            if (conv != null)
            {
                try
                {
                    objValue = conv.getAsObject(facesContext, uiParameter, strV);
                }
                catch (ConverterException e)
                {
                    objValue = null;
                    LogUtil.getLogger().severe("Could not reconvert hyperlink parameter " + name + " to Object.");
                }
            }
            else
            {
                objValue = ConverterUtils.deserializeAndDecodeBase64(strV);
            }

            uiCommand.setAttribute(name, objValue);
        }
    }


    public void encodeBegin(FacesContext context, UIComponent uiComponent) throws IOException
    {
        //because of possibly embedded UIParameter components, that would not
        //yet have been created, everything is done in encodeEnd
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        if (!uiComponent.isRendered())
        {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();

        String visibleOnUserRole = (String)uiComponent.getAttribute(VISIBLE_ON_USER_ROLE_ATTR);
        if (visibleOnUserRole != null &&
            !request.isUserInRole(visibleOnUserRole))
        {
            return;
        }

        String enabledOnUserRole = (String)uiComponent.getAttribute(ENABLED_ON_USER_ROLE_ATTR);
        if (enabledOnUserRole != null &&
            !request.isUserInRole(enabledOnUserRole))
        {
            //write out body content
            BodyContent bodyContent = getBodyContent(facesContext, uiComponent);
            bodyContent.writeOut(writer);
            return;
        }

        writer.write("<a href=\"");
        String href = (String)uiComponent.getAttribute(HREF_ATTR);
        if (href == null)
        {
            //Modify URL for the faces servlet mapping:
            ServletContext servletContext = facesContext.getServletContext();
            ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
            ServletMapping sm = smf.getServletMapping(servletContext);
            String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

            href = request.getContextPath() + treeURL;
        }

        //Encode URL for those still using HttpSessions... ;-)
        href = ((HttpServletResponse)facesContext.getServletResponse()).encodeURL(href);

        writer.write(href);

        //value
        if (href.indexOf('?') == -1)
        {
            writer.write('?');
        }
        else
        {
            writer.write('&');
        }
        writer.write(uiComponent.getClientId(facesContext));
        writer.write('=');
        writer.write(URLEncoder.encode(getStringValue(facesContext, uiComponent), "UTF-8"));

        //nested parameters
        Iterator children = uiComponent.getChildren();
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UIParameter)
            {
                encodeNestedParameter(facesContext, (UIParameter)child);
            }
        }

        //state:
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\"");

        CommonAttributes.renderCssClass(writer, uiComponent, COMMAND_CLASS_ATTR);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_ANCHOR_ATTRIBUTES);

        writer.write(">");

        //write link text
        String key = (String)uiComponent.getAttribute(KEY_ATTR);
        if (key != null)
        {
            String text = BundleUtils.getString(facesContext,
                                                (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                                key);
            writer.write(HTMLEncoder.encode(text, true, true));
        }

        //write out body content
        BodyContent bodyContent = getBodyContent(facesContext, uiComponent);
        bodyContent.writeOut(writer);

        //close anchor
        writer.write("</a>");
    }


    protected void encodeNestedParameter(FacesContext facesContext,
                                         UIParameter uiParameter) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        Object objValue = uiParameter.currentValue(facesContext);
        if (objValue != null)
        {
            String name = uiParameter.getName();
            if (name == null)
            {
                name = uiParameter.getClientId(facesContext);
            }
            writer.write('&');
            writer.write(name);
            writer.write('=');

            Converter conv = ConverterUtils.findValueConverter(facesContext, uiParameter);
            if (conv != null)
            {
                try
                {
                    String strValue = conv.getAsString(facesContext, uiParameter, objValue);
                    writer.write(urlEncode(strValue));

                    if (uiParameter.getAttribute(CONVERTER_ATTR) == null)
                    {
                        //send type of parameter
                        writer.write('&');
                        writer.write(name + TYPE_SUFFIX);
                        writer.write('=');
                        writer.write(objValue.getClass().getName());
                    }
                }
                catch (ConverterException e)
                {
                    LogUtil.getLogger().severe("Could not convert hyperlink parameter " + name + " to String.");
                }
            }
            else
            {
                writer.write(urlEncode(ConverterUtils.serializeAndEncodeBase64(objValue)));
            }
        }
    }

}
