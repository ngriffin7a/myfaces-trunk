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
package net.sourceforge.myfaces.component.ext;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelLayout
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelLayout.class);

    //HTML table attributes
    //TODO!

    //Layout attributes
    private String _layout;
    private String _headerClass;
    private String _navigationClass;
    private String _bodyClass;
    private String _footerClass;

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ?
               (String)vb.getValue(getFacesContext()) :
               null;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public String getHeaderClass()
    {
        if (_headerClass != null) return _headerClass;
        ValueBinding vb = getValueBinding("headerClass");
        return vb != null ?
               (String)vb.getValue(getFacesContext()) :
               null;
    }

    public void setHeaderClass(String headerClass)
    {
        _headerClass = headerClass;
    }

    public String getNavigationClass()
    {
        if (_navigationClass != null) return _navigationClass;
        ValueBinding vb = getValueBinding("navigationClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setNavigationClass(String navigationClass)
    {
        _navigationClass = navigationClass;
    }

    public String getBodyClass()
    {
        if (_bodyClass != null) return _bodyClass;
        ValueBinding vb = getValueBinding("bodyClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setBodyClass(String bodyClass)
    {
        _bodyClass = bodyClass;
    }

    public String getFooterClass()
    {
        if (_footerClass != null) return _footerClass;
        ValueBinding vb = getValueBinding("footerClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFooterClass(String footerClass)
    {
        _footerClass = footerClass;
    }



    // typesafe facet getters

    public HtmlPanelGroup getHeader()
    {
        return (HtmlPanelGroup)getFacet("header");
    }

    //TODO: return HtmlPanelNavigation type
    public HtmlPanelGroup getNavigation()
    {
        return (HtmlPanelGroup)getFacet("navigation");
    }

    public HtmlPanelGroup getBody()
    {
        return (HtmlPanelGroup)getFacet("body");
    }

    public HtmlPanelGroup getFooter()
    {
        return (HtmlPanelGroup)getFacet("footer");
    }

}
