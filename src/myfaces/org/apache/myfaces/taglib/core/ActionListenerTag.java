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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ActionListenerTag
        extends TagSupport
{
    //private static final Log log = LogFactory.getLog(ActionListenerTag.class);

    private String _type = null;

    public ActionListenerTag()
    {
    }

    public void setType(String type)
    {
        _type = type;
    }


    public int doStartTag() throws JspException
    {
        if (_type == null)
        {
            throw new JspException("type attribute not set");
        }

        //Find parent UIComponentTag
        UIComponentTag componentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (componentTag == null)
        {
            throw new JspException("ActionListenerTag has no UIComponentTag ancestor");
        }

        if (componentTag.getCreated())
        {
            //Component was just created, so we add the Listener
            UIComponent component = componentTag.getComponentInstance();
            if (component instanceof ActionSource)
            {
                String className;
                if (UIComponentTag.isValueReference(_type))
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ValueBinding vb = facesContext.getApplication().createValueBinding(_type);
                    className = (String)vb.getValue(facesContext);
                }
                else
                {
                    className = _type;
                }
                ActionListener al = (ActionListener)ClassUtils.newInstance(className);
                ((ActionSource)component).addActionListener(al);
            }
            else
            {
                throw new JspException("Component " + component.getId() + " is no ActionSource");
            }
        }

        return Tag.SKIP_BODY;
    }
}
