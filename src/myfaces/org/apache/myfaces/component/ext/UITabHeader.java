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

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UITabHeader
    extends MyFacesUICommand
{
    public UITabHeader()
    {
        setValid(true);
    }

    public boolean isActive()
    {
        UITabbedPane tabbedPane = (UITabbedPane)getParent();
        int idx = tabbedPane.getTabIndex(this);
        if (idx == -1)
        {
            return false;
        }
        else
        {
            return tabbedPane.getActiveTab() == idx;
        }
    }

    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException
    {
        if (phaseId == PhaseId.APPLY_REQUEST_VALUES &&
            event instanceof ActionEvent &&
            event.getSource() == this)
        {
            //TabHeader was clicked
            UITabbedPane tabbedPane = (UITabbedPane)getParent();
            int idx = tabbedPane.getTabIndex(this);
            tabbedPane.setActiveTab(idx);
        }
        return super.broadcast(event, phaseId);
    }

}
