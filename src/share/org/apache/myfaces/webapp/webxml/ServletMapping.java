/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.webapp.webxml;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMapping
{
    private String _servletName;
    private Class _servletClass;
    private String _urlPattern;
    private boolean _isExtensionMapping = false;

    public ServletMapping(String servletName,
                          Class servletClass,
                          String urlPattern)
    {
        _servletName = servletName;
        _servletClass = servletClass;
        _urlPattern = urlPattern;
        if (_urlPattern != null)
        {
            if (_urlPattern.startsWith("*."))
            {
                _isExtensionMapping = true;
            }
        }
    }

    public boolean isExtensionMapping()
    {
        return _isExtensionMapping;
    }

    public String getServletName()
    {
        return _servletName;
    }

    public Class getServletClass()
    {
        return _servletClass;
    }

    public String getUrlPattern()
    {
        return _urlPattern;
    }
}
