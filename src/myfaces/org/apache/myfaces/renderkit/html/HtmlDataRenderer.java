/**
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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.util.commons.ArrayIterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlDataRenderer
    extends HtmlRenderer
{
    public static final String ITERATOR_ATTR = HtmlDataRenderer.class.getName() + ".ITERATOR";

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIPanel.class);

        // Init Iterator
        Iterator it = getIterator(facesContext, (UIPanel) uiComponent);

        // Set ModelValue VAR_ATTR
        String varAttr = (String)uiComponent.getAttributes().get(JSFAttr.VAR_ATTR);

        Object paramValue = it != null && it.hasNext() ? it.next() : null;

        putParam(facesContext, varAttr, paramValue);
    }

    private static void putParam(FacesContext facesContext, String varAttr, Object paramValue)
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestParameters = externalContext.getRequestParameterMap();
        requestParameters.put(varAttr, paramValue);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        //Remove iterator after last row
        uiComponent.getAttributes().put(ITERATOR_ATTR, null);

        //Reset ModelValue VAR_ATTR
        String varAttr = (String)uiComponent.getAttributes().get(JSFAttr.VAR_ATTR);
        putParam(facesContext, varAttr, null);
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
    }

    public static Iterator getIterator(FacesContext facesContext, UIPanel uiPanel)
    {
        Iterator iterator = (Iterator)uiPanel.getAttributes().get(ITERATOR_ATTR);
        if (iterator == null)
        {
            Object v = uiPanel.getValue();

            if (v == null)
            {
                return null;
            }
            else if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else if (v instanceof Collection)
            {
                iterator = ((Collection)v).iterator();
            }
            else if (v.getClass().isArray())
            {
                iterator = new ArrayIterator(v);
            }
            else
            {
                throw new IllegalArgumentException("Value of component " + UIComponentUtils.toString(uiPanel) + " is neither collection nor array.");
            }
            uiPanel.getAttributes().put(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

}
