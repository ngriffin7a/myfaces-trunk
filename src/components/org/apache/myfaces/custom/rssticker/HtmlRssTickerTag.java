/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.rssticker;

import javax.faces.component.UIComponent;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.html.HtmlOutputTextTagBase;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2005/02/22 13:41:10  matzew
 * added RENDER_TYPE constant
 *
 * Revision 1.5  2005/02/18 17:19:31  matzew
 * added release() to tag clazzes.
 *
 * Revision 1.4  2004/10/13 11:50:57  matze
 * renamed packages to org.apache
 *
 * Revision 1.3  2004/07/01 21:53:10  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/27 22:06:26  mwessendorf
 * Log
 *
 *
 */
public class HtmlRssTickerTag extends HtmlOutputTextTagBase{

	public String getComponentType()
	{
		return HtmlRssTicker.COMPONENT_TYPE;
	}

	public String getRendererType()
	{
		return HtmlRssTickerRenderer.RENDERER_TYPE;
	}

	//Ticker attribute
	private String _rssUrl = null;

	// User Role support
	private String _enabledOnUserRole;
	private String _visibleOnUserRole;
	
    public void release() {
        super.release();

    	_rssUrl=null;
    	_enabledOnUserRole=null;
    	_visibleOnUserRole=null;

    }
	
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
