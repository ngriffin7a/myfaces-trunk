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

import javax.faces.component.AttributeDescriptor;
import java.util.Set;
import java.util.HashSet;

/**
 * "Mixin" interface that defines the commonly used renderer attributes as defined in JSF.7.6
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface CommonRendererAttributes
{
    public static final String COLUMN_CLASSES_ATTR = "columnClasses";
    public static final String CONVERTER_ATTR = "converter";
    public static final String DATE_STYLE_ATTR = "dateStyle";
    public static final String FORMAT_PATTERN_ATTR = "formatPattern";
    public static final String NUMBER_STYLE_ATTR = "numberStyle";
    public static final String ROW_CLASSES_ATTR = "rowClasses";
    public static final String TIME_STYLE_ATTR = "timeStyle";
    public static final String TIMEZONE_ATTR = "timezone";

    //xxxClass
    public static final String INPUT_CLASS_ATTR = "inputClass";
    public static final String OUTPUT_CLASS_ATTR = "outputClass";
    public static final String COMMAND_CLASS_ATTR = "commandClass";
    //TODO: use AttributeDescription ...
    public static final AttributeDescriptor PANEL_CLASS_ATTR = new AttrDescrImpl("panelClass", String.class);
    //TODO: continue definitions...




    //universal attributes
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

    //event-hanlder attributes
    public static final String ONCLICK_ATTR = "onclick";
    public static final String ONDBLCLICK_ATTR = "ondblclick";
    public static final String ONMOUSEDOWN_ATTR = "onmousedown";
    public static final String ONMOUSEUP_ATTR = "onmouseup";
    public static final String ONMOUSEOVER_ATTR = "onmouseover";
    public static final String ONMOUSEMOVE_ATTR = "onmousemove";
    public static final String ONMOUSEOUT_ATTR = "onmouseout";
    public static final String ONKEYPRESS_ATTR = "onkeypress";
    public static final String ONKEYDOWN_ATTR = "onkeydown";
    public static final String ONKEYUP_ATTR = "onkeyup";
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
}
