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
 * see /tlds/entities/html_table_attributes.xml
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface HTMLTableAttributes
{
    public static final String ALIGN_ATTR = "align";
    public static final String BGCOLOR_ATTR = "bgcolor";
    public static final String BORDER_ATTR = "border";
    public static final String CELLPADDING_ATTR = "cellpadding";
    public static final String CELLSPACING_ATTR = "cellspacing";
    public static final String DATAFLD_ATTR = "datafld";
    public static final String DATASRC_ATTR = "datasrc";
    public static final String DATAFORMATAS_ATTR = "dataformatas";
    public static final String FRAME_ATTR = "frame";
    public static final String RULES_ATTR = "rules";
    public static final String SUMMARY_ATTR = "summary";
    public static final String WIDTH_ATTR = "width";

    public static final String[] HTML_TABLE_ATTRIBUTES = {
        ALIGN_ATTR,
        BGCOLOR_ATTR,
        BORDER_ATTR,
        CELLPADDING_ATTR,
        CELLSPACING_ATTR,
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        FRAME_ATTR,
        RULES_ATTR,
        SUMMARY_ATTR,
        WIDTH_ATTR
    };
}
