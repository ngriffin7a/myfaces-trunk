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


package net.sourceforge.myfaces.custom.tree;

import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;


/**
 * HTML image link.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:23  manolito
 *          tree component
 *
 */
public class HtmlTreeImageCommandLink
        extends HtmlCommandLink
{

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlTreeImageCommandLink";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.HtmlTreeImageCommandLink";

    private String image;


    public HtmlTreeImageCommandLink()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public String getFamily()
    {
        return "net.sourceforge.myfaces.HtmlTree";
    }


    public String getImage()
    {
        return image;
    }


    public void setImage(String image)
    {
        this.image = image;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = image;
        return ((Object)(values));
    }


    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        image = (String)values[1];
    }
}
