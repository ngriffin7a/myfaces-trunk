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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.MyfacesComponentBodyTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class VerbatimTag
        extends MyfacesComponentBodyTag
{
    //private static final Log log = LogFactory.getLog(VerbatimTag.class);

    public String getComponentType()
    {
        return "javax.faces.Output";
    }

    protected String getDefaultRendererType()
    {
        return "javax.faces.Text";
    }

    // HtmlOutputText attributes
    private String _escape;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        if (_escape != null)
        {
            setBooleanProperty(component, JSFAttr.ESCAPE_ATTR, _escape);
        }
        else
        {
            //Default escape value for component is true, but for this tag it is false,
            //so we must set it to false explicitly, if no attribute is given
            component.getAttributes().put(JSFAttr.ESCAPE_ATTR, Boolean.FALSE);
        }

        //No need to save component state
        //TODO: transient not yet supported - bug in UIComponentBase on restore state?
        // component.setTransient(true);
    }

    public void setEscape(String escape)
    {
        _escape = escape;
    }

    public int doAfterBody() throws JspException
    {
        BodyContent bodyContent = getBodyContent();
        if (bodyContent != null)
        {
            UIOutput component = (UIOutput)getComponentInstance();
            component.setValue(bodyContent.getString());
        }
        return super.doAfterBody();
    }
}
