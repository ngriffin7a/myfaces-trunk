/**
 * Smile, the open-source JSF implementation.
 * Copyright (C) 2004  The smile team (http://smile.sourceforge.net)
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
package net.sourceforge.myfaces.exception;

/**
 * This exception is thrown when a new instance for a given component class
 * name could not be created.
 * 
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class ComponentCreationException extends SmileRuntimeException {

	public ComponentCreationException() {
		super();
	}

	public ComponentCreationException(Throwable cause) {
		super(cause);
	}

	public ComponentCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ComponentCreationException(String message) {
		super(message);
	}
}
