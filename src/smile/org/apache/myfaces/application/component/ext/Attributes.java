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

/**
 * Single point of definition for attributes of components.
 * 
 * @author Dimitry D'hondt.
 */
public interface Attributes {
	static final String ATTR_MODEL_REFERENCE = "modelReference";
	static final String ATTR_RENDERER_TYPE = "rendererType";
	static final String ATTR_VALUE = "value";
	static final String ATTR_VALUEREF = "valueRef";
	static final String ATTR_VALID = "valid";
	static final String ATTR_PARENT = "parent";
	static final String ATTR_COMPONENT_ID = "componentId";
	static final String ATTR_COMPONENT_TYPE = "componentType";
	static final String ATTR_CONVERTER = "converter";
	static final String ATTR_CLIENT_ID = "clientId";
	static final String ATTR_RENDERED = "rendered";
	static final String ATTR_RENDERS_CHILDREN = "rendersChildren";
	static final String ATTR_RENDERS_SELF = "rendersSelf";
}