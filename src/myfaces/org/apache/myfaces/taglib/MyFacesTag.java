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
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyFacesTag
    extends FacesTag
{
    protected PageContext _pageContext;
    protected FacesContext _facesContext;
    private Set _attributes = null;

    public MyFacesTag()
    {
        super();
    }

    protected void init()
    {
        _attributes = null;
    }


    //Delegation methods:
    public int getDoStartValue() throws JspException
    {
        if (getComponent().getRendersChildren())
        {
            //No JSP rendering, when this component renders it's children
            return Tag.SKIP_BODY;
        }
        else
        {
            return Tag.EVAL_BODY_INCLUDE;
        }
    }


    public int getDoEndValue() throws JspException
    {
        return Tag.EVAL_PAGE;
    }


    public void release()
    {
        super.release();
        init();
    }


    //JSF Spec.


    //subclass helpers
    public void setPageContext(PageContext pageContext)
    {
        super.setPageContext(pageContext);
        _pageContext = pageContext;
    }

    protected PageContext getPageContext()
    {
        return _pageContext;
    }

    protected FacesContext getFacesContext()
    {
        if (_facesContext == null)
        {
            //FacesServlet saves the FacesContext as request attribute:
            _facesContext = (FacesContext)_pageContext.getAttribute("javax.faces.context.FacesContext",
                                                                    PageContext.REQUEST_SCOPE);
            if (_facesContext == null)
            {
                throw new IllegalStateException("No faces context!?");
            }
        }
        return _facesContext;
    }


    //property helpers

    /**
     * @deprecated use setComponentAttribute or setRendererAttribute instead
     */
    protected void setPropertyX(String attrName, Object attrValue)
    {
        setRendererAttribute(attrName, attrValue);
    }

    protected void setComponentAttribute(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, true));
    }

    protected void setRendererAttribute(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, false));
    }

    protected static class Attribute
    {
        public String name;
        public Object value;
        public boolean isComponentAttribute;

        public Attribute(String name, Object value, boolean isComponentAttribute)
        {
            this.name = name;
            this.value = value;
            this.isComponentAttribute = isComponentAttribute;
        }
    }


    protected void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);

        FacesContext facesContext = null;
        if (getPageContext() != null)
        {
            facesContext = (FacesContext)getPageContext()
                                    .getAttribute("javax.faces.context.FacesContext",
                                                  PageContext.REQUEST_SCOPE);
        }

        if (_attributes != null)
        {
            for (Iterator it = _attributes.iterator(); it.hasNext();)
            {
                Attribute attr = (Attribute)it.next();
                if (facesContext != null
                    && attr.isComponentAttribute
                    && attr.name.equals(CommonComponentAttributes.VALUE_ATTR))
                {
                    if (uiComponent.currentValue(facesContext) == null)
                    {
                        Object rtValue = attr.value;
                        if (rtValue instanceof String)
                        {
                            Converter conv = ConverterUtils.findConverter(facesContext,
                                                                          uiComponent);
                            if (conv != null)
                            {
                                UIComponentUtils.convertAndSetValue(facesContext,
                                                                    uiComponent,
                                                                    (String)rtValue,
                                                                    conv,
                                                                    false);    //No error message
                            }
                            else
                            {
                                UIComponentUtils.setComponentValue(uiComponent, rtValue);
                            }
                        }
                        else
                        {
                            UIComponentUtils.setComponentValue(uiComponent, rtValue);
                        }
                    }
                }
                else if (attr.isComponentAttribute)
                {
                    //Try bean property setter first
                    try
                    {
                        if (BeanUtils.getBeanPropertyValue(uiComponent, attr.name) == null)
                        {
                            BeanUtils.setBeanPropertyValue(uiComponent, attr.name, attr.value);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        //Component does not have this property!
                        LogUtil.getLogger().severe("Component " + uiComponent.getCompoundId() + " does not have valid property setter and getter methods for property '" + attr.name + "'.");
                        //Alternativly set by attribute name:
                        if (uiComponent.getAttribute(attr.name) == null)
                        {
                            uiComponent.setAttribute(attr.name, attr.value);
                        }
                    }
                }
                else
                {
                    if (uiComponent.getAttribute(attr.name) == null)
                    {
                        uiComponent.setAttribute(attr.name, attr.value);
                    }
                }
            }
        }
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

    public final int doAfterBody() throws JspException
    {
        return getDoAfterBodyValue();
    }

    public int getDoAfterBodyValue() throws JspException
    {
        return Tag.SKIP_BODY;
    }

}
