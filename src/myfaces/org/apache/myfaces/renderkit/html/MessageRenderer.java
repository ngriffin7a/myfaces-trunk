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

import net.sourceforge.myfaces.component.UIParameter;
import net.sourceforge.myfaces.renderkit.attr.MessageRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageRenderer
        extends HTMLRenderer
        implements MessageRendererAttributes
{
    public static final String TYPE = "Message";

    public String getRendererType()
    {
        return TYPE;
    }

    public MessageRenderer()
    {
        super();
        addAttributeDescriptor(UIOutput.TYPE, KEY_ATTR);
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIOutput.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof javax.faces.component.UIOutput;
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
        ResponseWriter writer = facesContext.getResponseWriter();
        String css = (String)uiComponent.getAttribute(OUTPUT_CLASS_ATTR);
        if (css != null)
        {
            writer.write("<span class=\"");
            writer.write(css);
            writer.write("\">");
        }

        String pattern;
        String key = (String)uiComponent.getAttribute(KEY_ATTR.getName());
        if (key != null)
        {
            pattern = BundleUtils.getString(facesContext,
                                            (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                            key);
        }
        else
        {
            pattern = getStringValue(facesContext, uiComponent);
        }

        MessageFormat format = new MessageFormat(pattern, facesContext.getLocale());

        //nested parameters
        List params = null;
        Iterator children = uiComponent.getChildren();
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child.getComponentType().equals(UIParameter.TYPE))
            {
                if (params == null)
                {
                    params = new ArrayList();
                }
                params.add(child.currentValue(facesContext));
            }
        }

        String text;
        if (params == null)
        {
            text = format.format(new Object[] {});  //OPTIMIZE: static constant
        }
        else
        {
            text = format.format(params.toArray());
        }

        writer.write(HTMLEncoder.encode(text, true, true));

        if (css != null)
        {
            writer.write("</span>");
        }
    }

}
