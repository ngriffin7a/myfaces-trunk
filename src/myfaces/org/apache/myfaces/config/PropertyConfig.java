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


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/07/01 22:05:08  mwessendorf
 * ASF switch
 *
 * Revision 1.6  2004/05/12 07:57:43  manolito
 * Log in javadoc header
 *
 */
public class PropertyConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    protected String _description;
//    protected String _displayName;
//    protected IconConfig _iconConfig;
    protected Class  _propertyClass;
    protected String _propertyName;

    //~ Methods ------------------------------------------------------------------------------------

    public void setDescription(String description)
    {
// ignore        
//        _description = description;
    }

    public void setDisplayName(String displayName)
    {
// ignore        
//        _displayName = displayName;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore        
//        _iconConfig = iconConfig;
    }

    public void setPropertyClass(String propertyClassName)
    {
        _propertyClass = ClassUtils.javaTypeToClass(propertyClassName);
    }

    public Class getPropertyClass()
    {
        return _propertyClass;
    }

    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName.intern();
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    public void setSuggestedValue(Object suggestedValue)
    {
// ignore        
//        _suggestedValue = suggestedValue;
    }
}
