package net.sourceforge.myfaces.component;

import javax.faces.component.UIComponent;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class MyFacesComponentDelegate
    implements MyFacesComponent
{
    private UIComponent _uiComponent;

    public MyFacesComponentDelegate(UIComponent uiComponent)
    {
        _uiComponent = uiComponent;
    }

    public boolean isTransient()
    {
        Boolean b = (Boolean)_uiComponent.getAttribute(TRANSIENT_ATTR);
        return b != null && b.booleanValue();
    }

    public void setTransient(boolean b)
    {
        _uiComponent.setAttribute(TRANSIENT_ATTR, b ? Boolean.TRUE : Boolean.FALSE);
    }
}
