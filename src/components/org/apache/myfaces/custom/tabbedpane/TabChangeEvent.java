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
package org.apache.myfaces.custom.tabbedpane;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/10/13 11:50:58  matze
 * renamed packages to org.apache
 *
 * Revision 1.3  2004/07/01 21:53:05  mwessendorf
 * ASF switch
 *
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
