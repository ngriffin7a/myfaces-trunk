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

import javax.faces.context.FacesContext;

/**
 * @author Dimitry D'hondt
 *
 */
public class DesignerContext {

	private static final String design_context = "net.sourceforge.myfaces.designercontext";

	public static boolean inDesignMode() {
		boolean ret = false;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		if(ctx != null) {
			if(ctx.getExternalContext().getSessionMap().get(design_context) != null) {
				ret = true;
			}
		}
		
		return ret;
	}
	
	public static void setDesignMode(boolean inDesign) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if(ctx != null) {
			if(inDesign) {
				ctx.getExternalContext().getSessionMap().put(design_context,Boolean.TRUE);
			} else {
				ctx.getExternalContext().getSessionMap().remove(design_context);
			}
		}		
	}
}