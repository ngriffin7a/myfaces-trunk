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
package net.sourceforge.myfaces.custom.date;

import java.util.Date;

import javax.faces.component.UIInput;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputDate extends UIInput {

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlInputDate";

    public static final String COMPONENT_FAMILY = "javax.faces.Input";

    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Date";
    
    public HtmlInputDate() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /*
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    */

    public Date getDate() {
        return (Date) getValue();
    }
}