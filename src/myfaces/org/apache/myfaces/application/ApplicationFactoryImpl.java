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
package net.sourceforge.myfaces.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * JSF 1.0 PRD2, 7.2
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class ApplicationFactoryImpl
    extends ApplicationFactory
{
    private static final Log log = LogFactory.getLog(ApplicationFactory.class);

    public ApplicationFactoryImpl()
    {
        if (log.isTraceEnabled()) log.trace("New ApplicationFactory instance created");
    }

    private static final String APPLICATION_ATTR = Application.class.getName();

    public Application getApplication()
    {
        Map appMap = getApplicationMap();
        Application application = (Application)appMap.get(APPLICATION_ATTR);

        if (application == null)
        {
            application = new ApplicationImpl(FacesContext.getCurrentInstance().getExternalContext());
            appMap.put(APPLICATION_ATTR, application);
        }

        return application;
    }

    public void setApplication(Application application)
    {
        getApplicationMap().put(APPLICATION_ATTR, application);
    }

    private Map getApplicationMap()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            log.error("FacesContext is null");
            throw new IllegalStateException("FacesContext is null");
        }
        return facesContext.getExternalContext().getApplicationMap();
    }
}
