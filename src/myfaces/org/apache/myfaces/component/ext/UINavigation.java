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

import net.sourceforge.myfaces.component.MyFacesUICommand;
import net.sourceforge.myfaces.component.MyFacesUIPanel;

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
    extends MyFacesUIPanel
    implements ActionListener,
               NamingContainer
{
    public UINavigation()
    {
        setValid(true);
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public static class UINavigationItem
        extends MyFacesUICommand
    {
        public static final String OPEN_PROP = "open";
        private boolean _open;

        public UINavigationItem()
        {
            setValid(true);
        }

        public boolean isOpen()
        {
            return _open;
        }

        public void setOpen(boolean open)
        {
            _open = open;
        }

        public String getAction()
        {
            String action = super.getAction();
            return action == null ? "" : action;
        }

        public boolean broadcast(FacesEvent event, PhaseId phaseId)
            throws AbortProcessingException
        {
            if (event instanceof ActionEvent &&
                phaseId == PhaseId.APPLY_REQUEST_VALUES)
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
            UINavigationItem item = (UINavigationItem)source;
            if (item.getChildCount() > 0)
            {
                //group
                if (item.isOpen())
                {
                    //close group
                    closeAllChildren(item.getChildren());//close group children
                    item.setOpen(false);
                }
                else
                {
                    closeAllChildren(this.getChildren());//close all items
                    item.setOpen(true);
                    //open all parents
                    UIComponent p = item.getParent();
                    while (p != null && p instanceof UINavigationItem)
                    {
                        ((UINavigationItem)p).setOpen(true);
                        p = p.getParent();
                    }
                }
            }
            else
            {
                //single item
                closeAllChildren(item.getParent().getChildren());//close siblings
                item.setOpen(true);
            }

            //String treeId = (String)item.getAttribute(NavigationItemRenderer.TREE_ID_ATTR);
            String treeId = ((UINavigationItem)item).getAction();
            if (treeId != null && treeId.length() > 0)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
                Tree responseTree = tf.getTree(facesContext, treeId);
                facesContext.setTree(responseTree);
                facesContext.renderResponse();
            }
            //TODO: always render response?
        }
    }


    private void closeAllChildren(Iterator children)
    {
        while (children.hasNext())
        {
            UIComponent ni = (UIComponent)children.next();
            if (ni instanceof UINavigationItem)
            {
                ((UINavigationItem)ni).setOpen(false);
            }
            if (ni.getChildCount() > 0)
            {
                closeAllChildren(ni.getChildren());
            }
        }
    }



    //NamingContainer Support
    private NamingContainer _namingContainer = new NamingContainerSupport();

    public void addComponentToNamespace(UIComponent uicomponent)
    {
        String componentId = uicomponent.getComponentId();
        if (componentId != null)
        {
            //HACK: Because there is a bug in the API implementation of UIComponentBase
            //(removeChild does not call removeComponentFromNamespace) we ignore
            //component already in namespace
            UIComponent old = _namingContainer.findComponentInNamespace(componentId);
            if (old != null)
            {
                _namingContainer.removeComponentFromNamespace(old);
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
