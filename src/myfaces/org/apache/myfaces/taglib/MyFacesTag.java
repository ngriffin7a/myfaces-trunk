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
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyFacesTag
    extends FacesTag
    implements MyFacesTagBaseIF
{
    protected MyFacesTagHelper _helper;

    public MyFacesTag()
    {
        super();
        _helper = new MyFacesTagHelper(this);
    }

    public int doStartTag()
        throws JspException
    {
        return super.doStartTag();
    }

    public int getDoStartValue() throws JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
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
    public int doAfterBody()
        throws JspException
    {
        int ret = super.doAfterBody();

        //Reset number of children for next iteration
        numChildren = 0;

        return ret;
    }

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

    //universal tag properties
    public void setDir(String value)
    {
        setRendererAttribute(CommonRendererAttributes.DIR_ATTR, value);
    }

    public void setLang(String value)
    {
        setRendererAttribute(CommonRendererAttributes.LANG_ATTR, value);
    }

    public void setStyle(String value)
    {
        setRendererAttribute(CommonRendererAttributes.STYLE_ATTR, value);
    }

    public void setTitle(String value)
    {
        setRendererAttribute(CommonRendererAttributes.TITLE_ATTR, value);
    }


    //event-hanlder attributes
    public void setOnclick(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONCLICK_ATTR, value);
    }

    public void setOndblclick(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONDBLCLICK_ATTR, value);
    }

    public void setOnmousedown(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONMOUSEDOWN_ATTR, value);
    }

    public void setOnmouseup(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONMOUSEUP_ATTR, value);
    }

    public void setOnmouseover(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONMOUSEOVER_ATTR, value);
    }

    public void setOnmousemove(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONMOUSEMOVE_ATTR, value);
    }

    public void setOnmouseout(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONMOUSEOUT_ATTR, value);
    }

    public void setOnkeypress(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONKEYPRESS_ATTR, value);
    }

    public void setOnkeydown(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONKEYDOWN_ATTR, value);
    }

    public void setOnkeyup(String value)
    {
        setRendererAttribute(CommonRendererAttributes.ONKEYUP_ATTR, value);
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
}
