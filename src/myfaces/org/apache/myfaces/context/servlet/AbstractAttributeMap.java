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

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Helper Map implementation for use with different Attribute Maps.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractAttributeMap
    implements Map
{
    private Set              _keySet;
    private Collection       _values;

    public void clear()
    {
        List names = new ArrayList();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();)
        {
            names.add(e.nextElement());
        }

        for (Iterator it = names.iterator(); it.hasNext();)
        {
            removeAttribute((String) it.next());
        }
    }

    public boolean containsKey(Object key)
    {
        return getAttribute(key.toString()) != null;
    }

    public boolean containsValue(Object findValue)
    {
        if (findValue == null)
        {
            return false;
        }

        for (Enumeration e = getAttributeNames(); e.hasMoreElements();)
        {
            Object value = getAttribute((String) e.nextElement());
            if (findValue.equals(value))
            {
                return true;
            }
        }

        return false;
    }

    public Set entrySet()
    {
        EntrySetMap entryMap = new EntrySetMap();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();)
        {
            String key = (String) e.nextElement();
            entryMap.putInternal(key, getAttribute(key));
        }
        return entryMap.entrySet();
    }

    public Object get(Object key)
    {
        return getAttribute(key.toString());
    }

    public boolean isEmpty()
    {
        return !getAttributeNames().hasMoreElements();
    }

    public Set keySet()
    {
        return (_keySet != null) ? _keySet : (_keySet = new KeySet());
    }

    public Object put(Object key, Object value)
    {
        String key_ = key.toString();
        Object retval = getAttribute(key_);
        setAttribute(key_, value);
        return retval;
    }

    public void putAll(Map t)
    {
        for (Iterator it = t.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            setAttribute(entry.getKey().toString(), entry.getValue());
        }
    }

    public Object remove(Object key)
    {
        String key_ = key.toString();
        Object retval = getAttribute(key_);
        removeAttribute(key_);
        return retval;
    }

    public int size()
    {
        int size = 0;
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();)
        {
            size++;
            e.nextElement();
        }
        return size;
    }

    public Collection values()
    {
        return (_values != null) ? _values : (_values = new Values());
    }


    abstract protected Object getAttribute(String key);

    abstract protected void setAttribute(String key, Object value);

    abstract protected void removeAttribute(String key);

    abstract protected Enumeration getAttributeNames();


    private class KeySet extends AbstractSet
    {
        public Iterator iterator()
        {
            return new KeyIterator(getAttributeNames());
        }

        public boolean isEmpty()
        {
            return AbstractAttributeMap.this.isEmpty();
        }

        public int size()
        {
            return AbstractAttributeMap.this.size();
        }

        public boolean contains(Object o)
        {
            return AbstractAttributeMap.this.containsKey(o);
        }

        public boolean remove(Object o)
        {
            return AbstractAttributeMap.this.remove(o) != null;
        }

        public void clear()
        {
            AbstractAttributeMap.this.clear();
        }
    }

    private class KeyIterator
        implements Iterator
    {
        protected final Enumeration _e;
        protected Object            _currentKey;

        public KeyIterator(Enumeration e)
        {
            _e = e;
        }

        public void remove()
        {
            // remove() may cause ConcurrentModificationException.
            // We could throw an exception here, but not throwing an exception
            //   allows one call to remove() to succeed
            if (_currentKey == null)
            {
                throw new NoSuchElementException(
                    "You must call next() at least once");
            }
            AbstractAttributeMap.this.remove(_currentKey);
        }

        public boolean hasNext()
        {
            return _e.hasMoreElements();
        }

        public Object next()
        {
            return _currentKey = _e.nextElement();
        }
    }

    private class Values extends KeySet
    {
        public Iterator iterator()
        {
            return new ValuesIterator(getAttributeNames());
        }

        public boolean contains(Object o)
        {
            return AbstractAttributeMap.this.containsValue(o);
        }
        
        public boolean remove(Object o)
        {
            if (o == null)
            {
                return false;
            }
            
            for (Iterator it = iterator(); it.hasNext();)
            {
                if (o.equals(it.next()))
                {
                    it.remove();
                    return true;
                }
            }
            
            return false;
        }
    }

    private class ValuesIterator extends KeyIterator
    {
        public ValuesIterator(Enumeration e)
        {
            super(e);
        }

        public Object next()
        {
            _currentKey = null;
            return AbstractAttributeMap.this.get(_currentKey = _e.nextElement());
        }
        
        public void remove()
        {
            AbstractAttributeMap.this.remove(_currentKey);
        }
    }

    private class EntrySetMap extends HashMap {
        Set _entrySet;
        
        public void putInternal(Object key, Object value)
        {
            super.put(key, value);
        }
        
        public Object put(Object key, Object value)
        {
            Object ret = AbstractAttributeMap.this.put(key, value);
            if (ret != super.put(key, value))
            {
                throw new ConcurrentModificationException(
                    "EntrySetMap and AttributeMap put() return values are not the same");
            }
            return ret;
        }
        
        public void putAll(Map m)
        {
            AbstractAttributeMap.this.putAll(m);
            super.putAll(m);
        }
        
        public Object remove(Object key)
        {
            // AttributeMap.remove() must be called first to 
            // abort remove when it throws UnsupportedOperationException
            Object ret = AbstractAttributeMap.this.remove(key);
            if (ret != super.remove(key))
            {
                throw new ConcurrentModificationException(
                    "EntrySetMap and AttributeMap remove() return values are not the same");
            }
            return ret;
        }
        
        public void clear()
        {
            AbstractAttributeMap.this.clear();
            super.clear();
        }
        
        public Set entrySet() {
            Set es = _entrySet;
            return (es != null ? es : (_entrySet = new EntrySetMapEntrySet()));
        }

        private class EntrySetMapEntrySet extends AbstractSet
        {
            final Set entrySet = EntrySetMap.super.entrySet();
            
            public Iterator iterator() {
                return new EntryIterator(entrySet.iterator());
            }
            
            public boolean contains(Object o) {
                return entrySet.contains(o);
            }
            
            public boolean remove(Object o) {
                if (!(o instanceof Entry))
                {
                    return false;
                }
                return EntrySetMap.this.remove(((Entry) o).getKey()) != null;
            }
            
            public int size() {
                return entrySet.size();
            }
            
            public void clear() {
                EntrySetMap.this.clear();
            }
            
        }
    }

    private class EntryIterator implements Iterator
    {
        private final Iterator _it; 
        private Object _current;

        public EntryIterator(Iterator it)
        {
            _it = it;
        }
        
        public void remove()
        {
            if (_current == null)
            {
                throw new IllegalStateException("must call next() at least once");
            }
            AbstractAttributeMap.this.remove(((Entry) _current).getKey());
            _it.remove();
        }

        public boolean hasNext()
        {
            return _it.hasNext();
        }

        public Object next()
        {
            return _current = _it.next();
        }
    }
}
