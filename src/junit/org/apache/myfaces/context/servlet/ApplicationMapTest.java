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
package org.apache.myfaces.context.servlet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.myfaces.MyFacesBaseTest;

/**
 * Test for AbstractAttributeMap core functionality through ApplicationMap.
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationMapTest extends MyFacesBaseTest
{
    private Object _test0 = new Object();
    private Object _test1 = new Object();

    public ApplicationMapTest(String name)
    {
        super(name);
    }

    private void init()
    {
        Map appMap = _facesContext.getExternalContext().getApplicationMap();

        appMap.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());

        assertNull(appMap.put("test1", _test1));

        assertFalse(appMap.isEmpty());
        assertEquals(1, appMap.size());

        Object test2 = new Object();
        assertNull(appMap.put("test2", test2));

        Object test3 = new Object();
        assertNull(appMap.put("test3", test3));

        Object test4 = new Object();
        assertNull(appMap.put("test4", test4));

        assertEquals(4, appMap.size());

        assertSame(_test1, appMap.put("test1", test2));
        assertSame(test2, appMap.put("test1", _test1));

        assertEquals(4, appMap.size());

        assertSame(test4, appMap.remove("test4"));
        assertEquals(3, appMap.size());
        assertNull(appMap.put("test4", test4));
        assertEquals(4, appMap.size());
    }

    public void testClear()
    {
        // Map
        init();
        Map appMap = _facesContext.getExternalContext().getApplicationMap();

        assertFalse(appMap.isEmpty());

        appMap.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());

        // EntrySet
        init();
        Set entrySet = appMap.entrySet();

        assertFalse(entrySet.isEmpty());

        entrySet.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());
        assertTrue(entrySet.isEmpty());
        assertEquals(0, entrySet.size());

        // KeySet
        init();
        Set keySet = appMap.keySet();

        assertFalse(keySet.isEmpty());

        keySet.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());
        assertTrue(keySet.isEmpty());
        assertEquals(0, keySet.size());

        // Values
        init();
        Collection values = appMap.values();

        assertFalse(values.isEmpty());

        values.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());
        assertTrue(values.isEmpty());
        assertEquals(0, values.size());
    }

    public void testEntrySet()
    {
        init();
        Map appMap = _facesContext.getExternalContext().getApplicationMap();
        Map cache = new HashMap();

        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            assertNull(cache.put(entry.getKey(), entry.getValue()));
        }

        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            assertSame(entry.getValue(), cache.put(entry.getKey(), entry.getValue()));
        }

        assertSame(_test1, cache.get("test1"));

        int size = appMap.size();

        assertNull(appMap.put("test0", _test0));
        assertEquals(size + 1, appMap.size());

        findTest0: {
            for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
            {
                Entry entry = (Entry) it.next();
                if (entry.getKey().equals("test0"))
                {
                    assertTrue(appMap.entrySet().contains(entry));
                    assertTrue(appMap.entrySet().remove(entry));
                    assertEquals(size, appMap.size());
                    assertNull(appMap.remove(_test0));
                    assertFalse(appMap.entrySet().remove(new Object()));
                    assertEquals(size, appMap.size());
                    break findTest0;
                }
            }
            throw new IllegalStateException("Test0 not found");
        }

        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            // Only one remove() will succeed
            it.next();
            it.remove();
            break;
        }

        assertEquals(size - 1, appMap.size());
        assertEquals(size - 1, appMap.entrySet().size());
    }

    public void testKeySet()
    {
        init();
        Map appMap = _facesContext.getExternalContext().getApplicationMap();
        Map cache = new HashMap();

        for (Iterator it = appMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            assertNull(cache.put(key, appMap.get(key)));
        }

        for (Iterator it = appMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            assertSame(appMap.get(key), cache.put(key, appMap.get(key)));
        }

        assertSame(_test1, cache.get("test1"));

        int size = appMap.size();

        assertNull(appMap.put("test0", _test0));
        assertEquals(size + 1, appMap.size());

        findTest0: {
            for (Iterator it = appMap.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
                if (key.equals("test0"))
                {
                    assertTrue(appMap.keySet().contains(key));
                    assertTrue(appMap.keySet().remove(key));
                    assertEquals(size, appMap.size());
                    assertNull(appMap.remove(_test0));
                    assertFalse(appMap.keySet().remove(new Object()));
                    assertEquals(size, appMap.size());
                    break findTest0;
                }
            }
            throw new IllegalStateException("Test0 not found");
        }

        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            // Only one remove() will succeed
            it.next();
            it.remove();
            break;
        }

        assertEquals(size - 1, appMap.size());
        assertEquals(size - 1, appMap.keySet().size());
    }

    public void testValues()
    {
        init();
        Map appMap = _facesContext.getExternalContext().getApplicationMap();
        Set cache = new HashSet();

        for (Iterator it = appMap.values().iterator(); it.hasNext();)
        {
            assertTrue(cache.add(it.next()));
        }

        for (Iterator it = appMap.values().iterator(); it.hasNext();)
        {
            assertFalse(cache.add(it.next()));
        }

        assertTrue(cache.contains(_test1));

        int size = appMap.size();

        assertNull(appMap.put("test0", _test0));
        assertEquals(size + 1, appMap.size());

        findTest0: {
            for (Iterator it = appMap.values().iterator(); it.hasNext();)
            {
                Object value = it.next();
                if (value.equals(_test0))
                {
                    assertTrue(appMap.values().contains(value));
                    assertTrue(appMap.values().remove(value));
                    assertEquals(size, appMap.size());
                    assertNull(appMap.remove(_test0));
                    assertFalse(appMap.keySet().remove(new Object()));
                    assertEquals(size, appMap.size());
                    break findTest0;
                }
            }
            throw new IllegalStateException("Test0 not found");
        }

        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            // Only one remove() will succeed
            it.next();
            it.remove();
            break;
        }

        assertEquals(size - 1, appMap.size());
        assertEquals(size - 1, appMap.values().size());
    }
}
