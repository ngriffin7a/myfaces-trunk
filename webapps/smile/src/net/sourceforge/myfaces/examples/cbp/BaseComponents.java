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
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.model.SelectItem;

import net.sourceforge.myfaces.cbp.PageUtils;

/**
 * Screen provided as demo application.
 */
public class BaseComponents extends AbstractDemo  {
	public UIComponent createContent() {
		UIComponent ret = null;
		
		HtmlPanelGrid layout = new HtmlPanelGrid();
		layout.setId("layout");
		layout.setColumns(2);

		HtmlInputText input = new HtmlInputText();
		input.setId("input");
		input.setValue("init val.");
		PageUtils.addChild(layout,input);

		HtmlCommandButton command = new HtmlCommandButton();
		command.setId("command");
		command.getAttributes().put("label","SmIlE !");
		PageUtils.addChild(layout,command);

		HtmlGraphicImage graphic = new HtmlGraphicImage();
		graphic.setId("graphic");
		graphic.setValue("images/wilbert.png");
		PageUtils.addChild(layout,graphic);

//		UIInput labelChild = new UIInput();
//		labelChild.setValue("xyz");
		HtmlOutputLabel label = new HtmlOutputLabel();
		label.setId("label");
		label.setValue("Label : ");
//		PageUtils.addChild(label,labelChild);
		PageUtils.addChild(layout,label);

		HtmlSelectBooleanCheckbox checkbox = new HtmlSelectBooleanCheckbox();
		checkbox.setId("checkbox");
		PageUtils.addChild(layout,checkbox);

		List list = new ArrayList();
		list.add(new SelectItem("value a","label a","description a"));
		list.add(new SelectItem("value b","label b","description b"));
		UISelectItems items = new UISelectItems();
		items.setId("items");
		items.setValue(list);
		
		List list2 = new ArrayList();
		list2.add(new SelectItem("value a","label a","description a"));
		list2.add(new SelectItem("value b","label b","description b"));
		UISelectItems items2 = new UISelectItems();
		items2.setId("items2");
		items2.setValue(list2);		

		List list3 = new ArrayList();
		list3.add(new SelectItem("value a","label a","description a"));
		list3.add(new SelectItem("value b","label b","description b"));
		UISelectItems items3 = new UISelectItems();
		items3.setId("items3");
		items3.setValue(list3);	
		
		HtmlSelectManyListbox many = new HtmlSelectManyListbox();
		many.setId("many");
		PageUtils.addChild(many,items);
		PageUtils.addChild(layout,many);
		
		HtmlSelectOneListbox one = new HtmlSelectOneListbox();
		one.setId("one");
		PageUtils.addChild(one,items2);
		PageUtils.addChild(layout,one);
		
		HtmlSelectManyCheckbox manyCheckbox = new HtmlSelectManyCheckbox();
		manyCheckbox.setId("manyCheckbox");
		PageUtils.addChild(manyCheckbox,items3);
		PageUtils.addChild(layout,manyCheckbox);

		// Test menu renderer.

		List listMenu = new ArrayList();
		listMenu.add(new SelectItem("value a","label a","description a"));
		listMenu.add(new SelectItem("value b","label b","description b"));
		listMenu.add(new SelectItem("value c","label c","description c"));
		listMenu.add(new SelectItem("value d","label d","description d"));
		listMenu.add(new SelectItem("value e","label e","description e"));
		UISelectItems itemsMenu = new UISelectItems();
		itemsMenu.setId("itemsMenu");
		itemsMenu.setValue(listMenu);

		List listMenu2 = new ArrayList();
		listMenu2.add(new SelectItem("value a","label a","description a"));
		listMenu2.add(new SelectItem("value b","label b","description b"));
		listMenu2.add(new SelectItem("value c","label c","description c"));
		listMenu2.add(new SelectItem("value d","label d","description d"));
		listMenu2.add(new SelectItem("value e","label e","description e"));
		UISelectItems itemsMenu2 = new UISelectItems();
		itemsMenu2.setId("itemsMenu2");
		itemsMenu2.setValue(listMenu2);
		
		List listMenu3 = new ArrayList();
		listMenu3.add(new SelectItem("value a","label a","description a"));
		listMenu3.add(new SelectItem("value b","label b","description b"));
		listMenu3.add(new SelectItem("value c","label c","description c"));
		listMenu3.add(new SelectItem("value d","label d","description d"));
		listMenu3.add(new SelectItem("value e","label e","description e"));
		UISelectItems itemsMenu3 = new UISelectItems();
		itemsMenu3.setId("itemsMenu3");
		itemsMenu3.setValue(listMenu3);
		
		HtmlSelectManyMenu manyMenu = new HtmlSelectManyMenu();
		manyMenu.setId("manyMenu");
		//manyMenu.setSize(2);
		PageUtils.addChild(manyMenu,itemsMenu);
		PageUtils.addChild(layout,manyMenu);

		HtmlSelectOneMenu oneMenu = new HtmlSelectOneMenu();
		oneMenu.setId("oneMenu");
		//oneMenu.setSize(2);
		PageUtils.addChild(oneMenu,itemsMenu2);
		PageUtils.addChild(layout,oneMenu);
		
		HtmlSelectOneRadio radio = new HtmlSelectOneRadio();
		radio.setId("radio");
		radio.setValue("value c");
		PageUtils.addChild(radio,itemsMenu3);
		PageUtils.addChild(layout,radio);
		
		HtmlPanelGroup group = new HtmlPanelGroup();
		group.setId("group");
		
		HtmlInputText groupInput1 = new HtmlInputText();
		groupInput1.setId("groupInput1");
		groupInput1.setValue("groupInput1");
		PageUtils.addChild(group,groupInput1);

		HtmlInputText groupInput2 = new HtmlInputText();
		groupInput2.setId("groupInput2");
		groupInput2.setValue("groupInput2");
		PageUtils.addChild(group,groupInput2);
		
		PageUtils.addChild(layout,group);
		
		ret = layout;
		
		return ret;		
	}
}