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
package javax.faces.component;

import javax.faces.context.FacesContext;


/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISelectOne
        extends UIInput
{
    public static final String INVALID_MESSAGE_ID = "javax.faces.component.UISelectOne.INVALID";

    public void validate(FacesContext context)
    {
        super.validate(context);
        //TODO: see javadoc: iterate through UISelectItem and UISelectItems and check
        //current value against these items
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.SelectOne";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectOne";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Menu";


    public UISelectOne()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }


    //------------------ GENERATED CODE END ---------------------------------------
}
