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
package net.sourceforge.myfaces.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A bi-level cache based on HashMap for caching objects with minimal sychronization
 * overhead. The limitation is that <code>remove()</code> is very expensive.
 * <p>
 * Access to L1 map is not sychronized, to L2 map is synchronized. New values
 * are first stored in L2. Once there have been more that a specified mumber of
 * misses on L1, L1 and L2 maps are merged and the new map assigned to L1
 * and L2 cleared.
 * </p>
 * <p>
 * IMPORTANT:entrySet(), keySet(), and values() return snapshot collections.  
 * </p>
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class BiLevelCacheMap implements Map
{
    //~ Instance fields ----------------------------------------------------------------------------

    /** To preinitialize <code>_cacheL1</code> with default values use an initialization block */
    protected Map       _cacheL1;
    
    /** Must be final because it is used for synchronization */
    private final Map   _cacheL2;
    private final int   _mergeThreshold;
    private int         _missCount;
    private final int   _initialSizeL1;

    //~ Constructors -------------------------------------------------------------------------------

    public BiLevelCacheMap(int initialSizeL1, int initialSizeL2, int mergeThreshold)
    {
        _cacheL1            = new HashMap(initialSizeL1);
        _cacheL2            = new HashMap(initialSizeL2);
        _mergeThreshold     = mergeThreshold;
        _initialSizeL1      = initialSizeL1;
    }

    //~ Methods ------------------------------------------------------------------------------------
    
    public boolean isEmpty()
    {
        synchronized (_cacheL2) {
            return _cacheL1.isEmpty() && _cacheL2.isEmpty();
        }
    }

    public void clear()
    {
        synchronized (_cacheL2) {
            _cacheL1 = new HashMap(_initialSizeL1);
            _cacheL2.clear();
        }
    }

    public boolean containsKey(Object key)
    {
        synchronized (_cacheL2) {
            return _cacheL1.containsKey(key) || _cacheL2.containsKey(key);
        }
    }

    public boolean containsValue(Object value)
    {
        synchronized (_cacheL2) {
            return _cacheL1.containsValue(value) || _cacheL2.containsValue(value);
        }
    }

    public Set entrySet()
    {
        synchronized (_cacheL2) 
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }

            return _cacheL1.entrySet();
        }
    }

    public Object get(Object key)
    {
        Map    cacheL1 = _cacheL1;
        Object retval = cacheL1.get(key);
        if (retval != null)
        {
            return retval;
        }

        synchronized (_cacheL2)
        {
            // Has another thread merged caches while we were waiting on the mutex? Then check L1 again
            if (cacheL1 != _cacheL1)
            {
                if ((retval = _cacheL1.get(key)) != null)
                {
                    // do not update miss count (it is not a miss anymore)
                    return retval;
                }
            }

            if ((retval = _cacheL2.get(key)) == null)
            {
                retval = newInstance(key);
                if (retval != null)
                {
                    put(key, retval);
                }
            }

            if (retval != null)
            {
                // do not merge if no new instance created
                mergeIfNeeded();
            }
        }
        
        return retval;
    }

    public Set keySet()
    {
        synchronized (_cacheL2) 
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }

            return _cacheL1.keySet();
        }
    }

    public Object put(Object key, Object value)
    {
        synchronized (_cacheL2)
        {
            _cacheL2.put(key, value);
        }
        
        return value;
    }

    public void putAll(Map map)
    {
        synchronized (_cacheL2)
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }
            
            merge(map);
        }
    }

    /** This operation is very expensive. A full copy of the Map is created */
    public Object remove(Object key)
    {
        synchronized (_cacheL2)
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }
            
            Object retval;
            Map newMap;
            synchronized (_cacheL1)
            {
                // "dummy" synchronization to guarantee _cacheL1 will be assigned after fully initialized
                // at least until JVM 1.5 where this should be guaranteed by the volatile keyword
                // But is this enough (in our particular case) to resolve the issues with DCL?
                newMap = new HashMap(HashMapUtils.calcCapacity(_cacheL1.size()));
                newMap.putAll(_cacheL1);
                retval = newMap.remove(key);
            }
            
            _cacheL1 = newMap;
            return retval;
        }
    }

    public int size()
    {
        synchronized (_cacheL2) 
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }

            return _cacheL1.size();
        }
    }

    public Collection values()
    {
        synchronized (_cacheL2) 
        {
            if (_missCount > 0)
            {
                merge(_cacheL2);
            }

            return _cacheL1.values();
        }
    }
    
    private void mergeIfNeeded() 
    {
        if (++_missCount >= _mergeThreshold)
        {
            merge(_cacheL2);
        }
    }
    
    private void merge(Map map) 
    {
        Map newMap;
        synchronized (_cacheL1)
        {
            // "dummy" synchronization to guarantee _cacheL1 will be assigned after fully initialized
            // at least until JVM 1.5 where this should be guaranteed by the volatile keyword
            // But is this enough (in our particular case) to resolve the issues with DCL?
            newMap = HashMapUtils.merge(_cacheL1, map);
        }
        _cacheL1 = newMap;
        _cacheL2.clear();
        _missCount = 0;
    }

    /**
     * Subclasses must implement to have automatic creation of new instances
     * or alternatively can use <code>put<code> to add new items to the cache.<br>
     * 
     * Implementing this method is prefered to guarantee that there will be only
     * one instance per key ever created. Calling put() to add items in a multi-
     * threaded situation will require external synchronization to prevent two
     * isntances for the same key, which defeats the perpose of this cache 
     * (put() is useful when initiazlization is done during startup and items 
     * are not added during execution or when (temporarily) having possibly two 
     * or more instances of for the same key is not of concearn).<br>
     * 
     * @param key lookup key
     * @return new instace for the requested key
     */
    protected abstract Object newInstance(Object key);
}
