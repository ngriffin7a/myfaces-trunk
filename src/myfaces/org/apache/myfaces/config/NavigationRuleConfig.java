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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class NavigationRuleConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private List   _navigationCaseConfigList = null;
    private String _fromViewId = null;

    //~ Methods ------------------------------------------------------------------------------------

    public void setDescription(String description)
    {
// ignore        
//        _description = description;
    }

    public void setDisplayName(String displayName)
    {
// ignore        
//        _displayName = displayName;
    }

    public void setFromViewId(String fromViewId)
    {
        _fromViewId = fromViewId.intern();
    }

    public String getFromViewId()
    {
        return _fromViewId;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore        
//        _iconConfig = iconConfig;
    }

    public List getNavigationCaseConfigList()
    {
        return (_navigationCaseConfigList == null) ? Collections.EMPTY_LIST
                                                   : _navigationCaseConfigList;
    }

    public void addNavigationCaseConfig(NavigationCaseConfig navigationCaseConfig)
    {
        if (_navigationCaseConfigList == null)
        {
            _navigationCaseConfigList = new ArrayList();
        }
        _navigationCaseConfigList.add(navigationCaseConfig);
    }
}
