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
import net.sourceforge.myfaces.component.UISelectMany;
import net.sourceforge.myfaces.component.UISelectOne;
import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;
import net.sourceforge.myfaces.renderkit.html.util.HTMLSelectUtil;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLSelectAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListboxRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLSelectAttributes,
               ListboxRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Listbox";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UISelectOne.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof javax.faces.component.UISelectOne;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_listbox", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_listbox", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_listbox", HTML_SELECT_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_listbox", SELECT_MANY_LISTBOX_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_listbox", USER_ROLE_ATTRIBUTES);

        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_listbox", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_listbox", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_listbox", HTML_SELECT_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_listbox", SELECT_ONE_LISTBOX_ATTRIBUTES);
        addAttributeDescriptors(UISelectOne.TYPE, TLD_HTML_URI, "selectone_listbox", USER_ROLE_ATTRIBUTES);
    }



    public void encodeBegin(FacesContext facescontext, UIComponent uicomponent)
            throws IOException
    {
        if (uicomponent.getComponentType() != UISelectMany.TYPE &&
            uicomponent.getComponentType() != UISelectOne.TYPE)
        {
            throw new IllegalArgumentException("UIComponent must be of type " + UISelectMany.class.getName() +
                                               " or " + UISelectOne.class.getName());
        }
    }

    public void encodeEnd(FacesContext facescontext, UIComponent uicomponent)
        throws IOException
    {
        int size = SelectItemUtil.getSelectItemsCount(facescontext, uicomponent);
        HTMLSelectUtil.drawHTMLSelect(facescontext,
                                      uicomponent,
                                      TYPE,
                                      size);
    }

}
