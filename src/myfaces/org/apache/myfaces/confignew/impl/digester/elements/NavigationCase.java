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

package net.sourceforge.myfaces.confignew.impl.digester.elements;




/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class NavigationCase implements net.sourceforge.myfaces.confignew.element.NavigationCase
{

    private String fromAction;
    private String fromOutcome;
    private String toViewId;
    private String redirect;


    public String getFromAction()
    {
        return fromAction;
    }


    public void setFromAction(String fromAction)
    {
        this.fromAction = fromAction;
    }


    public String getFromOutcome()
    {
        return fromOutcome;
    }


    public void setFromOutcome(String fromOutcome)
    {
        this.fromOutcome = fromOutcome;
    }


    public String getToViewId()
    {
        return toViewId;
    }


    public void setToViewId(String toViewId)
    {
        this.toViewId = toViewId;
    }


    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }


    public boolean isRedirect()
    {
        return Boolean.valueOf(redirect).booleanValue();
    }
}
