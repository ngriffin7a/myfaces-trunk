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
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class AttributeConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private String _attributeClass; // keep as String for now
    private String _attributeName;

    //~ Methods ------------------------------------------------------------------------------------

    public void setAttributeClass(String attributeClass)
    {
        _attributeClass = attributeClass.intern();
    }

    public String getAttributeClass()
    {
        return _attributeClass;
    }

    public void setAttributeName(String attributeName)
    {
        _attributeName = attributeName.intern();
    }

    public String getAttributeName()
    {
        return _attributeName;
    }
}
