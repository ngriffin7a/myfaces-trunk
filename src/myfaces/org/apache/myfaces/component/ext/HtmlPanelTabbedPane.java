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

import net.sourceforge.myfaces.event.TabChangeEvent;
import net.sourceforge.myfaces.event.TabChangeListener;
import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelTabbedPane
        extends HtmlPanelGroup
{
    private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private static final int ATTRIBUTE_COUNT = 2;
    private int _selectedIndex = 0;
    private String _bgcolor = null;
    //TODO: additional HTML Table attributes (see HtmlPanelTabbedPaneTag)

    public int getSelectedIndex()
    {
        return _selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        _selectedIndex = selectedIndex;
    }

    public String getBgcolor()
    {
        return _bgcolor;
    }

    public void setBgcolor(String bgcolor)
    {
        _bgcolor = bgcolor;
    }


    public void addTabChangeListener(TabChangeListener listener)
    {
        addFacesListener(listener);
    }

    public void removeTabChangeListener(TabChangeListener listener)
    {
        removeFacesListener(listener);
    }


    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof TabChangeEvent)
        {
            TabChangeEvent tabChangeEvent = (TabChangeEvent)event;
            if (tabChangeEvent.getComponent() == this)
            {
                setSelectedIndex(tabChangeEvent.getNewTabIndex());
            }
        }
        super.broadcast(event);
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[ATTRIBUTE_COUNT + 1];
        int i = 0;
        values[i++] = super.saveState(context);
        values[i++] = new Integer(_selectedIndex);
        values[i++] = _bgcolor;
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to save differs!");
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _selectedIndex = ((Integer)values[i++]).intValue();
        _bgcolor       = (String)values[i++];
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to restore differs!");
    }
}
