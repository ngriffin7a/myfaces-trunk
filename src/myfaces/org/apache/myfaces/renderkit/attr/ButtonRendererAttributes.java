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
 * see JSF 7.4.1
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface ButtonRendererAttributes
    extends CommonRendererAttributes, KeyBundleAttributes
{
    public static final String LABEL_ATTR   = "label";
    public static final String TYPE_ATTR    = "type";

    public static final String IMAGE_ATTR = "image";    //TODO: Taglib ?


    public static final String[] COMMAND_BUTTON_ATTRIBUTES = {
        COMMAND_CLASS_ATTR,
        LABEL_ATTR,
        TYPE_ATTR,
        KEY_ATTR,
        BUNDLE_ATTR
    };
}
