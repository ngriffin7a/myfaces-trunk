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
import java.util.Enumeration;
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
 * 
 * $Log$
 * Revision 1.6  2004/03/30 05:34:56  dave0000
 * change entrySet() to not use HashMap and avoid copying data
 *
 */
public abstract class AbstractAttributeMap
    implements Map
{
    private Set              _keySet;
    private Collection       _values;
    private Set              _entrySet;

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
        return (_entrySet != null) ? _entrySet : (_entrySet = new EntrySet());
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
            return new KeyIterator();
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
        protected final Enumeration _e = getAttributeNames();
        protected Object            _currentKey;

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
            _currentKey = null;
            return _currentKey = _e.nextElement();
        }
    }

    private class Values extends KeySet
    {
        public Iterator iterator()
        {
            return new ValuesIterator();
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
        public Object next()
        {
            super.next();
            return AbstractAttributeMap.this.get(_currentKey);
        }
    }

    private class EntrySet extends KeySet
    {
        public Iterator iterator() {
            return new EntryIterator();
        }
        
        public boolean contains(Object o) {
            if (!(o instanceof Entry))
            {
                return false;
            }
            
            Entry entry = (Entry) o;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null)
            {
                return false;
            }
            
            return value.equals(AbstractAttributeMap.this.get(key));
        }
        
        public boolean remove(Object o) {
            if (!(o instanceof Entry))
            {
                return false;
            }
            
            Entry entry = (Entry) o;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null 
                || !value.equals(AbstractAttributeMap.this.get(key)))
            {
                return false;
            }
            
            return AbstractAttributeMap.this.remove(((Entry) o).getKey()) != null;
        }
    }

    private class EntryIterator extends KeyIterator
    {
        public Object next()
        {
            super.next();
            // Must create new Entry every time, since those are supposed to be
            // immutable
            return new EntrySetEntry(_currentKey);
        }
        
    }

    private class EntrySetEntry implements Entry
    {
        private final Object _currentKey;
        
        public EntrySetEntry(Object currentKey)
        {
            _currentKey = currentKey;
        }
        
        public Object getKey()
        {
            return _currentKey;
        }

        public Object getValue()
        {
            return AbstractAttributeMap.this.get(_currentKey);
        }

        public Object setValue(Object value)
        {
            return AbstractAttributeMap.this.put(_currentKey, value);
        }
    }
}
