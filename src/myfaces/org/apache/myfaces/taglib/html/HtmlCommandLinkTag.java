/**
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

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandLink;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 */
public class HtmlCommandLinkTag
    extends HtmlComponentTag
{
    //private static final Log log = LogFactory.getLog(HtmlCommandLinkTag.class);

    public String getComponentType()
    {
        return HtmlCommandLink.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "javax.faces.Link";
    }



    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML anchor attributes relevant for command link
    private String _accesskey;
    private String _charset;
    private String _coords;
    private String _hreflang;
    private String _rel;
    private String _rev;
    private String _shape;
    private String _tabindex;
    private String _type;
    //HtmlCommandLink Attributes
    //FIXME: is mentioned in JSF API, but is no official anchor-attribute of HTML 4.0... what to do?
    private String _onblur;
    //FIXME: is mentioned in JSF API, but is no official anchor-attribute of HTML 4.0... what to do?
    private String _onfocus;

    // UICommand attributes
    private String _action;
    private String _immediate;
    private String _actionListener;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, HTML.CHARSET_ATTR, _charset);
        setStringProperty(component, HTML.COORDS_ATTR, _coords);
        setStringProperty(component, HTML.HREFLANG_ATTR, _hreflang);
        setStringProperty(component, HTML.REL_ATTR, _rel);
        setStringProperty(component, HTML.REV_ATTR, _rev);
        setStringProperty(component, HTML.SHAPE_ATTR, _shape);
        setStringProperty(component, HTML.TABINDEX_ATTR, _tabindex);
        setStringProperty(component, HTML.TYPE_ATTR, _type);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setActionProperty(component, _action);
        setActionListenerProperty(component, _actionListener);
        setBooleanProperty(component, JSFAttr.IMMEDIATE_ATTR, _immediate);
   }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setCharset(String charset)
    {
        _charset = charset;
    }

    public void setCoords(String coords)
    {
        _coords = coords;
    }

    public void setHreflang(String hreflang)
    {
        _hreflang = hreflang;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setRel(String rel)
    {
        _rel = rel;
    }

    public void setRev(String rev)
    {
        _rev = rev;
    }

    public void setShape(String shape)
    {
        _shape = shape;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setType(String type)
    {
        _type = type;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public void setImmediate(String immediate)
    {
        _immediate = immediate;
    }

    public void setActionListener(String actionListener)
    {
        _actionListener = actionListener;
    }
}
