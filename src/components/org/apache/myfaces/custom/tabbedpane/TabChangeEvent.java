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
package net.sourceforge.myfaces.custom.tabbedpane;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/04/06 15:32:14  manolito
 * correct phase is APPLY_REQUEST_VALUES
 *
 */
public class TabChangeEvent
        extends FacesEvent
{
    //private static final Log log = LogFactory.getLog(TabChangeEvent.class);

    private int _oldTabIndex;
    private int _newTabIndex;

    public TabChangeEvent(UIComponent component, int oldTabIndex, int newTabIndex)
    {
        super(component);
        _oldTabIndex = oldTabIndex;
        _newTabIndex = newTabIndex;
        setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
    }

    public int getOldTabIndex()
    {
        return _oldTabIndex;
    }

    public int getNewTabIndex()
    {
        return _newTabIndex;
    }

    public boolean isAppropriateListener(FacesListener listener)
    {
        return listener instanceof TabChangeListener;
    }

    public void processListener(FacesListener listener)
    {
        ((TabChangeListener)listener).processTabChange(this);
    }

}
