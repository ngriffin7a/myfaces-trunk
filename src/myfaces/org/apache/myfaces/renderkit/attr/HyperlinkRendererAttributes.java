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
package net.sourceforge.myfaces.renderkit.attr;

/**
 * Constant definitions for the specified render dependent attributes of the
 * "Hyperlink" renderer type.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface HyperlinkRendererAttributes
    extends CommonRendererAttributes
{
    public static final String HREF_ATTR = "href";

    public static final String ACCESSKEY_ATTR = "accesskey";
    public static final String CHARSET_ATTR = "charset";
    public static final String COORDS_ATTR = "coords";
    public static final String HREFLANG_ATTR = "hreflang";
    public static final String REL_ATTR = "rel";
    public static final String REV_ATTR = "rev";
    public static final String SHAPE_ATTR = "shape";
    public static final String TABINDEX_ATTR = "tabindex";
    public static final String TARGET_ATTR = "target";
    public static final String TYPE_ATTR = "type";

    public static final String[] COMMON_HYPERLINK_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        CHARSET_ATTR,
        COORDS_ATTR,
        //HREF_ATTR, href is not a common attribute!
        HREFLANG_ATTR,
        REL_ATTR,
        REV_ATTR,
        SHAPE_ATTR,
        TABINDEX_ATTR,
        TARGET_ATTR,
        TYPE_ATTR
    };
}
