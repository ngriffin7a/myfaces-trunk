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
package net.sourceforge.myfaces.component.ext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Panel, that includes navigation items ({@link HtmlCommandNavigation}) and other
 * components (separators).
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelNavigation
        extends HtmlPanelGroup
{
    private static final Log log = LogFactory.getLog(HtmlPanelNavigation.class);

    private static final String PREVIOUS_VIEW_ROOT = HtmlPanelNavigation.class.getName() + ".PREVIOUS_VIEW_ROOT";
    private boolean _itemOpenActiveStatesRestored = false;

    public void decode(FacesContext context)
    {
        super.decode(context);    //To change body of overridden methods use File | Settings | File Templates.
        
        //Save the current view root for later reference...
        context.getExternalContext().getRequestMap().put(PREVIOUS_VIEW_ROOT, context.getViewRoot());
        //...and remember that this instance needs NO special treatment on rendering:
        _itemOpenActiveStatesRestored = true;
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (!_itemOpenActiveStatesRestored && getChildCount() > 0)
        {
            UIViewRoot previousRoot = (UIViewRoot)context.getExternalContext().getRequestMap().get(PREVIOUS_VIEW_ROOT);
            if (previousRoot != null)
            {
                restoreOpenActiveStates(context, previousRoot, getChildren());
            }
            else
            {
                //no previous root, means no decode was done
                //--> a new request
            }
        }
        
        super.encodeBegin(context);    //To change body of overridden methods use File | Settings | File Templates.
    }
    
    public void restoreOpenActiveStates(FacesContext facesContext,
                                        UIViewRoot previousRoot,
                                        List children)
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof HtmlCommandNavigation)
            {
                HtmlCommandNavigation previousItem = (HtmlCommandNavigation)previousRoot.findComponent(child.getClientId(facesContext));
                if (previousItem != null)
                {
                    ((HtmlCommandNavigation)child).setOpen(previousItem.isOpen());
                    ((HtmlCommandNavigation)child).setActive(previousItem.isActive());
                }
                else
                {
                    log.error("Navigation item " + child.getClientId(facesContext) + " not found in previous view.");
                }
                if (child.getChildCount() > 0)
                {
                    restoreOpenActiveStates(facesContext, previousRoot, child.getChildren());
                }
            }
        }
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlPanelNavigation";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Navigation";

    private String _itemClass = null;
    private String _openItemClass = null;
    private String _activeItemClass = null;
    private String _separatorClass = null;
    private String _itemStyle = null;
    private String _openItemStyle = null;
    private String _activeItemStyle = null;
    private String _separatorStyle = null;

    public HtmlPanelNavigation()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setItemClass(String itemClass)
    {
        _itemClass = itemClass;
    }

    public String getItemClass()
    {
        if (_itemClass != null) return _itemClass;
        ValueBinding vb = getValueBinding("itemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOpenItemClass(String openItemClass)
    {
        _openItemClass = openItemClass;
    }

    public String getOpenItemClass()
    {
        if (_openItemClass != null) return _openItemClass;
        ValueBinding vb = getValueBinding("openItemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveItemClass(String activeItemClass)
    {
        _activeItemClass = activeItemClass;
    }

    public String getActiveItemClass()
    {
        if (_activeItemClass != null) return _activeItemClass;
        ValueBinding vb = getValueBinding("activeItemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSeparatorClass(String separatorClass)
    {
        _separatorClass = separatorClass;
    }

    public String getSeparatorClass()
    {
        if (_separatorClass != null) return _separatorClass;
        ValueBinding vb = getValueBinding("separatorClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setItemStyle(String itemStyle)
    {
        _itemStyle = itemStyle;
    }

    public String getItemStyle()
    {
        if (_itemStyle != null) return _itemStyle;
        ValueBinding vb = getValueBinding("itemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOpenItemStyle(String openItemStyle)
    {
        _openItemStyle = openItemStyle;
    }

    public String getOpenItemStyle()
    {
        if (_openItemStyle != null) return _openItemStyle;
        ValueBinding vb = getValueBinding("openItemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveItemStyle(String activeItemStyle)
    {
        _activeItemStyle = activeItemStyle;
    }

    public String getActiveItemStyle()
    {
        if (_activeItemStyle != null) return _activeItemStyle;
        ValueBinding vb = getValueBinding("activeItemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSeparatorStyle(String separatorStyle)
    {
        _separatorStyle = separatorStyle;
    }

    public String getSeparatorStyle()
    {
        if (_separatorStyle != null) return _separatorStyle;
        ValueBinding vb = getValueBinding("separatorStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[9];
        values[0] = super.saveState(context);
        values[1] = _itemClass;
        values[2] = _openItemClass;
        values[3] = _activeItemClass;
        values[4] = _separatorClass;
        values[5] = _itemStyle;
        values[6] = _openItemStyle;
        values[7] = _activeItemStyle;
        values[8] = _separatorStyle;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _itemClass = (String)values[1];
        _openItemClass = (String)values[2];
        _activeItemClass = (String)values[3];
        _separatorClass = (String)values[4];
        _itemStyle = (String)values[5];
        _openItemStyle = (String)values[6];
        _activeItemStyle = (String)values[7];
        _separatorStyle = (String)values[8];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
