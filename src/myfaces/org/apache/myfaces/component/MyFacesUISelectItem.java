/**
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
package net.sourceforge.myfaces.component;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesUISelectItem
    extends UISelectItem
{
    private static final Log log = LogFactory.getLog(MyFacesUISelectItem.class);

    private static final int ATTRIBUTE_COUNT = 1;
    private Boolean _disabled;  // missing in API of UISelectItem
    private static final boolean DISABLED_DEFAULT = false;

    public boolean getDisabled()
    {
        if (_disabled != null) return _disabled.booleanValue();
        ValueBinding vb = getValueBinding(JSFAttr.DISABLED_ATTR);
        return vb != null ?
               ((Boolean)vb.getValue(getFacesContext())).booleanValue() :
               DISABLED_DEFAULT;
    }

    public static boolean getDisabled(UISelectItem uiSelectItem)
    {
        if (uiSelectItem instanceof MyFacesUISelectItem)
        {
            return ((MyFacesUISelectItem)uiSelectItem).getDisabled();
        }
        else
        {
            Boolean boolVal = (Boolean)uiSelectItem.getAttributes().get(JSFAttr.DISABLED_ATTR);
            return  boolVal != null ? boolVal.booleanValue() : DISABLED_DEFAULT;
        }
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = Boolean.valueOf(disabled);
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[ATTRIBUTE_COUNT + 1];
        int i = 0;
        values[i++] = super.saveState(context);
        values[i++] = _disabled;
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to save differs!");
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _disabled = (Boolean)values[i++];
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to restore differs!");
    }

}
