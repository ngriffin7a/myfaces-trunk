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

import java.io.Serializable;

/**
 * This object represents a treenode in a component tree, it can be
 * serialized for state saving purposes.
 * 
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class TreeNode implements Serializable {
		
	/**
	 * The id of the component
	 */
	private String id;

	/**
	 * The parent id of this component, null if this is the root
	 */
	private String parentId;
	
	/**
	 * The fully qualified classname of this component
	 */
	private String className;
	
	/**
	 * The view id, only used for root objects
	 */
	private String viewId;
	
	/**
	 * The key from the facet map, only used for components object that are facets
	 */
	private String facetKey;
	
	/**
	 * Constructor for a viewRoot element
	 * @param id
	 * @param parentId
	 * @param className
	 */
	public TreeNode(String viewId, String id) {
		this.viewId = viewId;
		this.id = id;
		this.className = "javax.faces.component.UIViewRoot";
		this.parentId = null;
		this.facetKey = null;
	}
	
	/**
	 * Constructor for a component element
	 * @param id
	 * @param parentId
	 * @param className
	 */
	public TreeNode(String id, String parentId, String className) {
		this.viewId = null;
		this.id = id;
		this.parentId = parentId;
		this.className = className;
		this.facetKey = null;
	}
	
	/**
	 * Constructor for a facet element
	 * @param id
	 * @param parentId
	 * @param className
	 */
	public TreeNode(String id, String parentId, String className, String facetKey) {
		this.viewId = null;
		this.id = id;
		this.parentId = parentId;
		this.className = className;
		this.facetKey = facetKey;
	}
	
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public boolean isViewRoot() {
		if (parentId == null) {
			return true;
		}
		return false;
	}
	
	public boolean isFacet() {
		if (facetKey != null) {
			return true;
		}
		return false;
	}	

	/**
	 * @return Returns the facetKey.
	 */
	public String getFacetKey() {
		return facetKey;
	}

	/**
	 * @param facetKey The facetKey to set.
	 */
	public void setFacetKey(String facetKey) {
		this.facetKey = facetKey;
	}

	/**
	 * @return Returns the viewId.
	 */
	public String getViewId() {
		return viewId;
	}

	/**
	 * @param viewId The viewId to set.
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

}
