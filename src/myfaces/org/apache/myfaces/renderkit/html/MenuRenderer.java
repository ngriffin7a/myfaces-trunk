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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.MenuRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLSelectAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MenuRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLSelectAttributes,
               MenuRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Menu";
    private static final int DEFAULT_SIZE = 1;

    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(String s)
    {
        return s.equals(UISelectMany.TYPE) ||
               s.equals(UISelectOne.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UISelectMany ||
               uicomponent instanceof UISelectOne;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_menu", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_menu", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_menu", HTML_SELECT_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_menu", SELECT_MANY_MENU_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_menu", USER_ROLE_ATTRIBUTES);

        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_menu", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_menu", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_menu", HTML_SELECT_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_menu", SELECT_ONE_MENU_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_menu", USER_ROLE_ATTRIBUTES);
    }
    */




    public void encodeBegin(FacesContext facescontext, UIComponent uicomponent)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        String strSize = (String)uiComponent.getAttribute(SIZE_ATTR);
        int size;
        try
        {
            size = strSize != null ? Integer.parseInt(strSize) : DEFAULT_SIZE;
        }
        catch (NumberFormatException e)
        {
            size = DEFAULT_SIZE;
        }

        HTMLUtil.renderSelect(facesContext,
                                      uiComponent,
                                      TYPE,
                                      size);
    }

}
