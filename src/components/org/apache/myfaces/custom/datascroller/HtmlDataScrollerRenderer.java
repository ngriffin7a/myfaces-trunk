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

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScrollerRenderer
    extends HtmlRenderer
{
    private static final String FACET_FIRST         = "first".intern();
    private static final String FACET_PREVOIUS      = "previous".intern();
    private static final String FACET_NEXT          = "next".intern();
    private static final String FACET_LAST          = "last".intern();
    private static final String FACET_FAST_FORWARD  = "fastf".intern();
    private static final String FACET_FAST_REWIND   = "fastr".intern();

    public static final String RENDERER_TYPE = "net.sourceforge.myfaces.DataScroller";

    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlDataScroller.class);

        HtmlDataScroller scroller = (HtmlDataScroller)component;

        UIData uiData = findUIData(scroller, component);

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String param = (String)parameter.get(component.getClientId(context));
        if (param != null)
        {
            if (param.equals(FACET_FIRST))
            {
                uiData.setFirst(0);
            }
            else if (param.equals(FACET_PREVOIUS))
            {
                int previous = uiData.getFirst() - uiData.getRows();
                if (previous >= 0)
                    uiData.setFirst(previous);
            }
            else if (param.equals(FACET_NEXT))
            {
                int next = uiData.getFirst() + uiData.getRows();
                if (next < uiData.getRowCount())
                    uiData.setFirst(next);
            }
            else if (param.equals(FACET_FAST_FORWARD))
            {
                int fastStep = scroller.getFastStep();
                if (fastStep <= 0)
                    fastStep = 1;
                int next = uiData.getFirst() + uiData.getRows() * fastStep;
                int rowcount = uiData.getRowCount();
                if (next > rowcount)
                    next = rowcount - (rowcount % uiData.getRows());
                uiData.setFirst(next);
            }
            else if (param.equals(FACET_FAST_REWIND))
            {
                int fastStep = scroller.getFastStep();
                if (fastStep <= 0)
                    fastStep = 1;
                int previous = uiData.getFirst() - uiData.getRows() * fastStep;
                if (previous < 0)
                    previous = 0;
                uiData.setFirst(previous);
            }
            else if (param.equals(FACET_LAST))
            {
                int rowcount = uiData.getRowCount();
                int last = rowcount - (rowcount % uiData.getRows());
                if (last >= 0)
                {
                    uiData.setFirst(last);
                }
                else
                {
                    uiData.setFirst(0);
                }
            }

        }
    }

    private UIData findUIData(HtmlDataScroller scroller, UIComponent component)
    {
        String forStr = scroller.getFor();
        UIComponent forComp;
        if (forStr == null)
        {
            // DataScroller may be a child of uiData
            forComp = component.getParent();
        }
        else
        {
            forComp = component.findComponent(scroller.getFor());
            if (forComp == null)
            {
                throw new IllegalArgumentException("dataScroller's for attribute is mandatory if dataScroller is defined outside of UIData");
            }
        }
        if (!(forComp instanceof UIData))
        {
            throw new IllegalArgumentException("uiComponent referenced by attribute tableScroller@for must be of type " + UIData.class.getName());
        }
        return (UIData)forComp;
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
        RendererUtils.checkParamValidity(facescontext, uicomponent, HtmlDataScroller.class);

        Map requestMap = facescontext.getExternalContext().getRequestMap();
        HtmlDataScroller scroller = (HtmlDataScroller)uicomponent;

        UIData uiData = findUIData(scroller, uicomponent);

        int rows = uiData.getRows();
        int pageCount;
        if (rows > 0)
        {
            pageCount = rows <= 0 ? 1 : uiData.getRowCount() / rows;
            if (uiData.getRowCount() % rows > 0)
            {
                pageCount++;
            }
        }
        else
        {
            pageCount = 1;
        }

        String pageCountVar = scroller.getPageCountVar();
        if (pageCountVar != null)
        {
            requestMap.put(pageCountVar, new Integer(pageCount));
        }
        String pageIndexVar = scroller.getPageIndexVar();
        if (pageIndexVar != null)
        {
            int pageIndex = uiData.getFirst() / rows + 1;
            if (uiData.getFirst() % rows > 0)
            {
                pageIndex++;
            }
            requestMap.put(pageIndexVar, new Integer(pageIndex));
        }

        RendererUtils.renderChildren(facescontext, uicomponent);

        if (pageCountVar != null)
        {
            requestMap.remove(pageCountVar);
        }
        if (pageIndexVar != null)
        {
            requestMap.remove(pageIndexVar);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlDataScroller scroller = (HtmlDataScroller)uiComponent;

        UIComponent facetComp = scroller.getFirst();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_FIRST);
            writer.write("&nbsp;");
        }
        facetComp = scroller.getFastRewind();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_FAST_REWIND);
            writer.write("&nbsp;");
        }
        facetComp = scroller.getPrevious();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_PREVOIUS);
            writer.write("&nbsp;");
        }
        facetComp = scroller.getNext();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_NEXT);
            writer.write("&nbsp;");
        }
        facetComp = scroller.getFastForward();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_FAST_FORWARD);
            writer.write("&nbsp;");
        }
        facetComp = scroller.getLast();
        if (facetComp != null)
        {
            renderFacet(facesContext, scroller, facetComp, FACET_LAST);
        }
    }

    private void renderFacet(FacesContext facesContext,
                             HtmlDataScroller scroller,
                             UIComponent facetComp,
                             String facetName)
        throws IOException
    {
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
