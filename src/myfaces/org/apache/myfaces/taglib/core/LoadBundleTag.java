/*
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
package net.sourceforge.myfaces.taglib.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.*;

/**
 * TODO:
 * We should find a way to save loaded bundles in the state, because otherwise
 * on the next request the bundle map will not be present before the render phase
 * and value bindings that reference to the bundle will always log annoying
 * "Variable 'xxx' could not be resolved" error messages.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LoadBundleTag
        extends TagSupport
{
    private static final Log log = LogFactory.getLog(LoadBundleTag.class);

    private String _basename;
    private String _var;

    public void setBasename(String basename)
    {
        _basename = basename;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public int doStartTag() throws JspException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            throw new JspException("No faces context?!");
        }

        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot == null)
        {
            throw new JspException("No view root! LoadBundle must be nested inside <f:view> action.");
        }

        Locale locale = viewRoot.getLocale();
        if (locale == null)
        {
            locale = facesContext.getApplication().getDefaultLocale();
        }

        final ResourceBundle bundle;
        try
        {
            bundle = ResourceBundle.getBundle(_basename, locale);
        }
        catch (MissingResourceException e)
        {
            log.error("Resource bundle '" + _basename + "' could not be found.");
            return Tag.SKIP_BODY;
        }

        facesContext.getExternalContext().getRequestMap().put(_var,
                                                              new BundleMap(bundle));
        return Tag.SKIP_BODY;
    }


    private static class BundleMap implements Map
    {
        private ResourceBundle _bundle;
        private List _values;

        public BundleMap(ResourceBundle bundle)
        {
            _bundle = bundle;
        }

        //Optimized methods

        public Object get(Object key)
        {
            try {
                return _bundle.getObject(key.toString());
            } catch (Exception e) {
                return "MISSING: " + key + " :MISSING";
            }
        }

        public boolean isEmpty()
        {
            return !_bundle.getKeys().hasMoreElements();
        }

        public boolean containsKey(Object key)
        {
            return _bundle.getObject(key.toString()) != null;
        }


        //Unoptimized methods

        public Collection values()
        {
            if (_values == null)
            {
                _values = new ArrayList();
                for (Enumeration enum = _bundle.getKeys(); enum.hasMoreElements(); )
                {
                    String v = _bundle.getString((String)enum.nextElement());
                    _values.add(v);
                }
            }
            return _values;
        }

        public int size()
        {
            return values().size();
        }

        public boolean containsValue(Object value)
        {
            return values().contains(value);
        }

        public Set entrySet()
        {
            Set set = new HashSet();
            for (Enumeration enum = _bundle.getKeys(); enum.hasMoreElements(); )
            {
                final String k = (String)enum.nextElement();
                set.add(new Map.Entry() {
                    public Object getKey()
                    {
                        return k;
                    }

                    public Object getValue()
                    {
                        return _bundle.getObject(k);
                    }

                    public Object setValue(Object value)
                    {
                        throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
                    }
                });
            }
            return set;
        }

        public Set keySet()
        {
            Set set = new HashSet();
            for (Enumeration enum = _bundle.getKeys(); enum.hasMoreElements(); )
            {
                set.add(enum.nextElement());
            }
            return set;
        }


        //Unsupported methods

        public Object remove(Object key)
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public void putAll(Map t)
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public Object put(Object key, Object value)
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public void clear()
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

    }

}
