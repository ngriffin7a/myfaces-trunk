/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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

import javax.faces.FactoryFinder;
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

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class UIComponentTag
        implements Tag
{
    protected PageContext pageContext;

    private String _binding;
    private String _id;
    private String _rendered;
    private UIComponent _componentInstance;
    private boolean _created;
    private UIComponentTag _parentUIComponentTag;
    private Tag _parent;
    private FacesContext _facesContext;
    private String _facetName;
    private Boolean _suppressed;

    public UIComponentTag()
    {

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

    public boolean isCreated()
    {
        return _created;
    }

    public UIComponentTag getParentUIComponentTag()
    {
        if (_parentUIComponentTag == null)
        {
            Tag parentTag = getParent();
            while (parentTag != null)
            {
                if (parentTag instanceof UIComponentTag)
                {
                    return (_parentUIComponentTag = (UIComponentTag)parentTag);
                }
            }
        }
        return _parentUIComponentTag;
    }

    public abstract String getRendererType();

    public static boolean isValueReference(String value)
    {
        int start = value.indexOf("#{");
        int end = value.lastIndexOf('}');
        return (start != -1 && end != -1 && start < end);
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
            throws javax.servlet.jsp.JspException
    {
        setupResponseWriter();
        
        //TODO
    }

    public int doEndTag()
            throws javax.servlet.jsp.JspException
    {
        //TODO
    }

    public void release()
    {
        //TODO
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
        //TODO
    }


    protected int getDoStartValue()
            throws javax.servlet.jsp.JspException
    {
        return Tag.EVAL_BODY_INCLUDE;
    }

    protected int getDoEndValue()
            throws javax.servlet.jsp.JspException
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
            if (isFacet() || !_componentInstance.isRendered())
            {
                return (_suppressed = Boolean.TRUE).booleanValue();
            }
            else
            {
                UIComponent parent = _componentInstance.getParent();
                while (parent != null)
                {
                    if (parent.getRendersChildren() || !parent.isRendered())
                    {
                        return (_suppressed = Boolean.TRUE).booleanValue();
                    }
                }
            }
            return (_suppressed = Boolean.FALSE).booleanValue();
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
        ResponseWriter writer = facesContext.getResponseWriter();
        if (writer == null)
        {
            RenderKitFactory renderFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = renderFactory.getRenderKit(facesContext,
                                                             facesContext.getViewRoot().getRenderKitId());

            ServletRequest request = (ServletRequest)facesContext.getExternalContext().getRequest();

            writer = renderKit.createResponseWriter(pageContext.getOut(),
                                                    request.getContentType(),
                                                    request.getCharacterEncoding());
            facesContext.setResponseWriter(writer);
        }
    }
}
