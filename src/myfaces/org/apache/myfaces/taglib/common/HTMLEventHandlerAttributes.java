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
package net.sourceforge.myfaces.taglib.common;

/**
 * Convenient mixin interface to make sure that there is a setter for each of
 * the "html_event_handler_attributes"
 * (see entity declaration in myfaces_html.tld).
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface HTMLEventHandlerAttributes
{
    public void setOnclick(String v);
    public void setOndblclick(String v);
    public void setOnmousedown(String v);
    public void setOnmouseup(String v);
    public void setOnmouseover(String v);
    public void setOnmousemove(String v);
    public void setOnmouseout(String v);
    public void setOnkeypress(String v);
    public void setOnkeydown(String v);
    public void setOnkeyup(String v);
}
