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

import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ListboxRenderer
extends HtmlRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Listbox";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facescontext, UIComponent uicomponent)
    throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        int size = SelectItemUtil.getSelectItemsCount(facesContext, uiComponent);
        HTMLUtil.renderSelect(facesContext, uiComponent, TYPE, size);
    }
}
