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
package org.apache.myfaces.taglib.html;

import javax.faces.component.html.HtmlDataTable;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.14  2004/07/01 22:05:05  mwessendorf
 * ASF switch
 *
 * Revision 1.13  2004/04/05 11:04:55  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.12  2004/04/01 12:57:43  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.11  2004/03/31 11:58:41  manolito
 * custom component refactoring
 *
 */
public class HtmlDataTableTag
        extends HtmlDataTableTagBase
{
    public String getComponentType()
    {
        return HtmlDataTable.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "javax.faces.Table";
    }
}
