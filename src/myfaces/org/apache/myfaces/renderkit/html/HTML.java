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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.util.ArrayUtils;



/**
 * Constant declarations for HTML rendering.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTML
{
    private HTML() {}

    // Common attributes
    public static final String ALIGN_ATTR = "align";
    public static final String DATAFLD_ATTR = "datafld";
    public static final String DATASRC_ATTR = "datasrc";
    public static final String DATAFORMATAS_ATTR = "dataformatas";
    public static final String BORDER_ATTR = "border";
    public static final String WIDTH_ATTR = "width";
    public static final String READONLY_ATTR = "readonly";
    public static final String ACCEPT_ATTR = "accept";

    // Common event handler attributes
    public static final String ONCLICK_ATTR     = "onclick";
    public static final String ONDBLCLICK_ATTR  = "ondblclick";
    public static final String ONMOUSEDOWN_ATTR = "onmousedown";
    public static final String ONMOUSEUP_ATTR   = "onmouseup";
    public static final String ONMOUSEOVER_ATTR = "onmouseover";
    public static final String ONMOUSEMOVE_ATTR = "onmousemove";
    public static final String ONMOUSEOUT_ATTR  = "onmouseout";
    public static final String ONKEYPRESS_ATTR  = "onkeypress";
    public static final String ONKEYDOWN_ATTR   = "onkeydown";
    public static final String ONKEYUP_ATTR     = "onkeyup";
    public static final String[] EVENT_HANDLER_ATTRIBUTES =
    {
        ONCLICK_ATTR,
        ONDBLCLICK_ATTR,
        ONMOUSEDOWN_ATTR,
        ONMOUSEUP_ATTR,
        ONMOUSEOVER_ATTR,
        ONMOUSEMOVE_ATTR,
        ONMOUSEOUT_ATTR,
        ONKEYPRESS_ATTR,
        ONKEYDOWN_ATTR,
        ONKEYUP_ATTR
    };

    // Input field event handler attributes
    public static final String ONFOCUS_ATTR = "onfocus";
    public static final String ONBLUR_ATTR = "onblur";
    public static final String ONSELECT_ATTR = "onselect";
    public static final String ONCHANGE_ATTR = "onchange";
    public static final String[] COMMON_FIELD_EVENT_ATTRIBUTES =
    {
        ONFOCUS_ATTR,
        ONBLUR_ATTR,
        ONSELECT_ATTR,
        ONCHANGE_ATTR
    };

    // universal attributes
    public static final String DIR_ATTR   = "dir";
    public static final String LANG_ATTR  = "lang";
    public static final String STYLE_ATTR = "style";
    public static final String TITLE_ATTR = "title";
    public static final String[] UNIVERSAL_ATTRIBUTES =
    {
        DIR_ATTR,
        LANG_ATTR,
        STYLE_ATTR,
        TITLE_ATTR,
    };

    // common form field attributes
    public static final String ACCESSKEY_ATTR   = "accesskey";
    public static final String TABINDEX_ATTR    = "tabindex";
    public static final String DISABLED_ATTR = "disabled";
    public static final String[] COMMON_FIELD_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        TABINDEX_ATTR,
        DISABLED_ATTR,
    };
    
    // Common Attributes    
    public static final String[] COMMON_PASSTROUGH_ATTRIBUTES = 
        (String[]) ArrayUtils.concat(
            EVENT_HANDLER_ATTRIBUTES, 
            UNIVERSAL_ATTRIBUTES);
    public static final String[] COMMON_FIELD_PASSTROUGH_ATTRIBUTES = 
        (String[]) ArrayUtils.concat(
            COMMON_PASSTROUGH_ATTRIBUTES, 
            COMMON_FIELD_ATTRIBUTES,
            COMMON_FIELD_EVENT_ATTRIBUTES);

    // <a>
    public static final String TARGET_ATTR = "target";  //used by <a> and <form>
    public static final String CHARSET_ATTR     = "charset";
    public static final String COORDS_ATTR      = "coords";
    public static final String HREF_ATTR    = "href";
    public static final String HREFLANG_ATTR    = "hreflang";
    public static final String REL_ATTR         = "rel";
    public static final String REV_ATTR         = "rev";
    public static final String SHAPE_ATTR       = "shape";
    public static final String TYPE_ATTR        = "type";
    public static final String[] ANCHOR_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        CHARSET_ATTR,
        COORDS_ATTR,
        HREFLANG_ATTR,
        REL_ATTR,
        REV_ATTR,
        SHAPE_ATTR,
        TABINDEX_ATTR,
        TARGET_ATTR,
        TYPE_ATTR
    };

    // <form>
    public static final String ACCEPT_CHARSET_ATTR = "accept-charset";
    public static final String ENCTYPE_ATTR = "enctype";
    public static final String ONRESET_ATTR = "onreset";
    public static final String ONSUMBIT_ATTR = "onsubmit";
    public static final String[] FORM_ATTRIBUTES =
    {
        ACCEPT_ATTR,
        ACCEPT_CHARSET_ATTR,
        ENCTYPE_ATTR,
        ONRESET_ATTR,
        ONSUMBIT_ATTR,
        TARGET_ATTR,
    };

    // <img>
    public static final String ALT_ATTR = "alt";
    public static final String HEIGHT_ATTR = "height";
    public static final String HSPACE_ATTR = "hspace";
    public static final String ISMAP_ATTR = "ismap";
    public static final String LONGDESC_ATTR = "longdesc";
    public static final String USEMAP_ATTR = "usemap";
    public static final String VSPACE_ATTR = "vspace";
    public static final String[] IMG_ATTRUBUTES =
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

    // <input>
    public static final String SIZE_ATTR = "size";
    public static final String[] INPUT_ATTRIBUTES = {
        ACCESSKEY_ATTR,
        ALIGN_ATTR,
        ALT_ATTR,
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        DISABLED_ATTR,
        ONBLUR_ATTR,
        ONCHANGE_ATTR,
        ONFOCUS_ATTR,
        ONSELECT_ATTR,
        READONLY_ATTR,
        SIZE_ATTR,
        TABINDEX_ATTR,
    };

    // <button>
    public static final String[] BUTTON_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        ALIGN_ATTR,
        ALT_ATTR,
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        DISABLED_ATTR,
        ONBLUR_ATTR,
        ONCHANGE_ATTR,
        ONFOCUS_ATTR,
        TABINDEX_ATTR,
    };

    // <label>
    public static final String[] LABEL_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        ONBLUR_ATTR,
        ONFOCUS_ATTR
    };

    // <select>
    public static final String[] SELECT_ATTRIBUTES =
    {
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
    };
    public static final String[] SELECT_PASSTHROUGH_ATTRIBUTES =
        (String[]) ArrayUtils.concat(
            COMMON_FIELD_PASSTROUGH_ATTRIBUTES, 
            SELECT_ATTRIBUTES);

    // <table>
    public static final String BGCOLOR_ATTR = "bgcolor";
    public static final String CELLPADDING_ATTR = "cellpadding";
    public static final String CELLSPACING_ATTR = "cellspacing";
    public static final String FRAME_ATTR = "frame";
    public static final String RULES_ATTR = "rules";
    public static final String SUMMARY_ATTR = "summary";
    public static final String[] TABLE_ATTRIBUTES = {
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

    // <textarea>
    public static final String COLS_ATTR = "cols";
    public static final String ROWS_ATTR = "rows";
    public static final String[] TEXTAREA_ATTRIBUTES =
    {
        ACCESSKEY_ATTR,
        COLS_ATTR,
        DATAFLD_ATTR,
        DATASRC_ATTR,
        DATAFORMATAS_ATTR,
        DISABLED_ATTR,
        ONBLUR_ATTR,
        ONCHANGE_ATTR,
        ONFOCUS_ATTR,
        ONSELECT_ATTR,
        READONLY_ATTR,
        ROWS_ATTR,
        TABINDEX_ATTR,
    };

    // <input type=file>
    public static final String MAX_LENGTH_ATTR = "maxlength";
    public static final String[] INPUT_FILE_UPLOAD_ATTRIBUTES =
    {
        ACCEPT_ATTR,
        MAX_LENGTH_ATTR
    };
}
