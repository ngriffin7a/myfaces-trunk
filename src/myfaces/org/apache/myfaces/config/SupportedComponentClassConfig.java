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

import net.sourceforge.myfaces.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class SupportedComponentClassConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private final Set _attributeName  = new HashSet();
    private Class     _componentClass;

    //~ Constructors -------------------------------------------------------------------------------

    public SupportedComponentClassConfig(Class componentClass)
    {
        _componentClass = componentClass;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public Set getAttributeNames()
    {
        return _attributeName;
    }

    public void setComponentClass(String componentClass)
    {
        _componentClass = ClassUtils.classForName(componentClass);
    }

    public Class getComponentClass()
    {
        return _componentClass;
    }

    public void addAttributeName(String attributeName)
    {
        _attributeName.add(attributeName.intern());
    }
}
