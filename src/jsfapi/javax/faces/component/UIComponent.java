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
package javax.faces.component;

import javax.faces.event.AbortProcessingException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class UIComponent
        implements StateHolder
{
    public UIComponent()
    {
    }

    public abstract java.util.Map getAttributes();

    public abstract javax.faces.el.ValueBinding getValueBinding(java.lang.String name);

    public abstract void setValueBinding(java.lang.String name,
                                         javax.faces.el.ValueBinding binding);

    public abstract java.lang.String getClientId(javax.faces.context.FacesContext context);

    public abstract java.lang.String getFamily();

    public abstract java.lang.String getId();

    public abstract void setId(java.lang.String id);

    public abstract javax.faces.component.UIComponent getParent();

    public abstract void setParent(javax.faces.component.UIComponent parent);

    public abstract boolean isRendered();

    public abstract void setRendered(boolean rendered);

    public abstract java.lang.String getRendererType();

    public abstract void setRendererType(java.lang.String rendererType);

    public abstract boolean getRendersChildren();

    public abstract java.util.List getChildren();

    public abstract int getChildCount();

    public abstract javax.faces.component.UIComponent findComponent(java.lang.String expr);

    public abstract java.util.Map getFacets();

    public abstract javax.faces.component.UIComponent getFacet(java.lang.String name);

    public abstract java.util.Iterator getFacetsAndChildren();

    public abstract void broadcast(javax.faces.event.FacesEvent event)
            throws AbortProcessingException;

    public abstract void decode(javax.faces.context.FacesContext context);

    public abstract void encodeBegin(javax.faces.context.FacesContext context)
            throws java.io.IOException;

    public abstract void encodeChildren(javax.faces.context.FacesContext context)
            throws java.io.IOException;

    public abstract void encodeEnd(javax.faces.context.FacesContext context)
            throws java.io.IOException;

    protected abstract void addFacesListener(javax.faces.event.FacesListener listener);

    protected abstract javax.faces.event.FacesListener[] getFacesListeners(java.lang.Class clazz);

    protected abstract void removeFacesListener(javax.faces.event.FacesListener listener);

    public abstract void queueEvent(javax.faces.event.FacesEvent event);

    public abstract void processRestoreState(javax.faces.context.FacesContext context,
                                             java.lang.Object state);

    public abstract void processDecodes(javax.faces.context.FacesContext context);

    public abstract void processValidators(javax.faces.context.FacesContext context);

    public abstract void processUpdates(javax.faces.context.FacesContext context);

    public abstract java.lang.Object processSaveState(javax.faces.context.FacesContext context);

    protected abstract javax.faces.context.FacesContext getFacesContext();

    protected abstract javax.faces.render.Renderer getRenderer(javax.faces.context.FacesContext context);
}
