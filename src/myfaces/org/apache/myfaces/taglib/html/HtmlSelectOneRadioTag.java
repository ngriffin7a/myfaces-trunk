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


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 */
public class HtmlSelectOneRadioTag
    extends HtmlComponentTag
{
    public String getComponentType()
    {
        return "SelectOneRadio";
    }

    public String getDefaultRendererType()
    {
        return "Radio";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML input attributes relevant for radio
    private String _accesskey;
    private String _disabled;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _tabindex;

    // UIInput attributes
    private String _required;
    private String _validator;

    // HTMLSelectOneRadio attributes
    private String _border;
    private String _disabledClass;
    private String _enabledClass;
    private String _layout;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setBooleanProperty(component, HTML.DISABLED_ATTR, _disabled);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, HTML.ONSELECT_ATTR, _onselect);
        setStringProperty(component, HTML.TABINDEX_ATTR, _tabindex);

        setBooleanProperty(component, JSFAttr.REQUIRED_ATTR, _required);
        setStringProperty(component, JSFAttr.VALIDATOR_ATTR, _validator);

        setIntegerProperty(component, JSFAttr.BORDER_ATTR, _border);
        setStringProperty(component, JSFAttr.DISABLED_CLASS_ATTR, _disabledClass);
        setStringProperty(component, JSFAttr.ENABLED_CLASS_ATTR, _enabledClass);
        setStringProperty(component, JSFAttr.LAYOUT_ATTR, _layout);
    }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setBorder(String border)
    {
        _border = border;
    }

    public void setDisabled(String disabled)
    {
        _disabled = disabled;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnchange(String onchange)
    {
        _onchange = onchange;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setOnselect(String onselect)
    {
        _onselect = onselect;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setRequired(String required)
    {
        _required = required;
    }

    public void setValidator(String validator)
    {
        _validator = validator;
    }

    public void setDisabledClass(String disabledClass)
    {
        _disabledClass = disabledClass;
    }

    public void setEnabledClass(String enabledClass)
    {
        _enabledClass = enabledClass;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }
}
