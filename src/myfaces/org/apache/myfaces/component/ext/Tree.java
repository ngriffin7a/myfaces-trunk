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
package net.sourceforge.myfaces.component.ext;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.renderkit.RenderUtils;

/**
 * @author Dimitry D'hondt
 *
 * This component renders a tree view of a structure of nodes and subnodes.
 * For details about the individual nodes see the TreeNode class.
 * Event listeners may be attached to the Tree, to listen for events such as
 * selecting of a leaf node, expansion of a container node, etc..
 */
public class Tree extends UIComponentBase {

	private List rootNodes = new ArrayList();
	
	public Tree() {
		super();
		setRendererType("Tree");
	}

	/**
	 * @see javax.faces.component.UIPanel#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[3];
		values[0] = super.saveState(ctx);
		values[1] = rootNodes;
		return values;
	}
	
	/**
	 * @see javax.faces.component.UIPanel#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[])state;
		super.restoreState(ctx, values[0]);
		rootNodes = (List) values[1];
	}
	
	/**
	 * @return
	 */
	public List getRootNodes() {
		return rootNodes;
	}

	/**
	 * @param list
	 */
	public void setRootNodes(List list) {
		rootNodes = list;
	}
	
	/**
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		return RenderUtils.SMILE_FAMILY;
	}	
}