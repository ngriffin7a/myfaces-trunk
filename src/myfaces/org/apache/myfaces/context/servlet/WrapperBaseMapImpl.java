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
package net.sourceforge.myfaces.context.servlet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.myfaces.exception.OperationNotSupportedException;

/**
 * Base class for wrapper implementations that expose context relative information(eg. HttpSessionAttributes)
 * as a java.util.Map interface.
 */
public class WrapperBaseMapImpl implements Map {

	private static Log log = LogFactory.getLog(WrapperBaseMapImpl.class);

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		String msg = "This operation is not supported, nor is it mandated by the JSF specs...";
		log.info(msg);
		throw new OperationNotSupportedException(msg);
	}
}