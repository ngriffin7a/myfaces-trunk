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
package net.sourceforge.myfaces.examples.cbp;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import net.sourceforge.myfaces.cbp.Page;
import net.sourceforge.myfaces.cbp.PageUtils;
import net.sourceforge.myfaces.component.ext.HtmlLayout;
import net.sourceforge.myfaces.component.ext.Screen;

/**
 * Screen provided as demo application.
 */
public abstract class AbstractDemo implements Page, ActionListener {

	/**
	 * Construct the common bahaviour, layout, etc..
	 * for all Demos.
	 */
	public void init(FacesContext context,UIComponent root) {
		Screen screen = new Screen();
		screen.setId("testScreen");
		screen.setTitle("Smile demo application...");
		PageUtils.addChild(root,screen);
		
		HtmlLayout template = new HtmlLayout();
		template.setId("template");
		template.setResourceName("template.html");
		PageUtils.addChild(screen,template);
		
		HtmlCommandButton designButton = new HtmlCommandButton();
		designButton.setId("designButton");
		designButton.setValue("design on/off");
		designButton.addActionListener(this);
		designButton.setRendered(false);
		PageUtils.addChild(template,designButton);
		
		UIComponent content = createContent();
		PageUtils.addChild(template,content);
	}
	
	/**
	 * This method should be used by a baseclass to create the 
	 * the content that is to be rendered in the body of the page. 
	 * You would have more methods if you wanted to create
	 * a template that has more than one dynamic part.
	 * 
	 * @param content
	 */
	protected abstract UIComponent createContent();
	
	/**
	 * @see javax.faces.event.ActionListener#processAction(javax.faces.event.ActionEvent)
	 */
	public void processAction(ActionEvent event) throws AbortProcessingException {
		UIComponent source = event.getComponent();
		if(source.getId().equals("designButton")) {
//			DesignerContext.setDesignMode(!DesignerContext.inDesignMode());
		}
	}
}