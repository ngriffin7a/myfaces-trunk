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
