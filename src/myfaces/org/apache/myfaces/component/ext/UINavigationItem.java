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
import net.sourceforge.myfaces.renderkit.html.state.StateRestorer;

import javax.faces.FactoryFinder;
import javax.faces.application.Action;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigationItem
    extends MyFacesUICommand
{
    public static final String OPEN_PROP = "open";
    private boolean _open = false;
    private boolean _reconstituted = false;

    public UINavigationItem()
    {
        setValid(true);
    }

    public void reconstitute(FacesContext context) throws IOException
    {
        super.reconstitute(context);
        //remember that this item was reconstituted and therefore the open state is ok
        _reconstituted = true;
    }

    public boolean isOpen()
    {
        if (!_reconstituted)
        {
            //item was not reconstituted but newly created,
            //so we try to determine the open state of this item in the previous tree
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletRequest servletRequest = (ServletRequest)facesContext.getExternalContext().getRequest();
            StateRestorer stateRestorer
                = (StateRestorer)servletRequest.getAttribute(StateRestorer.STATE_RESTORER_REQUEST_ATTR);
            if (stateRestorer != null)
            {
                Tree previousTree  = stateRestorer.getPreviousTree(facesContext);
                if (previousTree != null && previousTree != facesContext.getTree())
                {
                    String clientId = getClientId(facesContext);
                    UINavigationItem prevNavItem
                        = (UINavigationItem)previousTree.getRoot().findComponent(clientId);
                    if (prevNavItem != null)
                    {
                        setOpen(prevNavItem.isOpen());
                    }
                }
            }
            _reconstituted = true;
        }
        return _open;
    }

    public void setOpen(boolean open)
    {
        _open = open;
    }

    /**
     * @return false, if this item is child of another UINavigationItem, which is closed
     */
    public boolean isRendered()
    {
        UIComponent parent = getParent();
        while (parent != null)
        {
            if (parent instanceof UINavigationItem)
            {
                if (!((UINavigationItem)parent).isOpen())
                {
                    return false;
                }
            }

            if (parent instanceof UINavigation)
            {
                break;
            }
            else
            {
                parent = parent.getParent();
            }
        }

        return super.isRendered();
    }


    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException
    {
        if (phaseId == PhaseId.APPLY_REQUEST_VALUES &&
            event instanceof ActionEvent &&
            event.getSource() == this)
        {
            //Item was clicked
            toggleOpen();
            handleNavigation();
        }
        return super.broadcast(event, phaseId);
    }


    public void toggleOpen()
    {
        if (isOpen())
        {
            //close all children
            closeAllChildren();
            //close item
            setOpen(false);
        }
        else
        {
            //close all siblings and children
            closeAllChildren(getParent().getChildren());
            //open item
            setOpen(true);
            //open all parents (to be sure)
            UIComponent p = getParent();
            while (p != null && !(p instanceof UINavigation))
            {
                if (p instanceof UINavigationItem)
                {
                    ((UINavigationItem)p).setOpen(true);
                }
                p = p.getParent();
            }
        }
    }


    public void closeAllChildren()
    {
        closeAllChildren(getChildren());
    }

    private static void closeAllChildren(Iterator children)
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

    protected void handleNavigation()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = af.getApplication();

        String outcome = getAction();
        if (getActionRef() != null)
        {
            Object actionObj = application.getValueBinding(getActionRef()).getValue(facesContext);
            if (actionObj == null || !(actionObj instanceof Action))
            {
                throw new IllegalArgumentException("Referenced value '" + getActionRef() + "' is not a valid Action!");
            }

            Action action = (Action)actionObj;
            outcome = action.invoke();
        }

        if (outcome != null)
        {
            application.getNavigationHandler().handleNavigation(facesContext,
                                                                getActionRef(),
                                                                outcome);
        }
    }

}
