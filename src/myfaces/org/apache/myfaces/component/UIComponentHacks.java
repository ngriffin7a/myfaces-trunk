/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.component;

import javax.faces.component.UIComponent;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * Misc. hacks for accessing protected fields in UIComponent.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIComponentHacks
{
    private UIComponentHacks() {}

    public static List[] getListeners(UIComponent uiComponent)
    {
        if (uiComponent instanceof MyFacesUICommand)
        {
            return ((MyFacesUICommand)uiComponent).getListeners();
        }
        else if (uiComponent instanceof MyFacesUIInput)
        {
            return ((MyFacesUIInput)uiComponent).getListeners();
        }
        else if (uiComponent instanceof javax.faces.component.UICommand ||
                 uiComponent instanceof javax.faces.component.UIInput)
        {
            //HACK to get protected field "listeners" from the given UICommand or UIInput:
            //TODO: try to convince Sun of making listeners public or give us a method to access them
            try
            {
                Field f = null;
                Class c = uiComponent.getClass();
                while (f == null && c != null && !c.equals(Object.class))
                {
                    try
                    {
                        f = c.getDeclaredField("listeners");
                    }
                    catch (NoSuchFieldException e)
                    {
                    }
                    c = c.getSuperclass();
                }

                if (f == null)
                {
                    throw new RuntimeException(new NoSuchFieldException());
                }

                List[] theListeners;
                if (f.isAccessible())
                {
                    theListeners = (List[])f.get(uiComponent);
                }
                else
                {
                    final Field finalF = f;
                    AccessController.doPrivileged(
                        new PrivilegedAction()
                        {
                            public Object run()
                            {
                                finalF.setAccessible(true);
                                return null;
                            }
                        });
                    theListeners = (List[])f.get(uiComponent);
                    f.setAccessible(false);
                }

                return theListeners;
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }


}
