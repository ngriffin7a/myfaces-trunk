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

import net.sourceforge.myfaces.renderkit.attr.ext.LayoutChildRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.RenderKitWrapper;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LayoutChildRenderer
    extends HTMLRenderer
    implements LayoutChildRendererAttributes
{
    public static final String TYPE = "LayoutChild";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent component)
    {
        return component instanceof UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIPanel.TYPE);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!uiComponent.getComponentType().equals(UIPanel.TYPE))
        {
            throw new IllegalArgumentException("Only UIPanel supported.");
        }

        if (uiComponent.getAttribute(HEADER_CLASS_ATTR) != null)
        {
            writer.write(beginToken(LayoutRenderer.HEADER));
        }
        else if (uiComponent.getAttribute(NAVIGATION_CLASS_ATTR) != null)
        {
            writer.write(beginToken(LayoutRenderer.NAVIGATION));
        }
        else if (uiComponent.getAttribute(BODY_CLASS_ATTR) != null)
        {
            writer.write(beginToken(LayoutRenderer.BODY));
        }
        else if (uiComponent.getAttribute(FOOTER_CLASS_ATTR) != null)
        {
            writer.write(beginToken(LayoutRenderer.FOOTER));
        }

        RenderKitWrapper.originalEncodeBegin(facesContext, uiComponent);

        RenderKitWrapper.suspendChildrenRedirection(facesContext, uiComponent, this);
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        RenderKitWrapper.originalEncodeChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!uiComponent.getComponentType().equals(UIPanel.TYPE))
        {
            throw new IllegalArgumentException("Only UIPanel supported.");
        }

        RenderKitWrapper.originalEncodeEnd(facesContext, uiComponent);

        if (uiComponent.getAttribute(HEADER_CLASS_ATTR) != null)
        {
            writer.write(endToken(LayoutRenderer.HEADER));
        }
        else if (uiComponent.getAttribute(NAVIGATION_CLASS_ATTR) != null)
        {
            writer.write(endToken(LayoutRenderer.NAVIGATION));
        }
        else if (uiComponent.getAttribute(BODY_CLASS_ATTR) != null)
        {
            writer.write(endToken(LayoutRenderer.BODY));
        }
        else if (uiComponent.getAttribute(FOOTER_CLASS_ATTR) != null)
        {
            writer.write(endToken(LayoutRenderer.FOOTER));
        }
    }

    protected static String beginToken(String part)
    {
        return "__" + part + "_BEGIN__";
    }

    protected static String endToken(String part)
    {
        return "__" + part + "_END__";
    }

}
