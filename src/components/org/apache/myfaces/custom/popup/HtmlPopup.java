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
package org.apache.myfaces.custom.popup;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPopup
        extends UIComponentBase
{
    //private static final Log log = LogFactory.getLog(HtmlPopup.class);

    private static final String POPUP_FACET_NAME            = "popup";

    public void setPopup(UIComponent popup)
    {
        getFacets().put(POPUP_FACET_NAME, popup);
    }

    public UIComponent getPopup()
    {
        return (UIComponent)getFacets().get(POPUP_FACET_NAME);
    }

    public boolean getRendersChildren()
    {
        return true;
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlPopup";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Popup";


    public HtmlPopup()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }



    //------------------ GENERATED CODE END ---------------------------------------
}
