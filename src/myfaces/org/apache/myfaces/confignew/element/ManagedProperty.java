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

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/17 14:28:26  manolito
 * new configuration concept
 *
 */
public interface ManagedProperty
{
    // <!ELEMENT managed-property (description*, display-name*, icon*, property-name, property-class?, (map-entries|null-value|value|list-entries))>

    public static final int TYPE_MAP = 1;
    public static final int TYPE_NULL = 2;
    public static final int TYPE_VALUE = 3;
    public static final int TYPE_LIST = 4;

    public String getPropertyName();
    public String getPropertyClass();

    public int getType();
    public MapEntries getMapEntries();
    public String getValue();
    public ListEntries getListEntries();
}
