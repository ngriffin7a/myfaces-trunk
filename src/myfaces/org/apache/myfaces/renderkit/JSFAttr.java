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
package net.sourceforge.myfaces.renderkit;


/**
 * Constant declarations for JSF tags
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JSFAttr
{
    //~ Static fields/initializers -----------------------------------------------------------------

    // MyFaces Extended Attributes
    public static final String   ENABLED_ON_USER_ROLE_ATTR      = "enabledOnUserRole";
    public static final String   VISIBLE_ON_USER_ROLE_ATTR      = "visibleOnUserRole";
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   ALT_KEY_ATTR                   = "altKey";
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   ALT_BUNDLE_ATTR                = "altBundle";
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   TITLE_KEY_ATTR                 = "titleKey";
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   TITLE_BUNDLE_ATTR              = "titleBundle";
    // Key/Bundle attributes
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   KEY_ATTR                       = "key";
    /**@deprecated directly access attribute instead and use value binding mechanism */
    public static final String   BUNDLE_ATTR                    = "bundle";

    // Common Attributes
    public static final String   ID_ATTR                        = "id";
    public static final String   VALUE_ATTR                     = "value";
    public static final String   BINDING_ATTR                   = "binding";
    public static final String   STYLE_CLASS_ATTR               = "styleClass";
    public static final String   ESCAPE_ATTR                    = "escape";
    public static final String   TRANSIENT_ATTR                 = "transient";
    /**@deprecated */
    public static final String   DATE_STYLE_ATTR                = "dateStyle";
    /**@deprecated */
    public static final String   FORMAT_PATTERN_ATTR            = "formatPattern";
    /**@deprecated */
    public static final String   NUMBER_STYLE_ATTR              = "numberStyle";

    // Common Class Attributes
    /**@deprecated */
    public static final String   SELECT_BOOLEAN_CLASS_ATTR      = "selectBooleanClass";
    /**@deprecated */
    public static final String   SELECT_MANY_CLASS_ATTR         = "selectManyClass";
    /**@deprecated */
    public static final String   SELECT_ONE_CLASS_ATTR          = "selectOneClass";
    /**@deprecated */
    public static final String   FORM_CLASS_ATTR                = "formClass";
    /**@deprecated */
    public static final String   GRAPHIC_CLASS_ATTR             = "graphicClass";

    // Common Output Attributes
    /**@deprecated */
    public static final String   OUTPUT_CLASS_ATTR              = "outputClass";
    public static final String   FOR_ATTR                       = "for";
    public static final String   CONVERTER_ATTR                 = "converter";

    // Ouput_Time Attributes
    public static final String   TIME_STYLE_ATTR                = "timeStyle";
    public static final String   TIMEZONE_ATTR                  = "timezone";

    // Common Input Attributes
    /**@deprecated */
    public static final String   INPUT_CLASS_ATTR               = "inputClass";
    /**@deprecated */
    public static final String   MAXLENGTH_ATTR                 = "maxlength";
    public static final String   REQUIRED_ATTR                  = "required";
    public static final String   VALIDATOR_ATTR                 = "validator";

    // Input_Secret Attributes
    public static final String   REDISPLAY_ATTR                 = "redisplay";

    // Input_Checkbox Attributes
    public static final String   LAYOUT_ATTR                    = "layout";

    // Select_Menu Attributes
    public static final String   SIZE_ATTR                     = "size";

    // SelectMany Checkbox List/ Select One Radio Attributes
    public static final String BORDER_ATTR                 = "border";
    public static final String DISABLED_CLASS_ATTR         = "disabledClass";
    public static final String ENABLED_CLASS_ATTR          = "enabledClass";

    // Common Command Attributes
    /**@deprecated */
    public static final String   COMMAND_CLASS_ATTR           = "commandClass";
    public static final String   LABEL_ATTR                   = "label";
    public static final String   IMAGE_ATTR                   = "image";
    public static final String   ACTION_ATTR                 = "action";
    public static final String   IMMEDIATE_ATTR              = "immediate";


    // Command_Button Attributes
    public static final String   TYPE_ATTR                    = "type";

    // Common Panel Attributes
    /**@deprecated */
    public static final String   PANEL_CLASS_ATTR       = "panelClass";
    public static final String   FOOTER_CLASS_ATTR      = "footerClass";
    public static final String   HEADER_CLASS_ATTR      = "headerClass";
    public static final String   COLUMN_CLASSES_ATTR    = "columnClasses";
    public static final String   ROW_CLASSES_ATTR       = "rowClasses";

    // Panel_Grid Attributes
    public static final String   COLUMNS_ATTR          = "columns";
    public static final String   COLSPAN_ATTR          = "colspan"; // extension

    // Panel_Data Attributes
    public static final String VAR_ATTR                = "var";

    // UIMessage and UIMessages attributes
    public static final String SHOW_SUMMARY_ATTR            = "showSummary";
    public static final String SHOW_DETAIL_ATTR             = "showDetail";
    public static final String GLOBAL_ONLY_ATTR             = "globalOnly";

    // HtmlOutputMessage attributes
    public static final String ERROR_CLASS_ATTR            = "errorClass";
    public static final String ERROR_STYLE_ATTR            = "errorStyle";
    public static final String FATAL_CLASS_ATTR            = "fatalClass";
    public static final String FATAL_STYLE_ATTR            = "fatalStyle";
    public static final String INFO_CLASS_ATTR             = "infoClass";
    public static final String INFO_STYLE_ATTR             = "infoStyle";
    public static final String WARN_CLASS_ATTR             = "warnClass";
    public static final String WARN_STYLE_ATTR             = "warnStyle";
    public static final String TITLE_ATTR                  = "title";
    public static final String TOOLTIP_ATTR                = "tooltip";

    // GraphicImage attributes
    public static final String URL_ATTR                    = "url";

    //~ Constructors -------------------------------------------------------------------------------

    protected JSFAttr()
    {
        // hide from public view
    }
}
