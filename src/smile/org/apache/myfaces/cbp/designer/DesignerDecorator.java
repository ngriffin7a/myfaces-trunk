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
package net.sourceforge.myfaces.cbp.designer;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sourceforge.myfaces.application.component.ext.Screen;
import net.sourceforge.myfaces.renderkit.RenderUtil;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.ext.ScreenRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.lang.reflect.Method;


/**
 * @author Dimitry D'hondt
 *
 * Decorator for Renderers.
 */
public class DesignerDecorator implements MethodInterceptor {

	private static DesignerDecorator instance = new DesignerDecorator();
	
	private DesignerDecorator() {
        // disable public instantiation
	}
	
	public static DesignerDecorator getInstance() {
		return instance;
	}

	/**
	 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object ret = null;

        // during finalize() we do not have FacesContext
		if(!method.getName().endsWith(".finalize()") || !DesignerContext.inDesignMode()) {
			ret = proxy.invokeSuper(obj,args); 
		} else if(obj instanceof ScreenRenderer) {
			ret = proxy.invokeSuper(obj,args); 
		} else if(obj instanceof Renderer && method.getName().equals("encodeBegin")) {
			UIComponent component = (UIComponent) args[1];

			FacesContext ctx = FacesContext.getCurrentInstance();
			ResponseWriter out = ctx.getResponseWriter();
			
			int z=16000;
			UIComponent current = component;
			while(current.getParent() != null) {
				z++;
				current = current.getParent();
			}

			UIForm form = RenderUtil.getEnclosingForm(component);
			String formName = form.getClientId(ctx);
			
			out.startElement("div",null);
			out.writeAttribute("class","smiledesignmenuclass",null);
			out.writeAttribute("id","smiledesignmenu_" + component.getId(),null);
//			out.write("<span onmouseout=\"hideDesignerMenu('smiledesignmenu_" + component.getId() + "');\"");
			out.write("<table><tr><td>");
			out.write("<div><b>" + component.getId() + "</b></div>");
			out.write("<div><a href=\"#\" onclick=\"submitForm('" + formName + "');\">properties</a></div>");
			out.write("<div><a href=\"#\" onclick=\"hideDesignerMenu('smiledesignmenu_" + component.getId() + "');\">close</a></div>");
			out.write("</td></tr></table>");
//			out.write("</span>");
			out.endElement("div");

			out.startElement("div",null);
			out.writeAttribute("style","z-index: " + z,null);
			out.writeAttribute("id","smiledesignbutton_" + component.getId(),null);
			out.writeAttribute("class","smiledesignbuttonclass",null);
			out.writeAttribute("onclick","showDesignerMenu('smiledesignmenu_" + component.getId() + "','smiledesignspan_" + component.getId() + "');",null);
			out.write("<p></p>");
			out.endElement("div");
			out.startElement("span",null);
			out.writeAttribute("id","smiledesignspan_" + component.getId(),null);
			Screen screen = getScreen(component);
			if(screen!=null){
				StringBuffer script = getScreen(component).getEndScript();
				if(DesignerContext.inDesignMode()) {
					script.append("positionDesignerSpan('smiledesignbutton_" + component.getId() + "','smiledesignspan_" + component.getId() + "');\n");
				}
			}

			ret = proxy.invokeSuper(obj,args);
		} else if(obj instanceof Renderer && method.getName().equals("encodeEnd")) {
			// UIComponent component = (UIComponent) args[1];

			FacesContext ctx = FacesContext.getCurrentInstance();
			ResponseWriter out = ctx.getResponseWriter();
			
			ret = proxy.invokeSuper(obj,args); 
			out.endElement(HTML.SPAN_ELEM);
		} else if(method.getName().equals("decode")) {
			// Disable the decode operations in design mode. Don't want application specific event
			// handlers firing, etc....
			
			// Both component and renderer decode is disabled.
		} else {
			ret = proxy.invokeSuper(obj,args); 
		}

		return ret;
	}

	private Screen getScreen(UIComponent component) {
		Screen ret = null;
		
		UIComponent current = component.getParent();
		while(current != null) {
			if(current instanceof Screen) {
				ret = (Screen) current;
				break;
			}
			current = current.getParent();
		}
		
		return ret;
	}
}