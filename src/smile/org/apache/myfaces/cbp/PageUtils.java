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
package net.sourceforge.myfaces.cbp;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessage;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.component.ext.Screen;

/**
 * @author Dimitry D'hondt
 * @author Anton Koinov
 *
 * This class provides convenience methods to make working with the class-based
 * pages easier. It provides convient access to the component tree, etc..
 */
public class PageUtils {

	/**
	 * Finds a component a specific type.
	 * @return
	 */
	public static UIViewRoot getRoot() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getViewRoot();
	}
	
	/**
	 * Find the component with the specified id in the current component tree.
	 * If the first child of the screen is a namingcontainer, it will
	 * automatically search in that context.
	 * 
	 * @param id
	 * @return the component, or null if no component was found with the specified id.
	 */
	public static UIComponent getUIComponent(String id) {
		if(id == null) throw new NullPointerException("Please supply the id of the component you're looking for.");
		
		UIComponent ret = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		UIComponent root = ctx.getViewRoot();
		
		if(root.getChildCount() == 1 && root.getChildren().get(0) instanceof Screen) {
			Screen rootScreen = (Screen) root.getChildren().get(0);
			ret = root.findComponent(rootScreen.getId() + NamingContainer.SEPARATOR_CHAR + id);
		} else {
			ret = root.findComponent(id);
		}		 
		
		return ret;
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIInput getUIInput(String id) {
		return (UIInput) getComponentOfType(id,UIInput.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIOutput getUIOutput(String id) {
		return (UIOutput) getComponentOfType(id,UIOutput.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIColumn getUIColumn(String id) {
		return (UIColumn) getComponentOfType(id,UIColumn.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UICommand getUICommand(String id) {
		return (UICommand) getComponentOfType(id,UICommand.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIData getUIData(String id) {
		return (UIData) getComponentOfType(id,UIData.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIForm getUIForm(String id) {
		return (UIForm) getComponentOfType(id,UIForm.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIGraphic getUIGraphic(String id) {
		return (UIGraphic) getComponentOfType(id,UIGraphic.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIMessage getUIMessage(String id) {
		return (UIMessage) getComponentOfType(id,UIMessage.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UIPanel getUIPanel(String id) {
		return (UIPanel) getComponentOfType(id,UIPanel.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UISelectBoolean getUISelectBoolean(String id) {
		return (UISelectBoolean) getComponentOfType(id,UISelectBoolean.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UISelectItem getUISelectItem(String id) {
		return (UISelectItem) getComponentOfType(id,UISelectItem.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UISelectItems getUISelectItems(String id) {
		return (UISelectItems) getComponentOfType(id,UISelectItems.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UISelectMany getUISelectMany(String id) {
		return (UISelectMany) getComponentOfType(id,UISelectMany.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static UISelectOne getUISelectOne(String id) {
		return (UISelectOne) getComponentOfType(id,UISelectOne.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static HtmlPanelGrid getHtmlPanelGrid(String id) {
		return (HtmlPanelGrid) getComponentOfType(id,HtmlPanelGrid.class);
	}
	
	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static HtmlInputText getHtmlInputText(String id) {
		return (HtmlInputText) getComponentOfType(id,HtmlInputText.class);
	}
		

	/**
	 * Finds a component a specific type.
	 * @param id
	 * @return
	 */
	public static HtmlSelectOneMenu getHtmlSelectOneMenu(String id) {
		return (HtmlSelectOneMenu) getComponentOfType(id,HtmlSelectOneMenu.class);
	}
		
	
	/**
	 * Searches for a named component of the specified type. 
	 * @param id
	 * @param clazz
	 * @return the component, or null if no component of the right type was found.
	 */
	private static UIComponent getComponentOfType(String id, Class clazz) {
		UIComponent ret = null;
		
		ret = getUIComponent(id);
		if(!clazz.isAssignableFrom(ret.getClass())) {
			ret = null;
		}
		
		return ret;
	}
	
	/**
	 * Convenience operation for adding children to a parent.
	 * @param parent
	 * @param child
	 */
	public static void addChild(UIComponent parent, UIComponent child) {
		parent.getChildren().add(child);
	}

	/**
	 * Adds a component to another component
	 * @param parent
	 * @param facet
	 */
	public static void addFacet(UIComponent parent, UIComponent facet, String key) {
		parent.getFacets().put(key,facet);
	}
	
	/**
	 * Adds a message to the default message context.
	 * dding a string via this message makes it appear in the UIMessages components.
	 * @param message
	 */
	public static void addMessage(String message) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(message,message);
		facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
		ctx.addMessage(null,facesMessage);
	}
	
	/**
	 * This method may be used to navigate to a new page.
	 * Specify the name of the page. (e.g. "Welcome.jsf")
	 * @param pageName
	 */
	public static void navigateTo(String pageName) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		UIViewRoot newRoot = getApp().getViewHandler().createView(ctx,pageName);
		if(newRoot != null) {
			ctx.setViewRoot(newRoot);	
		}
	}
	
	private static Application getApp() {
		ApplicationFactory appFactory = 
            (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication();
	}
}