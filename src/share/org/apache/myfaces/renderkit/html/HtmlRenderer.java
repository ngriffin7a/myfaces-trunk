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
package org.apache.myfaces.renderkit.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;


/**
 * $Log$
 * Revision 1.4  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.3  2004/07/01 22:00:57  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/23 13:44:21  royalts
 * no message
 *
 * Revision 1.1  2004/03/29 14:57:00  manolito
 * refactoring for implementation and non-standard component split
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class HtmlRenderer
        extends Renderer
{
    private static final Log log = LogFactory.getLog(HtmlRenderer.class);

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        if (log.isWarnEnabled())
        {
            if (getRendersChildren())
            {
                log.warn("Renderer " + getClass().getName() + " renders its children and should therefore implement encodeChildren.");
            }
            else
            {
                log.warn("Renderer " + getClass().getName() + " does not render its children. Method encodeChildren should not be called.");
            }
        }
    }
}
