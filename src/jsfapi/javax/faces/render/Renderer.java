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
package javax.faces.render;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class Renderer
{
    public void decode(FacesContext context,
                       UIComponent component)
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public void encodeBegin(FacesContext context,
                            UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public void encodeChildren(FacesContext context,
                               UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public void encodeEnd(FacesContext context,
                          UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public String convertClientId(FacesContext context,
                                  String clientId)
    {
        if (context == null) throw new NullPointerException("context");
        if (clientId == null) throw new NullPointerException("clientId");
        return clientId;
    }

    public boolean getRendersChildren()
    {
        return false;
    }

    public Object getConvertedValue(FacesContext context,
                                    UIComponent component,
                                    Object submittedValue)
            throws ConverterException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
        return submittedValue;
    }

}
