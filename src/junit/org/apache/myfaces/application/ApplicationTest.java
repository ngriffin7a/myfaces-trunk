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
package org.apache.myfaces.application;

import org.apache.myfaces.MyFacesBaseTest;

import javax.faces.application.Application;
import javax.faces.el.ValueBinding;
import java.util.*;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationTest extends MyFacesBaseTest
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final int THREAD_COUNT = 100;
    private static final int VB_COUNT     = 20000;

    //~ Instance fields ----------------------------------------------------------------------------

    final Map _bindingsMap                = new HashMap(VB_COUNT * 2);

    //~ Constructors -------------------------------------------------------------------------------

    public ApplicationTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void testValueBindingCaching()
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{test}");
        for (int i = 0; i < 1000; i++)
        {
            assertSame(vb, _application.createValueBinding("#{test}"));
        }
    }

    public void testValueBindingMutithreadedCaching()
    throws InterruptedException
    {
        final Random random   = new Random();

        Set          varNames = new HashSet(VB_COUNT * 2);
        for (int i = 0; i < VB_COUNT; i++)
        {
            String name = null;
            do
            {
                name =
                    "#{t" + Math.abs(random.nextLong()) + ".t" + Math.abs(random.nextLong()) + '}';
            }
            while (varNames.contains(name));

            varNames.add(name);
        }

        String[] names = toStringArray(varNames);
        varNames = null; // free memory

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++)
        {
            threads[i] = new Thread(new ValueBindingCachingTesterThread((names = shuffle(names))));
        }
        names = null; // free memory

        for (int i = 0; i < THREAD_COUNT; i++)
        {
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++)
        {
            threads[i].join();
        }
    }

    Application getApplication()
    {
        return _application;
    }

    private String[] shuffle(String[] strings)
    {
        strings = (String[]) strings.clone();

        final Random random = new Random();

        for (int i = strings.length; i > 0;)
        {
            int    j    = random.nextInt(i);
            i--;
            String temp = strings[i];
            strings[i]     = strings[j];
            strings[j]     = temp;
        }

        return strings;
    }

    private String[] toStringArray(Set set)
    {
        String[] strings = new String[set.size()];
        int      i = 0;
        for (Iterator it = set.iterator(); it.hasNext();)
        {
            strings[i++] = (String) it.next();
        }
        return strings;
    }

    //~ Inner Classes ------------------------------------------------------------------------------

    class ValueBindingCachingTesterThread implements Runnable
    {
        //~ Instance fields ------------------------------------------------------------------------

        final String[] _names;

        //~ Constructors ---------------------------------------------------------------------------

        ValueBindingCachingTesterThread(String[] names)
        {
            _names = names;
        }

        //~ Methods --------------------------------------------------------------------------------

        public void run()
        {
            try {
                // invoke to initialize FacesContext.currentInstance() for this thread
                new MyFacesBaseTest("dummy") {
                    public void setUp()
                        throws Exception
                    {
                        super.setUp();
                    }
                }.setUp();
    
                final Application    application = getApplication();
                final ValueBinding[] bindings = new ValueBinding[_names.length];
                for (int i = 0, len = _names.length; i < len; i++)
                {
                    bindings[i] = application.createValueBinding(_names[i]);
                }
    
                final Map bindingsMap = _bindingsMap;
                synchronized (bindingsMap)
                {
                    boolean put = !bindingsMap.containsKey(_names[0]);
                    for (int i = 0, len = _names.length; i < len; i++)
                    {
                        String       name = _names[i];
                        ValueBinding vb = bindings[i];
                        if (put)
                        {
                            bindingsMap.put(name, vb);
                        }
                        else
                        {
                            assertSame(
                                "Probably serious mutli-threading issue, please report to MyFaces team",
                                vb, bindingsMap.get(name));
                        }
                    }
                }
            } 
            catch (Exception e)
            {
                assertTrue(false);
            }
        }
    }
}
