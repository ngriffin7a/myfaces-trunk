package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SupportedComponentClassConfig
{
    private static final Log log = LogFactory.getLog(SupportedComponentClassConfig.class);

    private Class _componentClass;
    private Set _attributeName;

    public SupportedComponentClassConfig()
    {
    }

    public SupportedComponentClassConfig(Class componentClass)
    {
        _componentClass = componentClass;
    }

    public Set getAttributeNames()
    {
        return _attributeName;
    }

    public void addAttributeName(String attributeName)
    {
        _attributeName.add(attributeName);
    }

    public Class getComponentClass()
    {
        return _componentClass;
    }

    public void setComponentClass(String componentClass)
    {
        try
        {
            _componentClass = Class.forName(componentClass, true, Thread.currentThread().getContextClassLoader()));
        }
        catch (ClassNotFoundException e)
        {
            log.error("Error in faces-config.xml - Class not found: " + componentClass);
        }
    }
}
