package net.sourceforge.myfaces.config;

import java.util.Map;
import java.util.Set;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SupportedComponentTypeConfig
{
    private String _componentType;
    private Set _attributeName;

    public Set getAttributeNames()
    {
        return _attributeName;
    }

    public void addAttributeName(String attributeName)
    {
        _attributeName.add(attributeName);
    }

    public String getComponentType()
    {
        return _componentType;
    }

    public void setComponentType(String componentType)
    {
        _componentType = componentType;
    }
}
