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

import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLButtonAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.component.UIComponentUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ButtonRenderer
    extends HTMLRenderer
    implements CommonRendererAttributes,
    HTMLUniversalAttributes,
    HTMLEventHandlerAttributes,
    HTMLButtonAttributes,
               ButtonRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Button";
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
        return uiComponent instanceof UICommand;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_button", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_button", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_button", HTML_BUTTON_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_button", COMMAND_BUTTON_ATTRIBUTES);
        addAttributeDescriptors(UICommand.TYPE, TLD_HTML_URI, "command_button", USER_ROLE_ATTRIBUTES);
    }


    private String getHiddenValueParamName(FacesContext facesContext, UIComponent uiComponent)
    {
        return uiComponent.getClientId(facesContext) + ".VALUE";
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value is handled here

        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        boolean submitted = false;
        if (paramValue == null)
        {
            if (facesContext.getServletRequest().getParameter(paramName + ".x") != null) //image button
            {
                submitted = true;
            }
        }
        else
        {
            if (paramValue.length() > 0)
            {
                submitted = true;
            }
        }

        if (submitted)
        {
            String commandName;
            String hiddenValue = facesContext.getServletRequest()
                                    .getParameter(getHiddenValueParamName(facesContext,
                                                                          uiComponent));
            if (hiddenValue != null)
            {
                commandName = hiddenValue;
            }
            else
            {
                if (paramValue != null)
                {
                    commandName = paramValue;
                }
                else
                {
                    commandName = getStringValue(facesContext, uiComponent);
                }
            }

            uiComponent.setValue(commandName);
            uiComponent.setValid(true);

            //Old event processing:

            ApplicationEvent appEvent;

            //Form suchen
            UIForm form = null;
            for (UIComponent parent = uiComponent.getParent(); parent != null; parent = parent.getParent())
            {
                if (parent instanceof UIForm)
                {
                    form = (UIForm)parent;
                    break;
                }
            }

            if (form == null)
            {
                appEvent = new CommandEvent(uiComponent, commandName);
            }
            else
            {
                appEvent = new FormEvent(uiComponent, form.getFormName(), commandName);
            }

            facesContext.addApplicationEvent(appEvent);

            //New event processing:
            if (uiComponent instanceof UICommand)
            {
                ((UICommand)uiComponent).fireActionEvent(facesContext);
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

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        boolean hiddenParam = true;
        writer.write("<input type=");
        String imageSrc = (String)uiComponent.getAttribute(IMAGE_ATTR);
        if (imageSrc != null)
        {
            writer.write("\"image\" src=\"");
            writer.write(imageSrc);
            writer.write("\"");
            writer.write(" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\"");
        }
        else
        {
            String type = (String)uiComponent.getAttribute(TYPE_ATTR);
            if (type == null)
            {
                type = "submit";
            }
            writer.write("\"");
            writer.write(type);
            writer.write("\" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\"");
            writer.write(" value=\"");

            String label;
            String key = (String)uiComponent.getAttribute(KEY_ATTR);
            if (key != null)
            {
                label = BundleUtils.getString(facesContext,
                                              (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                              key);
            }
            else
            {
                label = (String)uiComponent.getAttribute(LABEL_ATTR);
            }
            if (label == null)
            {
                label = getStringValue(facesContext, uiComponent);
                hiddenParam = false;
            }
            writer.write(HTMLEncoder.encode(label, false, false));
            writer.write("\"");
        }

        CommonAttributes.renderCssClass(writer, uiComponent, COMMAND_CLASS_ATTR);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_BUTTON_ATTRIBUTES);

        writer.write(">");

        if (hiddenParam)
        {
            writer.write("<input type=\"hidden\" name=\"");
            writer.write(getHiddenValueParamName(facesContext, uiComponent));
            writer.write("\" value=\"");
            String strVal = getStringValue(facesContext, uiComponent);
            writer.write(HTMLEncoder.encode(strVal, false, false));
            writer.write("\">");
        }
    }

}
