/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.examples.util;

import javax.faces.context.FacesContext;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExampleMessagesResourceBundle
    extends ResourceBundle
{
    private static String BUNDLE_NAME = "net.sourceforge.myfaces.examples.resource.example_messages";

    protected ResourceBundle getMyBundle()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return ResourceBundle.getBundle(BUNDLE_NAME, facesContext.getViewRoot().getLocale());
    }

    protected Object handleGetObject(String key)
    {
        return getMyBundle().getObject(key);
    }

    public Enumeration getKeys()
    {
        return getMyBundle().getKeys();
    }
}
