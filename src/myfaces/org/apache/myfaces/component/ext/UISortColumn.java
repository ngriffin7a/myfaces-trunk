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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISortColumn
    extends UICommand
{
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException
    {
        if (event instanceof ActionEvent)
        {
            //We call processAction directly, so we can avoid having to register
            //the sort header component as an ActionListener of all it's children
            UISortHeader uiSortHeader = findUISortHeader();
            if (uiSortHeader == null)
            {
                throw new FacesException("UISortColumn has no UISortHeader ancestor!");
            }
            uiSortHeader.processAction((ActionEvent)event);
        }
        return super.broadcast(event, phaseId);
    }

    protected UISortHeader findUISortHeader()
    {
        UIComponent parent = getParent();
        while (parent != null)
        {
            if (parent instanceof UISortHeader)
            {
                return (UISortHeader)parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
