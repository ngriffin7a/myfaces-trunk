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

import net.sourceforge.myfaces.util.DebugUtils;
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
 * Panel, that includes navigation items ({@link HtmlCommandNavigation}) and separators
 * ({@link HtmlOutputNavigation}).
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

    private String _itemClass;
    private String _openItemClass;
    private String _activeItemClass;
    private String _separatorClass;
    private String _itemStyle;
    private String _openItemStyle;
    private String _activeItemStyle;
    private String _separatorStyle;

    public String getItemClass()
    {
        if (_itemClass != null) return _itemClass;
        ValueBinding vb = getValueBinding("itemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setItemClass(String itemClass)
    {
        _itemClass = itemClass;
    }

    public String getOpenItemClass()
    {
        if (_openItemClass != null) return _openItemClass;
        ValueBinding vb = getValueBinding("openItemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOpenItemClass(String openItemClass)
    {
        _openItemClass = openItemClass;
    }

    public String getActiveItemClass()
    {
        if (_activeItemClass != null) return _activeItemClass;
        ValueBinding vb = getValueBinding("activeItemClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveItemClass(String activeItemClass)
    {
        _activeItemClass = activeItemClass;
    }

    public String getSeparatorClass()
    {
        if (_separatorClass != null) return _separatorClass;
        ValueBinding vb = getValueBinding("separatorClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSeparatorClass(String separatorClass)
    {
        _separatorClass = separatorClass;
    }

    public String getItemStyle()
    {
        if (_itemStyle != null) return _itemStyle;
        ValueBinding vb = getValueBinding("itemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setItemStyle(String itemStyle)
    {
        _itemStyle = itemStyle;
    }

    public String getOpenItemStyle()
    {
        if (_openItemStyle != null) return _openItemStyle;
        ValueBinding vb = getValueBinding("openItemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOpenItemStyle(String openItemStyle)
    {
        _openItemStyle = openItemStyle;
    }

    public String getActiveItemStyle()
    {
        if (_activeItemStyle != null) return _activeItemStyle;
        ValueBinding vb = getValueBinding("activeItemStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveItemStyle(String activeItemStyle)
    {
        _activeItemStyle = activeItemStyle;
    }

    public String getSeparatorStyle()
    {
        if (_separatorStyle != null) return _separatorStyle;
        ValueBinding vb = getValueBinding("separatorStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSeparatorStyle(String separatorStyle)
    {
        _separatorStyle = separatorStyle;
    }



    private static final int ATTRIBUTE_COUNT = 9;

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[ATTRIBUTE_COUNT];
        int i = 0;
        values[i++] = super.saveState(context);
        values[i++] = _itemClass;
        values[i++] = _openItemClass;
        values[i++] = _activeItemClass;
        values[i++] = _separatorClass;
        values[i++] = _itemStyle;
        values[i++] = _openItemStyle;
        values[i++] = _activeItemStyle;
        values[i++] = _separatorStyle;
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT, log, "Number of attributes to save differs!");
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _itemClass        = (String)values[i++];
        _openItemClass    = (String)values[i++];
        _activeItemClass  = (String)values[i++];
        _separatorClass   = (String)values[i++];
        _itemStyle        = (String)values[i++];
        _openItemStyle    = (String)values[i++];
        _activeItemStyle  = (String)values[i++];
        _separatorStyle   = (String)values[i++];
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT, log, "Number of attributes to restore differs!");
    }

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
    
}
