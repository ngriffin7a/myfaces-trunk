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
package org.apache.myfaces.component.html.util;

import org.apache.myfaces.renderkit.JSFAttr;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.util.HashMap;

/**
 * <p>Utility class for providing basic functionality to the HTML faces 
 * extended components.<p>
 * 
 * @author not attributable
 * @version 
 */
public class HtmlComponentUtils 
{
    private static final String TRUE = "true";
    private static final String KEY_COMPONENT_ID_MAP = "KEY_COMPONENT_ID_MAP";
    
    /**
     * Constructor (Private)
     */
    private HtmlComponentUtils() 
    {}
    
    public static String getClientId(UIComponent component,
                                     Renderer renderer,
                                     FacesContext context)
    {
        // see if the originally supplied id should be used 
        Boolean forceValue = (Boolean)component.getAttributes().get(JSFAttr.FORCE_ID_ATTR);
        boolean forceId = forceValue.booleanValue();
        
        if (forceId && component.getId() != null)
        {
            /** @todo handle "indexed" data values */            
            String clientId = component.getId();
            
            // JSF spec requires that renderer get a chance to convert the id
            if (renderer != null)
            {
                clientId = renderer.convertClientId(context, clientId);
            }
            
            // avoid having duplicate id's 
            HashMap idMap = (HashMap)context.getViewRoot().getAttributes().get(KEY_COMPONENT_ID_MAP);
            
            if (idMap == null)
            {
                idMap = new HashMap();
                context.getViewRoot().getAttributes().
                    put(KEY_COMPONENT_ID_MAP, idMap);
            }

            /**
             * Since components that use this utility are intended to be 
             * rendered in HTML they should conform to the XHTML standard 
             * which mandates that each component have a unique id.  
             * Right now this is pretty much impossible to enforce in the 
             * appropriate Renderer class so its being done here.
             */
            if (idMap.containsKey(clientId))
            {
                UIComponent mappedComponent = (UIComponent)idMap.get(clientId);
                if (component.equals(mappedComponent))
                {
                    throw new IllegalArgumentException("Duplicate cliientId not allowed.  View already contains the id: " + 
                        clientId);
                }
            }
            
            return clientId;
        }
        else
        {
            return component.getClientId(context);
        }
    }
}