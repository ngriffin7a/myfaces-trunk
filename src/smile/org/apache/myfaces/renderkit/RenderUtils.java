/**
 * Smile, the open-source JSF implementation.
 * Copyright (C) 2003  The smile team (http://smile.sourceforge.net)
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

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author codehawk
 *
 * Helper operations for the renderers.
 */
public class RenderUtils {
	
	public static String SMILE_FAMILY="smile";
	
	public static void checkInput(FacesContext ctx, UIComponent component) {
		if (ctx == null) {
			throw new NullPointerException("FacesContext cannot be null");
		}
		if (component == null) {
			throw new NullPointerException("UIComponent cannot be null");
		}
	}
	
	/**
	 * Lookup a resource, if the bundle attribute is not found on the component, the bundle of the root component is used.
	 * @param bundleAttr 
	 * @param component
	 * @param context
	 * @param keyAttr 
	 * @throws MissingResourceException
	 */
	public static String getKeyAndLookupInBundle(FacesContext context, UIComponent component, String keyAttr, String bundleAttr)
	throws MissingResourceException
	{
		String key = null;
		String bundleName = null;
		ResourceBundle bundle = null;
		if (context == null) {
			throw new NullPointerException("context cannot be null");
		}
		if (component == null) {
			throw new NullPointerException("component cannot be null");
		}	
		if (keyAttr == null) {
			throw new NullPointerException("keyAttr cannot be null");
		}
		if (bundleAttr == null) {
			throw new NullPointerException("bundleAttr cannot be null");
		}		
		key = net.sourceforge.myfaces.renderkit.RenderUtil.getStringAttribute(component,keyAttr);
		bundleName = net.sourceforge.myfaces.renderkit.RenderUtil.getStringAttribute(component,bundleAttr);
		if(bundleName == null)
		{
			UIViewRoot root = context.getViewRoot();
			bundleName = net.sourceforge.myfaces.renderkit.RenderUtil.getStringAttribute(root,bundleAttr);
		}
		if(null == key || null == bundleName)
			throw new MissingResourceException("Resource cannot be found", bundleName, key);
		else
			return bundle.getString(key);
	}		
	
	/**
	 * Renders a child component for the current component. This operation is handy when implementing
	 * renderes that perform child rendering themselves (eg. a layout renderer/grid renderer/ etc..).
	 * Passes on any IOExceptions thrown by the child/child renderer.
	 * 
	 * @param context the current FacesContext
	 * @param child which child to render
	 */
	public static void renderChild(FacesContext context, UIComponent child) throws IOException {
		child.encodeBegin(context);
		child.encodeChildren(context);
		child.encodeEnd(context);
	}

	/**
	 * This operation ensures that all previous tags
	 * are written out completly. A subcomponent
	 * may be writing inside the content of a parent's tag
	 * To avoid this, it's advisable to call this utility at the start
	 * of all component renderes ! 
	 *
	 */	
	public static void ensureAllTagsFinished() throws IOException {
		FacesContext.getCurrentInstance().getResponseWriter().writeText(new char[] {},0,0);
	}
	
	/**
	 * Renders out a hidden input field, useful for posting back all sorts of information.
	 * @param parameterName
	 * @throws IOException
	 */
	public static void renderHiddenField(String parameterName) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ResponseWriter out = ctx.getResponseWriter();
		
		out.startElement("input",null);
		out.writeAttribute("type","hidden",null);
		out.writeAttribute("name",parameterName,null);
		out.writeAttribute("id",parameterName,null);
		out.endElement("input");
	}
	
	/**
	 * Convenience method to retrieve the JSF application.
	 * @return
	 */
	public static Application getApplication() {
		Application ret = null;
		ApplicationFactory appFactory = null;

		appFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
		ret = appFactory.getApplication();
		
		return ret;
	}
	
	/**
	 * Determines the client id of the form in which a component is enclosed.
	 * Useful for generateing submitForm('xyz') javascripts...
	 * 
	 * @return
	 */
	public static String determineFormName(UIComponent component) {
		String ret = null;
		UIComponent current = component.getParent();
		UIComponent form = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		while(current != null) {
			if(current instanceof UIForm) {
				form = current;
				break;
			}
			current = current.getParent();
		}
		
		if(form != null) {
			ret = form.getClientId(ctx);
		}
		
		return ret;
	}
}
