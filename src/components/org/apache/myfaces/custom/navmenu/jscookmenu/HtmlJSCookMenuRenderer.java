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
package net.sourceforge.myfaces.custom.navmenu.jscookmenu;

import net.sourceforge.myfaces.custom.navmenu.NavigationMenuItem;
import net.sourceforge.myfaces.custom.navmenu.NavigationMenuUtils;
import net.sourceforge.myfaces.el.SimpleActionMethodBinding;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class HtmlJSCookMenuRenderer
    extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlJSCookMenuRenderer.class);

    private static final String JSCOOK_ACTION_PARAM = "jscook_action";

    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String actionParam = (String)parameter.get(JSCOOK_ACTION_PARAM);
        MethodBinding mb;
        if (UIComponentTag.isValueReference(actionParam))
        {
            mb = context.getApplication().createMethodBinding(actionParam, null);
        }
        else
        {
            mb = new SimpleActionMethodBinding(actionParam);
        }
        ((HtmlCommandJSCookMenu)component).setAction(mb);

        component.queueEvent(new ActionEvent(component));
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);

        List list = NavigationMenuUtils.getNavigationMenuItemList(component);
        if (list.size() > 0)
        {
            DummyFormUtils.getDummyFormResponseWriter(context).addDummyFormParameter(JSCOOK_ACTION_PARAM);
            ResponseWriter writer = context.getResponseWriter();

            writer.write("\n<script language=\"JavaScript\"><!--\n" +
                         "var myMenu =\n[");
            encodeNavigationMenuItems(writer, list);
            writer.write("];\n" +
                         "--></script>\n");
        }
    }

    private void encodeNavigationMenuItems(ResponseWriter writer, List items)
        throws IOException
    {
        for (int i = 0, size = items.size(); i < size; i++)
        {
            if (i > 0)
                writer.write(",\n");
            NavigationMenuItem item = (NavigationMenuItem)items.get(i);

            if (item.isSplit())
            {
                writer.write("_cmSplit,");
            }

            String icon = item.getIcon() != null ?
                "'<img src=\"" + item.getIcon() + "\"/>'" : "''";
            String action = item.getAction() != null ?
                "'" + item.getAction() + "'" : "null";
            writer.write("[" + icon + ", '" + item.getLabel() + "', " + action +", '#', null");

            List children = item.getChildren();
            if (children.size() > 0)
            {
                writer.write(",");
                encodeNavigationMenuItems(writer, children);
            }
            writer.write("]");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);
        HtmlCommandJSCookMenu menu = (HtmlCommandJSCookMenu)component;
        ResponseWriter writer = context.getResponseWriter();

        String menuId = component.getId() + "_menu";
        writer.write("<div id=" + menuId + "></div>\n" +
                     "<script language=\"JavaScript\"><!--\n" +
                     "\tcmDraw ('" + menuId + "', myMenu, 'hbr', cm" + menu.getTheme() + ", '" + menu.getTheme() + "');\n" +
                     "--></script>\n");
    }

}
