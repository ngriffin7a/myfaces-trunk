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
package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class UIComponentBodyTag
    extends UIComponentTag
    implements BodyTag
{
    private BodyContent _bodyContent;

    public int doAfterBody()
            throws JspException
    {
        return getDoAfterBodyValue();
    }

    public void doInitBody()
            throws JspException
    {
    }

    public void release()
    {
        super.release();
        _bodyContent = null;
    }

    public BodyContent getBodyContent()
    {
        return _bodyContent;
    }

    public void setBodyContent(BodyContent bodyContent)
    {
        _bodyContent = bodyContent;
    }

    public JspWriter getPreviousOut()
    {
        return _bodyContent.getEnclosingWriter();
    }

    protected int getDoStartValue()
            throws JspException
    {
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    protected int getDoAfterBodyValue()
            throws JspException
    {
        return BodyTag.SKIP_BODY;
    }

}
