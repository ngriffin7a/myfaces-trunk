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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspBeanInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfoUtils;
import net.sourceforge.myfaces.renderkit.html.state.StateRestorer;

import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Map;

/**
 * Abstract base class for StateRestorers that restore state from the client.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ClientStateRestorer
    implements StateRestorer
{
    protected void recreateRequestScopeBeans(FacesContext facesContext)
    {
        //autocreate request scope beans
        if (MyFacesConfig.isAutoCreateRequestScopeBeans(facesContext.getServletContext()))
        {
            Iterator it = JspInfo.getJspBeanInfos(facesContext,
                                                  facesContext.getTree().getTreeId());
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry)it.next();
                JspInfoUtils.checkModelInstance(facesContext,
                                                (JspBeanInfo)entry.getValue());
            }
        }
    }
}
