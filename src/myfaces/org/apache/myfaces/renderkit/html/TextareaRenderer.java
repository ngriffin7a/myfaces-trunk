/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class TextareaRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Textarea";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(String s)
    {
        return s.equals(UIInput.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIInput;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_TEXTAREA_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", INPUT_TEXTAREA_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", USER_ROLE_ATTRIBUTES);
    }
    */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<textarea");

        String coumpoundId = uiComponent.getClientId(facesContext);
        writer.write(" name=\"");
        writer.write(coumpoundId);
        writer.write("\" id=\"");
        writer.write(coumpoundId);
        writer.write('"');

        HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.INPUT_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TEXTAREA_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);

        writer.write('>');

        String currentValue = getStringValue(facesContext, (UIInput) uiComponent);

        if (currentValue != null)
        {
            writer.write(HTMLEncoder.encode(currentValue, false, false));
        }

        writer.write("</textarea>");
    }
}
