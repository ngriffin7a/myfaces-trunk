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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValidatorConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private List   _propertyList; // List to maintaing ordering
    private Set    _attributeSet;
    private String _validatorClass; // must stay String because of Application.addValidator()
    private String _validatorId;

    //~ Methods ------------------------------------------------------------------------------------

    public void setAttribute(Set attribute)
    {
        _attributeSet = attribute;
    }

    public Set getAttributeSet()
    {
        return _attributeSet;
    }

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

    public List getPropertyList()
    {
        return _propertyList;
    }

    public void setValidatorClass(String validatorClass)
    {
        _validatorClass = validatorClass.intern();
    }

    public String getValidatorClass()
    {
        return _validatorClass;
    }

    public void setValidatorId(String validatorId)
    {
        _validatorId = validatorId.intern();
    }

    public String getValidatorId()
    {
        return _validatorId;
    }

    public void addAttribute(String attributeName)
    {
        if (_attributeSet == null)
        {
            _attributeSet = new HashSet();
        }
        _attributeSet.add(attributeName);
    }

    public void addPropertyConfig(PropertyConfig propertyConfig)
    {
        if (_propertyList == null)
        {
            _propertyList = new ArrayList();
        }
        _propertyList.add(propertyConfig);
    }
}
