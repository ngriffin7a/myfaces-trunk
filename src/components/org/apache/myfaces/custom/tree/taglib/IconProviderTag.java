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
package net.sourceforge.myfaces.custom.tree.taglib;

import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.custom.tree.HtmlTree;
import net.sourceforge.myfaces.custom.tree.IconProvider;

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
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/05/04 12:19:14  o_rossmueller
 *          added icon provider
 *
 */
public class IconProviderTag
    extends TagSupport
{

    private String type = null;


    public IconProviderTag()
    {
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public int doStartTag() throws JspException
    {
        if (type == null)
        {
            throw new JspException("type attribute not set");
        }

        //Find parent UIComponentTag
        UIComponentTag componentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (componentTag == null)
        {
            throw new JspException("IconProviderTag has no UIComponentTag ancestor");
        }

        UIComponent component = componentTag.getComponentInstance();
        if (component instanceof HtmlTree)
        {
            String className;
            if (UIComponentTag.isValueReference(type))
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ValueBinding vb = facesContext.getApplication().createValueBinding(type);
                className = (String) vb.getValue(facesContext);
            } else
            {
                className = type;
            }
            IconProvider provider = (IconProvider) ClassUtils.newInstance(className);
            ((HtmlTree) component).setIconProvider(provider);
        } else
        {
            throw new JspException("Component " + component.getId() + " is no HtmlTree");
        }

        return Tag.SKIP_BODY;
    }
}
