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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.application.cbp.StateManagerImpl;

/**
 * @author Dimitry D'hondt
 *
 * Utilities for the ViewHandler and StateManager selectors.
 */
public class SelectorUtils {
	/**
	 * Determines the package where the descriptor classes are located.
	 * @param context
	 * @return
	 */
	public static String getDescriptorPackage(FacesContext context) {
		String ret = "";	// Default package.
		String temp;
		
		// Try to determine descriptor package...
		temp = context.getExternalContext().getInitParameter("net.sourceforge.smile.descriptor.package"); 
		if(temp != null) {
			ret = temp;
			if(!ret.endsWith(".")) {
				ret = ret + ".";
			}
		}
		
		return ret;
	}

	/**
	 * Determines the postfix for the descriptor class.
	 * @param context
	 * @return
	 */
	public static String getDescriptorPostfix(FacesContext context) {
		String ret = "";	// Default.
		String temp;
		
		// Try to determine descriptor package...
		temp = context.getExternalContext().getInitParameter("net.sourceforge.smile.descriptor.postfix"); 
		if(temp != null) {
			ret = temp;
		}
		
		return ret;
	}

	/**
	 * This operation determines if the supplied viewId maps to a CBP page.
	 * This is basically done by determining if any of the CBT packages contain
	 * classes that correspond to the name of the supplied viewId.
	 *  
	 * @param viewRoot
	 * @return
	 */
	public static boolean isPageJSP(String viewId) {
		boolean ret = false;
		
		if(viewId != null && getDescriptorClassNameForViewId(viewId) == null) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * This operation determines if the supplied viewRoot maps to a CBP page.
	 * This is basically done by determining if any of the CBT packages contain
	 * classes that correspond to the name of the supplied viewId.
	 *  
	 * @param viewRoot
	 * @return
	 */	
	public static boolean isPageJSP(UIViewRoot viewRoot) {
		boolean ret = false;
		
		if(viewRoot != null) {
			String viewId = viewRoot.getViewId();
			ret = isPageJSP(viewId);
		}
		
		return ret;
	}
	
	/**
	 * This operation looks at the current component tree, and determines if
	 * it is a CBP page, or JSP page.
	 * @return
	 */
	public static boolean isCurrentPageJSP() {
		boolean ret = false;
		UIViewRoot viewRoot = null;
		
		viewRoot = getCurrentViewRoot();
		if(viewRoot != null) {
			ret = isPageJSP(viewRoot);
		}
		
		return ret;
	}
	
	/**
	 * This function is responsible for finding the descriptor class for a given
	 * viewId. The policy may change over time, like supporting multiple packages
	 * or mory flexible mapping of viewIds to descriptor classes,etc..
	 *  
	 * @param viewId
	 * @return the descriptor class for a given viewId, or null if no descriptor found.
	 */
	public static Class getDescriptorClassNameForViewId(String viewId) {
		Class ret = null;
		String className;
		FacesContext ctx = FacesContext.getCurrentInstance();
				
		// TODO : We should implement a configurable scheme with more than one package.	
		if(viewId.endsWith(".jsf") || viewId.endsWith(".jsp")) {
			String shortClassName = viewId.substring(0,viewId.length()-4);
			if(shortClassName.startsWith("/")) {
				shortClassName = shortClassName.substring(1,shortClassName.length());
			}
			
			className = getDescriptorPackage(ctx) + shortClassName + getDescriptorPostfix(ctx);
			try {
				ret = Class.forName(className);
			} catch(ClassNotFoundException e) {
				ret = null;
			}
		}
		
		return ret;
	}


	/**
	 * Determines the current ViewRoot.
	 * @return
	 */
	public static UIViewRoot getCurrentViewRoot() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return (UIViewRoot) ctx.getExternalContext().getSessionMap().get(StateManagerImpl.SESSION_KEY_CURRENT_VIEW);
	}
}
