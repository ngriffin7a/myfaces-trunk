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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.renderkit.RenderUtils;

/**
 * @author Dimitry D'hondt.
 *
 * This class represents a toolbar on the screen. It is implemented to
 * support images (gif/jpg) for rendering the buttons, and supports tooltips on
 * the individual images. It also support indicating the seperators between 
 * buttons, by specifying one of the button image URLs as "-".
 * If the renderer encounters ">" as image URL, all buttons after that are
 * rendered on the right-hand side of the toolbar.
 * 
 * The toolbar itself will occupy as much space as it can. (It renders a table with
 * width="100%")
 * 
 * You may also specify the background color of the toolbar, using CSS style
 * 'smile.toolbar.background'. The rendered toolbar will have this background
 * color, only if you set attribute 'solidBackground' to true.
 */
public class Toolbar extends UIComponentBase {

	private List buttons = new ArrayList();
	private boolean solidBackground = false;
	private boolean rendered = true;

	public Toolbar() {
		super();
		setRendererType("net.sourceforge.myfaces.Toolbar");
	}
	
	
	/**
	 * @see javax.faces.component.UIPanel#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[4];
		values[0] = super.saveState(ctx);
		values[1] = buttons;
		values[2] = new Boolean(rendered);
		values[3] = new Boolean(solidBackground);
		return values;
	}
	
	/**
	 * @see javax.faces.component.UIPanel#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[])state;
		super.restoreState(ctx, values[0]);
		buttons = (List) values[1];
		rendered = ((Boolean)values[2]).booleanValue();
		solidBackground = ((Boolean)values[3]).booleanValue();
	}
	
	/**
	 * Provides access to a single button of the toolbar.
	 * 
	 * @param index
	 * @return
	 */
	public ToolbarButton getButton(int index) {
		ToolbarButton ret = null;
		
		if(index < buttons.size()) {
			ret = (ToolbarButton) buttons.get(index);
		}
		
		return ret;
	}
	
	/**
	 * Allows you to add a button to the toolbar.
	 * 
	 * @param button
	 */
	public void addButton(ToolbarButton button) {
		buttons.add(button);
	}
	
	/**
	 * Allows you to remove a button from the toolbar.
	 * @param button
	 */
	public void removeButton(ToolbarButton button) {
		if(buttons.contains(button)) {
			buttons.remove(button);
		} else {
			throw new IllegalArgumentException("The supplied button is NOT part of this toolbar !"); 
		}
	}
	
	/**
	 * Returns a (read-only) list of all the buttons in this toolbar.
	 * 
	 * @return
	 */
	public List getButtons() {
		return Collections.unmodifiableList(buttons);
	}
	
	/**
	 * @return
	 */
	public boolean isRendered() {
		return rendered;
	}

	/**
	 * If you want the toolbar to (temporarily) not appear on your screen
	 * set this to false. You could also remove the toolbar from the component tree,
	 * which is slightly different from this.
	 * 
	 * @param b
	 */
	public void setRendered(boolean b) {
		rendered = b;
	}

	/**
	 * @return
	 */
	public boolean isSolidBackground() {
		return solidBackground;
	}

	/**
	 * Should the background of the toolbar be solid, or transparent ?
	 * The color of the background is configured in the component's stylesheet.
	 * 
	 * @param b
	 */
	public void setSolidBackground(boolean b) {
		solidBackground = b;
	}

	/**
	 * Register a listener for button press events.
	 * @param listener
	 */
	public void addToolbarButtonPressedListener(ToolbarButtonPressedListener listener) {
		addFacesListener(listener);
	}

	/**
	 * Remove a listener for button presses.
	 * @param listener
	 */
	public void removeToolbarButtonPressedListener(ToolbarButtonPressedListener listener) {
		addFacesListener(listener);
	}
	
	/**
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		return RenderUtils.SMILE_FAMILY;
	}
}