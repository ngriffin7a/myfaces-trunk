/*
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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTabbedPaneRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTabbedPaneRenderer.class);

    private static final String TABLE_STYLE =
        "border-style: none; " +
        "padding: 0px; " +
        "border-spacing: 0px; " +
        "empty-cells: show; ";


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //TODO: RendererUtils.checkParamValidity(facesContext, component, HtmlPanelNavigation.class);
        ResponseWriter writer = facesContext.getResponseWriter();
        writeTableStart(writer, facesContext, uiComponent);
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
        super.encodeChildren(facescontext, uicomponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }

    public void decode(FacesContext facescontext, UIComponent uicomponent)
    {
        super.decode(facescontext, uicomponent);
    }


    protected void writeTableStart(ResponseWriter writer,
                                   FacesContext facesContext,
                                   UIComponent uiComponent)
        throws IOException
    {
        String oldStyle = (String)uiComponent.getAttributes().get(HTML.STYLE_ATTR);
        if (oldStyle == null)
        {
            uiComponent.getAttributes().put(HTML.STYLE_ATTR, TABLE_STYLE);
        }
        else
        {
            uiComponent.getAttributes().put(HTML.STYLE_ATTR, TABLE_STYLE + "; " + oldStyle);
        }

        String oldBgColor = (String)uiComponent.getAttributes().get(HTML.BGCOLOR_ATTR);
        uiComponent.getAttributes().put(HTML.BGCOLOR_ATTR, null);

        writer.startElement(HTML.TABLE_ELEM, uiComponent);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        writer.flush();

        uiComponent.getAttributes().put(HTML.STYLE_ATTR, oldStyle);
        uiComponent.getAttributes().put(HTML.BGCOLOR_ATTR, oldBgColor);
    }


}
