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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.html.MyFacesHtmlInputText;
import net.sourceforge.myfaces.model.UploadedFile;
import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputFileUpload
        extends MyFacesHtmlInputText
{
    private static final Log log = LogFactory.getLog(HtmlInputFileUpload.class);

    private static final int ATTRIBUTE_COUNT = 1;
    private String _accept;

    public HtmlInputFileUpload()
    {
        super();
        setRendererType("FileUpload");
    }

    public void setUploadedFile(UploadedFile upFile)
    {
        setValue(upFile);
    }

    public UploadedFile getUploadedFile()
    {
        return (UploadedFile)getValue();
    }

    public String getAccept()
    {
        if (_accept != null) return _accept;
        ValueBinding vb = getValueBinding("accept");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setAccept(String accept)
    {
        _accept = accept;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[ATTRIBUTE_COUNT + 1];
        int i = 0;
        values[i++] = super.saveState(context);
        values[i++] = _accept;
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to save differs!");
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _accept        = (String)values[i++];
        DebugUtils.assertFatal(i == ATTRIBUTE_COUNT + 1, log, "Number of attributes to restore differs!");
    }

}
