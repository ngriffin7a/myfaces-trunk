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
