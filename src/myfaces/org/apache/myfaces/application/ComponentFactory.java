/*
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
package net.sourceforge.myfaces.application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.cglib.proxy.Enhancer;
import net.sourceforge.myfaces.cbp.designer.DesignerDecorator;
import net.sourceforge.myfaces.cbp.designer.DesignerContext;
import net.sourceforge.myfaces.exception.InternalServerException;
import net.sourceforge.myfaces.exception.UnknownComponentException;

/**
 * This class is responsible for keeping track of the registered component types
 * in the system and also functions as a factory for creating components, based on
 * component type.
 * 
 * @author Dimitry D'hondt
 */
public class ComponentFactory {
	private static Log log = LogFactory.getLog(ComponentFactory.class);
	private static ComponentFactory instance = new ComponentFactory();

	private Map componentClazzByType = new HashMap();

	private ComponentFactory() {
	}

	public static ComponentFactory getInstance() {
		return instance;
	}

	/**
	 * Registers a component type with the factory.
	 * @param componentType
	 * @param componentClass
	 */
	public void addComponent(String componentType, Class componentClass) {
		if (!UIComponent.class.isAssignableFrom(componentClass)) {
			throw new InternalServerException(
				"The class <"
					+ componentClass.getName()
					+ " specified for component type <"
					+ componentType
					+ "> is not a javax.faces.component.UIComponent !");
		}
		componentClazzByType.put(componentType, componentClass);
	}

	public UIComponent createComponent(String componentType) throws FacesException {
		UIComponent ret = null;
		Class clazz = null;

//		try {
			synchronized(componentClazzByType) {
				clazz = (Class) componentClazzByType.get(componentType);
			}
			
			if (clazz == null) {
				String msg = "Request to create a component type <" + componentType + "> that is unknown.";
				log.error(msg);
				throw new UnknownComponentException(msg);
			}
			
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clazz);
			enhancer.setCallback(DesignerDecorator.getInstance());
			ret = (UIComponent) enhancer.create();   	
//		} catch (IllegalAccessException e) {
//			String msg =
//				"Component type <"
//					+ componentType
//					+ "> does not have a default constructor ! It's class is <"
//					+ clazz.getName()
//					+ ">";
//			log.error(msg, e);
//			throw new FacesException(msg);
//		} catch (InstantiationException e) {
//			String msg =
//				"Constructor for component type <"
//					+ componentType
//					+ "> threw an exception ! It's class is <"
//					+ clazz.getName()
//					+ ">";
//			log.error(msg, e);
//			throw new FacesException(msg);
//		}

		return ret;
	}

	/**
	 * Returns an iterator that contains all registered classes.
	 * @return
	 */
	public Iterator getComponentClasses() {
		return componentClazzByType.values().iterator();
	}

	/**
	 * Returns an iterator that contains all registeres component types.
	 * @return
	 */
	public Iterator getComponentTypes() {
		return componentClazzByType.keySet().iterator();
	}
}
