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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.DataRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.DataRenderer;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * see "panel_data" tag in myfaces_html.tld
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PanelDataTag
    extends MyFacesTag
    implements DataRendererAttributes
{
    public String getComponentType()
    {
        return "Panel";
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        UIComponentUtils.setTransient(uiComponent, true);
    }

    public String getRendererType()
    {
        return DataRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIPanel attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setPanelClass(String v)
    {
        setRendererAttributeString(PANEL_CLASS_ATTR, v);
    }

    // Data Renderer attributes

    public void setVar(String v)
    {
        setRendererAttributeString(VAR_ATTR, v);
    }




    public int getDoStartValue() throws JspException
    {
        //is there at least one row?
        if (hasNext())
        {
            //yes
            return super.getDoStartValue();
        }
        else
        {
            //no, underlying list is empty
            return SKIP_BODY;
        }
    }


    public int getDoAfterBodyValue() throws JspException
    {
        try
        {
            //encodeBegin is responsible for stepping to next row
            //and render start of new row if we are not yet at the end of the list
            if (hasNext())
            {
                getComponent().encodeBegin(getFacesContext());
            }
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        return hasNext() ? EVAL_BODY_AGAIN : SKIP_BODY;
    }


    /**
     * The encodeBegin method of the corresponding component must store the
     * current iteration item as a request attribute named according to the "var"
     * attribute. If there are no more rows left, this model value must be
     * removed from the context.
     * @return true, if there is a request attribute for an attribute with the
     *         name determined from the "var" renderer attribute
     */
    protected boolean hasNext()
    {
        String varAttr = (String)getComponent().getAttribute(DataRenderer.VAR_ATTR);
        try
        {
            return ((ServletRequest)getFacesContext().getExternalContext().getRequest())
                        .getAttribute(varAttr) != null;
        }
        catch (FacesException e)
        {
            return false;
        }
    }

}
