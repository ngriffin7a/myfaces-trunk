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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.myfaces.MyFacesBaseTest;

/**
 * Test for AbstractAttributeMap core functionality through ApplicationMap.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.4  2004/04/02 13:57:18  bdudney
 * cleaned up the tests so they all run and added 
execution to the build.xml file.
 *
 * Revision 1.3  2004/03/30 05:38:50  dave0000
 * add copyright statement
 *
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
