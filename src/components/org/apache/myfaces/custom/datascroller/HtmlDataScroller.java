/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.custom.datascroller;

import net.sourceforge.myfaces.component.html.ext.HtmlPanelGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScroller
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private static final String FIRST_FACET_NAME            = "first";
    private static final String LAST_FACET_NAME             = "last";
    private static final String NEXT_FACET_NAME             = "next";
    private static final String PREVIOUS_FACET_NAME         = "previous";
    private static final String FAST_FORWARD_FACET_NAME     = "fastforward";
    private static final String FAST_REWIND_FACET_NAME      = "fastrewind";

    public void setFirst(UIComponent first)
    {
        getFacets().put(FIRST_FACET_NAME, first);
    }

    public UIComponent getFirst()
    {
        return (UIComponent)getFacets().get(FIRST_FACET_NAME);
    }

    public void setLast(UIComponent last)
    {
        getFacets().put(LAST_FACET_NAME, last);
    }

    public UIComponent getLast()
    {
        return (UIComponent)getFacets().get(LAST_FACET_NAME);
    }

    public void setNext(UIComponent next)
    {
        getFacets().put(NEXT_FACET_NAME, next);
    }

    public UIComponent getNext()
    {
        return (UIComponent)getFacets().get(NEXT_FACET_NAME);
    }

    public void setFastForward(UIComponent previous)
    {
        getFacets().put(FAST_FORWARD_FACET_NAME, previous);
    }

    public UIComponent getFastForward()
    {
        return (UIComponent)getFacets().get(FAST_FORWARD_FACET_NAME);
    }

    public void setFastRewind(UIComponent previous)
    {
        getFacets().put(FAST_REWIND_FACET_NAME, previous);
    }

    public UIComponent getFastRewind()
    {
        return (UIComponent)getFacets().get(FAST_REWIND_FACET_NAME);
    }

    public void setPrevoius(UIComponent previous)
    {
        getFacets().put(PREVIOUS_FACET_NAME, previous);
    }

    public UIComponent getPrevious()
    {
        return (UIComponent)getFacets().get(PREVIOUS_FACET_NAME);
    }


    public boolean getRendersChildren()
    {
        return true;
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataScroller";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.DataScroller";

    private String _for = null;
    private Integer _fastStep = null;
    private String _pageIndexVar = null;
    private String _pageCountVar = null;

    public HtmlDataScroller()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFastStep(int fastStep)
    {
        _fastStep = new Integer(fastStep);
    }

    public int getFastStep()
    {
        if (_fastStep != null) return _fastStep.intValue();
        ValueBinding vb = getValueBinding("fastStep");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : Integer.MIN_VALUE;
    }

    public void setPageIndexVar(String pageIndexVar)
    {
        _pageIndexVar = pageIndexVar;
    }

    public String getPageIndexVar()
    {
        if (_pageIndexVar != null) return _pageIndexVar;
        ValueBinding vb = getValueBinding("pageIndexVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPageCountVar(String pageCountVar)
    {
        _pageCountVar = pageCountVar;
    }

    public String getPageCountVar()
    {
        if (_pageCountVar != null) return _pageCountVar;
        ValueBinding vb = getValueBinding("pageCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }



    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _fastStep;
        values[3] = _pageIndexVar;
        values[4] = _pageCountVar;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
        _fastStep = (Integer)values[2];
        _pageIndexVar = (String)values[3];
        _pageCountVar = (String)values[4];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
