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


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class NavigationCaseConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private String  _fromAction  = null;
    private String  _fromOutcome = null;
    private String  _toViewId    = null;
    private boolean _redirect    = false;

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

    public void setFromAction(String fromAction)
    {
        _fromAction = fromAction.intern();
    }

    public String getFromAction()
    {
        return _fromAction;
    }

    public void setFromOutcome(String fromOutcome)
    {
        _fromOutcome = fromOutcome.intern();
    }

    public String getFromOutcome()
    {
        return _fromOutcome;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore        
//        _iconConfig = iconConfig;
    }

    public void setRedirect(boolean redirect)
    {
        _redirect = redirect;
    }

    public boolean isRedirect()
    {
        return _redirect;
    }

    public void setToViewId(String toViewId)
    {
        _toViewId = toViewId.intern();
    }

    public String getToViewId()
    {
        return _toViewId;
    }
}
