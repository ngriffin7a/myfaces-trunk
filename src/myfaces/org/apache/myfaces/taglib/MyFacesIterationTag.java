/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public abstract class MyFacesIterationTag
        extends MyFacesTag
        implements IterationTag
{
    public final int doAfterBody() throws JspException
    {
        FacesContext facesContext = getFacesContext();
        UIComponent renderingParent = (UIComponent)facesContext.getServletRequest().getAttribute(RENDERING_PARENT);
        if (renderingParent != null)
        {
            //a parent is rendering, so child must not render itself
            return Tag.SKIP_BODY;
        }

        return doAfterAfterBody();
    }


    public int doAfterAfterBody() throws JspException
    {
        return Tag.SKIP_BODY;
    }
}
