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
package net.sourceforge.myfaces.config;

/**
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MapEntryConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    private String       _key = null;
    private String       _value = null;

    //~ Methods ------------------------------------------------------------------------------------

    public void setKey(String key)
    {
        _key = key.intern();
    }

    public String getKey()
    {
        return _key;
    }

    public void setNullValue(String dummy)
    {
        _value = null;
    }

    public void setValue(String value)
    {
        _value = value.intern();
    }

    public String getValue()
    {
        return _value;
    }

}
