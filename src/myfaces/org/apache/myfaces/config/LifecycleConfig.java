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
package net.sourceforge.myfaces.config;

import javax.faces.event.PhaseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @author Thomas Spiegl*
 * @version $Revision$ $Date$
 */
public class LifecycleConfig
    implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private ArrayList _phaseListeners;

    //~ Methods ------------------------------------------------------------------------------------

    public void addPhaseListenerClasses(Iterator phaseListenerClasses)
    {
        while (phaseListenerClasses.hasNext())
        {
            _phaseListeners.add(phaseListenerClasses.next());
        }
    }

    public void addPhaseListener(PhaseListener phaseListener)
    {
            if (_phaseListeners == null)
            {
                _phaseListeners = new ArrayList();
            }
            _phaseListeners.add(phaseListener);
    }

    public Iterator getPhaseListenerClasses()
    {
        if (_phaseListeners == null)
        {
            return Collections.EMPTY_LIST.iterator();
        }
        return _phaseListeners.iterator();
    }
}
