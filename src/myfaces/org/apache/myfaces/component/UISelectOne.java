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

import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISelectOne
    extends javax.faces.component.UISelectOne
{
    public static final String SIZE_ATTR = "size";

    public boolean getRendersSelf()
    {
        return false;
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public Object getSelectedValue()
    {
        throw new UnsupportedOperationException("not supported.");
    }

    public void setSelectedValue(Object selectedValue)
    {
        throw new UnsupportedOperationException("not supported.");
    }

    protected Iterator getSelectItems(FacesContext context)
    {
        throw new UnsupportedOperationException("not supported.");
    }

    public void setSize(int s)
    {
        setAttribute(SIZE_ATTR, new Integer(s));
    }

    public int getSize()
    {
        Integer size = (Integer)getAttribute(SIZE_ATTR);
        return size == null ? 0 : size.intValue();
    }

}
