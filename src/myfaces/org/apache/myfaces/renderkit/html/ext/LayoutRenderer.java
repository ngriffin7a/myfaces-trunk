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

import net.sourceforge.myfaces.renderkit.attr.ext.LayoutRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.RenderKitWrapper;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LayoutRenderer
    extends HTMLRenderer
    implements LayoutRendererAttributes
{
    static final String HEADER = "LayoutHeader";
    static final String NAVIGATION = "LayoutNavigation";
    static final String BODY = "LayoutBody";
    static final String FOOTER = "LayoutFooter";

    public static final String BODY_CONTENT_REQUEST_ATTR = LayoutRenderer.class.getName() + ".BODY_CONTENT";


    public static final String CLASSIC_LAYOUT = "classic";
    public static final String NAV_RIGHT_LAYOUT = "navigationRight";


    public static final String TYPE = "Layout";
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
        // wrap current renderKit
        RenderKitWrapper.wrapRenderKit(facesContext,
                                       uiComponent,
                                       "LayoutRenderKit");
        RenderKitWrapper.startChildrenRedirection(facesContext,
                                                  uiComponent,
                                                  this,
                                                  new LayoutChildRenderer());
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        writeBody(facesContext, uiComponent);

        // unwrap current renderKit
        RenderKitWrapper.unwrapRenderKit(facesContext,
                                         uiComponent);
    }

    protected void writeBody(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        BodyContent bodyContent = (BodyContent)facesContext.getServletRequest()
            .getAttribute(BODY_CONTENT_REQUEST_ATTR);
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }

        String layout = (String)uiComponent.getAttribute(LAYOUT_ATTR);
        if (layout == null)
        {
            LogUtil.getLogger().severe("No layout attribute!");
            ResponseWriter writer = facesContext.getResponseWriter();
            bodyContent.writeOut(writer);
            return;
        }

        if (layout.equals(CLASSIC_LAYOUT))
        {
            writeClassicLayout(facesContext, bodyContent);
        }
        else if (layout.equals(NAV_RIGHT_LAYOUT))
        {
            writeNavRightLayout(facesContext, bodyContent);
        }
        else
        {
            LogUtil.getLogger().severe("Layout '" + layout + "' not supported.");
            ResponseWriter writer = facesContext.getResponseWriter();
            bodyContent.writeOut(writer);
            return;
        }
    }


    protected void writeClassicLayout(FacesContext facesContext,
                                      BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        String bodyString = bodyContent.getString();
        //TODO
    }

    protected void writeNavRightLayout(FacesContext facesContext,
                                       BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        String bodyString = bodyContent.getString();
        //TODO
    }

}
