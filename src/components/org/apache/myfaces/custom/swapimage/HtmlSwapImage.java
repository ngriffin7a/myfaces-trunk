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
package org.apache.myfaces.custom.swapimage;

import org.apache.myfaces.component.UserRoleUtils;

import javax.faces.component.UIGraphic;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2005/01/09 12:36:58  tomsp
 *          added new component swapimage
 *
 */
public class HtmlSwapImage extends UIGraphic
{

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlSwapImage";
    public static final String COMPONENT_FAMILY = "javax.faces.Graphic";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SwapImage";
    private static final boolean DEFAULT_ISMAP = false;

    private String _swapImageUrl = null;
    private String _alt = null;
    private String _dir = null;
    private String _height = null;
    private Boolean _ismap = null;
    private String _lang = null;
    private String _longdesc = null;
    private String _onclick = null;
    private String _ondblclick = null;
    private String _onkeydown = null;
    private String _onkeypress = null;
    private String _onkeyup = null;
    private String _style = null;
    private String _styleClass = null;
    private String _title = null;
    private String _usemap = null;
    private String _width = null;

    public HtmlSwapImage()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSwapImageUrl(String swapImageUrl)
    {
        _swapImageUrl = swapImageUrl;
    }

    public String getSwapImageUrl()
    {
        if (_swapImageUrl != null) return _swapImageUrl;
        ValueBinding vb = getValueBinding("swapImageUrl");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setAlt(String alt)
    {
        _alt = alt;
    }

    public String getAlt()
    {
        if (_alt != null) return _alt;
        ValueBinding vb = getValueBinding("alt");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setDir(String dir)
    {
        _dir = dir;
    }

    public String getDir()
    {
        if (_dir != null) return _dir;
        ValueBinding vb = getValueBinding("dir");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setHeight(String height)
    {
        _height = height;
    }

    public String getHeight()
    {
        if (_height != null) return _height;
        ValueBinding vb = getValueBinding("height");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setIsmap(boolean ismap)
    {
        _ismap = Boolean.valueOf(ismap);
    }

    public boolean isIsmap()
    {
        if (_ismap != null) return _ismap.booleanValue();
        ValueBinding vb = getValueBinding("ismap");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_ISMAP;
    }

    public void setLang(String lang)
    {
        _lang = lang;
    }

    public String getLang()
    {
        if (_lang != null) return _lang;
        ValueBinding vb = getValueBinding("lang");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setLongdesc(String longdesc)
    {
        _longdesc = longdesc;
    }

    public String getLongdesc()
    {
        if (_longdesc != null) return _longdesc;
        ValueBinding vb = getValueBinding("longdesc");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnclick(String onclick)
    {
        _onclick = onclick;
    }

    public String getOnclick()
    {
        if (_onclick != null) return _onclick;
        ValueBinding vb = getValueBinding("onclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOndblclick(String ondblclick)
    {
        _ondblclick = ondblclick;
    }

    public String getOndblclick()
    {
        if (_ondblclick != null) return _ondblclick;
        ValueBinding vb = getValueBinding("ondblclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeydown(String onkeydown)
    {
        _onkeydown = onkeydown;
    }

    public String getOnkeydown()
    {
        if (_onkeydown != null) return _onkeydown;
        ValueBinding vb = getValueBinding("onkeydown");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeypress(String onkeypress)
    {
        _onkeypress = onkeypress;
    }

    public String getOnkeypress()
    {
        if (_onkeypress != null) return _onkeypress;
        ValueBinding vb = getValueBinding("onkeypress");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeyup(String onkeyup)
    {
        _onkeyup = onkeyup;
    }

    public String getOnkeyup()
    {
        if (_onkeyup != null) return _onkeyup;
        ValueBinding vb = getValueBinding("onkeyup");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyle(String style)
    {
        _style = style;
    }

    public String getStyle()
    {
        if (_style != null) return _style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public String getStyleClass()
    {
        if (_styleClass != null) return _styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public String getTitle()
    {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setUsemap(String usemap)
    {
        _usemap = usemap;
    }

    public String getUsemap()
    {
        if (_usemap != null) return _usemap;
        ValueBinding vb = getValueBinding("usemap");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setWidth(String width)
    {
        _width = width;
    }

    public String getWidth()
    {
        if (_width != null) return _width;
        ValueBinding vb = getValueBinding("width");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[18];
        values[0] = super.saveState(context);
        values[1] = _swapImageUrl;
        values[2] = _alt;
        values[3] = _dir;
        values[4] = _height;
        values[5] = _ismap;
        values[6] = _lang;
        values[7] = _longdesc;
        values[8] = _onclick;
        values[9] = _ondblclick;
        values[10] = _onkeydown;
        values[11] = _onkeypress;
        values[12] = _onkeyup;
        values[13] = _style;
        values[14] = _styleClass;
        values[15] = _title;
        values[16] = _usemap;
        values[17] = _width;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _swapImageUrl = (String)values[1];
        _alt = (String)values[2];
        _dir = (String)values[3];
        _height = (String)values[4];
        _ismap = (Boolean)values[5];
        _lang = (String)values[6];
        _longdesc = (String)values[7];
        _onclick = (String)values[8];
        _ondblclick = (String)values[9];
        _onkeydown = (String)values[10];
        _onkeypress = (String)values[11];
        _onkeyup = (String)values[12];
        _style = (String)values[13];
        _styleClass = (String)values[14];
        _title = (String)values[15];
        _usemap = (String)values[16];
        _width = (String)values[17];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
