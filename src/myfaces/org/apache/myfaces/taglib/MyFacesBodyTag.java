/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public abstract class MyFacesBodyTag
        extends MyFacesIterationTag
        implements BodyTag
{
    private BodyContent _bodyContent;

    protected void initDelegation()
    {
        _delegation = new BodyTagSupport();
    }

    protected void init()
    {
        super.init();
        _bodyContent = null;
    }

    //Delegation:
    public void doInitBody() throws JspException
    {
        ((BodyTag)_delegation).doInitBody();
    }

    public void setBodyContent(BodyContent bodyContent)
    {
        ((BodyTag)_delegation).setBodyContent(bodyContent);
        _bodyContent = bodyContent;
    }

    protected BodyContent getBodyContent()
    {
        return _bodyContent;
    }
}
