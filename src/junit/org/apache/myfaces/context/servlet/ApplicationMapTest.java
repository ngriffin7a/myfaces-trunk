package net.sourceforge.myfaces.context.servlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.myfaces.MyFacesTest;


public class ApplicationMapTest extends MyFacesTest
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
        init();
        Map appMap = _facesContext.getExternalContext().getApplicationMap();
        
        assertFalse(appMap.isEmpty());
        
        appMap.clear();

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());
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
        
        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            if (entry.getKey().equals("test0"))
            {
                assertTrue(appMap.entrySet().remove(entry));
                assertEquals(size, appMap.size());
                assertNull(appMap.remove(_test0));
                assertFalse(appMap.entrySet().remove(new Object()));
                assertEquals(size, appMap.size());
                break;
            }
        }
        
        for (Iterator it = appMap.entrySet().iterator(); it.hasNext();)
        {
            it.next();
            it.remove();
        }

        assertTrue(appMap.isEmpty());
        assertEquals(0, appMap.size());
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
        
        for (Iterator it = appMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            if (key.equals("test0"))
            {
                assertTrue(appMap.keySet().remove(key));
                assertEquals(size, appMap.size());
                assertNull(appMap.remove(_test0));
                assertFalse(appMap.keySet().remove(new Object()));
                assertEquals(size, appMap.size());
                break;
            }
        }
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
        
        for (Iterator it = appMap.values().iterator(); it.hasNext();)
        {
            Object value = it.next();
            if (value.equals(_test0))
            {
                assertTrue(appMap.values().remove(value));
                assertEquals(size, appMap.size());
                assertNull(appMap.remove(_test0));
                assertFalse(appMap.keySet().remove(new Object()));
                assertEquals(size, appMap.size());
                break;
            }
        }
    }
}
