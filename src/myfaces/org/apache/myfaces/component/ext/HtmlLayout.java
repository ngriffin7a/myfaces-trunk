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
package net.sourceforge.myfaces.component.ext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.myfaces.exception.InvalidAttributeException;
import net.sourceforge.myfaces.renderkit.RenderUtils;

/**
 * @author codehawk
 *
 * HTML free form layout component.
 * This component can be used to template the look and feel
 * of your site in a simple manner. You supply an HTML template
 * directly, or the name of a resource on the classpath
 * that contains the HTML layout.
 * Inside the Layout template tags are used to insert the child components
 * when rendering :
 * 
 * <code><pre>
 * <HTML>... <BODY>... <!-- CHILD:1 --> ... <table><tr><td><!-- CHILD:2 --></td></tr></table>
 * </pre></code>
 * 
 * Where the comment CHILD:1 and CHILD:2 will get replaced with the render output of the
 * first and second child component of this layout manager.
 */
public class HtmlLayout extends UIComponentBase {

	private String html;
	private String resourceName;
	private String family = RenderUtils.SMILE_FAMILY;

	public HtmlLayout() {
		super();
	}
	
	
	/**
	 * @return
	 */
	public String getTemplate() {
		String ret = "";
		
		ret = getHtml();
		if(ret == null && getResourceName() != null) {
			InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream(getResourceName());
			if(htmlStream != null) {
				try {
					InputStreamReader htmlReader = new InputStreamReader(htmlStream);
					StringBuffer html = new StringBuffer();
					int nr = 0;
					char[] buffer = new char[1024];
					while(nr != -1) {
						nr = htmlReader.read(buffer);
						if(nr != -1) {
							html.append(buffer,0,nr);
						}
					}
					setHtml(html.toString());
					ret = getHtml();
				} catch(IOException e) {
					throw new InvalidAttributeException("Attribute <resourceName> for component <" + getId() + ">. Could not load the html from named resource <" + getResourceName() + ">");
				}
			}
		}
		
		return ret;
	}

	/**
	 * Need to render my children myself. Typical for layout 
	 * management components.
	 * 
	 * @see javax.faces.component.UIComponent#getRendersChildren()
	 */
	public boolean getRendersChildren() {
		return true;
	}

	/**
	 * @see javax.faces.component.UIComponent#encodeChildren(javax.faces.context.FacesContext)
	 */
	public void encodeChildren(FacesContext ctx) throws IOException {
		ResponseWriter out = ctx.getResponseWriter();
		RenderUtils.ensureAllTagsFinished();
		
		String[] parts = getTemplate().split("<!-- CHILD:");
		out.write(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			String part = parts[i];
			String[] t = part.split(" -->",2);
			
			int childNumber = Integer.parseInt(t[0]) - 1;
			
			if(childNumber < getChildren().size()) {
				UIComponent currentChild = (UIComponent) getChildren().get(childNumber);
				RenderUtils.renderChild(ctx,currentChild);
			}
			
			out.write(t[1]);
		}
	}

	/**
	 * @see javax.faces.component.UIPanel#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[3];
		values[0] = super.saveState(ctx);
		values[1] = getHtml();
		values[2] = getResourceName();
		return values;
	}
	
	/**
	 * @see javax.faces.component.UIPanel#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[])state;
		super.restoreState(ctx, values[0]);
		setHtml((String)values[1]);
		setResourceName((String)values[2]);
	}

	/**
	 * @return
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param string
	 */
	public void setHtml(String string) {
		html = string;
	}


	/**
	 * @return
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * @param string
	 */
	public void setResourceName(String string) {
		resourceName = string;
	}


	/**
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		return family;
	}

//	/**
//	 * @see javax.faces.component.UIComponent#decode(javax.faces.context.FacesContext)
//	 */
//	public void decode(FacesContext ctx) {
//		List children = getChildren();
//		for (Iterator iter = children.iterator(); iter.hasNext();) {
//			UIComponent comp = (UIComponent) iter.next();
//			comp.decode(ctx);
//		}
//	}

}
