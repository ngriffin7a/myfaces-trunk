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
package javax.faces.component;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/07/01 22:01:37  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/04/06 13:02:48  manolito
 * junit tests for UISelectMany
 *
 */
public class UISelectManyTest
        extends TestCase
{
    //private static final Log log = LogFactory.getLog(UISelectManyTest.class);

    public void testComparePrimitiveArrays()
    {
        UISelectMany selectMany = new UISelectMany();
        int[] intArray1;
        int[] intArray2;
        boolean[] boolArray1;
        boolean[] boolArray2;

        // "false" cases (arrays are the same)
        intArray1 = new int[] {1, 2, 3};
        intArray2 = new int[] {2, 3, 1};
        assertFalse(selectMany.compareValues(intArray1, intArray2));
        assertFalse(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {1, 2, 3};
        intArray2 = new int[] {1, 2, 3};
        assertFalse(selectMany.compareValues(intArray1, intArray2));
        assertFalse(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {};
        intArray2 = new int[] {};
        assertFalse(selectMany.compareValues(intArray1, intArray2));
        assertFalse(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {1, 2, 2, 3, 3, 3, 4};
        intArray2 = new int[] {1, 4, 2, 3, 2, 3, 3};
        assertFalse(selectMany.compareValues(intArray1, intArray2));
        assertFalse(selectMany.compareValues(intArray2, intArray1));

        boolArray1 = new boolean[] {true, true, false, false, false};
        boolArray2 = new boolean[] {true, false, true, false, false};
        assertFalse(selectMany.compareValues(boolArray1, boolArray2));
        assertFalse(selectMany.compareValues(boolArray2, boolArray1));

        intArray1 = null;
        intArray2 = null;
        assertFalse(selectMany.compareValues(intArray1, intArray2));
        assertFalse(selectMany.compareValues(intArray2, intArray1));


        // "true" cases (arrays are different)

        intArray1 = new int[] {1, 2, 3};
        intArray2 = new int[] {1, 2, 3, 4};
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {};
        intArray2 = new int[] {1, 2, 3, 4};
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {1, 2, 3, 4};
        intArray2 = new int[] {1, 2, 5, 4};
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {1, 2, 2, 3, 3, 3, 4};
        intArray2 = new int[] {1, 2, 2, 2, 3, 3, 4};
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

        intArray1 = new int[] {1, 2, 2, 3, 3, 3, 4};
        intArray2 = new int[] {1, 4, 2, 3, 2, 3, 2};
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

        boolArray1 = new boolean[] {true, true, false, false, false};
        boolArray2 = new boolean[] {true, false, true, false, true};
        assertTrue(selectMany.compareValues(boolArray1, boolArray2));
        assertTrue(selectMany.compareValues(boolArray2, boolArray1));

        intArray1 = new int[] {1, 2, 3};
        intArray2 = null;
        assertTrue(selectMany.compareValues(intArray1, intArray2));
        assertTrue(selectMany.compareValues(intArray2, intArray1));

    }


    public void testCompareObjectArrays()
    {
        UISelectMany selectMany = new UISelectMany();
        Object[] array1;
        Object[] array2;
        int[] intArray;

        // "false" cases (arrays are the same)
        array1 = new Object[] {"1", "2", "3"};
        array2 = new Object[] {"2", "3", "1"};
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "3"};
        array2 = new Object[] {"1", "2", "3"};
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));

        array1 = new Object[] {};
        array2 = new Object[] {};
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "2", "3", "3", "3", "4"};
        array2 = new Object[] {"1", "4", "2", "3", "2", "3", "3"};
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", new Long(2), "2", "3", "3", "3", "4"};
        array2 = new Object[] {"1", "4", "2", "3", new Long(2), "3", "3"};
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));

        array1 = null;
        array2 = null;
        assertFalse(selectMany.compareValues(array1, array2));
        assertFalse(selectMany.compareValues(array2, array1));


        // "true" cases (arrays are different)

        array1 = new Object[] {"1", "2", "3"};
        array2 = new Object[] {"1", "2", "3", "4"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {};
        array2 = new Object[] {"1", "2", "3", "4"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "3", "4"};
        array2 = new Object[] {"1", "2", "5", "4"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "2", "3", "3", "3", "4"};
        array2 = new Object[] {"1", "2", "2", "2", "3", "3", "4"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "2", "3", "3", "3", "4"};
        array2 = new Object[] {"1", "4", "2", "3", "2", "3", "2"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", new Long(2), "2", "3", "3", "3", "4"};
        array2 = new Object[] {"1", "4", "2", "3", new Integer(2), "3", "3"};
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {"1", "2", "3", "4"};
        intArray = new int[] {1, 2, 3, 4};
        assertTrue(selectMany.compareValues(array1, intArray));
        assertTrue(selectMany.compareValues(intArray, array1));

        array1 = new Object[] {"1", "2"};
        array2 = null;
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

        array1 = new Object[] {};
        array2 = null;
        assertTrue(selectMany.compareValues(array1, array2));
        assertTrue(selectMany.compareValues(array2, array1));

    }


    public void testCompareLists()
    {
        UISelectMany selectMany = new UISelectMany();
        List list1;
        List list2;
        int[] intArray;
        Object[] objArray;

        // "false" cases (arrays are the same)
        list1 = Arrays.asList(new Object[] {"1", "2", "3"});
        list2 = Arrays.asList(new Object[] {"2", "3", "1"});
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "3"});
        list2 = Arrays.asList(new Object[] {"1", "2", "3"});
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));

        list1 = new ArrayList();
        list2 = Collections.EMPTY_LIST;
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "2", "3", "3", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "4", "2", "3", "2", "3", "3"});
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", new Long(2), "2", "3", "3", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "4", "2", "3", new Long(2), "3", "3"});
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));

        list1 = null;
        list2 = null;
        assertFalse(selectMany.compareValues(list1, list2));
        assertFalse(selectMany.compareValues(list2, list1));


        // "true" cases (arrays are different)

        list1 = Arrays.asList(new Object[] {"1", "2", "3"});
        list2 = Arrays.asList(new Object[] {"1", "2", "3", "4"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {});
        list2 = Arrays.asList(new Object[] {"1", "2", "3", "4"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "2", "5", "4"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "2", "3", "3", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "2", "2", "2", "3", "3", "4"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "2", "3", "3", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "4", "2", "3", "2", "3", "2"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", new Long(2), "2", "3", "3", "3", "4"});
        list2 = Arrays.asList(new Object[] {"1", "4", "2", "3", new Integer(2), "3", "3"});
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {"1", "2", "3", "4"});
        intArray = new int[] {1, 2, 3, 4};
        assertTrue(selectMany.compareValues(list1, intArray));
        assertTrue(selectMany.compareValues(intArray, list1));

        list1 = Arrays.asList(new Object[] {"1", "2"});
        objArray = new Object[] {"1", "2"};
        assertTrue(selectMany.compareValues(list1, objArray));
        assertTrue(selectMany.compareValues(objArray, list1));

        list1 = Arrays.asList(new Object[] {"1", "2"});
        list2 = null;
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

        list1 = Arrays.asList(new Object[] {});
        list2 = null;
        assertTrue(selectMany.compareValues(list1, list2));
        assertTrue(selectMany.compareValues(list2, list1));

    }


}
