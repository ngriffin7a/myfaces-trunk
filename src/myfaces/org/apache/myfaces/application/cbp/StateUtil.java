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
package net.sourceforge.myfaces.application.cbp;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Utility class for state saving
 * 
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class StateUtil {

	public static TreeNode createTreeNode(UIComponent component,FacesContext ctx) {
		TreeNode node = null;
		if (component instanceof UIViewRoot) {
			UIViewRoot viewRoot = (UIViewRoot)component;
			node = new TreeNode(viewRoot.getViewId(),viewRoot.getId());
		} else {
			if (component.getId() == null) {
				component.setId(component.getClientId(ctx));
			}
			String id = component.getId();
			UIComponent parent = component.getParent();
			String parentId = parent.getId();
			String className = component.getClass().getName();
			node = new TreeNode(id,parentId,className);
		}
		return node;
	}
	
	public static TreeNode createFacetTreeNode(UIComponent component, String facetKey, FacesContext ctx) {
		TreeNode node = null;
		if (component.getId() == null) {
			component.setId(component.getClientId(ctx));
		}
		node = new TreeNode(component.getId(),component.getParent().getId(),component.getClass().getName(),facetKey);
		return node;
	}	
}
