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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.UICommand;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationItemRenderer;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationRenderer;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.NamingContainer;
import javax.faces.component.NamingContainerSupport;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigation
    extends UIPanel
    implements ActionListener,
               NamingContainer
{
    public UINavigation()
    {
        super(true);
        setValid(true);
    }


    public static class UINavigationItem
        extends UICommand
    {
        public static final String OPEN_ATTR = "open";

        public UINavigationItem()
        {
            setValid(true);
        }

        public boolean isOpen()
        {
            Boolean open = (Boolean)getAttribute(OPEN_ATTR);
            return open != null && open.booleanValue();
        }

        public void setOpen(boolean open)
        {
            setAttribute(OPEN_ATTR, open ? Boolean.TRUE : null);
        }

        public void setAttribute(String name, Object value)
        {
            if (name.equals(OPEN_ATTR))
            {
                if (value != null && !((Boolean)value).booleanValue())
                {
                    setAttribute(name, null);
                    return;
                }
            }
            super.setAttribute(name, value);
        }

        public boolean broadcast(FacesEvent event, PhaseId phaseId)
            throws AbortProcessingException
        {
            if (event instanceof ActionEvent)
            {
                //We call processAction directly, so we can avoid having to register
                //the navigation component as an ActionListener of all it's children
                UINavigation uiNavigation = findUINavigation();
                if (uiNavigation == null)
                {
                    throw new FacesException("NavigationItem has no navigation ancestor!");
                }
                uiNavigation.processAction((ActionEvent)event);
            }
            return super.broadcast(event, phaseId);
        }

        private UINavigation findUINavigation()
        {
            UIComponent parent = getParent();
            while (parent != null)
            {
                if (parent instanceof UINavigation)
                {
                    return (UINavigation)parent;
                }
                parent = parent.getParent();
            }
            return null;
        }
    }




    public PhaseId getPhaseId()
    {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    public void processAction(ActionEvent actionEvent)
        throws AbortProcessingException
    {
        UIComponent source = actionEvent.getComponent();
        if (source instanceof UINavigationItem)
        {
            UIComponent item = source;
            if (item.getChildCount() > 0)
            {
                //group
                if (UIComponentUtils.getBooleanAttribute(item, UINavigationItem.OPEN_ATTR, false))
                {
                    //close group
                    closeAllChildren(item.getChildren());//close group children
                    UIComponentUtils.setBooleanAttribute(item, UINavigationItem.OPEN_ATTR, false);
                }
                else
                {
                    closeAllChildren(this.getChildren());//close all items
                    UIComponentUtils.setBooleanAttribute(item, UINavigationItem.OPEN_ATTR, true);
                    //open all parents
                    UIComponent p = item.getParent();
                    while (p != null && p.getRendererType().equals(NavigationItemRenderer.TYPE))
                    {
                        UIComponentUtils.setBooleanAttribute(p, UINavigationItem.OPEN_ATTR, true);
                        p = p.getParent();
                    }
                }
            }
            else
            {
                //single item
                closeAllChildren(item.getParent().getChildren());//close siblings
                UIComponentUtils.setBooleanAttribute(item, UINavigationItem.OPEN_ATTR, true);
            }

            String treeId = (String)item.getAttribute(NavigationItemRenderer.TREE_ID_ATTR);
            if (treeId != null)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
                Tree responseTree = tf.getTree(facesContext, treeId);
                facesContext.setTree(responseTree);
                //Save current navigation with all it's children in request context, so that
                //current state of children can be accessed when rendering new tree
                facesContext.getServletRequest().setAttribute(NavigationRenderer.CURRENT_NAVIGATION_ATTR,
                                                              this);
                facesContext.renderResponse();
            }
        }
    }


    private void closeAllChildren(Iterator children)
    {
        while (children.hasNext())
        {
            UIComponent ni = (UIComponent)children.next();
            UIComponentUtils.setBooleanAttribute(ni, UINavigationItem.OPEN_ATTR, false);
            if (ni.getChildCount() > 0)
            {
                closeAllChildren(ni.getChildren());
            }
        }
    }



    //NamingContainer
    private NamingContainer _namingContainer = new NamingContainerSupport();

    public void addComponentToNamespace(UIComponent uicomponent)
    {
        String componentId = uicomponent.getComponentId();
        if (componentId != null)
        {
            //HACK: Because there is a bug in the API implementation of UIComponentBase
            //(removeChild does not call removeComponentFromNamespace) we ignore
            //common already in namespace
            if (_namingContainer.findComponentInNamespace(componentId) != null)
            {
                return;
            }
        }
        _namingContainer.addComponentToNamespace(uicomponent);
    }

    public void removeComponentFromNamespace(UIComponent uicomponent)
    {
        _namingContainer.removeComponentFromNamespace(uicomponent);
    }

    public UIComponent findComponentInNamespace(String s)
    {
        return _namingContainer.findComponentInNamespace(s);
    }

    public String generateClientId()
    {
        return _namingContainer.generateClientId();
    }

}
