/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIParameter;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * This component defines a model value (via the "modelReference" attribute),
 * of which the state has to be saved and restored by the StateRenderer.
 * A UISaveState component is only a "parameter" for the StateRenderer, it does no rendering
 * of its own. The decode and encodeXxx methods do nothing.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISaveState
    extends UIParameter
{
    public static final String TYPE = UISaveState.class.getName();

    public static final String GLOBAL_ATTR = "global";

    public String getComponentType()
    {
        return TYPE;
    }

    public UISaveState()
    {
        UIComponentUtils.setTransient(this, true);  //No need to save state of the component itself
    }

    public final boolean isValid()
    {
        return true;   //model update must not occur!
    }

    public boolean isGlobal()
    {
        Boolean global = (Boolean)getAttribute(GLOBAL_ATTR);
        return global != null ? global.booleanValue() : false;
    }

    public void setGlobal(boolean global)
    {
        if (global)
        {
            setAttribute(GLOBAL_ATTR, Boolean.TRUE);
        }
        else
        {
            setAttribute(GLOBAL_ATTR, null);    //false is default
        }
    }

    public void decode(FacesContext context)
        throws IOException
    {
        //all decoding and encoding is done by StateRenderer
        setValid(true);
    }

    public void encodeBegin(FacesContext context)
        throws IOException
    {
        //all decoding and encoding is done by StateRenderer
    }

    public void encodeChildren(FacesContext context)
        throws IOException
    {
        //all decoding and encoding is done by StateRenderer
    }

    public void encodeEnd(FacesContext context)
        throws IOException
    {
        //all decoding and encoding is done by StateRenderer
    }

    public void updateModel(FacesContext context)
    {
        //do not update the model
    }
}
