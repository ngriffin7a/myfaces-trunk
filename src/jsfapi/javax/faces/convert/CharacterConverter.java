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
package javax.faces.convert;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 *
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CharacterConverter implements Converter {

	// FIELDS
	public static final String CONVERTER_ID = "javax.faces.Character";

	// CONSTRUCTORS
	public CharacterConverter(){
	
	}

	// METHODS
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value)
	{
		//TODO
		throw new UnsupportedOperationException();
	}

	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value)
	{
		//TODO
		throw new UnsupportedOperationException();
	}

}
