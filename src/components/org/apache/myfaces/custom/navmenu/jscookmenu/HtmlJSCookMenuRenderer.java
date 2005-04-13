/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.navmenu.jscookmenu;

import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.custom.navmenu.NavigationMenuUtils;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;
import org.apache.myfaces.el.SimpleActionMethodBinding;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.util.DummyFormResponseWriter;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.15  2005/04/13 13:52:01  schof
 *          Fixes MYFACES-185 (patch submitted by Martin Bosak)
 *
 *          Revision 1.14  2005/04/12 17:47:51  schof
 *          Fixes MYFACES-182 (Thanks to David Heffelfinger for reporting and fixing.)
 *
 *          Revision 1.13  2005/04/08 13:05:59  schof
 *          Fixes MyFaces-20 (Patch by Martin Bosak)
 *
 *          Revision 1.12  2004/12/27 04:11:11  mmarinschek
 *          Data Table stores the state of facets of children; script tag is rendered with type attribute instead of language attribute, popup works better as a column in a data table
 *
 *          Revision 1.11  2004/12/24 14:49:29  svieujot
 *          Upgrade the navmenu component to use the Extensions filter.
 *
 *          Revision 1.10  2004/12/13 23:14:37  oros
 *          fix #1044663: handle enabledOnUserRole/visibleOnUserRole, disabled menu items are rendered with null actions
 *
 *          Revision 1.9  2004/10/13 11:50:57  matze
 *          renamed packages to org.apache
 *
 *          Revision 1.8  2004/10/05 15:11:43  manolito
 *          #1020264 x:navigationMenuItem icon problem
 *
 *          Revision 1.7  2004/07/16 13:06:30  manolito
 *          encode javascript strings for jscook menu labels
 *
 *          Revision 1.6  2004/07/05 08:28:24  royalts
 *          added example for <x:navigationMenuItems>
 *
 *          Revision 1.5  2004/07/01 21:53:09  mwessendorf
 *          ASF switch
 *
 *          Revision 1.4  2004/06/25 10:58:43  royalts
 *          fixed bug 979038
 *
 *          Revision 1.3  2004/06/23 14:17:31  royalts
 *          no message
 *
 *          Revision 1.2  2004/06/23 13:50:18  royalts
 *          no message
 *
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
        if (actionParam != null && !actionParam.trim().equals("") && 
                !actionParam.trim().equals("null"))
        {
            String compId = component.getId();
            int idx = actionParam.indexOf(':');
            if (idx == -1) {
                return;
            }
            String actionId = actionParam.substring(0, idx);
            if (! compId.equals(actionId)) {
                return;
            }
            actionParam = actionParam.substring(idx + 1);
            actionParam = decodeValueBinding(actionParam, context);
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
    }

    private String decodeValueBinding(String actionParam, FacesContext context) 
    {
        int idx = actionParam.indexOf(";#{"); 
        if (idx == -1) {
            return actionParam;
        }
        
        String newActionParam = actionParam.substring(0, idx);
        String vbParam = actionParam.substring(idx + 1);
        
        idx = vbParam.indexOf('=');
        if (idx == -1) {
            return newActionParam;
        }
        String vbExpressionString = vbParam.substring(0, idx);
        String vbValue = vbParam.substring(idx + 1);
        
        ValueBinding vb = 
            context.getApplication().createValueBinding(vbExpressionString);        
        vb.setValue(context, vbValue);
        
        return newActionParam;
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
            List uiNavMenuItemList = component.getChildren();
            DummyFormResponseWriter dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter(context);
            dummyFormResponseWriter.addDummyFormParameter(JSCOOK_ACTION_PARAM);
            dummyFormResponseWriter.setWriteDummyForm(true);

            String myId = component.getId();
            
            ResponseWriter writer = context.getResponseWriter();

            writer.write("\n<script type=\"text/javascript\"><!--\n" +
                         "var myMenu =\n[");
            encodeNavigationMenuItems(context, writer,
                                      (NavigationMenuItem[]) list.toArray(new NavigationMenuItem[list.size()]),
                                      uiNavMenuItemList,
                                      myId);

            writer.write("];\n" +
                         "--></script>\n");
        }
    }

    private void encodeNavigationMenuItems(FacesContext context,
                                           ResponseWriter writer,
                                           NavigationMenuItem[] items,
                                           List uiNavMenuItemList,
                                           String menuId)
        throws IOException
    {
        for (int i = 0; i < items.length; i++)
        {
            NavigationMenuItem item = (NavigationMenuItem)items[i];
            Object tempObj = null;
            UINavigationMenuItem uiNavMenuItem = null;
            try {
                tempObj = uiNavMenuItemList.get(i);
            } catch (IndexOutOfBoundsException  e) {
            }
            if (tempObj != null) {
                if (tempObj instanceof UINavigationMenuItem) {
                    uiNavMenuItem = (UINavigationMenuItem) tempObj;
                }
            }

            if (! item.isRendered()) {
                continue;
            }

            if (i > 0)
            {
                writer.write(",\n");
            }

            if (item.isSplit())
            {
                writer.write("_cmSplit,");
            }

            writer.write("[");
            if (item.getIcon() != null)
            {
                String iconSrc = context.getApplication().getViewHandler().getResourceURL(context, item.getIcon());
                writer.write("'<img src=\"");
                writer.write(context.getExternalContext().encodeResourceURL(iconSrc));
                writer.write("\"/>'");
            }
            else
            {
                writer.write("''");
            }
            writer.write(", '");
            writer.write(JavascriptUtils.encodeString(item.getLabel()));
            writer.write("', ");
            if (item.getAction() != null && ! item.isDisabled())
            {
                writer.write("'");
                writer.write(menuId);
                writer.write(':');
                writer.write(item.getAction());
                if (uiNavMenuItem != null) {
                    encodeValueBinding(writer, uiNavMenuItem, item);
                }
                writer.write("'");
            }
            else
            {
                writer.write("null");
            }
            writer.write(", '#', null");

            if (item.isRendered() && ! item.isDisabled()) {
                // render children only if parent is visible/enabled
                NavigationMenuItem[] menuItems = item.getNavigationMenuItems();
                if (menuItems != null && menuItems.length > 0)
                {
                    writer.write(",");
                    if (uiNavMenuItem != null)
                    {
                        encodeNavigationMenuItems(context, writer, menuItems, 
                                uiNavMenuItem.getChildren(), menuId);
                    } 
                }
            };
            writer.write("]");
        }
    }

    private void encodeValueBinding(ResponseWriter writer, UINavigationMenuItem uiNavMenuItem, 
            NavigationMenuItem item) throws IOException 
    {
        ValueBinding vb = uiNavMenuItem.getValueBinding("NavMenuItemValue");
        if (vb == null) {
            return;
        }
        String vbExpression = vb.getExpressionString();
        if (vbExpression == null) {
            return;
        }
        Object tempObj = item.getValue();
        if (tempObj == null) {
            return;
        }
        
        writer.write(";");
        writer.write(vbExpression);
        writer.write("=");
        writer.write(tempObj.toString());
    }
    
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);
        HtmlCommandJSCookMenu menu = (HtmlCommandJSCookMenu)component;
        
        AddResource.addJavaScriptToHeader(NavigationMenuItem.class, "jscookmenu/JSCookMenu.js", context);

        AddResource.addJavaScriptToHeader(NavigationMenuItem.class, "jscookmenu/ThemeOffice/theme.js", context);
        AddResource.addStyleSheet(NavigationMenuItem.class, "jscookmenu/ThemeOffice/theme.css", context);

        AddResource.addJavaScriptToHeader(NavigationMenuItem.class, "jscookmenu/ThemeMiniBlack/theme.js", context);
        AddResource.addStyleSheet(NavigationMenuItem.class, "jscookmenu/ThemeMiniBlack/theme.css", context);

        AddResource.addJavaScriptToHeader(NavigationMenuItem.class, "jscookmenu/ThemeIE/theme.js", context);
        AddResource.addStyleSheet(NavigationMenuItem.class, "jscookmenu/ThemeIE/theme.css", context);

        AddResource.addJavaScriptToHeader(NavigationMenuItem.class, "jscookmenu/ThemePanel/theme.js", context);
        AddResource.addStyleSheet(NavigationMenuItem.class, "jscookmenu/ThemePanel/theme.css", context);
        
        ResponseWriter writer = context.getResponseWriter();

        String menuId = component.getClientId(context).replaceAll(":","_") + "_menu";

        while(menuId.startsWith("_"))
        {
            menuId = menuId.substring(1);
        }

        writer.write("<div id=\"" + menuId + "\"></div>\n" +
                     "<script type=\"text/javascript\"><!--\n" +
                     "\tcmDraw ('" + menuId + "', myMenu, '" + menu.getLayout() + "', cm" + menu.getTheme() + ", '" + menu.getTheme() + "');\n" +
                     "--></script>\n");
    }

}
