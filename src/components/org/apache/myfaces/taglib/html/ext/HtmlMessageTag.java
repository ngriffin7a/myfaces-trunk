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
package net.sourceforge.myfaces.taglib.html.ext;

import net.sourceforge.myfaces.component.html.ext.HtmlMessage;
import net.sourceforge.myfaces.taglib.html.HtmlMessageTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/03/31 14:51:47  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.2  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
 *
 * Revision 1.1  2004/03/30 13:27:05  manolito
 * extended Message component
 *
 */
public class HtmlMessageTag
        extends HtmlMessageTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlOutputFormatTag.class);

    public String getComponentType()
    {
        return HtmlMessage.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.Message";
    }

    private String _summaryFormat;
    private String _detailFormat;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "summaryFormat", _summaryFormat);
        setStringProperty(component, "detailFormat", _detailFormat);
    }

    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
    }
}
