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
package net.sourceforge.myfaces.custom.navigation;

import net.sourceforge.myfaces.taglib.html.ext.HtmlCommandLinkTag;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 21:53:08  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/04/05 11:04:53  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.2  2004/04/01 12:57:40  manolito
 * additional extended component classes for user role support
 *
 */
public class HtmlCommandNavigationTag
        extends HtmlCommandLinkTag
{
    public String getComponentType()
    {
        return HtmlCommandNavigation.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "net.sourceforge.myfaces.Navigation";
    }
}
