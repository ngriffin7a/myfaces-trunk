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
package net.sourceforge.myfaces.examples.misc;

import net.sourceforge.myfaces.custom.navmenu.NavigationMenuItem;
import net.sourceforge.myfaces.examples.util.GuiUtil;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationMenu
{
    public NavigationMenuItem[] getInfoItems()
    {
        String label = GuiUtil.getMessageResource("nav_Info", null);
        NavigationMenuItem[] menu= new NavigationMenuItem[1];

        menu[0] = new NavigationMenuItem(label, null, null, true);

        NavigationMenuItem[] items = new NavigationMenuItem[2];
        menu[0].setNavigationMenuItems(items);

        label = GuiUtil.getMessageResource("nav_Contact", null);
        items[0] = new NavigationMenuItem(label, "go_contact", "jscookmenu/ThemeOffice/help.gif", false);

        label = GuiUtil.getMessageResource("nav_Copyright", null);
        items[1] = new NavigationMenuItem(label, "go_copyright", "jscookmenu/ThemeOffice/help.gif", false);

        return menu;
    }

}
