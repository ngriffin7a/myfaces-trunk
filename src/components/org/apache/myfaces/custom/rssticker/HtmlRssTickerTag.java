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
package net.sourceforge.myfaces.custom.rssticker;

import javax.faces.component.UIComponent;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.html.HtmlOutputTextTagBase;

/**
 * @author mwessendorf
 *
 */
public class HtmlRssTickerTag extends HtmlOutputTextTagBase{

	public String getComponentType()
	{
		return HtmlRssTicker.COMPONENT_TYPE;
	}

	public String getRendererType()
	{
		return "net.sourceforge.myfaces.Ticker";
	}

	//Ticker attribute
	private String _rssUrl = null;

	// User Role support
	private String _enabledOnUserRole;
	private String _visibleOnUserRole;
	
	/**
	 * overrides setProperties() form UIComponentTag.
	 */
	protected void setProperties(UIComponent component)
	{
		super.setProperties(component);

		setStringProperty(component, "rssUrl", _rssUrl);
		setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
		setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
	}
	

   //---------------------------------------------only the Setters
	
	public void setRssUrl(String string) {
		_rssUrl = string;
	}

	public void setEnabledOnUserRole(String string) {
		_enabledOnUserRole = string;
	}

	public void setVisibleOnUserRole(String string) {
		_visibleOnUserRole = string;
	}
}
