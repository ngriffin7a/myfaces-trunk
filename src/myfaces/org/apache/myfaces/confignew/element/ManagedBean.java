/*
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

package net.sourceforge.myfaces.confignew.element;

import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/06/16 23:02:22  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.1.2.1  2004/06/16 02:07:22  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.1  2004/05/17 14:28:26  manolito
 * new configuration concept
 *
 */
public interface ManagedBean
{
    // <!ELEMENT managed-bean (description*, display-name*, icon*, managed-bean-name, managed-bean-class, managed-bean-scope, (managed-property* | map-entries | list-entries))>

    public static final int INIT_MODE_NO_INIT = 0;
    public static final int INIT_MODE_PROPERTIES = 1;
    public static final int INIT_MODE_MAP = 2;
    public static final int INIT_MODE_LIST = 3;

    public String getManagedBeanName();
    public String getManagedBeanClassName();
    public Class getManagedBeanClass();
    public String getManagedBeanScope();

    public int getInitMode();

    /**
     * @return Iterator over {@link ManagedProperty} entries
     */
    public Iterator getManagedProperties();

    public MapEntries getMapEntries();

    public ListEntries getListEntries();
}
