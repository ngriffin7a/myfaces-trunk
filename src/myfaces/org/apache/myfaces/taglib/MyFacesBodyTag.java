/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.KeyBundleAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesBodyTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * BodyContent is automatically added as an attribute ({@link #BODY_CONTENT_ATTR)
 * to the associated component. This is a convenient way to get the
 * BodyContent in the encodeEnd of the corresponding Renderer.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyFacesBodyTag
    extends FacesBodyTag
    implements MyFacesTagBaseIF,
               CommonComponentAttributes,
               CommonRendererAttributes,
    HTMLUniversalAttributes,
    HTMLEventHandlerAttributes,
               KeyBundleAttributes,
               UserRoleAttributes
{
    public static final String BODY_CONTENT_ATTR
        = MyFacesBodyTag.class.getName() + ".BODY_CONTENT";

    protected MyFacesTagHelper _helper;

    public MyFacesBodyTag()
    {
        super();
        _helper = new MyFacesTagHelper(this);
    }

    public int getDoStartValue() throws JspException
    {
        return BodyTag.EVAL_BODY_BUFFERED;
    }


    public int doEndTag() throws JspException
    {
        try
        {
            UIComponent comp = getComponent();
            comp.setAttribute(BODY_CONTENT_ATTR, getBodyContent());
            int ret = super.doEndTag();
            comp.setAttribute(BODY_CONTENT_ATTR, null);
            return ret;
        }
        finally
        {
            _helper.release();
            id = null;
            modelReference = null;
            created = false;
        }
    }

    public int getDoEndValue() throws JspException
    {
        return Tag.EVAL_PAGE;
    }


    public void release()
    {
        super.release();
        _helper.release();
    }


    //Iteration Tag support
    public int getDoAfterBodyValue() throws JspException
    {
        return Tag.SKIP_BODY;
    }


    //subclass helpers
    public void setPageContext(PageContext pageContext)
    {
        super.setPageContext(pageContext);
        _helper.setPageContext(pageContext);
    }

    protected PageContext getPageContext()
    {
        return _helper.getPageContext();
    }

    protected FacesContext getFacesContext()
    {
        return _helper.getFacesContext();
    }


    //property helpers
    protected void setComponentPropertyString(String attrName, Object attrValue)
    {
        _helper.setComponentPropertyString(attrName, attrValue);
    }

    protected void setComponentPropertyBoolean(String attrName, Object attrValue)
    {
        _helper.setComponentPropertyBoolean(attrName, attrValue);
    }

    protected void setRendererAttributeString(String attrName, Object attrValue)
    {
        _helper.setRendererAttributeString(attrName, attrValue);
    }

    protected void setRendererAttributeBoolean(String attrName, Object attrValue)
    {
        _helper.setRendererAttributeBoolean(attrName, attrValue);
    }

    protected void setRendererAttributeInteger(String attrName, Object attrValue)
    {
        _helper.setRendererAttributeInteger(attrName, attrValue);
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        _helper.overrideProperties(uiComponent);
    }

    protected final UIComponent findComponent()
        throws JspException
    {
        UIComponent c = _helper.findComponent();
        if (c == null)
        {
            c = super.findComponent();
        }
        return c;
    }

    public void setCreated(boolean b)
    {
        created = b;
    }

    /**
     * Overwrite to make public.
     * @param value
     */
    protected void setValue(Object value)
    {
        setComponentPropertyString(CommonComponentAttributes.VALUE_ATTR, value);
    }




//----------------- common tag attributes -------------------------------

    // UIComponent attributes

    public void setId(String s)
    {
        super.setId(s);
    }

    public void setConverter(Object converter)
    {
        setRendererAttributeString(CONVERTER_ATTR, converter);
    }

    public void setModelReference(String s)
    {
        setComponentPropertyString(MODEL_REFERENCE_ATTR, s);
    }

    public void setRendered(boolean rendered)
    {
        super.setRendered(rendered);
    }

    public void setRendered(Boolean rendered)
    {
        super.setRendered(rendered.booleanValue());
    }


    // HTML 4.0 universal attributes

    public void setDir(String value)
    {
        setRendererAttributeString(DIR_ATTR, value);
    }

    public void setLang(String value)
    {
        setRendererAttributeString(LANG_ATTR, value);
    }

    public void setStyle(String value)
    {
        setRendererAttributeString(STYLE_ATTR, value);
    }

    public void setTitle(String value)
    {
        setRendererAttributeString(TITLE_ATTR, value);
    }



    // HTML 4.0 event-handler attributes

    public void setOnclick(String value)
    {
        setRendererAttributeString(ONCLICK_ATTR, value);
    }

    public void setOndblclick(String value)
    {
        setRendererAttributeString(ONDBLCLICK_ATTR, value);
    }

    public void setOnmousedown(String value)
    {
        setRendererAttributeString(ONMOUSEDOWN_ATTR, value);
    }

    public void setOnmouseup(String value)
    {
        setRendererAttributeString(ONMOUSEUP_ATTR, value);
    }

    public void setOnmouseover(String value)
    {
        setRendererAttributeString(ONMOUSEOVER_ATTR, value);
    }

    public void setOnmousemove(String value)
    {
        setRendererAttributeString(ONMOUSEMOVE_ATTR, value);
    }

    public void setOnmouseout(String value)
    {
        setRendererAttributeString(ONMOUSEOUT_ATTR, value);
    }

    public void setOnkeypress(String value)
    {
        setRendererAttributeString(ONKEYPRESS_ATTR, value);
    }

    public void setOnkeydown(String value)
    {
        setRendererAttributeString(ONKEYDOWN_ATTR, value);
    }

    public void setOnkeyup(String value)
    {
        setRendererAttributeString(ONKEYUP_ATTR, value);
    }


    // key & bundle attributes

    public void setKey(String v)
    {
        setRendererAttributeString(KEY_ATTR, v);
    }

    public void setBundle(String v)
    {
        setRendererAttributeString(BUNDLE_ATTR, v);
    }



    // MyFaces extension: user role attributes

    public void setEnabledOnUserRole(String value)
    {
        setRendererAttributeString(ENABLED_ON_USER_ROLE_ATTR, value);
    }

    public void setVisibleOnUserRole(String value)
    {
        setRendererAttributeString(VISIBLE_ON_USER_ROLE_ATTR, value);
    }

}
