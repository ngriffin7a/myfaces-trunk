package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.faces.el.ValueBinding;


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
    throws InterruptedException
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{test}");
        for (int i = 0; i < 1000; i++)
        {
            assertSame(vb, _application.createValueBinding("#{test}"));
        }

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
                                ValueBinding vb = _application.createValueBinding(varNames[i]);
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
