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
