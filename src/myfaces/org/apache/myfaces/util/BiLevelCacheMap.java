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
 * overhead. The method has a limitation that values cannot be deleted/updated.
 * <p>
 * Access to L1 map is not sychronized, to L2 map is synchronized. New values
 * are first stored in L2. Once there have been more that a specified mumber of
 * misses on L1, L1 and L2 maps are merged and the new map assigned to L1
 * (and L2 cleared).
 * </p>
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class BiLevelCacheMap implements Map
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Map       _cacheL1;
    private final Map _cacheL2;
    private final int _mergeThreshold;
    private int       _missCount;

    //~ Constructors -------------------------------------------------------------------------------

    public BiLevelCacheMap(int initialSizeL1, int initialSizeL2, int mergeThreshold)
    {
        _cacheL1            = new HashMap(initialSizeL1);
        _cacheL2            = new HashMap(initialSizeL2);
        _mergeThreshold     = mergeThreshold;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public boolean isEmpty()
    {
        throw new UnsupportedOperationException();
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key)
    {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value)
    {
        throw new UnsupportedOperationException();
    }

    public Set entrySet()
    {
        throw new UnsupportedOperationException();
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
                _cacheL2.put(key, retval);
            }

            if (++_missCount >= _mergeThreshold)
            {
                Map newMap;
                synchronized (cacheL1)
                {
                    // synchronized to guarantee _cacheL1 will be assigned after fully initialized
                    // at least until JVM 1.5 where this should be possible with the volatile keyword
                    // But is this enough (in our particular case) to resolve the issues with DCL?
                    newMap = HashMapUtils.merge(_cacheL1, _cacheL2);
                }
                _cacheL1 = newMap;
                _cacheL2.clear();
                _missCount = 0;
            }
        }
        return retval;
    }

    public Set keySet()
    {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value)
    {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map t)
    {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key)
    {
        throw new UnsupportedOperationException();
    }

    public int size()
    {
        throw new UnsupportedOperationException();
    }

    public Collection values()
    {
        throw new UnsupportedOperationException();
    }

    protected abstract Object newInstance(Object key);
}
