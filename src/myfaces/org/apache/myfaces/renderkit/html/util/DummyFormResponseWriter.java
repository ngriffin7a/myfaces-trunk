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
package net.sourceforge.myfaces.renderkit.html.util;

/**
 * Myfaces extension for ResponseWriters that support the automatical rendering
 * of a dummy form when endDocument is called.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface DummyFormResponseWriter
{
    /**
     * Switch on/off the writing of a dummy form in endDocument.
     * @param writeDummyForm
     */
    public void setWriteDummyForm(boolean writeDummyForm);

    /**
     * @return name of the dummy form, that will be rendered
     */
    public String getDummyFormName();

    /**
     * Adds a parameter that will be rendered as a hidden input within the dummy form.
     */
    public void addDummyFormParameter(String paramName);
}
