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
package net.sourceforge.myfaces.custom.sortheader;


import net.sourceforge.myfaces.taglib.html.ext.HtmlCommandLinkTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.5  2004/08/13 13:34:20  manolito
 * HtmlCommandSortHeader now supports immediate attribute
 *
 * Revision 1.4  2004/07/01 21:53:10  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/04/05 11:04:54  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.2  2004/04/01 12:57:41  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.1  2004/03/31 12:15:27  manolito
 * custom component refactoring
 *
 */
public class HtmlCommandSortHeaderTag
        extends HtmlCommandLinkTag
{
    //private static final Log log = LogFactory.getLog(HtmlCommandSortHeaderTag.class);

    public String getComponentType()
    {
        return HtmlCommandSortHeader.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "net.sourceforge.myfaces.SortHeader";
    }

    private String _columnName;
    private String _arrow;
    private boolean _immediateSet;

    // User Role support --> already handled by HtmlPanelGroupTag

    
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "columnName", _columnName);
        setBooleanProperty(component, "arrow", _arrow);

        if (!_immediateSet)
        {
            //Default of immediate is true (contrary to normal command links)
            setBooleanProperty(component, "immediate", "true");
        }
    }

    public void setColumnName(String columnName)
    {
        _columnName = columnName;
    }

    public void setArrow(String arrow)
    {
        _arrow = arrow;
    }

    public void setImmediate(String immediate)
    {
        super.setImmediate(immediate);
        _immediateSet = true;
    }
}
