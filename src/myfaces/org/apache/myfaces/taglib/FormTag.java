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

import net.sourceforge.myfaces.component.UIForm;
import net.sourceforge.myfaces.renderkit.html.FormRenderer;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FormTag
        extends MyFacesTag
{
    //MyFaces tag extensions:
    public UIComponent createComponent()
    {
        return new UIForm();
    }

    public String getRendererType()
    {
        return FormRenderer.TYPE;
    }

    public void setFormName(String v)
    {
        setComponentAttribute(UIForm.FORM_NAME_ATTR, v);
    }

    //form tag properties

    public void setAction(String value)
    {
        setRendererAttribute(FormRenderer.ACTION_ATTR, value);
    }

    public void setAccept(String value)
    {
        setRendererAttribute(FormRenderer.ACCEPT_ATTR, value);
    }

    public void setAcceptCharset(String value)
    {
        setRendererAttribute(FormRenderer.ACCEPT_CHARSET_ATTR, value);
    }

    public void setEnctype(String value)
    {
        setRendererAttribute(FormRenderer.ENCTYPE_ATTR, value);
    }

    public void setMethod(String value)
    {
        setRendererAttribute(FormRenderer.METHOD_ATTR, value);
    }

    public void setOnreset(String value)
    {
        setRendererAttribute(FormRenderer.ONRESET_ATTR, value);
    }

    public void setOnsubmit(String value)
    {
        setRendererAttribute(FormRenderer.ONSUMBIT_ATTR, value);
    }

    public void setTarget(String value)
    {
        setRendererAttribute(FormRenderer.TARGET_ATTR, value);
    }



}
