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
package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class FacesBodyTag
        extends FacesTag
        implements BodyTag
{
    private BodyContent _bodyContent;

    protected void init()
    {
        super.init();
        _bodyContent = null;
    }


    protected int getDoStartValue() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }


    //body tag support

    public void doInitBody() throws JspException
    {
    }

    public void setBodyContent(BodyContent bodyContent)
    {
        _bodyContent = bodyContent;
    }

    protected BodyContent getBodyContent()
    {
        return _bodyContent;
    }
}
