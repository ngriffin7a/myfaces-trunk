package net.sourceforge.myfaces.context.servlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import net.sourceforge.myfaces.MyFacesTest;


public class CookieMapTest extends MyFacesTest
{
    public CookieMapTest(String name)
    {
        super(name);
    }
    
    protected Cookie[] getCookies()
    {
        Cookie[] cookies = new Cookie[5];
        
        Cookie cookie = new Cookie("cookie0", "cookie0.value");
        cookies[0] = cookie;
        
        cookie = new Cookie("cookie1", "cookie1.value");
        cookies[1] = cookie;
        
        cookie = new Cookie("cookie2", "cookie2.value");
        cookies[2] = cookie;
        
        cookie = new Cookie("cookie3", "cookie3.value");
        cookies[3] = cookie;
        
        cookie = new Cookie("cookie4", "cookie4.value");
        cookies[4] = cookie;
        
        return cookies;
    }
    
    public void testCookies()
    {
        Map appMap = _facesContext.getExternalContext().getRequestCookieMap();
        
        assertFalse(appMap.isEmpty());
        assertEquals(5, appMap.size());
    }

    public void testEntrySet()
    {
        Map cookieMap = _facesContext.getExternalContext().getRequestCookieMap();
        Map cache = new HashMap();
        
        for (Iterator it = cookieMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            assertNull(cache.put(entry.getKey(), entry.getValue()));
        }

        for (Iterator it = cookieMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            assertSame(entry.getValue(), cache.put(entry.getKey(), entry.getValue()));
        }
        
        assertSame("cookie1.value", cache.get("cookie1"));
    }

    public void testKeySet()
    {
        Map cookieMap = _facesContext.getExternalContext().getRequestCookieMap();
        Map cache = new HashMap();
        
        for (Iterator it = cookieMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            assertNull(cache.put(key, cookieMap.get(key)));
        }

        for (Iterator it = cookieMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            assertSame(cookieMap.get(key), cache.put(key, cookieMap.get(key)));
        }

        assertSame("cookie1.value", cache.get("cookie1"));
    }

    public void testValues()
    {
        Map cookieMap = _facesContext.getExternalContext().getRequestCookieMap();
        Set cache = new HashSet();
        
        for (Iterator it = cookieMap.values().iterator(); it.hasNext();)
        {
            assertTrue(cache.add(it.next()));
        }

        for (Iterator it = cookieMap.values().iterator(); it.hasNext();)
        {
            assertFalse(cache.add(it.next()));
        }
        
        assertTrue(cache.contains("cookie1.value"));
    }
}
