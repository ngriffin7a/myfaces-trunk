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
package net.sourceforge.myfaces.custom.datascroller;

import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.Application;
import java.io.IOException;
import java.util.Map;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScrollerRenderer
    extends HtmlRenderer
{
    private static final String FACET_FIRST     = "first".intern();
    private static final String FACET_PREVOIUS  = "previous".intern();
    private static final String FACET_NEXT      = "next".intern();
    private static final String FACET_LAST      = "last".intern();

    public static final String RENDERER_TYPE = "net.sourceforge.myfaces.DataScroller";

    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlDataScroller.class);

        HtmlDataScroller scroller = (HtmlDataScroller)component;

        UIComponent forComp = component.findComponent(scroller.getFor());
        if (!(forComp instanceof UIData))
        {
            throw new IllegalArgumentException("uiComponent referenced by attribute tableScroller@for must be of type " + UIData.class.getName());
        }

        UIData uiData = (UIData)forComp;

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String param = (String)parameter.get(component.getId());
        if (param != null)
        {
            if (param.equals(FACET_FIRST))
            {
                uiData.setFirst(0);
            }
            else if (param.equals(FACET_PREVOIUS))
            {
                int first = uiData.getFirst() - uiData.getRows();
                if (first >= 0)
                    uiData.setFirst(first);
            }
            else if (param.equals(FACET_NEXT))
            {
                int first = uiData.getFirst() + uiData.getRows();
                if (first < uiData.getRowCount())
                    uiData.setFirst(first);
            }
            else if (param.equals(FACET_LAST))
            {
                int first = uiData.getRowCount() - uiData.getRows();
                if (first >= 0)
                {
                    uiData.setFirst(first);
                }
                else
                {
                    uiData.setFirst(0);
                }
            }

        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlDataScroller scroller = (HtmlDataScroller)uiComponent;
        renderFacet(facesContext, scroller, scroller.getFirst(), FACET_FIRST);
        writer.write("&nbsp;");
        renderFacet(facesContext, scroller, scroller.getPrevious(), FACET_PREVOIUS);
        writer.write("&nbsp;");
        renderFacet(facesContext, scroller, scroller.getNext(), FACET_NEXT);
        writer.write("&nbsp;");
        renderFacet(facesContext, scroller, scroller.getLast(), FACET_LAST);
    }

    private void renderFacet(FacesContext facesContext,
                             HtmlDataScroller scroller,
                             UIComponent facetComp,
                             String facetName)
        throws IOException
    {
        if (facetComp == null)
        {
            return;
        }
        UIComponent link = getLink(facesContext, scroller, facetComp, facetName);
        link.encodeBegin(facesContext);
        facetComp.encodeBegin(facesContext);
        if (facetComp.getRendersChildren())
            facetComp.encodeChildren(facesContext);
        facetComp.encodeEnd(facesContext);
        link.encodeEnd(facesContext);
    }

    private UIComponent getLink(FacesContext facesContext,
                                 HtmlDataScroller scroller,
                                 UIComponent facetComp,
                                 String facetName)
    {
        Application application = facesContext.getApplication();

        HtmlCommandLink link
                = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(scroller.getId() + facetName);
        link.setTransient(true);
        UIParameter parameter
                = (UIParameter)application.createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(scroller.getId() + facetName + "_param");
        parameter.setTransient(true);
        parameter.setName(scroller.getClientId(facesContext));
        parameter.setValue(facetName);
        List children = link.getChildren();
        children.add(parameter);
        children.add(facetComp);
        return link;
    }
}
