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
package net.sourceforge.myfaces.context.servlet;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * A wrapper that exposes request headers as a java Collections API
 * Map object.
 * 
 * @author Dimitry D'hondt
 */
public class RequestHeaderMap extends WrapperBaseMapImpl {
	private HttpServletRequest req;
	
	RequestHeaderMap(HttpServletRequest req) {
		this.req = req;
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		boolean ret = false;
		if(key instanceof String) {
			ret = req.getHeader((String)key) == null;
		}
		return ret;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object findValue) {
		boolean ret = false;
		Enumeration e = req.getHeaderNames();
		while(e.hasMoreElements()) {
			String element = (String) e.nextElement();
			Object value = req.getHeader(element);
			if(value != null && value.equals(findValue)) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		Set ret = new HashSet();
		
		Enumeration e = req.getHeaderNames();
		while(e.hasMoreElements()) {
			ret.add(req.getHeader((String)e.nextElement()));
		}
		
		return ret;
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		Object ret = null;
		
		if(key instanceof String) {
			ret = req.getHeader((String)key);
		}
		
		return ret;
	}
	
	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		boolean ret = true;
		
		if(req.getHeaderNames().hasMoreElements()) ret = false;
		
		return ret;
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		Set ret = new HashSet();
		
		Enumeration e = req.getHeaderNames();
		while(e.hasMoreElements()) {
			ret.add(e.nextElement());
		}
		
		return ret;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		return null;
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t) {
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		return null;
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size() {
		int ret = 0;
		
		Enumeration e = req.getHeaderNames();
		while(e.hasMoreElements()) {
			ret ++;
			e.nextElement();
		}
		
		return ret;
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		return entrySet();
	}
}