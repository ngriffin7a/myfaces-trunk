/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.renderkit.html.state;

import javax.faces.component.UIComponent;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class RequestParameterNames
{
    private RequestParameterNames() {} //Utility class

    protected static String getModelValueStateParameterName(String modelRef)
    {
        return "SM_" + modelRef;
    }

    protected static String getUIComponentStateParameterName(UIComponent uiComponent,
                                                             String attributeName)
    {
        return "SC_" + uiComponent.getCompoundId() + "." + attributeName;
    }

    protected static String restoreUIComponentStateParameterAttributeName(UIComponent uiComponent,
                                                                          String paramName)
    {
        String prefix = "SC_" + uiComponent.getCompoundId() + ".";
        if (paramName.startsWith(prefix))
        {
            return paramName.substring(prefix.length());
        }
        else
        {
            return null;
        }
    }


}
