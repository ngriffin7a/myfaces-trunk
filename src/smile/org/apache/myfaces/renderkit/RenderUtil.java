

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
package net.sourceforge.myfaces.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility class used for rendering
 * 
 * @author codehawk
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class RenderUtil {
	private static Log log = LogFactory.getLog(RenderUtil.class);

	/**
	 * Array of all html attributes that passed through from the components attributes
	 */
	private static String passthruAttributes[] =
		{
			"accesskey",
			"alt",
			"cols",
			"height",
			"lang",
			"longdesc",
			"maxlength",
			"rows",
			"size",
			"tabindex",
			"styleClass",
			"title",
			"style",
			"width",
			"dir",
			"rules",
			"frame",
			"border",
			"cellspacing",
			"cellpadding",
			"summary",
			"bgcolor",
			"usemap",
			"enctype",
			"accept-charset",
			"accept",
			"target" };

	private static String[] positiveAttributes = { "maxlength", "size", "tabindex", "border", "cols", "rows" };

	private static String booleanPassthruAttributes[] = { "disabled", "readonly", "ismap" };

	private static String eventHandlerPassthruAttributes[] =
		{
			"onblur",
			"onchange",
			"onclick",
			"ondblclick",
			"onfocus",
			"onkeydown",
			"onkeypress",
			"onkeyup",
			"onload",
			"onmousedown",
			"onmousemove",
			"onmouseout",
			"onmouseover",
			"onmouseup",
			"onreset",
			"onselect",
			"onsubmit",
			"onunload",
			"onsubmit",
			"onreset" };

	public static void renderBooleanPassthruAttributes(FacesContext context, UIComponent component)
		throws IOException {
		int i = 0;
		int len = booleanPassthruAttributes.length;
		ResponseWriter writer = context.getResponseWriter();
		for (i = 0; i < len; i++) {
			Boolean value;
			if (null != (value = (Boolean) component.getAttributes().get(booleanPassthruAttributes[i]))) {
				if (value.booleanValue())
					writer.write(" " + booleanPassthruAttributes[i]);
			}
		}
	}

	public static void renderPassthruAttributes(FacesContext context, UIComponent component) throws IOException {
		renderAttributes(component, context, passthruAttributes);
	}

	public static void renderEventHandlerPassthruAttributes(FacesContext context, UIComponent component)
		throws IOException {
		renderAttributes(component, context, eventHandlerPassthruAttributes);
	}

	private static void renderAttributes(UIComponent component, FacesContext context, String[] attributes)
		throws IOException {
		int i = 0;
		int len = attributes.length;
		ResponseWriter writer = context.getResponseWriter();
		List positiveAtts = Arrays.asList(positiveAttributes);
		boolean valid;
		for (i = 0; i < len; i++) {
			Object value;
			String attribute = attributes[i];
			if (null != (value = component.getAttributes().get(attribute))) {
				valid = true;
				if (positiveAtts.contains(attributes[i])) {
					if (value instanceof String) {
						try {
							long v = Long.parseLong(value.toString());
							if (v < 0) {
								valid = false;
							}
						} catch (NumberFormatException e) {
							valid = false;
						}
					} else if (value instanceof Number) {
						Number n = (Number) value;
						if (n.longValue() < 0) {
							valid = false;
						}
					}
				}

				if (valid) {
					if (attributes[i].equals("styleClass")) {
						writer.writeAttribute("class", value, attributes[i]);
					} else {
						writer.writeAttribute(attributes[i], value, attributes[i]);
					}
				}
			}
		}
	}

	/**
	 * This operation is a convenience helper that checks the type safety for the
	 * attributes of type String.This operation throws an appropriate exception, 
	 * or returns the down-casted string for a given attribute.
	 * 
	 * @param component the component to query for the attribute.
	 * @param attribute the attribute to query for.
	 * @return The string value for the attribute, or null if attribute not present.
	 * 
	 * @throws IllegalArgumentException when the requested attribute turns out to be of a different type than java.lang.String. 
	 */
	public static String getStringAttribute(UIComponent component, String attribute) {
		Object o;
		String ret = null;
		o = component.getAttributes().get(attribute);
		if (o != null) {
			if (o instanceof String) {
				ret = (String) o;
			} else {
				throw new IllegalArgumentException(
					"The attribute <"
						+ attribute
						+ "> on component <"
						+ component.getId()
						+ "> should be a java.lang.String object instead of <"
						+ o.getClass().getName()
						+ ">");
			}
		}

		return ret;
	}

	/**
	 * This operation is a convenience helper that checks the type safety for the
	 * attributes of type Boolean.This operation throws an appropriate exception, 
	 * or returns a primitive boolean for a given attribute.
	 * 
	 * @param component the component to query for the attribute.
	 * @param attribute the attribute to query for.
	 * @return The string value for the attribute.
	 * 
	 * @throws IllegalArgumentException when the requested attribute turns out to be of a different type than java.lang.Boolean. 
	 */
	public static Boolean getBooleanAttribute(UIComponent component, String attribute) {
		Object o;
		Boolean ret = null;
		o = component.getAttributes().get(attribute);
		if (o != null) {
			if (o instanceof Boolean) {
				ret = (Boolean) o;
			} else {
				String msg =
					"The attribute <"
						+ attribute
						+ "> on component <"
						+ component.getId()
						+ "> should be a java.lang.Boolean object instead of <"
						+ o.getClass().getName()
						+ ">";
				log.error(msg);
				throw new IllegalArgumentException(msg);
			}
		}

		return ret;
	}

	/**
	 * This operation retrieves a numeric attribute. It checks and converts the attribute
	 * into a Long, which provides for convenient access to the attribute. It also
	 * throws an IllegalArgumentException when the attribute is not of the requested type. 
	 * @param component the component to check.
	 * @param attribute the attribute to look up.
	 * @return a long with the value or null if the attribute is not specified for the component.
	 */
	public static Long getNumericAttribute(UIComponent component, String attribute) {
		Long ret = null;
		Object o;
		o = component.getAttributes().get(attribute);
		if (o != null) {
			if (o instanceof java.lang.Number) {
				ret = new Long(((Number) o).longValue());
			} else {
				String msg =
					"The attribute <"
						+ attribute
						+ "> on component <"
						+ component.getId()
						+ "> should be a java.lang.Number (e.g. Long/Integer/etc..) instead of <"
						+ o.getClass().getName()
						+ ">";
				log.error(msg);
				throw new IllegalArgumentException(msg);
			}
		}

		return ret;
	}

	/**
	 * Retrieves a string list attribute from a specified component. This operation is 
	 * useful to find and retrieve an attribute which is described in to have a comma separated
	 * list of elements. The individual elements are returned (in order) as a array of strings
	 * of easy interpretation. If the attribute is not available a null value is returned.
	 * @param component the component on which to look.
	 * @param attribute the attribute to look for.
	 * @return a String[] with the individual values or null if the attribute is not set on the component. 
	 */
	public static String[] getStringListAttribute(UIComponent component, String attribute) {
		Object o;
		String[] ret = null;
		String v = null;
		o = component.getAttributes().get(attribute);
		if (o != null) {
			if (o instanceof java.lang.String) {
				v = (String) o;
				StringTokenizer tok = new StringTokenizer(v, ",");
				int size = tok.countTokens();
				ret = new String[size];
				int i = 0;
				while (tok.hasMoreElements()) {
					ret[i++] = tok.nextToken();
				}
			} else {
				String msg =
					"The attribute <"
						+ attribute
						+ "> on component <"
						+ component.getId()
						+ "> should be a java.lang.String, with a comma-separated list of values, instead of <"
						+ o.getClass().getName()
						+ ">";
				log.error(msg);
				throw new IllegalArgumentException(msg);
			}
		}
		return ret;
	}

	/**
	 * Looks for the closest UIForm ancestor component.
	 * @param component
	 * @return a UIForm or null if there is no enclosing UIForm available
	 */
	public static UIForm getEnclosingForm(UIComponent component) {
		UIComponent currentComponent = component;
		UIForm result = null;
		while (currentComponent != null) {
			if (currentComponent instanceof UIForm) {
				result = (UIForm) currentComponent;
				break;
			}
			currentComponent = currentComponent.getParent();
		}
		return result;
	}

	/**
	 * Convenience method for setting a component attribute
	 */
	public static void setComponentAttribute(UIComponent component, String attribute, Object value) {
		component.getAttributes().put(attribute, value);
	}

	/**
	 * Encode the passed component and any of it's children recursively
	 * @param ctx
	 * @param component
	 * @throws IOException
	 */
	public static void encodeRecursive(FacesContext ctx, UIComponent component) throws IOException {
		component.encodeBegin(ctx);
		if (component.getRendersChildren()) {
			component.encodeChildren(ctx);
		} else {
			UIComponent child;
			for (Iterator children = component.getChildren().iterator();
				children.hasNext();
				encodeRecursive(ctx, child))
				child = (UIComponent) children.next();

		}
		component.encodeEnd(ctx);
	}
}
