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
package net.sourceforge.myfaces.taglib.html;

import net.sourceforge.myfaces.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFormTag
        extends HtmlComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlFormTag.class);

    public String getComponentType()
    {
        return HtmlForm.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "javax.faces.Form";
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HTML form attributes

    private String _accept;
    private String _acceptCharset;
    private String _enctype;
    private String _name;
    private String _onreset;
    private String _onsubmit;
    private String _target;

    // UIForm attributes --> none so far


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, HTML.ACCEPT_ATTR, _accept);
        setStringProperty(component, HTML.ACCEPT_CHARSET_ATTR, _acceptCharset);
        setStringProperty(component, HTML.ENCTYPE_ATTR, _enctype);
        setStringProperty(component, HTML.NAME_ATTR, _name);
        setStringProperty(component, HTML.ONRESET_ATTR, _onreset);
        setStringProperty(component, HTML.ONSUMBIT_ATTR, _onsubmit);
        setStringProperty(component, HTML.TARGET_ATTR, _target);
    }

    public void setAccept(String accept)
    {
        _accept = accept;
    }

    public void setAcceptCharset(String acceptCharset)
    {
        _acceptCharset = acceptCharset;
    }

    public void setEnctype(String enctype)
    {
        _enctype = enctype;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setOnreset(String onreset)
    {
        _onreset = onreset;
    }

    public void setOnsubmit(String onsubmit)
    {
        _onsubmit = onsubmit;
    }

    public void setTarget(String target)
    {
        _target = target;
    }

}
