package net.sourceforge.myfaces.custom.navmenu;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.core.SelectItemTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class HtmlNavigationMenuItemTag
    extends SelectItemTagBase
{

    private static final String ICON_ATTR   = "icon";
    private static final String ACTION_ATTR = "action";
    private static final String SPLIT_ATTR  = "split";

    private String _icon = null;
    private String _action = null;
    private String _split;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    public String getComponentType()
    {
        return UINavigationMenuItem.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    protected void setProperties(UIComponent component)
    {
        setItemValue("0"); // itemValue not used
        super.setProperties(component);
        setStringProperty(component, ICON_ATTR, _icon);
        setStringProperty(component, ACTION_ATTR, _action);
        setBooleanProperty(component, SPLIT_ATTR, _split);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }


    public void setAction(String action)
    {
        _action = action;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public void setSplit(String split)
    {
        _split = split;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }
}
