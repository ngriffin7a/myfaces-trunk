/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public abstract class MyFacesTag
    implements Tag, MyFacesTagExtension
{
    protected static final String RENDERING_PARENT = MyFacesTag.class.getName() + ".RENDERING_PARENT";
    protected Tag _delegation;
    protected PageContext _pageContext;
    protected FacesContext _facesContext;
    protected String _id;
    private Stack _componentStack;
    private boolean _componentNotFound;
    private boolean _componentOnStack;
    private Map _requestTimeValues = null;

    public MyFacesTag()
    {
        initDelegation();
        init();
    }

    protected void initDelegation()
    {
        _delegation = new TagSupport();
    }

    protected void init()
    {
        _pageContext = null;
        _facesContext = null;
        _id = null;
        _componentStack = null;
        _componentNotFound = false;
        _componentOnStack = false;
    }

    //Delegation methods:

    public final int doStartTag() throws JspException
    {
        UIComponent component = findComponent();
        if (component == null)
        {
            return Tag.SKIP_BODY;
        }

        applyRequestTimeValues(component);

        FacesContext facesContext = getFacesContext();
        UIComponent renderingParent = (UIComponent)facesContext.getServletRequest().getAttribute(RENDERING_PARENT);
        if (renderingParent != null)
        {
            //a parent is rendering, so child must not render itself
            return Tag.EVAL_BODY_INCLUDE;
        }

        try
        {
            component.encodeBegin(getFacesContext());
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }

        if (component.getRendersChildren())
        {
            //remember this renering parent for all children
            facesContext.getServletRequest().setAttribute(RENDERING_PARENT, component);
        }

        return doAfterStartTag();
    }

    protected int doAfterStartTag() throws JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
    }


    public final int doEndTag() throws JspException
    {
        FacesContext facesContext = getFacesContext();
        UIComponent component = findComponent();

        UIComponent renderingParent = (UIComponent)facesContext.getServletRequest().getAttribute(RENDERING_PARENT);
        if (renderingParent != null)
        {
            if (renderingParent == component)
            {
                //it's me!
                facesContext.getServletRequest().removeAttribute(RENDERING_PARENT);
            }
            else
            {
                //a parent is rendering, so child must not render itself
                popComponent();
                init();
                return Tag.EVAL_PAGE;
            }
        }

        try
        {
            if (component.getRendersChildren())
            {
                component.encodeChildren(facesContext);
            }
            component.encodeEnd(facesContext);
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        popComponent();
        init();
        return Tag.EVAL_PAGE;
    }


    public Tag getParent()
    {
        return _delegation.getParent();
    }

    public void release()
    {
        _delegation.release();
    }

    public void setPageContext(PageContext pageContext)
    {
        _pageContext = pageContext;
        _delegation.setPageContext(pageContext);
    }

    public void setParent(Tag tag)
    {
        _delegation.setParent(tag);
    }


    //JSF Spec.

    protected final UIComponent findComponent()
    {
        if (_componentNotFound)
        {
            return null;
        }

        Stack stack = getComponentStack();
        UIComponent peek = (UIComponent)stack.peek();

        if (_componentOnStack)
        {
            return peek;
        }

        String id = getId();
        if (id == null)
        {
            throw new IllegalArgumentException("No id attribute specified!");
        }
        //UIComponent find = peek.findComponent(id);
        //ComponentBase.findComponent throws IllegalArgument exception, therefore find by loop:
        UIComponent find = findChild(peek, id);
        if (find == null)
        {
            //_componentNotFound = true;
            //We automatically create the component on the fly
            //and insert it into the tree
            find = createComponent();
            find.setComponentId(id);
            String rendererType = getRendererType();
            if (rendererType != null)
            {
                find.setRendererType(rendererType);
            }
            peek.addChild(find);
        }

        stack.push(find);
        _componentOnStack = true;

        return find;
    }


    protected UIComponent findChild(UIComponent parent, String childId)
    {
        for (Iterator it = parent.getChildren(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            if (child.getComponentId().equals(childId))
            {
                return child;
            }
        }
        return null;
    }

    protected UIComponent getComponent()
    {
        UIComponent comp = findComponent();
        if (comp == null)
        {
            throw new IllegalStateException("Component not found!");
        }
        return comp;
    }

    public String getId()
    {
        return _id;
    }

    public void setId(String id)
    {
        _id = id;
    }


    //MyFaces tag extensions:
    public abstract UIComponent createComponent();

    public abstract String getRendererType();


    //MyFaces Helpers:

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

    protected PageContext getPageContext()
    {
        return _pageContext;
    }

    private static final String COMPONENT_STACK_ATTR = MyFacesTag.class.getName() + ".STACK";
    private Stack getComponentStack()
    {
        if (_componentStack == null)
        {
            _componentStack = (Stack)_pageContext.getAttribute(COMPONENT_STACK_ATTR, PageContext.REQUEST_SCOPE);
            if (_componentStack == null)
            {
                _componentStack = new Stack();
                _componentStack.push(getFacesContext().getResponseTree().getRoot());
                _pageContext.setAttribute(COMPONENT_STACK_ATTR, _componentStack, PageContext.REQUEST_SCOPE);
            }
        }
        return _componentStack;
    }

    protected final void popComponent()
    {
        getComponentStack().pop();
    }


    //request time attributes
    public void setModelReference(String s)
    {
        addRequestTimeValue(MyFacesComponent.MODEL_REFERENCE_ATTR, s);
    }

    public void setConverter(String s)
    {
        addRequestTimeValue(MyFacesComponent.CONVERTER_ATTR, s);
    }

    public void setValue(Object value)
    {
        addRequestTimeValue(MyFacesComponent.VALUE_ATTR, value);
    }


    protected void addRequestTimeValue(String attrName, Object attrValue)
    {
        if (_requestTimeValues == null)
        {
            _requestTimeValues = new HashMap();
        }
        _requestTimeValues.put(attrName, attrValue);
    }

    protected void applyRequestTimeValues(UIComponent uiComponent)
    {
        FacesContext facesContext = getFacesContext();
        if (_requestTimeValues != null)
        {
            for (Iterator it = _requestTimeValues.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                String attrName = (String)entry.getKey();
                if (attrName.equals(MyFacesComponent.VALUE_ATTR))
                {
                    if (uiComponent.currentValue(getFacesContext()) == null)
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

}
