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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.ext.UIMessageList;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageListRenderer
        extends HTMLRenderer
{
    public static final String TYPE = "MessageListRenderer";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIMessageList.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIMessageList;
    }

    public void encodeBegin(FacesContext facescontext, UIComponent uicomponent)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        Iterator it = facesContext.getMessages();
        if (it.hasNext())
        {
            writer.write("\n<ul>");
            while (it.hasNext())
            {
                Message msg = (Message)it.next();
                writer.write("\n\t<li>");
                writer.write(msg.getSummary());
                writer.write("</li>");
            }
            writer.write("\n</ul>");
        }
    }

}
