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
package net.sourceforge.myfaces.custom.navmenu;

import javax.faces.model.SelectItem;
import java.util.Collections;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.2  2004/07/01 21:53:07  mwessendorf
 *          ASF switch
 *
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class NavigationMenuItem
    extends SelectItem
{
    private String _icon;
    private String _action;
    boolean _split;
    private List _children = Collections.EMPTY_LIST;

    public NavigationMenuItem(Object value, String label, String action, String icon, boolean split)
    {
        super(value, label);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public NavigationMenuItem(Object value,
                              String label,
                              String description,
                              boolean disabled,
                              String action,
                              String icon,
                              boolean split)
    {
        super(value, label, description, disabled);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public String getAction()
    {
        return _action;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public boolean isSplit()
    {
        return _split;
    }

    public void setSplit(boolean split)
    {
        _split = split;
    }

    public String getIcon()
    {
        return _icon;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public List getChildren()
    {
        return _children;
    }

    public void setChildren(List children)
    {
        _children = children;
    }
}
