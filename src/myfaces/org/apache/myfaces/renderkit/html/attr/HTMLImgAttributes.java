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
 * see /tlds/entities/html_img_attributes.xml
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface HTMLImgAttributes
{
    public static final String ALIGN_ATTR = "align";
    public static final String ALT_ATTR = "alt";
    public static final String BORDER_ATTR = "border";
    public static final String HEIGHT_ATTR = "height";
    public static final String HSPACE_ATTR = "hspace";
    public static final String ISMAP_ATTR = "ismap";
    public static final String LONGDESC_ATTR = "longdesc";
    public static final String USEMAP_ATTR = "usemap";
    public static final String VSPACE_ATTR = "vspace";
    public static final String WIDTH_ATTR = "width";

    public static final String[] HTML_IMG_ATTRUBUTES =
    {
        ALIGN_ATTR,
        ALT_ATTR,
        BORDER_ATTR,
        HEIGHT_ATTR,
        HSPACE_ATTR,
        ISMAP_ATTR,
        LONGDESC_ATTR,
        USEMAP_ATTR,
        VSPACE_ATTR,
        WIDTH_ATTR
    };
}
