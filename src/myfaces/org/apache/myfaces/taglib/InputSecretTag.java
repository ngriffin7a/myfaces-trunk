/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.SecretRenderer;

import javax.faces.component.UIComponent;

/**
 * see "input_secret" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputSecretTag
extends InputTextTag
{
    public String getComponentType()
    {
        return "Input";
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        if (uiComponent.getAttributes().get(JSFAttr.REDISPLAY_ATTR) == null)
        {
            uiComponent.getAttributes().put(JSFAttr.REDISPLAY_ATTR, Boolean.FALSE); //Default (JSF.7.6.4)
        }
    }

    public String getDefaultRendererType()
    {
        return SecretRenderer.TYPE;
    }


    // Secret Renderer attributes

    public void setRedisplay(String b)
    {
        setRendererAttributeBoolean(JSFAttr.REDISPLAY_ATTR, b);
    }

    // converter attribute --> already implemented in MyFacesTag

    // setMaxLength() already defined in InputTextTag
}
