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

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.component.MyFacesUIOutput;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.KeyBundleAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyFacesTag
    extends UIComponentTag
    implements IterationTag,
               MyFacesTagBaseIF,
               CommonComponentProperties,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               KeyBundleAttributes,
               UserRoleAttributes
{
    protected MyFacesTagHelper _helper;

    public MyFacesTag()
    {
        super();
        _helper = new MyFacesTagHelper(this);
    }

    public int getDoStartValue() throws JspException
    {
        return _helper.isComponentVisible()
                ? Tag.EVAL_BODY_INCLUDE
                : Tag.SKIP_BODY;
    }


    public int doEndTag() throws JspException
    {
        try
        {
            return super.doEndTag();
        }
        finally
        {
            _helper.release();
            setId(null);
            setRendered(true);
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
    public int doAfterBody()
        throws JspException
    {
        return getDoAfterBodyValue();
    }

    public int getDoAfterBodyValue() throws JspException
    {
        return Tag.SKIP_BODY;
    }



    //Make protected properties accessible:

    public abstract String getComponentType();

    public String getId()
    {
        return id;
    }

    public String getFacetName()
    {
        return super.getFacetName();
    }


    /**
     * TODO: Why do they suppress facets ?!
     */
    protected boolean isSuppressed()
    {
        return _helper.isSuppressed();
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
    protected void setComponentPropertyObject(String attrName, Object attrValue)
    {
        _helper.setComponentPropertyObject(attrName, attrValue);
    }

    protected void setComponentPropertyString(String attrName, Object attrValue)
    {
        _helper.setComponentPropertyString(attrName, attrValue);
    }

    protected void setComponentPropertyBoolean(String attrName, Object attrValue)
    {
        _helper.setComponentPropertyBoolean(attrName, attrValue);
    }

    protected void setRendererAttributeObject(String attrName, Object attrValue)
    {
        _helper.setRendererAttributeObject(attrName, attrValue);
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

    protected final UIComponent findComponent(FacesContext facesContext)
        throws JspException
    {
        UIComponent c = _helper.findComponent();
        if (c == null)
        {
            c = super.findComponent(facesContext);
            if (getCreated())
            {
                LogUtil.getLogger().finest("Component " + getComponent() + " with id " + getComponent().getComponentId() + " was just created by UIComponentTag.");
            }
        }
        return c;
    }

    public void setCreated(boolean b)
    {
        UIComponentTagHacks.setCreated(this, b);
    }


    /**
     * Overwrite to make public.
     * @param value
     */
    protected void setValue(Object value)
    {
        setComponentPropertyObject(MyFacesUIOutput.VALUE_PROP, value);
    }




//----------------- common tag attributes -------------------------------

    // UIComponent attributes

    public void setId(String s)
    {
        super.setId(s);
    }

    public void setConverter(Object converter)
    {
        setRendererAttributeObject(CONVERTER_ATTR, converter);
    }

    public void setValueRef(String s)
    {
        setComponentPropertyString(MyFacesUIOutput.VALUE_REF_PROP, s);
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
