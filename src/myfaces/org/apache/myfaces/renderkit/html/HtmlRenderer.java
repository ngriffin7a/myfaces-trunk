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
package net.sourceforge.myfaces.renderkit.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class HtmlRenderer
        extends Renderer
{
    //~ Static fields/initializers -----------------------------------------------------------------
    private static final Log log = LogFactory.getLog(HtmlRenderer.class);

    //~ Methods ------------------------------------------------------------------------------------
    public void decode(FacesContext facescontext, UIComponent uicomponent)
    {
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
    throws IOException
    {
        if (getRendersChildren())
        {
            log.warn("Renderer " + getClass().getName() + " renders its children and should therefore implement encodeChildren.");
        }
        else
        {
            log.warn("Renderer " + getClass().getName() + " does not render its children. Method encodeChildren should not be called.");
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
    }


}
