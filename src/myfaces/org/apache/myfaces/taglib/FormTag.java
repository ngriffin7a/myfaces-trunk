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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.MyFacesUIForm;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.FormRenderer;
import net.sourceforge.myfaces.renderkit.html.HTML;


/**
 * see "form" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FormTag
extends MyFacesTag
{
    public String getComponentType()
    {
        return "Form";
    }

    public String getDefaultRendererType()
    {
        return FormRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIForm attributes

    public void setFormName(String v)
    {
        setComponentPropertyString(MyFacesUIForm.FORM_NAME_PROP, v);
    }

    public void setFormClass(String v)
    {
        setRendererAttributeString(JSFAttr.FORM_CLASS_ATTR, v);
    }


    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML form attributes

    public void setAccept(String value)
    {
        setRendererAttributeString(HTML.ACCEPT_ATTR, value);
    }

    public void setAcceptCharset(String value)
    {
        setRendererAttributeString(HTML.ACCEPT_CHARSET_ATTR, value);
    }

    public void setEnctype(String value)
    {
        setRendererAttributeString(HTML.ENCTYPE_ATTR, value);
    }

    public void setOnreset(String value)
    {
        setRendererAttributeString(HTML.ONRESET_ATTR, value);
    }

    public void setOnsubmit(String value)
    {
        setRendererAttributeString(HTML.ONSUMBIT_ATTR, value);
    }

    public void setTarget(String value)
    {
        setRendererAttributeString(HTML.TARGET_ATTR, value);
    }

    // Form Renderer attributes --> no attributes yet

    // key and bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}
