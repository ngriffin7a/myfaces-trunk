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

import java.io.Serializable;

/**
 * @author Dimitry D'hondt
 *
 * Responsible for representing state for individual toolbar buttons.
 */
public class ToolbarButton implements Serializable {

	private String unpressedImageURL = null;
	private String pressedImageURL = null;
	private String tooltip = null;
	private boolean toggleButton = false;
	private boolean pressed = false;
	private String action = null;
	
	public ToolbarButton() {
		super();
	}
	
	/**
	 * 
	 * @param unpressedImageURL
	 * @param pressedImageURL
	 * @param tooltip
	 * @param toggleButton
	 * @param action
	 */
	public ToolbarButton(String unpressedImageURL, String pressedImageURL, String tooltip, boolean toggleButton, String action) {
		this.unpressedImageURL = unpressedImageURL;
		this.pressedImageURL = pressedImageURL;
		this.tooltip = tooltip;
		this.toggleButton = toggleButton;
		this.action = action;
	}
	
	/**
	 * @return
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Use this property to store an identification string,
	 * which you use in your event listeners to easily identify
	 * which button was pressed.
	 * 
	 * @param string
	 */
	public void setAction(String string) {
		action = string;
	}

	/**
	 * This is an URL to an image that is shown when the button is NOT
	 * pressed.
	 * 
	 * @return
	 */
	public String getUnpressedImageURL() {
		return unpressedImageURL;
	}

	/**
	 * @param string
	 */
	public void setUnpressedImageURL(String string) {
		unpressedImageURL = string;
	}

	/**
	 * @return
	 */
	public String getPressedImageURL() {
		return pressedImageURL;
	}

	/**
	 * This is an URL to an image that is shown when the button is pressed.
	 * Set this property to "-" if you want to render a seperator between groups
	 * of buttons.
	 * Set it to ">" if you want sebsequent buttons to appear on the right of the 
	 * toolbar.
	 * 
	 * @param string
	 */
	public void setPressedImageURL(String string) {
		pressedImageURL = string;
	}

	/**
	 * @return
	 */
	public boolean isToggleButton() {
		return toggleButton;
	}

	/**
	 * set this property to true is you want this button to be a toggle button,
	 * rather than push-button. (toggles can be pressed, and de-pressed)
	 * 
	 * @param b
	 */
	public void setToggleButton(boolean b) {
		toggleButton = b;
	}

	/**
	 * Query this button to see if it is currently pressed (only use this
	 * method if the button is a toggle button - it's ALLWAYS false for simple
	 * push-buttons).
	 * 
	 * @return
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * @param b
	 */
	public void setPressed(boolean b) {
		if(isToggleButton()) {
			pressed = b;
		}
	}

	/**
	 * @return
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Set the text that is shown when the user hovers over this button.
	 * A null or empty string results in no tooltip.
	 * 
	 * @param string
	 */
	public void setTooltip(String string) {
		tooltip = string;
	}
	
	

}
