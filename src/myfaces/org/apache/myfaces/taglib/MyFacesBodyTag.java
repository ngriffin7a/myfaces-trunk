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
    implements MyFacesTagBaseIF
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


    public int doEndTag()
        throws JspException
    {
        UIComponent comp = getComponent();
        comp.setAttribute(BODY_CONTENT_ATTR, getBodyContent());
        int ret = super.doEndTag();
        comp.setAttribute(BODY_CONTENT_ATTR, null);
        return ret;
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
    protected void setComponentAttribute(String attrName, Object attrValue)
    {
        _helper.setComponentAttribute(attrName, attrValue);
    }

    protected void setComponentAttribute(String attrName, boolean attrValue)
    {
        _helper.setComponentAttribute(attrName, attrValue);
    }

    protected void setRendererAttribute(String attrName, Object attrValue)
    {
        _helper.setRendererAttribute(attrName, attrValue);
    }

    protected void setRendererAttribute(String attrName, boolean attrValue)
    {
        _helper.setRendererAttribute(attrName, attrValue);
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        _helper.overrideProperties(uiComponent);
    }


    //standard tag properties
    public void setModelReference(String s)
    {
        setComponentAttribute(CommonComponentAttributes.MODEL_REFERENCE_ATTR, s);
    }

    public void setValue(Object value)
    {
        setComponentAttribute(CommonComponentAttributes.VALUE_ATTR, value);
    }

    public void setConverter(Object converter)
    {
        setRendererAttribute(CommonRendererAttributes.CONVERTER_ATTR, converter);
    }

    //Iteration Tag support
    public int getDoAfterBodyValue() throws JspException
    {
        return Tag.SKIP_BODY;
    }


    protected final UIComponent findComponent()
        throws JspException
    {
        _helper.findComponent();
        boolean b = getCreated();
        UIComponent c = super.findComponent();
        setCreated(b);
        return c;
    }

    public void setCreated(boolean b)
    {
        created = b;
    }
}
