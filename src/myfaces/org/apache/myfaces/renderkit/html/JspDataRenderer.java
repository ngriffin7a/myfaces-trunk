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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspDataRenderer
    extends AbstractPanelRenderer
{
    public static final String TYPE = "Data";
    private static final String ITERATOR_ATTR = JspDataRenderer.class.getName() + ".iterator";

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {

        if (uicomponent.getParent().getRendererType().equals(ListRenderer.TYPE))
        {
            Object obj = uicomponent.getAttribute(ITERATOR_ATTR);
            if (obj == null)
            {
                // first call of encodeBegin
                incrementComponentCountAttr(context);
            }
            else
            {
                ResponseWriter writer = context.getResponseWriter();
                writer.write("</tr>\n");
            }

            Iterator it = getIterator(context, uicomponent);

            String varAttr = (String)uicomponent.getAttribute(DataRenderer.VAR_ATTR);
            if (it.hasNext())
            {
                context.setModelValue(varAttr, it.next());
                // new row
                openNewRow(context);
            }
            else
            {
                context.setModelValue(varAttr, null);
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent) throws IOException
    {
        if (uicomponent.getParent().getRendererType().equals(ListRenderer.TYPE))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write("</tr>\n");
        }
    }

    protected Iterator getIterator(FacesContext facesContext, UIComponent uiComponent)
    {
        Iterator iterator = (Iterator)uiComponent.getAttribute(ITERATOR_ATTR);
        if (iterator == null)
        {
            Object v = uiComponent.currentValue(facesContext);
            if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else if (v instanceof Collection)
            {
                iterator = ((Collection)v).iterator();
            }
            else if (v instanceof Object[])
            {
                iterator = Arrays.asList((Object[])v).iterator();
            }
            else if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else
            {
                throw new IllegalArgumentException("Value of component " + uiComponent.getCompoundId() + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

}


