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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SetBundleTag
    extends TagSupport
{
    private String _basename;
    private String _var;
    private String _scope;

    public void setBasename(String basename)
    {
        _basename = basename;
    }

    public void setScope(String scope)
    {
        _scope = scope;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public int doStartTag() throws JspException
    {
        if (_basename == null)
        {
            throw new IllegalArgumentException();
        }

        if (_var == null)
        {
            throw new IllegalArgumentException();
        }

        int oldScope = pageContext.getAttributesScope(_var);
        if (oldScope != 0)
        {
            pageContext.removeAttribute(_var, oldScope);
        }

        ResourceBundle bundle;
        try
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            bundle = ResourceBundle.getBundle(_basename, facesContext.getLocale());
        }
        catch (MissingResourceException e)
        {
            LogUtil.getLogger().severe("Resource bundle '" + _basename + "' could not be found.");
            bundle = null;
        }

        if (bundle != null)
        {
            int scopeInt = PageContext.REQUEST_SCOPE;
            if (_scope != null)
            {
                if (_scope.equalsIgnoreCase("page"))
                {
                    scopeInt = PageContext.PAGE_SCOPE;
                }
                else if (_scope.equalsIgnoreCase("request"))
                {
                    scopeInt = PageContext.REQUEST_SCOPE;
                }
                else if (_scope.equalsIgnoreCase("session"))
                {
                    scopeInt = PageContext.SESSION_SCOPE;
                }
                else if (_scope.equalsIgnoreCase("application"))
                {
                    scopeInt = PageContext.APPLICATION_SCOPE;
                }
                else
                {
                    throw new IllegalArgumentException("Illegal scope '" + _scope + "'");
                }
            }

            pageContext.setAttribute(_var, bundle, scopeInt);
        }

        return Tag.SKIP_BODY;
    }
}
