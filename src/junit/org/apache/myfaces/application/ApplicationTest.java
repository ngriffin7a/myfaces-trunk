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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.el.ValueBinding;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationTest extends MyFacesTest
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final int THREAD_COUNT = 100;
    private static final int VB_COUNT     = 30000;

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

        for (int i = 0, len = strings.length; i < len; i++)
        {
            int    a    = Math.abs(random.nextInt()) % len;
            int    b    = Math.abs(random.nextInt()) % len;
            String temp = strings[a];
            strings[a]     = strings[b];
            strings[b]     = temp;
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
    }
}
