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
package net.sourceforge.myfaces.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * JSF 1.0 PRD2, 7.2
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationFactoryImpl
    extends ApplicationFactory
{
    private static final String APPLICATION_ATTR = Application.class.getName();

    public Application getApplication()
    {
        ServletContext servletContext = getServletContext();
        Application application = (Application)servletContext.getAttribute(APPLICATION_ATTR);

        if (application == null)
        {
            application = new ApplicationImpl(servletContext);
            servletContext.setAttribute(APPLICATION_ATTR, application);
        }

        return application;
    }

    public void setApplication(Application application)
    {
        getServletContext().setAttribute(APPLICATION_ATTR, application);
    }

    private ServletContext getServletContext()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            throw new IllegalStateException("FacesContext is null");
        }
        return (ServletContext)facesContext.getExternalContext().getContext();
    }
}
