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
package net.sourceforge.myfaces.taglib.html;

import javax.faces.component.html.HtmlCommandLink;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.19  2004/07/01 22:05:05  mwessendorf
 * ASF switch
 *
 * Revision 1.18  2004/04/05 11:04:55  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.17  2004/04/01 12:57:43  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.16  2004/03/31 11:58:40  manolito
 * custom component refactoring
 *
 */
public class HtmlCommandLinkTag
    extends HtmlCommandLinkTagBase
{
    public String getComponentType()
    {
        return HtmlCommandLink.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "javax.faces.Link";
    }
}
