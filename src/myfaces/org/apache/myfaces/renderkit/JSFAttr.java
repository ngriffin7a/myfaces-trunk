/*
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
    public static final String[] USER_ROLE_ATTRIBUTES           =
    {ENABLED_ON_USER_ROLE_ATTR, VISIBLE_ON_USER_ROLE_ATTR};
    public static final String   ALT_KEY_ATTR                   = "altKey";
    public static final String   ALT_BUNDLE_ATTR                = "altBundle";
    public static final String   TITLE_KEY_ATTR                 = "titleKey";
    public static final String   TITLE_BUNDLE_ATTR              = "titleBundle";

    // Key/Bundle attributes
    public static final String   KEY_ATTR                       = "key";
    public static final String   BUNDLE_ATTR                    = "bundle";

    // Common Attributes
    public static final String   ID_ATTR                        = "id";
    public static final String   CONVERTER_ATTR                 = "converter";
    public static final String   DATE_STYLE_ATTR                = "dateStyle";
    public static final String   FORMAT_PATTERN_ATTR            = "formatPattern";
    public static final String   NUMBER_STYLE_ATTR              = "numberStyle";

    // Common Class Attributes
    public static final String   SELECT_BOOLEAN_CLASS_ATTR      = "selectBooleanClass";
    public static final String   SELECT_MANY_CLASS_ATTR         = "selectManyClass";
    public static final String   SELECT_ONE_CLASS_ATTR          = "selectOneClass";
    public static final String   FORM_CLASS_ATTR                = "formClass";
    public static final String   GRAPHIC_CLASS_ATTR             = "graphicClass";

    // Form Attributes
    public static final String[] FORM_ATTRIBUTES                = {FORM_CLASS_ATTR};

    // Common Output Attributes
    public static final String   OUTPUT_CLASS_ATTR              = "outputClass";
    public static final String   FOR_ATTR                       = "for";

    // Ouput_Label Attributes
    public static final String[] OUTPUT_LABEL_ATTRIBUTES        =
    {OUTPUT_CLASS_ATTR, FOR_ATTR, KEY_ATTR, BUNDLE_ATTR};

    // Ouput_Time Attributes
    public static final String   TIME_STYLE_ATTR                = "timeStyle";
    public static final String   TIMEZONE_ATTR                  = "timezone";
    public static final String[] OUTPUT_TIME_ATTRIBUTES         =
    {OUTPUT_CLASS_ATTR, TIME_STYLE_ATTR, TIMEZONE_ATTR};

    // Ouput_Errors Attributes
    public static final String[] OUTPUT_ERRORS_ATTRIBUTES       = {OUTPUT_CLASS_ATTR, FOR_ATTR};

    // Output_Message Attributes
    public static final String[] OUTPUT_MESSAGE_ATTRIBUTES      =
    {OUTPUT_CLASS_ATTR, KEY_ATTR, BUNDLE_ATTR, CONVERTER_ATTR};

    // Graphic_Image Attributes
    public static final String[] GRAPHIC_IMAGE_ATTRIBUTES       =
    {GRAPHIC_CLASS_ATTR, KEY_ATTR, BUNDLE_ATTR, ALT_KEY_ATTR, ALT_BUNDLE_ATTR};

    // Common Input Attributes
    public static final String   INPUT_CLASS_ATTR               = "inputClass";
    public static final String   MAXLENGTH_ATTR                 = "maxlength";

    // Input_Secret Attributes
    public static final String   REDISPLAY_ATTR                 = "redisplay";
    public static final String[] INPUT_SECRET_ATTRIBUTES        =
    {INPUT_CLASS_ATTR, MAXLENGTH_ATTR, REDISPLAY_ATTR};

    // Input_Hidden Attributes
    public static final String[] INPUT_HIDDEN_ATTRIBUTES        =
    {INPUT_CLASS_ATTR, CONVERTER_ATTR};

    // Input_Checkbox Attributes
    public static final String   LAYOUT_ATTR                    = "layout";

    // Select_Listbox Attributes
    public static final String[] SELECT_MANY_LISTBOX_ATTRIBUTES = {SELECT_MANY_CLASS_ATTR};
    public static final String[] SELECT_ONE_LISTBOX_ATTRIBUTES = {SELECT_ONE_CLASS_ATTR};

    // Select_Menu Attributes
    public static final String   SIZE_ATTR                     = "size";
    public static final String[] SELECT_MANY_MENU_ATTRIBUTES  = {SELECT_MANY_CLASS_ATTR, SIZE_ATTR};
    public static final String[] SELECT_ONE_MENU_ATTRIBUTES   = {SELECT_ONE_CLASS_ATTR, SIZE_ATTR};

    // Common Command Attributes
    public static final String   COMMAND_CLASS_ATTR           = "commandClass";
    public static final String   LABEL_ATTR                   = "label";
    public static final String   IMAGE_ATTR                   = "image";

    // Command_Button Attributes
    public static final String   TYPE_ATTR                    = "type";
    public static final String[] COMMAND_BUTTON_ATTRIBUTES    =
    {COMMAND_CLASS_ATTR, LABEL_ATTR, TYPE_ATTR, KEY_ATTR, BUNDLE_ATTR};

    // Command_Hyperlink Attributes
    public static final String[] COMMAND_HYPERLINK_ATTRIBUTES =
    {COMMAND_CLASS_ATTR, LABEL_ATTR, KEY_ATTR, BUNDLE_ATTR};

    // Common Panel Attributes
    public static final String   PANEL_CLASS_ATTR       = "panelClass";
    public static final String   FOOTER_CLASS_ATTR      = "footerClass";
    public static final String   HEADER_CLASS_ATTR      = "headerClass";
    public static final String   COLUMN_CLASSES_ATTR    = "columnClasses";
    public static final String   ROW_CLASSES_ATTR       = "rowClasses";

    // Panel_Group Attributes
    public static final String[] PANEL_GROUP_ATTRIBUTES = {PANEL_CLASS_ATTR};

    // Panel_List Attributes
    public static final String[] PANEL_LIST_ATTRIBUTES =
    {PANEL_CLASS_ATTR, COLUMN_CLASSES_ATTR, FOOTER_CLASS_ATTR, HEADER_CLASS_ATTR, ROW_CLASSES_ATTR};

    // Panel_Grid Attributes
    public static final String   COLUMNS_ATTR          = "columns";
    public static final String[] PANEL_GRID_ATTRIBUTES =
    {
        PANEL_CLASS_ATTR, COLUMN_CLASSES_ATTR, COLUMNS_ATTR, FOOTER_CLASS_ATTR, HEADER_CLASS_ATTR,
        ROW_CLASSES_ATTR
    };

    // Panel_Data Attributes
    public static final String VAR_ATTR                = "var";

    //~ Constructors -------------------------------------------------------------------------------

    private JSFAttr()
    {
    }
}
