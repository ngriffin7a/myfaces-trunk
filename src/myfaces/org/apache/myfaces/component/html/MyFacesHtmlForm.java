/*
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
package net.sourceforge.myfaces.component.html;

import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesHtmlForm
    extends HtmlForm
{
    private static final Log log = LogFactory.getLog(MyFacesHtmlForm.class);

    private static final int ATTRIBUTE_COUNT = 1;
    private String _name;

    public String getName()
    {
        if (_name != null) return _name;
        ValueBinding vb = getValueBinding("name");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public String getFormName(FacesContext facesContext)
    {
        String name = getName();
        return name != null ? name : getClientId(facesContext);
    }

    /**
     * MyFaces independent getter util for form name.
     */
    public static String getFormName(FacesContext facesContext, HtmlForm htmlForm)
    {
        if (htmlForm instanceof MyFacesHtmlForm)
        {
            return ((MyFacesHtmlForm)htmlForm).getFormName(facesContext);
        }
        else
        {
            String name = (String)htmlForm.getAttributes().get("name");
            return name != null ? name : htmlForm.getClientId(facesContext);
        }
    }

    public void setName(String name)
    {
        _name = name;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[ATTRIBUTE_COUNT + 1];
        int i = 0;
        values[i++] = super.saveState(context);
        values[i++] = _name;
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to save differs!");
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _name = (String)values[i++];
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to restore differs!");
    }
}
