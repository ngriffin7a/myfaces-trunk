package net.sourceforge.myfaces.taglib.core;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UINamingContainer;

/**
 * @author Thomas Spiegl
 */
public class SubviewTag
    extends UIComponentTag
{
    public String getComponentType()
    {
        return UINamingContainer.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }
}
