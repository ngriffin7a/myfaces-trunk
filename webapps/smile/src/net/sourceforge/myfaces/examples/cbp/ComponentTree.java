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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlMessages;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.model.SelectItem;

import net.sourceforge.myfaces.cbp.PageUtils;
import net.sourceforge.myfaces.component.ext.Dummy;
import net.sourceforge.myfaces.component.ext.Ruler;
import net.sourceforge.myfaces.exception.InternalServerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Screen provided as demo application.
 */
public class ComponentTree extends AbstractDemo implements ActionListener {
	private static Log log = LogFactory.getLog(ComponentTree.class);
	
	public UIComponent createContent() {
		UIComponent ret = null;
		
		HtmlPanelGrid partsPanel = new HtmlPanelGrid();
		partsPanel.setId("partsPanel");
		partsPanel.setColumns(1);

		HtmlMessages messages = new HtmlMessages();
		messages.setId("messages");
		PageUtils.addChild(partsPanel,messages);

		HtmlPanelGrid addComponentPanel = new HtmlPanelGrid();
		addComponentPanel.setId("addComponentPanel");
		addComponentPanel.setColumns(4);
		PageUtils.addChild(partsPanel,addComponentPanel);

		Ruler ruler = new Ruler();
		ruler.setId("ruler");
		PageUtils.addChild(partsPanel,ruler);		

		HtmlPanelGrid resultsPanel = new HtmlPanelGrid();
		resultsPanel.setId("resultsPanel");
		resultsPanel.setColumns(1);
		PageUtils.addChild(partsPanel,resultsPanel);		
		
		HtmlOutputLabel parentLabel = new HtmlOutputLabel();
		parentLabel.setId("parentLabel");
		parentLabel.setValue("Parent :");
		HtmlOutputLabel typeLabel = new HtmlOutputLabel();
		typeLabel.setId("typeLabel");
		typeLabel.setValue("Type :");
		HtmlOutputLabel idLabel = new HtmlOutputLabel();
		idLabel.setId("idLabel");
		idLabel.setValue("Id :");
		Dummy dummy = new Dummy();
		dummy.setId("dummy");
		
		PageUtils.addChild(addComponentPanel,parentLabel);
		PageUtils.addChild(addComponentPanel,typeLabel);
		PageUtils.addChild(addComponentPanel,idLabel);
		PageUtils.addChild(addComponentPanel,dummy);
		
		HtmlSelectOneMenu parentCombo = new HtmlSelectOneMenu();
		parentCombo.setId("parentCombo");
		UISelectItems parentComboItems = createComponentDisplayList(resultsPanel);
		parentCombo.getChildren().clear();
		parentCombo.getChildren().add(parentComboItems);
		PageUtils.addChild(addComponentPanel,parentCombo);
		
		HtmlSelectOneMenu typeCombo = new HtmlSelectOneMenu();
		typeCombo.setId("typeCombo");
		List componentTypes = new ArrayList();
		componentTypes.add(new SelectItem("input_label","input_label","Read-only label."));
		componentTypes.add(new SelectItem("input_text","input_text","Text box."));
		componentTypes.add(new SelectItem("input_textarea","input_textarea","Multiline text box."));
		componentTypes.add(new SelectItem("grid","grid","Grid Layout."));
		UISelectItems typeComboItems = new UISelectItems();
		typeComboItems.setId("typeComboItems");
		typeComboItems.setValue(componentTypes);
		typeCombo.getChildren().clear();
		typeCombo.getChildren().add(typeComboItems);
		addComponentPanel.getChildren().add(typeCombo);
		
		HtmlInputText identifier = new HtmlInputText();
		identifier.setId("identifier");
		identifier.setValue("");
		PageUtils.addChild(addComponentPanel,identifier);
		
		HtmlCommandButton addButton = new HtmlCommandButton();
		addButton.setId("addButton");
		addButton.setValue("Add Component");
		PageUtils.addChild(addComponentPanel,addButton);
		
		addButton.addActionListener(this);
		
		ret = partsPanel;
		
		return ret;		
	}

	/**
	 * Builds a list of components currently in the results pane. 
	 */	
	private UISelectItems createComponentDisplayList(UIComponent resultsPanel) {
		UISelectItems ret = new UISelectItems();
		ret.setId("parentComboItems");

		List list = new ArrayList();

		list.add(new SelectItem(resultsPanel.getId(),resultsPanel.getId(),resultsPanel.getId()));

		List children = resultsPanel.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			UIComponent element = (UIComponent) iter.next();
			if(element instanceof UIPanel) {
				list.add(new SelectItem(element.getId(),element.getId(),element.getId()));			
			}
		}

		ret.setValue(list);

		return ret;
	}
	
	/**
	 * @see javax.faces.event.ActionListener#processAction(javax.faces.event.ActionEvent)
	 */
	public void processAction(ActionEvent event) throws AbortProcessingException {
		if(event.getComponent().getId().equals("addButton")) {
			HtmlInputText identifier = PageUtils.getHtmlInputText("identifier");
			HtmlSelectOneMenu parentCombo = PageUtils.getHtmlSelectOneMenu("parentCombo");
			HtmlSelectOneMenu typeCombo = PageUtils.getHtmlSelectOneMenu("typeCombo");
			HtmlPanelGrid resultsPanel = PageUtils.getHtmlPanelGrid("resultsPanel");
			
			if(identifier == null) {
				throw new InternalServerException("unable to find 'id' text field !");
			}
			if(identifier.getValue() == null || ((String)identifier.getValue()).length() == 0) {
				PageUtils.addMessage("Enter an identifier for your new component.");
			} else {
				String name = (String)identifier.getValue();
				UIComponent component = PageUtils.getUIComponent(name);
				String parentId = (String) parentCombo.getValue();
				UIComponent parent = PageUtils.getUIComponent(parentId);
				if(component != null) {
					PageUtils.addMessage("A component with identifier '" + name + "' allready exists ! Enter a unique name.");
				} else {
					String type = (String) typeCombo.getValue();
					log.info("type = <" + type + ">");
					if(type.equalsIgnoreCase("input_label")) {
						HtmlOutputLabel input = new HtmlOutputLabel();
						input.setId((String)identifier.getValue());
						input.setValue("Label [" + name + "]");
						PageUtils.addChild(parent,input);
					} else if(type.equalsIgnoreCase("input_text")) {
						HtmlInputText input = new HtmlInputText();
						input.setId((String)identifier.getValue());
						input.setValue("");
						PageUtils.addChild(parent,input);
					} else if(type.equalsIgnoreCase("input_textarea")) {
						HtmlInputTextarea input = new HtmlInputTextarea();
						input.setId((String)identifier.getValue());
						input.setValue("");
						PageUtils.addChild(parent,input);
					} else if(type.equalsIgnoreCase("grid")) {
						HtmlPanelGrid grid = new HtmlPanelGrid();
						grid.setId((String)identifier.getValue());
						grid.setColumns(2);
						PageUtils.addChild(parent,grid);
					}
				}
				
				UISelectItems parentComboItems = createComponentDisplayList(resultsPanel);
				parentCombo.getChildren().clear();
				parentCombo.getChildren().add(parentComboItems);
				
				identifier.setValue("");
			}
		}
	}
}