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
import net.sourceforge.myfaces.component.UIOutput;
import net.sourceforge.myfaces.component.CommonComponentAttributes;

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
 * TODO: HTML universal and event handler attributes
 * TODO: user role attributes
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
                writer.write("\n<ul>");
                while (it.hasNext())
                {
                    Message msg = (Message)it.next();
                    writer.write("\n\t<li>");
                    writer.write(msg.getSummary());
                    writer.write(": ");
                    writer.write(msg.getDetail());
                    writer.write("</li>");
                }
                writer.write("\n</ul>");
            }
            else
            {
                while (it.hasNext())
                {
                    Message msg = (Message)it.next();
                    String cssClass = (String)uiComponent.getAttribute(OUTPUT_CLASS_ATTR);
                    if (cssClass != null)
                    {
                        writer.write("<span class=\"");
                        writer.write(cssClass);
                        writer.write("\">");
                    }
                    writer.write(msg.getSummary());
                    writer.write(": ");
                    writer.write(msg.getDetail());
                    if (cssClass != null)
                    {
                        writer.write("</span>");
                    }
                    if (it.hasNext())
                    {
                        writer.write("<br>");
                    }
                }
            }
        }
    }

}
