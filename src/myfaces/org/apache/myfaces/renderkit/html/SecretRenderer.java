/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class SecretRenderer
        extends AbstractInputRenderer
{
    private static final String IS_SECRET_ATTR = SecretRenderer.class.getName() + ".IS_SECRET";
    public static final String TYPE = "SecretRenderer";

    public String getRendererType()
    {
        return TYPE;
    }

    public void decode(FacesContext facescontext, UIComponent uicomponent)
        throws IOException
    {
        super.decode(facescontext, uicomponent);
        uicomponent.setAttribute(IS_SECRET_ATTR, Boolean.TRUE);
    }

    public void renderInput(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<input type=\"password\"");
        writer.write(" name=\"");
        writer.write(uiComponent.getCompoundId());
        writer.write("\"");
        String currentValue = getStringValue(facesContext, uiComponent);
        if (currentValue != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(currentValue, false, false));
            writer.write("\"");
        }
        String size = (String)uiComponent.getAttribute(UIInput.SIZE_ATTR);
        if (size != null)
        {
            writer.write(" size=\"");
            writer.write(size);
            writer.write("\"");
        }
        String maxLength = (String)uiComponent.getAttribute(UIInput.MAX_LENGTH_ATTR);
        if (maxLength != null)
        {
            writer.write(" maxlength=\"");
            writer.write(maxLength);
            writer.write("\"");
        }
        writer.write(">");
    }



    public static boolean isSecretComponent(UIComponent comp)
    {
        Boolean secret = (Boolean)comp.getAttribute(SecretRenderer.IS_SECRET_ATTR);
        return secret != null && secret.booleanValue();
    }

}
