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
package javax.faces.webapp;

import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.state.TreeCopier;
import net.sourceforge.myfaces.renderkit.html.state.JspInfo;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class FacesTag
    extends TagSupport
    implements Tag, IterationTag
{
    protected PageContext _pageContext;
    protected FacesContext _facesContext;
    private Stack _componentStack;
    private boolean _componentNotFound;
    private boolean _componentOnStack;
    private Map _properties = null;

    public FacesTag()
    {
        init();
        LogUtil.getLogger().fine("Yes, our patch is in use!");
    }

    protected void init()
    {
        _pageContext = null;
        _facesContext = null;
        _componentStack = null;
        _componentNotFound = false;
        _componentOnStack = false;
        _properties = null;
    }


    //Delegation methods:

    public final int doStartTag() throws JspException
    {
        UIComponent component = findComponent();
        if (component == null)
        {
            return Tag.SKIP_BODY;
        }

        overrideProperties(component);

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
            return SKIP_BODY;
        }

        return getDoStartValue();
    }

    protected int getDoStartValue() throws JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
    }


    public final int doEndTag() throws JspException
    {
        FacesContext facesContext = getFacesContext();
        UIComponent component = findComponent();

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
        return getDoEndValue();
    }

    protected int getDoEndValue() throws JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
    }


    public void release()
    {
        super.release();
        init();
    }

    public void setPageContext(PageContext pageContext)
    {
        _pageContext = pageContext;
        super.setPageContext(pageContext);
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

            //automatically create all children from parsed JspInfo:
            FacesContext facesContext = getFacesContext();
            Tree staticTree = JspInfo.getStaticTree(facesContext, facesContext.getResponseTree().getTreeId());
            UIComponent staticComp = null;
            try
            {
                staticComp = staticTree.getRoot().findComponent(find.getCompoundId());
            }
            catch (IllegalArgumentException e) {}
            if (staticComp == null)
            {
                LogUtil.getLogger().severe("Component " + find.getCompoundId() + " not found in static (parsed) tree.");
            }
            else
            {
                TreeCopier tc = new TreeCopier(facesContext);
                tc.copyStaticSubTree(staticComp, find);
            }
        }

        stack.push(find);
        _componentOnStack = true;

        return find;
    }

    private UIComponent findChild(UIComponent parent, String childId)
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



    //JSF API implementation

    protected UIComponent getComponent()
    {
        UIComponent comp = findComponent();
        if (comp == null)
        {
            throw new IllegalStateException("Component not found!");
        }
        return comp;
    }

    public abstract UIComponent createComponent();

    public abstract String getRendererType();



    //private helpers

    private static final String COMPONENT_STACK_ATTR = FacesTag.class.getName() + ".STACK";
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


    //subclass helpers

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


    //property helpers

    protected void setProperty(String attrName, Object attrValue)
    {
        if (_properties == null)
        {
            _properties = new HashMap();
        }
        _properties.put(attrName, attrValue);
    }

    public void overrideProperties(UIComponent uiComponent)
    {
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

    public void setValue(String value)
    {
        setProperty(MyFacesComponent.VALUE_ATTR, value);
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
