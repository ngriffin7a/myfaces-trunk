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

import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyFacesTag
    extends FacesTag
{
    protected PageContext _pageContext;
    protected FacesContext _facesContext;
    private Map _properties = null;

    public MyFacesTag()
    {
        super();
    }

    protected void init()
    {
        _properties = null;
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

    protected void setProperty(String attrName, Object attrValue)
    {
        if (_properties == null)
        {
            _properties = new HashMap();
        }
        _properties.put(attrName, attrValue);
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

        if (_properties != null)
        {
            for (Iterator it = _properties.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                String attrName = (String)entry.getKey();
                if (facesContext != null
                    && attrName.equals(MyFacesComponent.VALUE_ATTR))
                {
                    if (uiComponent.currentValue(facesContext) == null)
                    {
                        Object rtValue = entry.getValue();
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
                else
                {
                    if (uiComponent.getAttribute(attrName) == null)
                    {
                        uiComponent.setAttribute(attrName, entry.getValue());
                    }
                }
            }
        }
    }


    //standard tag properties

    public void setModelReference(String s)
    {
        setProperty(MyFacesComponent.MODEL_REFERENCE_ATTR, s);
    }

    public void setConverter(String s)
    {
        setProperty(MyFacesComponent.CONVERTER_ATTR, s);
    }

    public void setValue(Object value)
    {
        setProperty(MyFacesComponent.VALUE_ATTR, value);
    }

    /*
    public void setValue(String value)
    {
        setProperty(MyFacesComponent.VALUE_ATTR, value);
    }
    */


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
