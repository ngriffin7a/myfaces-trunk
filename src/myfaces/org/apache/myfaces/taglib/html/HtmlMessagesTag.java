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
package net.sourceforge.myfaces.taglib.html;

import javax.faces.component.html.HtmlMessages;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/04/05 11:04:56  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.9  2004/04/01 12:57:43  manolito
 * additional extended component classes for user role support
 *
 */
public class HtmlMessagesTag
        extends HtmlMessagesTagBase
{
    public String getComponentType()
    {
        return HtmlMessages.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "javax.faces.Messages";
    }
}
