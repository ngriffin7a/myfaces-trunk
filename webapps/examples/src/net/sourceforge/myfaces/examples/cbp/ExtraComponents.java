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
import javax.faces.component.html.HtmlPanelGrid;

import net.sourceforge.myfaces.cbp.PageUtils;
import net.sourceforge.myfaces.component.ext.Toolbar;
import net.sourceforge.myfaces.component.ext.ToolbarButton;
import net.sourceforge.myfaces.component.ext.ToolbarButtonPressedEvent;
import net.sourceforge.myfaces.component.ext.ToolbarButtonPressedListener;
import net.sourceforge.myfaces.component.ext.Tree;
import net.sourceforge.myfaces.component.ext.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Screen provided as demo application.
 */
public class ExtraComponents extends AbstractDemo implements ToolbarButtonPressedListener {
	private static Log log = LogFactory.getLog(ExtraComponents.class);
	
	public UIComponent createContent() {
		UIComponent ret = null;
		
		HtmlPanelGrid partsPanel = new HtmlPanelGrid();
		partsPanel.setId("partsPanel");
		partsPanel.setColumns(1);
		partsPanel.getAttributes().put("width","100%");

		Toolbar toolbar = new Toolbar();
		toolbar.setId("toolbar");
		toolbar.setSolidBackground(true);
		ToolbarButton button;
		button = new ToolbarButton("images/button1.jpg","images/button1b.jpg","tooltip for button 1...",false,"button1");
		toolbar.addButton(button);
		button = new ToolbarButton("images/button1.jpg","images/button1b.jpg",null,false,"button2");
		toolbar.addButton(button);
		button = new ToolbarButton("-",null,null,false,null);
		toolbar.addButton(button);
		button = new ToolbarButton("images/button1.jpg","images/button1b.jpg","this one is a toggle button...",true,"button3");
		toolbar.addButton(button);
		button = new ToolbarButton(">",null,null,false,null);
		toolbar.addButton(button);
		button = new ToolbarButton("images/button1.jpg","images/button1b.jpg","a button on the right ... woowie !",false,"button5");
		toolbar.addButton(button);
		toolbar.addToolbarButtonPressedListener(this);
		PageUtils.addChild(partsPanel,toolbar);
		
		Tree tree = new Tree();
		tree.setId("tree");
		TreeNode node1 = new TreeNode();
		node1.setLabel("node1");
		tree.getRootNodes().add(node1);
		TreeNode node2 = new TreeNode();
		node2.setLabel("node2");
		tree.getRootNodes().add(node2);
		TreeNode node3 = new TreeNode();
		node3.setLabel("node3");
		node2.getSubNodes().add(node3);
		
		ret = partsPanel;
		
		return ret;
	}


	/**
	 * @see net.sourceforge.smile.component.ToolbarButtonPressedListener#buttonPressed(net.sourceforge.smile.component.ToolbarButtonPressedEvent)
	 */
	public void buttonPressed(ToolbarButtonPressedEvent e) {
		log.info("toolbar button " + e.getButton().getAction() + " was pressed.");
	}
}