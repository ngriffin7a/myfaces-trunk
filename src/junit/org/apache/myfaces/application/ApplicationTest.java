/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
import java.util.Map;
import java.util.Random;

import javax.faces.application.Application;
import javax.faces.el.ValueBinding;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationTest
    extends MyFacesTest
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final int THREAD_COUNT = 400;
    private static final int VB_COUNT = 20000;

    //~ Constructors -------------------------------------------------------------------------------

    public ApplicationTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void testVariableBindingCaching()
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{test}");
        for (int i = 0; i < 1000; i++)
        {
            assertSame(vb, _application.createValueBinding("#{test}"));
        }
    }
        
    public void testVariableBindingMutithreadedCaching()
    throws InterruptedException
    {
        final Application application = _application;
        final Random   random   = new Random();

        final String[] varNames = new String[VB_COUNT];
        for (int i = 0; i < VB_COUNT; i++)
        {
            varNames[i] = "#{t" + random.nextLong() + random.nextLong() + '}';
        }

        final Map vbs     = new HashMap();

        Thread[]  threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++)
        {
            threads[i] =
                new Thread()
                    {
                        public void run()
                        {
                            int start;
                            int end;

                            synchronized (random)
                            {
                                // determine direction
                                if (random.nextBoolean())
                                {
                                    start     = 0;
                                    end       = VB_COUNT - 1;
                                }
                                else
                                {
                                    start     = VB_COUNT - 1;
                                    end       = 0;
                                }
                            }

                            for (
                                int i = start; (start < end) ? (i <= end) : (i >= end);
                                        i = (start < end) ? (i + 1) : (i - 1))
                            {
                                String       name = varNames[i];
                                ValueBinding vb = application.createValueBinding(varNames[i]);
                                synchronized (vbs)
                                {
                                    if (vbs.containsKey(name))
                                    {
                                        assertSame("Probably serious mutli-threading issues, please report to MyFaces team", vb, vbs.get(name));
                                    }
                                    else
                                    {
                                        vbs.put(name, vb);
                                    }
                                }
                            }
                        }
                    };
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++)
        {
            threads[i].join();
        }
    }
}
