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
 * @version $Revision$ $Date$
 */
public class HtmlInputTextareaTag
    extends HtmlComponentTag
{
    public String getComponentType()
    {
        return "javax.faces.InputTextarea";
    }

    public String getDefaultRendererType()
    {
        return "javax.faces.Textarea";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML input attributes
    private String _accesskey;
    private String _cols;
    private String _datafld; //FIXME: not in RI so far
    private String _datasrc; //FIXME: not in RI so far
    private String _dataformatas; //FIXME: not in RI so far
    private String _disabled;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _readonly;
    private String _rows;
    private String _tabindex;

    // UIOutput attributes
    // value and converter --> already implemented in MyfacesComponentTag

    // UIInput attributes
    private String _required;
    private String _validator;
    private String _valueChangeListener;

    //HtmlTextArea attributes
    // FIXME: is in RI, but not in HTML 4.0. what to do?
    private String _alt;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setIntegerProperty(component, HTML.COLS_ATTR, _cols);
        setStringProperty(component, HTML.DATAFLD_ATTR, _datafld);
        setStringProperty(component, HTML.DATASRC_ATTR, _datasrc);
        setStringProperty(component, HTML.DATAFORMATAS_ATTR, _dataformatas);
        setBooleanProperty(component, HTML.DISABLED_ATTR, _disabled);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, HTML.ONSELECT_ATTR, _onselect);
        setBooleanProperty(component, HTML.READONLY_ATTR, _readonly);
        setIntegerProperty(component, HTML.ROWS_ATTR, _rows);
        setIntegerProperty(component, HTML.TABINDEX_ATTR, _tabindex);

        setBooleanProperty(component, JSFAttr.REQUIRED_ATTR, _required);
        setStringProperty(component, JSFAttr.VALIDATOR_ATTR, _validator);
        setValueChangedListenerProperty(component, _valueChangeListener);

        setStringProperty(component, HTML.ALT_ATTR, _alt);
    }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setAlt(String alt)
    {
        _alt = alt;
    }

    public void setCols(String cols)
    {
        _cols = cols;
    }

    public void setDatafld(String datafld)
    {
        _datafld = datafld;
    }

    public void setDatasrc(String datasrc)
    {
        _datasrc = datasrc;
    }

    public void setDataformatas(String dataformatas)
    {
        _dataformatas = dataformatas;
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

    public void setReadonly(String readonly)
    {
        _readonly = readonly;
    }

    public void setRows(String rows)
    {
        _rows = rows;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setRequired(String required)
    {
        _required = required;
    }

    public void setValueChangeListener(String valueChangeListener)
    {
        _valueChangeListener = valueChangeListener;
    }

    public void setValidator(String validator)
    {
        _validator = validator;
    }
}
