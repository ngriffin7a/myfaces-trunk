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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.taglib.MyfacesComponentTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class HtmlComponentTag
        extends MyfacesComponentTag
{
    //private static final Log log = LogFactory.getLog(HtmlComponentTag.class);

    //HTML universal attributes
    private String _dir;
    private String _lang;
    private String _style;
    private String _styleClass;
    private String _title;

    //HTML event handler attributes
    private String _onclick;
    private String _ondblclick;
    private String _onkeydown;
    private String _onkeypress;
    private String _onkeyup;
    private String _onmousedown;
    private String _onmousemove;
    private String _onmouseout;
    private String _onmouseover;
    private String _onmouseup;


    //Common HtmlRenderer attributes
    private String _tooltip;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, HTML.DIR_ATTR, _dir);
        setStringProperty(component, HTML.LANG_ATTR, _lang);
        setStringProperty(component, HTML.STYLE_ATTR, _style);
        setStringProperty(component, HTML.TITLE_ATTR, _title);
        setStringProperty(component, HTML.STYLE_CLASS_ATTR, _styleClass);
        setStringProperty(component, HTML.ONCLICK_ATTR, _onclick);
        setStringProperty(component, HTML.ONDBLCLICK_ATTR, _ondblclick);
        setStringProperty(component, HTML.ONMOUSEDOWN_ATTR, _onmousedown);
        setStringProperty(component, HTML.ONMOUSEUP_ATTR, _onmouseup);
        setStringProperty(component, HTML.ONMOUSEOVER_ATTR, _onmouseover);
        setStringProperty(component, HTML.ONMOUSEMOVE_ATTR, _onmousemove);
        setStringProperty(component, HTML.ONMOUSEOUT_ATTR, _onmouseout);
        setStringProperty(component, HTML.ONKEYPRESS_ATTR, _onkeypress);
        setStringProperty(component, HTML.ONKEYDOWN_ATTR, _onkeydown);
        setStringProperty(component, HTML.ONKEYUP_ATTR, _onkeyup);
        setStringProperty(component, JSFAttr.TOOLTIP_ATTR, _tooltip);
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public void setDir(String dir)
    {
        _dir = dir;
    }

    public void setLang(String lang)
    {
        _lang = lang;
    }

    public void setStyle(String style)
    {
        _style = style;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public void setOnclick(String onclick)
    {
        _onclick = onclick;
    }

    public void setOndblclick(String ondblclick)
    {
        _ondblclick = ondblclick;
    }

    public void setOnmousedown(String onmousedown)
    {
        _onmousedown = onmousedown;
    }

    public void setOnmouseup(String onmouseup)
    {
        _onmouseup = onmouseup;
    }

    public void setOnmouseover(String onmouseover)
    {
        _onmouseover = onmouseover;
    }

    public void setOnmousemove(String onmousemove)
    {
        _onmousemove = onmousemove;
    }

    public void setOnmouseout(String onmouseout)
    {
        _onmouseout = onmouseout;
    }

    public void setOnkeypress(String onkeypress)
    {
        _onkeypress = onkeypress;
    }

    public void setOnkeydown(String onkeydown)
    {
        _onkeydown = onkeydown;
    }

    public void setOnkeyup(String onkeyup)
    {
        _onkeyup = onkeyup;
    }

    public void setTooltip(String tooltip)
    {
        _tooltip = tooltip;
    }
}
