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
package net.sourceforge.myfaces.renderkit.callback;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Interface for a Renderer, that wants to get called back before and after the
 * rendering of each of it's children.
 *
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public interface CallbackRenderer
{
   /**
    * Called before a child's Renderer's encodeBegin method is called.
    * @param facesContext
    * @param renderer       Renderer, that will do the encodeBegin
    * @param uiComponent
    * @throws IOException
    */
    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException;

    /**
     * Called after a child's Renderer's encodeEnd method has been called.
     * @param facesContext
     * @param renderer      Renderer, that just did the encodeEnd
     * @param uiComponent
     * @throws IOException
     */
    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException;
}
