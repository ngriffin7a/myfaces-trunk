/**
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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.MyFacesUIPanel;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigation
    extends MyFacesUIPanel
    implements NamingContainer
{
    public UINavigation()
    {
        //FIXME
        //setValid(true);
    }

    public boolean getRendersChildren()
    {
        return false;
    }

    //NamingContainer Support
    //FIXME
    //private NamingContainer _namingContainer = new NamingContainerSupport();

    public void addComponentToNamespace(UIComponent uicomponent)
    {
        //FIXME
        //String componentId = uicomponent.getComponentId();
        String componentId = null;
        if (componentId != null)
        {
            //HACK: Because there is a bug in the API implementation of UIComponentBase
            //(removeChild does not call removeComponentFromNamespace) we ignore
            //component already in namespace
            //FIXME
            //UIComponent old = _namingContainer.findComponentInNamespace(componentId);
            UIComponent old = null;
            if (old != null)
            {
                //FIXME
                //_namingContainer.removeComponentFromNamespace(old);
            }
        }
        //FIXME
        //_namingContainer.addComponentToNamespace(uicomponent);
    }

    public void removeComponentFromNamespace(UIComponent uicomponent)
    {
        //FIXME
        //_namingContainer.removeComponentFromNamespace(uicomponent);
    }

    public UIComponent findComponentInNamespace(String s)
    {
        //FIXME
        //return _namingContainer.findComponentInNamespace(s);
        return null;
    }

    public String generateClientId()
    {
        //FIXME
        //return _namingContainer.generateClientId();
        return null;
    }

}
