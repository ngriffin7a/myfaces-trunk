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
 * "Text" renderer type.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface TextRendererAttributes
    extends CommonRendererAttributes
{
    //public static final String ACCEPT_ATTR = "accept";
    public static final String ACCESSKEY_ATTR = "accesskey";
    public static final String ALIGN_ATTR = "align";
    public static final String ALT_ATTR = "alt";
    public static final String CHECKED_ATTR = "checked";
    public static final String DATAFLD_ATTR = "datafld";
    public static final String DATASRC_ATTR = "datasrc";
    public static final String DATAFORMATAS_ATTR = "dataformatas";
    //public static final String ISMAP_ATTR = "ismap";
    public static final String MAX_LENGTH_ATTR = "maxlength";
    public static final String NAME_ATTR = "name";
    public static final String ONBLUR_ATTR = "onblur";
    public static final String ONCHANGE_ATTR = "onchange";
    public static final String ONFOCUS_ATTR = "onfocus";
    public static final String ONSELECT_ATTR = "onselect";
    public static final String READONLY_ATTR = "readonly";
    public static final String SIZE_ATTR = "size";
    //public static final String SRC_ATTR = "src";
    public static final String TABINDEX_ATTR = "tabindex";
    //public static final String USEMAP_ATTR = "usemap";
    public static final String[] COMMON_TEXT_ATTRIBUTES =
    {
        //ACCEPT_ATTR,
        ACCESSKEY_ATTR,
        ALIGN_ATTR,
        ALT_ATTR,
        CHECKED_ATTR,
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        //ISMAP_ATTR,
        MAX_LENGTH_ATTR,
        NAME_ATTR,
        ONBLUR_ATTR,
        ONCHANGE_ATTR,
        ONFOCUS_ATTR,
        READONLY_ATTR,
        SIZE_ATTR,
        //SRC_ATTR,
        TABINDEX_ATTR,
        //USEMAP_ATTR
    };
}
