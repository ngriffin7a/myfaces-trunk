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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIOutput;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ErrorsRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

/**
 * ErrorsRenderer as specified in JSF.7.6.5
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ErrorsRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               ErrorsRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Errors";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIOutput;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIOutput.TYPE);
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", OUTPUT_ERRORS_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", USER_ROLE_ATTRIBUTES);
    }


    public void encodeBegin(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        boolean ulLayout;
        ResponseWriter writer = facesContext.getResponseWriter();
        Iterator it;
        String msgClientId = (String)uiComponent.getAttribute(CLIENT_ID_ATTR);
        if (msgClientId == null)
        {
            //All messages
            it = facesContext.getMessages();
            ulLayout = true;
        }
        else if (msgClientId.length() == 0)
        {
            //All component messages
            it = facesContext.getMessages(null);
            ulLayout = true;
        }
        else
        {
            //All messages for this component
            ulLayout = false;
            UIComponent comp = null;
            try
            {
                comp = facesContext.getTree().getRoot().findComponent(msgClientId);
            }
            catch (IllegalArgumentException e) {}
            if (comp != null)
            {
                it = facesContext.getMessages(comp);
            }
            else
            {
                it = Collections.EMPTY_SET.iterator();
            }
        }

        if (it.hasNext())
        {
            if (ulLayout)
            {
                writer.write("\n<ul");

                HTMLUtil.renderCssClass(writer, uiComponent, OUTPUT_CLASS_ATTR);
                HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
                HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);

                writer.write(">");
                while (it.hasNext())
                {
                    Message msg = (Message)it.next();
                    String summary = msg.getSummary();
                    String detail = msg.getDetail();

                    writer.write("\n\t<li>");

                    if (summary != null)
                        writer.write(summary);
                    if (summary != null &&
                        detail != null&&
                        summary.length() > 0 &&
                        detail.length() > 0)
                    {
                        writer.write(": ");
                    }
                    if (detail != null)
                        writer.write(detail);
                    writer.write("</li>");
                }
                writer.write("\n</ul>");
            }
            else
            {
                while (it.hasNext())
                {
                    writer.write("<span");
                    HTMLUtil.renderCssClass(writer, uiComponent, OUTPUT_CLASS_ATTR);
                    HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
                    HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
                    writer.write(">");

                    Message msg = (Message)it.next();
                    String summary = msg.getSummary();
                    String detail = msg.getDetail();

                    if (summary != null)
                        writer.write(summary);
                    if (summary != null &&
                        detail != null &&
                        summary.length() > 0 &&
                        detail.length() > 0)
                    {
                        writer.write(": ");
                    }
                    if (detail != null)
                        writer.write(detail);

                    writer.write("</span>");

                    if (it.hasNext())
                    {
                        writer.write("<br>");
                    }
                }
            }
        }
    }

}
