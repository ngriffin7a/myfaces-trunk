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


package net.sourceforge.myfaces.custom.tree.event;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.component.UIComponent;

import net.sourceforge.myfaces.custom.tree.model.TreePath;


/**
 * Event fired by {@link net.sourceforge.myfaces.custom.tree.HtmlTree} on selection changes.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.2  2004/04/23 19:09:34  o_rossmueller
 *          state transition magic
 *
 *          Revision 1.1  2004/04/22 21:14:54  o_rossmueller
 *          TreeSelectionListener support
 *
 */
public class TreeSelectionEvent extends FacesEvent
{

    private TreePath oldSelectionPath;
    private TreePath newSelectionPath;


    /**
     * Construct an event.
     *
     * @param uiComponent      event source
     * @param oldSelectionPath path of the old selection, null if no node was selected before
     * @param newSelectionPath path of the current selection
     */
    public TreeSelectionEvent(UIComponent uiComponent, TreePath oldSelectionPath, TreePath newSelectionPath)
    {
        super(uiComponent);
        this.oldSelectionPath = oldSelectionPath;
        this.newSelectionPath = newSelectionPath;
    }


    /**
     * Answer the path of the old selection.
     *
     * @return path of previous (old) selection, null if no node was selected before
     */
    public TreePath getOldSelectionPath()
    {
        return oldSelectionPath;
    }


    /**
     * Answer the path of the current (new) selection.
     *
     * @return path of the new selected node
     */
    public TreePath getNewSelectionPath()
    {
        return newSelectionPath;
    }


    public boolean isAppropriateListener(FacesListener faceslistener)
    {
        return faceslistener instanceof TreeSelectionListener;
    }


    public void processListener(FacesListener faceslistener)
    {
        ((TreeSelectionListener) faceslistener).valueChanged(this);
    }
}
