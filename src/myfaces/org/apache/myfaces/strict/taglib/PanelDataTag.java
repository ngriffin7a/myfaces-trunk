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
package net.sourceforge.myfaces.strict.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import java.io.IOException;


/**
 * see "panel_data" tag in myfaces_html.tld
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PanelDataTag
extends net.sourceforge.myfaces.taglib.PanelDataTag
implements BodyTag
{
    //~ Instance fields ----------------------------------------------------------------------------

    BodyContent _content;

    //~ Methods ------------------------------------------------------------------------------------

    public void setBodyContent(BodyContent content)
    {
        _content = content;
    }

    public int doEndTag()
    throws JspException
    {
        try
        {
            _content.clear();
        }
        catch (IOException e)
        {
            // ignore
            e.printStackTrace();
        }

        super.doEndTag();

        return EVAL_PAGE;
    }

    public void doInitBody()
    throws JspException
    {
    }

    public int doStartTag()
    throws JspException
    {
        super.doStartTag();

        return EVAL_BODY_BUFFERED;
    }
}
