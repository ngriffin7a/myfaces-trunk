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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.html.GroupRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TabRenderer
    extends GroupRenderer
{
    public static final String TYPE = "Tab";
    public String getRendererType()
    {
        return TYPE;
    }

    //private static final String SELECTED_ATTR = TabRenderer.class.getName() + ".SELECTED";


    public void encodeBegin(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        //component.setAttribute(SELECTED_ATTR, null);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write(TabbedPaneRenderer.TAB_START_TOKEN);

        super.encodeBegin(facesContext, component);
    }


    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        super.encodeEnd(facesContext, component);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write(TabbedPaneRenderer.TAB_END_TOKEN);
    }


    /*
    protected boolean isSelectedTab(FacesContext facesContext, UIComponent uiTab)
    {
        Boolean sel = (Boolean)uiTab.getAttribute(SELECTED_ATTR);
        if (sel == null)
        {
            UIComponent uiTabbedPane = uiTab.getParent();
            Integer selIdxInteger = (Integer)uiTabbedPane.getAttribute(TabbedPaneRendererAttributes.SELECTED_INDEX_ATTR);
            int selIdx = (selIdxInteger == null ? 0 : selIdxInteger.intValue());

            int cnt = 0;
            for (Iterator it = uiTabbedPane.getChildren(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child == uiTab)
                {
                    sel = Boolean.valueOf(cnt == selIdx);
                    break;
                }
                if (child.getRendererType().equals(TYPE))
                {
                    cnt++;
                }
            }

            if (sel == null)
            {
                LogUtil.getLogger().warning("Tab " + uiTab.getClientId(facesContext) + " not found in parent TabbedPane " + uiTabbedPane.getClientId(facesContext));
                sel = Boolean.FALSE;
            }
        }
        return sel.booleanValue();
    }
    */

}
