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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.myfaces.exception.InternalServerException;

/**
 * Wrapper object that exposes the ServletContext attributes as a collections API
 * Map interface.
 * 
 * @author Dimitry D'hondt
 */
public class RequestMap implements Map {
	private static Log log = LogFactory.getLog(RequestMap.class);
	private ServletRequest req;

	RequestMap(ServletRequest req) {
		this.req = req;
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		List names = new ArrayList();
		Enumeration e = req.getAttributeNames();
		while(e.hasMoreElements()) {
			names.add(e.nextElement());
		}
		Iterator i = names.iterator();
		while(i.hasNext()) {
			req.removeAttribute((String)i.next());
		}
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		boolean ret = false;
		if(key instanceof String) {
			ret = req.getAttribute((String)key) == null;
		}
		return ret;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object findValue) {
		boolean ret = false;
		Enumeration e = req.getAttributeNames();
		while(e.hasMoreElements()) {
			String element = (String) e.nextElement();
			Object value = req.getAttribute(element);
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
		
		Enumeration e = req.getAttributeNames();
		while(e.hasMoreElements()) {
			ret.add(req.getAttribute((String)e.nextElement()));
		}
		
		return ret;
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		Object ret = null;
		
		if(key instanceof String) {
			ret = req.getAttribute((String)key);
		}
		
		return ret;
	}
	
	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		boolean ret = true;
		
		if(req.getAttributeNames().hasMoreElements()) ret = false;
		
		return ret;
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		Set ret = new HashSet();
		
		Enumeration e = req.getAttributeNames();
		while(e.hasMoreElements()) {
			ret.add(e.nextElement());
		}
		
		return ret;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		Object ret = null;
		
		if(!(key instanceof String)) {
			throw new InternalServerException("The keys on the request map should allways be java.lang.String objects.");
		}

		ret = req.getAttribute((String)key);
		req.setAttribute((String)key,value);
		
		return ret;
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t) {
		for (Iterator iter = t.keySet().iterator(); iter.hasNext();) {
			Object elem = iter.next();

			if(!(elem instanceof String)) {
				throw new InternalServerException("The keys on the request map should allways be java.lang.String objects.");
			}

			String key = (String) elem;
			put(key,t.get(elem));
		}
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		Object ret = null;

		if(!(key instanceof String)) {
			throw new InternalServerException("The keys on the request map should allways be java.lang.String objects.");
		}
		
		ret = req.getAttribute((String)key);
		req.removeAttribute((String)key);
		
		return ret;
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size() {
		int ret = 0;
		
		Enumeration e = req.getAttributeNames();
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