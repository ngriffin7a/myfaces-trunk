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
package net.sourceforge.myfaces.custom.navigation;

import net.sourceforge.myfaces.component.html.ext.HtmlCommandLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.Iterator;

/**
 * Command, that represents a navigation item.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCommandNavigation
        extends HtmlCommandLink
{
    private static final Log log = LogFactory.getLog(HtmlCommandNavigation.class);

    private boolean _open = false;
    private boolean _active = false;

    public boolean isImmediate()
    {
        //always immediate
        return true;
    }

    public void setImmediate(boolean immediate)
    {
        if (log.isWarnEnabled()) log.warn("Immediate property of HtmlCommandNavigation cannot be set --> ignored.");
    }

    public boolean isOpen()
    {
        return _open;
    }

    public void setOpen(boolean open)
    {
        _open = open;
    }

    public boolean isActive()
    {
        return _active;
    }

    public void setActive(boolean active)
    {
        _active = active;
    }

    /**
     * @return false, if this item is child of another HtmlCommandNavigation, which is closed
     */
    public boolean isRendered()
    {
        UIComponent parent = getParent();
        while (parent != null)
        {
            if (parent instanceof HtmlCommandNavigation)
            {
                if (!((HtmlCommandNavigation)parent).isOpen())
                {
                    return false;
                }
            }

            if (parent instanceof HtmlPanelNavigation)
            {
                break;
            }
            else
            {
                parent = parent.getParent();
            }
        }

        return true;
    }

    public void setRendered(boolean rendered)
    {
        //cannot be set explicitly
    }


    public void toggleOpen()
    {
        if (isOpen())
        {
            if (getChildCount() > 0)
            {
                //item is a menu group --> close item
                setOpen(false);
            }
        }
        else
        {
            UIComponent parent = getParent();

            //close all siblings
            closeAllChildren(parent.getChildren().iterator());

            //open all parents (to be sure) and search HtmlPanelNavigation
            UIComponent p = parent;
            while (p != null && !(p instanceof HtmlPanelNavigation))
            {
                if (p instanceof HtmlCommandNavigation)
                {
                    ((HtmlCommandNavigation)p).setOpen(true);
                }
                p = p.getParent();
            }
            // p is now the HtmlPanelNavigation

            if (getChildCount() == 0)
            {
                //item is an end node --> deactivate all other nodes, and then...
                if (!(p instanceof HtmlPanelNavigation))
                {
                    log.error("HtmlCommandNavigation without parent HtmlPanelNavigation ?!");
                }
                else
                {
                    //deactivate all other items
                    deactivateAllChildren(p.getChildren().iterator());
                }
                //...activate this item
                setActive(true);
            }
            else
            {
                //open item
                setOpen(true);
            }
        }
    }


    private static void deactivateAllChildren(Iterator children)
    {
        while (children.hasNext())
        {
            UIComponent ni = (UIComponent)children.next();
            if (ni instanceof HtmlCommandNavigation)
            {
                ((HtmlCommandNavigation)ni).setActive(false);
                if (ni.getChildCount() > 0)
                {
                    deactivateAllChildren(ni.getChildren().iterator());
                }
            }
        }
    }

    private static void closeAllChildren(Iterator children)
    {
        while (children.hasNext())
        {
            UIComponent ni = (UIComponent)children.next();
            if (ni instanceof HtmlCommandNavigation)
            {
                ((HtmlCommandNavigation)ni).setOpen(false);
                if (ni.getChildCount() > 0)
                {
                    closeAllChildren(ni.getChildren().iterator());
                }
            }
        }
    }


    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof ActionEvent)
        {
            ActionEvent actionEvent = (ActionEvent)event;
            if (actionEvent.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES)
            {
                HtmlCommandNavigation navItem = (HtmlCommandNavigation)actionEvent.getComponent();
                navItem.toggleOpen();
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
        super.broadcast(event);
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = Boolean.valueOf(_open);
        values[2] = Boolean.valueOf(_active);
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _open = ((Boolean)values[1]).booleanValue();
        _active = ((Boolean)values[2]).booleanValue();
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlCommandNavigation";
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Link";


    public HtmlCommandNavigation()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }


    //------------------ GENERATED CODE END ---------------------------------------
    
}
