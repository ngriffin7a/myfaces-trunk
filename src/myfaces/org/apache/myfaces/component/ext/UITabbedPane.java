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

import net.sourceforge.myfaces.component.MyFacesUIPanel;

import javax.faces.component.UIComponent;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UITabbedPane
    extends MyFacesUIPanel
{
    private int _activeTab = 0;

    public int getActiveTab()
    {
        return _activeTab;
    }

    public void setActiveTab(int activeTab)
    {
        _activeTab = activeTab;
    }

    public int getTabIndex(UITab tab)
    {
        Iterator it = getChildren();
        int cnt = 0;
        while (it.hasNext())
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UITab)
            {
                if (child == tab)
                {
                    return cnt;
                }
                cnt++;
            }
        }
        return -1;
    }

    public int getTabIndex(UITabHeader tabHeader)
    {
        Iterator it = getChildren();
        int cnt = 0;
        while (it.hasNext())
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UITabHeader)
            {
                if (child == tabHeader)
                {
                    return cnt;
                }
                cnt++;
            }
        }
        return -1;
    }

    public int getTabCount()
    {
        Iterator it = getChildren();
        int cnt = 0;
        while (it.hasNext())
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UITabHeader)
            {
                cnt++;
            }
        }
        return cnt;
    }
}
