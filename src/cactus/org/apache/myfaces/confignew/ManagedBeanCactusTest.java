/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.confignew;

import java.util.List;
import java.util.Map;
import javax.faces.el.VariableResolver;

import net.sourceforge.myfaces.cactus.TestBean;
import net.sourceforge.myfaces.cactus.MyFacesServletTestCase;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class ManagedBeanCactusTest extends MyFacesServletTestCase
{

    private VariableResolver variableResolver;


    protected void setUp() throws Exception
    {
        super.setUp();
        variableResolver = getContext().getApplication().getVariableResolver();
    }


    public void testManagedProperties()
    {
        TestBean testManagedProperties = (TestBean) variableResolver.resolveVariable(getContext(), "testManagedProperties");

        assertNotNull(testManagedProperties);
        assertEquals(5, testManagedProperties.getNumberPrimitiveLong());
        assertEquals(99L, testManagedProperties.getNumberLong().longValue());
        assertNull(testManagedProperties.getValueWithDefault());

        List list = testManagedProperties.getList();

        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(29, ((Integer) list.get(0)).intValue());
        assertEquals(99, ((Integer) list.get(1)).intValue());
        assertNull(list.get(2));
        assertEquals(23, ((Integer) list.get(3)).intValue());

        Map map = testManagedProperties.getMap();

        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals(25, ((Integer) map.get("key1")).intValue());
        assertEquals(29, ((Integer) map.get("test")).intValue());
        assertEquals(99, ((Integer) map.get("test44")).intValue());
        assertNull(map.get("nullValue"));
        assertTrue(map.containsKey("nullValue"));
    }


    public void testManagedList()
    {
        List list = (List) variableResolver.resolveVariable(getContext(), "testList");

        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(29, ((Integer) list.get(0)).intValue());
        assertEquals(99, ((Integer) list.get(1)).intValue());
        assertNull(list.get(2));
        assertEquals(23, ((Integer) list.get(3)).intValue());
    }


    public void testManagedMap()
    {
        Map map = (Map) variableResolver.resolveVariable(getContext(), "testMap");

        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals(25, ((Integer) map.get("key1")).intValue());
        assertEquals(29, ((Integer) map.get("test")).intValue());
        assertEquals(99, ((Integer) map.get("test44")).intValue());
        assertNull(map.get("nullValue"));
        assertTrue(map.containsKey("nullValue"));
    }

    public void testAdditional()
     {
         TestBean testAdditional = (TestBean) variableResolver.resolveVariable(getContext(), "testAdditional");

         assertNotNull(testAdditional);
         assertEquals(5, testAdditional.getNumberPrimitiveLong());
         assertEquals(99L, testAdditional.getNumberLong().longValue());
         assertNull(testAdditional.getValueWithDefault());

         List list = testAdditional.getList();

         assertNotNull(list);
         assertEquals(4, list.size());
         assertEquals(29, ((Integer) list.get(0)).intValue());
         assertEquals(99, ((Integer) list.get(1)).intValue());
         assertNull(list.get(2));
         assertEquals(23, ((Integer) list.get(3)).intValue());

         Map map = testAdditional.getMap();

         assertNotNull(map);
         assertEquals(4, map.size());
         assertEquals(25, ((Integer) map.get("key1")).intValue());
         assertEquals(29, ((Integer) map.get("test")).intValue());
         assertEquals(99, ((Integer) map.get("test44")).intValue());
         assertNull(map.get("nullValue"));
         assertTrue(map.containsKey("nullValue"));
     }

}
