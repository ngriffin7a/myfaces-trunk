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
package net.sourceforge.myfaces.renderkit.html.jspinfo;

import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspInfoUtils
{
    private JspInfoUtils() {}

    /**
     * Checks, if the bean referenced by the given model reference exists and
     * creates it if necessary. Info about the bean (class, scope) is fetched
     * from the current tree's JspInfo.
     * @param facesContext
     * @param modelRef
     */
    public static void checkModelInstance(FacesContext facesContext, String modelRef)
    {
        BeanUtils.stripBracketsFromModelReference(modelRef);
        String modelId;
        int dot = modelRef.indexOf('.');
        if (dot == -1)
        {
            modelId = modelRef;
        }
        else
        {
            modelId = modelRef.substring(0, dot);
        }

        JspBeanInfo jspBeanInfo = JspInfo.getJspBeanInfo(facesContext,
                                                         facesContext.getTree().getTreeId(),
                                                         modelId);
        if (jspBeanInfo == null)
        {
            //There is no JspBeanInfo for that model bean
            // - either because of a typing error --> not our problem... :-)
            // - or the referenced model object is the variable of a DataRenderer
            // - or the object is created elsewhere (i.e. not via jsp:useBean)
            // --> so, we can do nothing other than ignore this issue
            return;
        }

        checkModelInstance(facesContext, jspBeanInfo);
    }


    public static void checkModelInstance(FacesContext facesContext,
                                          JspBeanInfo jspBeanInfo)
    {
        if (findModelBean(facesContext, jspBeanInfo.getId(), jspBeanInfo.getScope()) == null)
        {
            //Create bean instance
            if (jspBeanInfo.getClassName() == null &&
                jspBeanInfo.getBeanName() == null)
            {
                throw new IllegalArgumentException("Incomplete JspBeanInfo for model " + jspBeanInfo.getId());
            }

            Object bean = jspBeanInfo.instantiate();
            switch (jspBeanInfo.getScope())
            {
                case PageContext.PAGE_SCOPE:
                    throw new IllegalArgumentException("Page scope is not supported!");

                case PageContext.REQUEST_SCOPE:
                    facesContext.getServletRequest().setAttribute(jspBeanInfo.getId(), bean);
                    break;

                case PageContext.SESSION_SCOPE:
                    ServletRequest servletRequest = facesContext.getServletRequest();
                    if (servletRequest instanceof HttpServletRequest)
                    {
                        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
                        session.setAttribute(jspBeanInfo.getId(), bean);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Session scope is not allowed because ServletRequest is not a HttpServletRequest!");
                    }
                    break;

                case PageContext.APPLICATION_SCOPE:
                    facesContext.getServletContext().setAttribute(jspBeanInfo.getId(), bean);
                    break;

                default:
                    throw new IllegalArgumentException("Illegal scope " + jspBeanInfo.getScope());
            }
        }
    }


    private static Object findModelBean(FacesContext facesContext, String id, int scope)
    {
        switch (scope)
        {
            case PageContext.PAGE_SCOPE:
                throw new IllegalArgumentException("Page scope is not supported!");

            case PageContext.REQUEST_SCOPE:
                return facesContext.getServletRequest().getAttribute(id);

            case PageContext.SESSION_SCOPE:
                ServletRequest servletRequest = facesContext.getServletRequest();
                if (servletRequest instanceof HttpServletRequest)
                {
                    HttpSession session = ((HttpServletRequest)servletRequest).getSession(false);
                    if (session == null)
                    {
                        return null;
                    }
                    else
                    {
                        return session.getAttribute(id);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("Session scope is not allowed because ServletRequest is not a HttpServletRequest!");
                }

            case PageContext.APPLICATION_SCOPE:
                return facesContext.getServletContext().getAttribute(id);

            default:
                throw new IllegalArgumentException("Unknown scope " + scope);
        }
    }


}
