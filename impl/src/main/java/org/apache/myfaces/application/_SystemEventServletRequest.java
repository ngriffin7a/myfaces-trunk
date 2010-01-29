/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.application;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 * Dummy request for various system event listeners
 *
 * the problem with the system event listeners is that they
 * are triggered often outside of an existing request
 * hence we have to provide dummy objects
 */


public class _SystemEventServletRequest extends HttpServletRequestWrapper{

    Map<String, Object> _attributesMap = new HashMap<String, Object>();
    public _SystemEventServletRequest()
    {
        super( (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class[] { HttpServletRequest.class },
                new InvocationHandler()
                {
                    public Object invoke(Object proxy, Method m, Object[] args) 
                    {
                        throw new UnsupportedOperationException("This request class is an empty placeholder");
                    }
                }));
    }

    public Object getAttribute(String s) {
       return  _attributesMap.get(s);
    }

    public void setAttribute(String s, Object o) {
        _attributesMap.put(s, o);
    }

    public void removeAttribute(String s) {
        _attributesMap.remove(s);
    }

    public String getServletPath()
    {
        return null;
    }

    public String getPathInfo()
    {
        return null;
    }

    @Override
    public HttpSession getSession()
    {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create)
    {
        return null;
    }
    
}