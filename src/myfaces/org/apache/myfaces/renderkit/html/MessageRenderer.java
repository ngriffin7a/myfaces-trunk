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

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.MessageRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
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
    implements CommonComponentProperties,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               MessageRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Message";

    private static final Object[] EMPTY_PARAMS = new Object[0];

    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(String s)
    {
        return s.equals(UIOutput.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof javax.faces.component.UIOutput;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_message", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_message", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_message", OUTPUT_MESSAGE_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_message", USER_ROLE_ATTRIBUTES);
    }
    */



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
        String key = (String)uiComponent.getAttribute(KEY_ATTR);
        if (key != null)
        {
            pattern = BundleUtils.getString(facesContext,
                                            (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                            key);
        }
        else
        {
            pattern = getStringValue(facesContext, (UIOutput)uiComponent);
        }

        MessageFormat format = new MessageFormat(pattern, facesContext.getLocale());

        //nested parameters
        List params = null;
        Iterator children = uiComponent.getChildren();
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UIParameter)
            {
                if (params == null)
                {
                    params = new ArrayList();
                }
                params.add(((UIParameter)child).currentValue(facesContext));
            }
        }

        String text;
        try
        {
            if (params == null)
            {
                text = format.format(EMPTY_PARAMS);
            }
            else
            {
                text = format.format(params.toArray());
            }
        }
        catch (Exception e)
        {
            LogUtil.getLogger().severe(e.getMessage());
            text = pattern;
        }

        writer.write(HTMLEncoder.encode(text, true, true));

        if (css != null)
        {
            writer.write("</span>");
        }
    }

}
