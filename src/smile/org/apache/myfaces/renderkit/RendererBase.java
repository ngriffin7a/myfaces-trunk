/**
 * Smile, the open-source JSF implementation.
 * Copyright (C) 2004  The smile team (http://smile.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.MissingResourceException;

/**
 * Base class for smile renderers
 * 
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class RendererBase extends Renderer {

	protected void checkInput(FacesContext ctx, UIComponent component) {
		RenderUtils.checkInput(ctx,component);
	}
	
	/**
	 * Lookup a resource, if the bundle attribute is not found on the component, the bundle of the root component is used.
	 * @param bundleAttr 
	 * @param component
	 * @param context
	 * @param keyAttr 
	 * @throws MissingResourceException
	 */
	protected String getKeyAndLookupInBundle(FacesContext context, UIComponent component, String keyAttr, String bundleAttr)
	throws MissingResourceException
	{
		return RenderUtils.getKeyAndLookupInBundle(context,component,keyAttr,bundleAttr);
	}		
	
	/**
	 * Renders a child component for the current component. This operation is handy when implementing
	 * renderes that perform child rendering themselves (eg. a layout renderer/grid renderer/ etc..).
	 * Passes on any IOExceptions thrown by the child/child renderer.
	 * 
	 * @param context the current FacesContext
	 * @param child which child to render
	 */
	protected void renderChild(FacesContext context, UIComponent child) throws IOException {
		RenderUtils.renderChild(context,child);
	}
}
