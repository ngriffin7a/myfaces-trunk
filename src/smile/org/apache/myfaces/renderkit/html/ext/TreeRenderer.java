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
package net.sourceforge.myfaces.renderkit.html.ext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dimitry D'hondt
 * 
 * The Tree Renderer
 */
public class TreeRenderer extends Renderer {

	private static Log log = LogFactory.getLog(TreeRenderer.class);

	public TreeRenderer() {
	}

	/**
	 * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void decode(FacesContext ctx, UIComponent component) {
		if (component instanceof net.sourceforge.myfaces.application.component.ext.Tree) {
		} else {	
			throw new IllegalStateException(
				"Invalid component type '" + component.getClass().getName() + "' for TreeRenderer. Component '" + component.getId() + "'.");
		}
	}

	/**
	 * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeEnd(FacesContext ctx, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
		ResponseWriter out = ctx.getResponseWriter();
		String clientId = component.getClientId(ctx);
		
		if (component instanceof net.sourceforge.myfaces.application.component.ext.Tree) {
			net.sourceforge.myfaces.application.component.ext.Tree tree = (net.sourceforge.myfaces.application.component.ext.Tree) component;

			out.startElement("table",tree);
			out.writeAttribute("border","0",null);
			out.writeAttribute("cellspacing","0",null);
			out.writeAttribute("cellpadding","0",null);
			out.writeAttribute("class","smile.tree.outertable",null);
			// TODO: Dynamically determine maximum depth to render.
			renderNodes(tree.getRootNodes(),out,tree,1,2);
			out.endElement("table");
			
		} else {
			throw new IllegalStateException(
				"Invalid component type '" + component.getClass().getName() + "' for ToolbarRenderer. Component '" + component.getId() + "'.");
		}
	}
	
	private void renderNodes(List nodes, ResponseWriter out, net.sourceforge.myfaces.application.component.ext.Tree tree, int level, int maxlevel) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		int nodeNr = 1;
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			net.sourceforge.myfaces.application.component.ext.TreeNode node = (net.sourceforge.myfaces.application.component.ext.TreeNode) iter.next();
			int childCount = node.getSubNodes().size();
			String childrenId = tree.getClientId(ctx) + "_" + level + "_" + nodeNr; 
			
			out.startElement("tr",tree);
			if(childCount > 0) {
				out.startElement("td",tree);
				out.startElement("img",tree);
				out.writeAttribute("src","images/tree_+.png",null);
				out.writeAttribute("onclick","treeExpandCollapse('" + childrenId + "')",null);
				out.endElement("img");
				out.endElement("td");

			} else {
				out.startElement("td",tree);
				out.startElement("img",tree);
				out.writeAttribute("src","images/tree_I-.png",null);
				out.endElement("img");
				out.endElement("td");

			}
			out.startElement("td",tree);
			out.write(node.getLabel());
			out.endElement("td");
			out.endElement("tr");

			if(childCount > 0) { 
				out.startElement("tr",tree);
				out.startElement("td",tree);
				for(int i=0; i<childCount; i++) {
					out.startElement("img",tree);
					out.writeAttribute("src","images/tree_I.png",null);
					out.endElement("img");
					if(i<childCount-1) {
						out.startElement("br",tree);
						out.endElement("br");
					}
				}
				out.endElement("td");
				
				out.startElement("td",tree);
				out.startElement("table",tree);
				out.writeAttribute("id",childrenId,null);
				out.writeAttribute("border","0",null);
				out.writeAttribute("cellspacing","0",null);
				out.writeAttribute("cellpadding","0",null);
				renderNodes(node.getSubNodes(),out,tree,level+1,maxlevel);
				out.endElement("table");
				out.endElement("td");
				out.endElement("tr");
			}			
			nodeNr++;
		}
	}

	/**********************************************************************************/

	/**
	 * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeBegin(FacesContext facescontext, UIComponent uicomponent) throws IOException {
	}

	/**
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException {
	}
}
