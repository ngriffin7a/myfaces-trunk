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

import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.html.DataRenderer;

import javax.faces.component.UIComponent;
import javax.faces.webapp.FacesTag;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListRowTag
        extends MyFacesTag
{
    public UIComponent createComponent()
    {
        UIPanel panel = new UIPanel();
        // donot save State
        panel.setTransient(true);
        return panel;
    }

    public String getRendererType()
    {
        return DataRenderer.TYPE;
    }

    public void setVar(String v)
    {
        setProperty(UIPanel.VAR_ATTR, v);
    }

    /*
    private boolean hasNext()
    {
        String varAttr = (String)getComponent().getAttribute(UIPanel.VAR_ATTR);
        Object currentObj = getFacesContext().getServletRequest().getAttribute(varAttr);
        return currentObj != null;
    }

    public int doAfterStartTag() throws JspException
    {
        if (hasNext())
        {
            return EVAL_BODY_INCLUDE;
        }
        else
        {
            return SKIP_BODY;
        }
    }

    public int doAfterAfterBody() throws JspException
    {
        try
        {
            getComponent().encodeBegin(getFacesContext());
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }

        if (hasNext())
        {
            return EVAL_BODY_AGAIN;
        }
        else
        {
            return SKIP_BODY;
        }
    }
    */

}
