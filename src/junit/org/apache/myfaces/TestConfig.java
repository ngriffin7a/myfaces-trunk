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
package net.sourceforge.myfaces;

import java.io.IOException;
import java.util.Properties;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class TestConfig
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final String     TEST_PROP_FILE = "test.properties";
    private static final Properties _configProps = new Properties();

    static
    {
        try
        {
            _configProps.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_PROP_FILE));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //~ Constructors -------------------------------------------------------------------------------

    private TestConfig()
    {
        // disable
    }

    //~ Methods ------------------------------------------------------------------------------------

    public static String getContextPath()
    {
        return _configProps.getProperty("servletContextPath");
    }
}
