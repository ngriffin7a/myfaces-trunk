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
package net.sourceforge.myfaces.renderkit.html.attr;

/**
 * see /tlds/entities/html_select_attributes.xml
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface HTMLSelectAttributes
{
    public static final String DATAFLD_ATTR = "datafld";
    public static final String DATASRC_ATTR = "datasrc";
    public static final String DATAFORMATAS_ATTR = "dataformatas";
    public static final String DISABLED_ATTR = "disabled";
    public static final String ONBLUR_ATTR = "onblur";
    public static final String ONCHANGE_ATTR = "onchange";
    public static final String ONFOCUS_ATTR = "onfocus";
    public static final String TABINDEX_ATTR = "tabindex";

    public static final String[] HTML_SELECT_ATTRIBUTES =
    {
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        DISABLED_ATTR,
        ONBLUR_ATTR,
        ONCHANGE_ATTR,
        ONFOCUS_ATTR,
        TABINDEX_ATTR
    };
}
