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
package net.sourceforge.myfaces.taglib.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 */
public class HtmlInputHiddenTag
    extends HtmlComponentTag
{
    public String getComponentType()
    {
        return "javax.faces.InputHidden";
    }

    public String getDefaultRendererType()
    {
        return "javax.faces.Hidden";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML input attributes relevant for hidden-inputs
    //none relevant for hidden input

    // UIOutput attributes
    // value and converterId --> already implemented in MyfacesComponentTag    

    // UIInput attributes
    private String _required;
    private String _validator;
    private String _valueChangeListener;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.REQUIRED_ATTR, _required);
        setStringProperty(component, JSFAttr.VALIDATOR_ATTR, _validator);
        setValueChangedListenerProperty(component, _valueChangeListener);
    }

    public void setRequired(String required)
    {
        _required = required;
    }

    public void setValidator(String validator)
    {
        _validator = validator;
    }

    public void setValueChangeListener(String valueChangeListener)
    {
        _valueChangeListener = valueChangeListener;
    }
}
