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

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/04/28 08:12:45  manolito
 * comments
 *
 */
public class ApplicationFactoryImpl
    extends ApplicationFactory
{
    private static final Log log = LogFactory.getLog(ApplicationFactory.class);

    //Application is thread-safe (see Application javadoc)
    //"Application represents a per-web-application singleton object..."
    //FactoryFinder has a ClassLoader-Factory Map. Since each webapp has it's
    //own ClassLoader, each webapp will have it's own private factory instances.
    private Application _application;

    public ApplicationFactoryImpl()
    {
        _application = new ApplicationImpl();
        if (log.isTraceEnabled()) log.trace("New ApplicationFactory instance created");
    }

    public Application getApplication()
    {
        return _application;
    }

    public void setApplication(Application application)
    {
        _application = application;
    }

}
