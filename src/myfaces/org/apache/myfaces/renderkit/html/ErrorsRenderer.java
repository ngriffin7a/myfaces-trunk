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

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.Message;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;

/**
 * ErrorsRenderer as specified in JSF.7.6.5
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ErrorsRenderer
    extends HTMLRenderer
{
    //public static final String COMPOUND_ID_ATTR = "compoundId";
    public static final String COMPOUND_ID_ATTR = "msgCompoundId"; //Error in Spec.: Cannot be "compoundId" because components already have a compoundId property!

    public static final String TYPE = "Errors";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UIOutput;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIOutput.TYPE);
    }

    public void encodeBegin(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        Iterator it;
        String compoundId = (String)uiComponent.getAttribute(COMPOUND_ID_ATTR);
        if (compoundId == null)
        {
            it = facesContext.getMessages();
        }
        else if (compoundId.length() == 0)
        {
            it = facesContext.getMessages(null);
        }
        else
        {
            UIComponent comp = null;
            try
            {
                comp = facesContext.getResponseTree().getRoot().findComponent(compoundId);
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
    }

}
