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
import javax.faces.component.html.HtmlOutputLabel;

/**
 * @author codehawk
 *
 * Well, does it need words ?
 */
public class HelloWorld2 extends AbstractDemo {
	public UIComponent createContent() {
		HtmlOutputLabel label = new HtmlOutputLabel();
		label.setId("label");
		label.setValue("Hello World ! (2)");
		
		return label;
	}
}