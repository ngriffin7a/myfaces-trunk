/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.tree.event;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.tree.model.TreePath;


/**
 * Event fired by {@link org.apache.myfaces.custom.tree.HtmlTree} on selection changes.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.4  2004/10/13 11:50:58  matze
 *          renamed packages to org.apache
 *
 *          Revision 1.3  2004/07/01 21:53:08  mwessendorf
 *          ASF switch
 *
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
