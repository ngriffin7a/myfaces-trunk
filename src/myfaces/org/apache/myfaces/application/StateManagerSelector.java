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
package net.sourceforge.myfaces.application;

import java.io.IOException;

import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.application.cbp.StateManagerImpl;
import net.sourceforge.myfaces.application.jsp.JspStateManagerImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dimitry D'hondt
 *
 * This class selects between the CBP and the JSP implementation of the
 * state manager.
 */
public class StateManagerSelector extends MyfacesStateManager {

	private static Log log = LogFactory.getLog(StateManagerSelector.class);
	private StateManager cbpManager = null;
	private MyfacesStateManager jspManager = null;

	public StateManagerSelector() {
		cbpManager = new StateManagerImpl();
		jspManager = new JspStateManagerImpl();
	}

	/**
	 * @see javax.faces.application.StateManager#getComponentStateToSave(javax.faces.context.FacesContext)
	 */
	protected Object getComponentStateToSave(FacesContext context) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.application.StateManager#getTreeStructureToSave(javax.faces.context.FacesContext)
	 */
	protected Object getTreeStructureToSave(FacesContext context) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.application.StateManager#isSavingStateInClient(javax.faces.context.FacesContext)
	 */
	public boolean isSavingStateInClient(FacesContext context) {
		boolean ret = false;
		
		if(SelectorUtils.isCurrentPageJSP()) {
			ret = jspManager.isSavingStateInClient(context);
		} else {
			ret = cbpManager.isSavingStateInClient(context);
		}
		
		return ret;
	}

	/**
	 * @see javax.faces.application.StateManager#restoreComponentState(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot, java.lang.String)
	 */
	protected void restoreComponentState(FacesContext context, UIViewRoot viewRoot, String renderKitId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.application.StateManager#restoreTreeStructure(javax.faces.context.FacesContext, java.lang.String, java.lang.String)
	 */
	protected UIViewRoot restoreTreeStructure(FacesContext context, String viewId, String renderKitId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.application.StateManager#restoreView(javax.faces.context.FacesContext, java.lang.String, java.lang.String)
	 */
	public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
		UIViewRoot ret = null;
		
		if(SelectorUtils.isCurrentPageJSP()) {
			ret = jspManager.restoreView(context, viewId, renderKitId);
		} else {
			ret = cbpManager.restoreView(context, viewId, renderKitId);
		}
		
		return ret;
	}

	/**
	 * @see javax.faces.application.StateManager#saveSerializedView(javax.faces.context.FacesContext)
	 */
	public SerializedView saveSerializedView(FacesContext context) {
		SerializedView ret = null;
		
		if(SelectorUtils.isCurrentPageJSP()) {
			ret = jspManager.saveSerializedView(context);
		} else {
			ret = cbpManager.saveSerializedView(context);
		}
		
		return ret; 
	}

	/**
	 * @see javax.faces.application.StateManager#writeState(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)
	 */
	public void writeState(FacesContext context, SerializedView state) throws IOException {
		if(SelectorUtils.isCurrentPageJSP()) {
			jspManager.writeState(context, state);
		} else {
			cbpManager.writeState(context, state);
		}
	}
	
	/**
	 * @see net.sourceforge.myfaces.application.MyfacesStateManager#writeStateAsUrlParams(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)
	 */
	public void writeStateAsUrlParams(FacesContext facesContext, SerializedView serializedView) throws IOException {
		if(SelectorUtils.isCurrentPageJSP()) {
			jspManager.writeStateAsUrlParams(facesContext, serializedView);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}