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

import net.sourceforge.myfaces.component.UIInput;

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
public abstract class AbstractInputRenderer
         extends HTMLRenderer
{
    public boolean supportsComponentType(String s)
    {
        return s.equals(UIInput.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIInput;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        renderInput(facesContext, uiComponent);
        renderMessages(facesContext, uiComponent);
    }

    public abstract void renderInput(FacesContext facesContext, UIComponent uiComponent)
            throws IOException;

    public void renderMessages(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        Iterator it = facesContext.getMessages(uiComponent);
        if (it.hasNext())
        {
            writer.write(" ");
            while (it.hasNext())
            {
                Message msg = (Message)it.next();
                writer.write(msg.getSummary());
            }
        }
    }

}
