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
package net.sourceforge.myfaces.examples.util;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LocalizedSelectItem
    extends SelectItem
{
    private static String BUNDLE_NAME = "net.sourceforge.myfaces.examples.resource.example_messages";

    public LocalizedSelectItem(String key)
    {
        super(key);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME, facesContext.getViewRoot().getLocale());
        String label = rb.getString(key);
        if (label != null)
        {
            setLabel(label);
        }
        else
        {
            setLabel(key);
        }
    }
}
