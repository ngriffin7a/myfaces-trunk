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
package javax.faces.webapp;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.23  2004/08/27 12:47:52  manolito
 * automatically assign IDs to facets and children of components that where created by binding
 *
 * Revision 1.22  2004/07/05 23:43:36  o_rossmueller
 * fix #985274: call setProperties for viewRoot
 *
 * Revision 1.21  2004/07/01 22:00:54  mwessendorf
 * ASF switch
 *
 * Revision 1.20  2004/04/26 13:13:52  manolito
 * always release facesContext
 *
 * Revision 1.19  2004/04/26 12:01:39  manolito
 * more reluctant releasing of members for Resin compatibility
 *
 * Revision 1.18  2004/04/20 11:16:23  royalts
 * no message
 *
 * Revision 1.17  2004/04/20 11:11:03  royalts
 * no message
 *
 * Revision 1.16  2004/04/20 10:54:21  royalts
 * added index check to findComponent
 *
 * Revision 1.15  2004/03/31 12:48:03  manolito
 * bug: newly created children added always at end
 *
 * Revision 1.14  2004/03/31 02:29:41  dave0000
 * avoid lastIndexOf scan if not needed
 *
 * Revision 1.13  2004/03/26 11:48:33  manolito
 * additional NPE check
 */
public abstract class UIComponentTag
        implements Tag
{
    private static final String FORMER_CHILD_IDS_SET_ATTR = UIComponentTag.class.getName() + ".FORMER_CHILD_IDS";
    private static final String FORMER_FACET_NAMES_SET_ATTR = UIComponentTag.class.getName() + ".FORMER_FACET_NAMES";
    private static final String COMPONENT_STACK_ATTR =  UIComponentTag.class.getName() + ".COMPONENT_STACK";

    protected PageContext pageContext = null;
    private Tag _parent = null;

    //tag attributes
    private String _binding = null;
    private String _id = null;
    private String _rendered = null;

    private FacesContext _facesContext = null;
    private UIComponent _componentInstance = null;
    private boolean _created = false;
    private Boolean _suppressed = null;
    private ResponseWriter _writer = null;
    private Set _childrenAdded = null;
    private Set _facetsAdded = null;


    public UIComponentTag()
    {

    }

    public void release()
    {
        internalRelease();

        //members, that must/need only be reset when there is no more risk, that the container
        //wants to reuse this tag
        pageContext = null;
        _parent = null;

        //reset tag attribute members
        _binding = null;
        _id = null;
        _rendered = null;
    }


    /**
     * Reset any members that apply to the according component instance and
     * must not be reused if the container wants to reuse this tag instance.
     * This method is called when rendering for this tag is finished ( doEndTag() )
     * or when released by the container.
     */
    private void internalRelease()
    {
        _facesContext = null;
        _componentInstance = null;
        _created = false;
        _suppressed = null;
        _writer = null;
        _childrenAdded = null;
        _facetsAdded = null;
    }


    public void setBinding(String binding)
            throws JspException
    {
        if (!isValueReference(binding))
        {
            throw new IllegalArgumentException("not a valid binding: " + binding);
        }
        _binding = binding;
    }

    public void setId(String id)
    {
        _id = id;
    }

    public String getId()
    {
        return _id;
    }

    public void setRendered(String rendered)
    {
        _rendered = rendered;
    }

    public abstract String getComponentType();

    public UIComponent getComponentInstance()
    {
        return _componentInstance;
    }

    public boolean getCreated()
    {
        return _created;
    }

    public static UIComponentTag getParentUIComponentTag(PageContext pageContext)
    {
        List list = (List)pageContext.getAttribute(COMPONENT_STACK_ATTR,
                                                   PageContext.REQUEST_SCOPE);
        if (list != null)
        {
            return (UIComponentTag)list.get(list.size() - 1);
        }
        return null;
    }

    private void popTag()
    {
        List list = (List)pageContext.getAttribute(COMPONENT_STACK_ATTR,
                                                    PageContext.REQUEST_SCOPE);
        if (list != null)
        {
            int size = list.size();
            list.remove(size -1);
            if (size <= 1)
            {
                pageContext.removeAttribute(COMPONENT_STACK_ATTR,
                                             PageContext.REQUEST_SCOPE);
            }
        }
    }

    private void pushTag()
    {
        List list = (List)pageContext.getAttribute(COMPONENT_STACK_ATTR,
                                                    PageContext.REQUEST_SCOPE);
        if (list == null)
        {
            list = new ArrayList();
            pageContext.setAttribute(COMPONENT_STACK_ATTR,
                                      list,
                                      PageContext.REQUEST_SCOPE);
        }
        list.add(this);
    }


    public abstract String getRendererType();

    public static boolean isValueReference(String value)
    {
        if (value == null) throw new NullPointerException("value");
        
        int start = value.indexOf("#{");
        if (start < 0) return false;
        
        int end = value.lastIndexOf('}');
        return (end >=0 && start < end);
    }

    public void setPageContext(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }

    public Tag getParent()
    {
        return _parent;
    }

    public void setParent(Tag parent)
    {
        _parent = parent;
    }

    public int doStartTag()
            throws JspException
    {
        setupResponseWriter();
        FacesContext facesContext = getFacesContext();
        UIComponent component = findComponent(facesContext);
        if (!component.getRendersChildren() && !isSuppressed())
        {
            try
            {
                encodeBegin();
                _writer.flush();
            }
            catch (IOException e)
            {
                throw new JspException(e.getMessage(), e);
            }
        }
        pushTag();
        return getDoStartValue();
    }

    public int doEndTag()
            throws JspException
    {
        popTag();
        UIComponent component = getComponentInstance();
        removeFormerChildren(component);
        removeFormerFacets(component);

        try
        {
            if (!isSuppressed())
            {
                if (component.getRendersChildren())
                {
                    encodeBegin();
                    encodeChildren();
                }
                encodeEnd();
            }
        }
        catch (IOException e)
        {
            throw new JspException(e.getMessage(), e);
        }

        int retValue = getDoEndValue();
        internalRelease();
        return retValue;
    }

    private void removeFormerChildren(UIComponent component)
    {
        Set formerChildIdsSet = (Set)component.getAttributes().get(FORMER_CHILD_IDS_SET_ATTR);
        if (formerChildIdsSet != null)
        {
            for (Iterator iterator = formerChildIdsSet.iterator(); iterator.hasNext();)
            {
                String childId = (String)iterator.next();
                if (_childrenAdded == null || !_childrenAdded.contains(childId))
                {
                    UIComponent childToRemove = component.findComponent(childId);
                    if (childToRemove != null)
                    {
                        component.getChildren().remove(childToRemove);
                    }
                }
            }
            if (_childrenAdded == null)
            {
                component.getAttributes().remove(FORMER_CHILD_IDS_SET_ATTR);
            }
            else
            {
                component.getAttributes().put(FORMER_CHILD_IDS_SET_ATTR, _childrenAdded);
            }
        }
        else
        {
            if (_childrenAdded != null)
            {
                component.getAttributes().put(FORMER_CHILD_IDS_SET_ATTR, _childrenAdded);
            }
        }
    }

    private void removeFormerFacets(UIComponent component)
    {
        Set formerFacetNamesSet = (Set)component.getAttributes().get(FORMER_FACET_NAMES_SET_ATTR);
        if (formerFacetNamesSet != null)
        {
            for (Iterator iterator = formerFacetNamesSet.iterator(); iterator.hasNext();)
            {
                String facetName = (String)iterator.next();
                if (_facetsAdded == null || !_facetsAdded.contains(facetName))
                {
                    component.getFacets().remove(facetName);
                }
            }
            if (_facetsAdded == null)
            {
                component.getAttributes().remove(FORMER_FACET_NAMES_SET_ATTR);
            }
            else
            {
                component.getAttributes().put(FORMER_FACET_NAMES_SET_ATTR, _facetsAdded);
            }
        }
        else
        {
            if (_facetsAdded != null)
            {
                component.getAttributes().put(FORMER_FACET_NAMES_SET_ATTR, _facetsAdded);
            }
        }
    }



    protected void encodeBegin()
            throws IOException
    {
        _componentInstance.encodeBegin(getFacesContext());
    }

    protected void encodeChildren()
            throws IOException
    {
        _componentInstance.encodeChildren(getFacesContext());
    }

    protected void encodeEnd()
            throws IOException
    {
        _componentInstance.encodeEnd(getFacesContext());
    }

    protected UIComponent findComponent(FacesContext context)
            throws JspException
    {
        if (_componentInstance != null) return _componentInstance;
        UIComponentTag parentTag = getParentUIComponentTag(pageContext);
        if (parentTag == null)
        {
            //This is the root
            _componentInstance = context.getViewRoot();
            setProperties(_componentInstance);
            return _componentInstance;
        }

        UIComponent parent = parentTag.getComponentInstance();
        //TODO: what if parent == null?
        if (parent == null) throw new IllegalStateException("parent is null?");

        String facetName = getFacetName();
        if (facetName != null)
        {
            //Facet
            String id = getOrCreateUniqueId(context);
            _componentInstance = parent.getFacet(facetName);
            if (_componentInstance == null)
            {
                _componentInstance = createComponentInstance(context, id);
                setProperties(_componentInstance);
                parent.getFacets().put(facetName, _componentInstance);
            }
            addFacetNameToParentTag(parentTag, facetName);
            return _componentInstance;
        }
        else
        {
            //Child
            String id = getOrCreateUniqueId(context);
            _componentInstance = parent.findComponent(id);
            if (_componentInstance == null)
            {
                _componentInstance = createComponentInstance(context, id);
                setProperties(_componentInstance);
                int index = getAddedChildrenCount(parentTag);
                List children = parent.getChildren();
                if (index <= children.size())
                {
                    children.add(index, _componentInstance);
                }
                else
                {
                    throw new FacesException("cannot add component '" + _componentInstance.getId() + "'");
                }
            }
            addChildIdToParentTag(parentTag, id);
            return _componentInstance;
        }
    }


    private String getOrCreateUniqueId(FacesContext context)
    {
        String id = getId();
        if (id != null)
        {
            return id;
        }
        else
        {
            return context.getViewRoot().createUniqueId();
        }
    }

    private UIComponent createComponentInstance(FacesContext context, String id)
    {
        String componentType = getComponentType();
        if (componentType == null)
        {
            throw new NullPointerException("componentType");
        }

        if (_binding != null)
        {
            Application application = context.getApplication();
            ValueBinding componentBinding = application.createValueBinding(_binding);
            UIComponent component = application.createComponent(componentBinding,
                                                                context,
                                                                componentType);
            component.setId(id);
            component.setValueBinding("binding", componentBinding);
            recurseFacetsAndChildrenForId(context, component.getFacetsAndChildren(), id + "_", 0);
            _created = true;
            return component;
        }
        else
        {
            UIComponent component = context.getApplication().createComponent(componentType);
            component.setId(id);
            _created = true;
            return component;
        }
    }

    /**
     * Recurse all facets and children and assign them an unique ID if necessary.
     * We must *not* use UIViewRoot#createUniqueId here, because this would affect the
     * order of the created ids upon rerendering the page!
     */ 
    private int recurseFacetsAndChildrenForId(FacesContext context,
                                              Iterator facetsAndChildren,
                                              String idPrefix,
                                              int cnt)
    {
        while (facetsAndChildren.hasNext())
        {
            UIComponent comp = (UIComponent)facetsAndChildren.next();
            if (comp.getId() == null)
            {
                ++cnt;
                comp.setId(idPrefix + cnt);
            }
            cnt = recurseFacetsAndChildrenForId(context, comp.getFacetsAndChildren(), idPrefix, cnt);
        }
        return cnt;
    }



    private void addChildIdToParentTag(UIComponentTag parentTag, String id)
    {
        if (parentTag._childrenAdded == null)
        {
            parentTag._childrenAdded = new HashSet();
        }
        parentTag._childrenAdded.add(id);
    }

    private void addFacetNameToParentTag(UIComponentTag parentTag, String facetName)
    {
        if (parentTag._facetsAdded == null)
        {
            parentTag._facetsAdded = new HashSet();
        }
        parentTag._facetsAdded.add(facetName);
    }
                             
    private int getAddedChildrenCount(UIComponentTag parentTag)
    {
        return parentTag._childrenAdded != null ?
               parentTag._childrenAdded.size() :
               0;
    }




    protected int getDoStartValue()
            throws JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
    }

    protected int getDoEndValue()
            throws JspException
    {
        return Tag.EVAL_PAGE;
    }

    protected FacesContext getFacesContext()
    {
        if (_facesContext == null)
        {
            _facesContext = FacesContext.getCurrentInstance();
        }
        return _facesContext;
    }


    private boolean isFacet()
    {
        return _parent != null && _parent instanceof FacetTag;
    }

    protected String getFacetName()
    {
        return isFacet() ? ((FacetTag)_parent).getName() : null;
    }


    protected boolean isSuppressed()
    {
        if (_suppressed == null)
        {
            if (isFacet())
            {
                // facets are always rendered by their parents --> suppressed
                return (_suppressed = Boolean.TRUE).booleanValue();
            }

            UIComponent component = getComponentInstance();

            // Does any parent render its children?
            // (We must determine this first, before calling any isRendered method
            //  because rendered properties might reference a data var of a nesting UIData,
            //  which is not set at this time, and would cause a VariableResolver error!)
            UIComponent parent = component.getParent();
            while (parent != null)
            {
                if (parent.getRendersChildren())
                {
                    //Yes, parent found, that renders children --> suppressed
                    return (_suppressed = Boolean.TRUE).booleanValue();
                }
                parent = parent.getParent();
            }

            // does component or any parent has a false rendered attribute?
            while (component != null)
            {
                if (!component.isRendered())
                {
                    //Yes, component or any parent must not be rendered --> suppressed
                    return (_suppressed = Boolean.TRUE).booleanValue();
                }
                component = component.getParent();
            }

            // else --> not suppressed
            _suppressed = Boolean.FALSE;
        }
        return _suppressed.booleanValue();
    }

    protected void setProperties(UIComponent component)
    {
        if (getRendererType() != null)
        {
            _componentInstance.setRendererType(getRendererType());
        }

        if (_rendered != null)
        {
            if (isValueReference(_rendered))
            {
                ValueBinding vb = getFacesContext().getApplication().createValueBinding(_rendered);
                component.setValueBinding("rendered", vb);
            } else
            {
                boolean b = Boolean.valueOf(_rendered).booleanValue();
                component.setRendered(b);
            }
        }
    }

    protected void setupResponseWriter()
    {
        FacesContext facesContext = getFacesContext();
        _writer = facesContext.getResponseWriter();
        if (_writer == null)
        {
            RenderKitFactory renderFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = renderFactory.getRenderKit(facesContext,
                                                             facesContext.getViewRoot().getRenderKitId());

            ServletRequest request = (ServletRequest)facesContext.getExternalContext().getRequest();

            _writer = renderKit.createResponseWriter(new _PageContextOutWriter(pageContext),
                                                     request.getContentType(), //TODO: is this the correct content type?
                                                     request.getCharacterEncoding());
            facesContext.setResponseWriter(_writer);
        }
    }

}
