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
package net.sourceforge.myfaces.application.component.ext;

import net.sourceforge.myfaces.renderkit.RenderUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dimitry D'hondt
 *
 * This class represents a single node on in the rendered
 * Tree control. A node can obviously have sub-nodes (and so on).
 * You can associate an arbitrary (model) object with a node,
 * so that you can easily refer to the (model) object that a node
 * is representing in the UI.
 * Each node is also represented by a description on the screen.
 * This description may be local to the component, or associated
 * with a JSF expression that refers to an arbitrary object (perhaps a 
 * property on a JavaBean in the session, etc...) 
 */
public class TreeNode implements Serializable {
	private List subNodes = new ArrayList();
	private String localLabel = "unnamed node";
	private String labelReference;
	private String valueReference;

	public TreeNode() {
	}

	/**
	 * Use this to retrieve the list of subnodes for this node.
	 * You may also use the returned list to add and/or remove nodes
	 * from this node.
	 * 
	 * @return
	 */
	public List getSubNodes() {
		return subNodes;
	}
	
	/**
	 * Replace the list of subnodes for this node.
	 * @param list
	 */
	public void setSubNodes(List list) {
		subNodes = list;
	}

	/**
	 * @return
	 */
	public String getLabelReference() {
		return labelReference;
	}

	/**
	 * If you supply a JSF expression here, the display
	 * label of this node will be determined by this reference expression.
	 * If you set this property to null, the localLabel property determines the 
	 * label on the screen.
	 *   
	 * @param string
	 */
	public void setLabelReference(String string) {
		labelReference = string;
	}

	/**
	 * Sets the value for the local label. The local label
	 * is used for displaying this node in the rendered Tree, if
	 * the labelReference property is set to null. (Which is the
	 * default.)
	 *  
	 * @param string
	 */
	public void setLabel(String string) {
		if(labelReference == null) {
			localLabel = string;
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ValueBinding vb = (ValueBinding) RenderUtils.getApplication().createValueBinding(labelReference);
			vb.setValue(ctx,string);
		}
	}

	/**
	 * @return
	 */
	public String getLabel() {
		String ret = null;
		
		if(labelReference == null) {
			ret = localLabel;
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ValueBinding vb = (ValueBinding) RenderUtils.getApplication().createValueBinding(labelReference);
			ret = vb.getValue(ctx).toString();
		}
		
		return ret;
	}
	
	/**
	 * @return
	 */
	public String getValueReference() {
		return valueReference;
	}

	/**
	 * @param string
	 */
	public void setValueReference(String string) {
		valueReference = string;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		Object ret = null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		ValueBinding vb = (ValueBinding) RenderUtils.getApplication().createValueBinding(labelReference);
		ret = vb.getValue(ctx);
		
		return ret;
	}

	/**
	 * @param object
	 */
	public void setValue(Object object) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ValueBinding vb = (ValueBinding) RenderUtils.getApplication().createValueBinding(labelReference);
		vb.setValue(ctx,object);
	}
}