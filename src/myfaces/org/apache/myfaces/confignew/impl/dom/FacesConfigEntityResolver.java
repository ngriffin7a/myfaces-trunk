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
package net.sourceforge.myfaces.confignew.impl.dom;

import net.sourceforge.myfaces.util.ClassUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class FacesConfigEntityResolver
    implements EntityResolver
{
    private static final Log log = LogFactory.getLog(FacesConfigEntityResolver.class);

    private static final String FACES_CONFIG_1_0_DTD_SYSTEM_ID = "http://java.sun.com/dtd/web-facesconfig_1_0.dtd";
    private static final String FACES_CONFIG_1_0_DTD_RESOURCE
            = "net.sourceforge.myfaces.resource".replace('.', '/') + "/web-facesconfig_1_0.dtd";
    private static final String FACES_CONFIG_1_1_DTD_SYSTEM_ID = "http://java.sun.com/dtd/web-facesconfig_1_1.dtd";
    private static final String FACES_CONFIG_1_1_DTD_RESOURCE
            = "net.sourceforge.myfaces.resource".replace('.', '/') + "/web-facesconfig_1_1.dtd";

    private ExternalContext _externalContext = null;

    public FacesConfigEntityResolver(ExternalContext context)
    {
        _externalContext = context;
    }

    public FacesConfigEntityResolver()
    {
    }

    public InputSource resolveEntity(String publicId,
                                     String systemId)
        throws IOException
    {
        InputStream stream;
        if (systemId.equals(FACES_CONFIG_1_0_DTD_SYSTEM_ID))
        {
            stream = ClassUtils.getResourceAsStream(FACES_CONFIG_1_0_DTD_RESOURCE);
        }
        else if (systemId.equals(FACES_CONFIG_1_1_DTD_SYSTEM_ID))
        {
            stream = ClassUtils.getResourceAsStream(FACES_CONFIG_1_1_DTD_RESOURCE);
        }

        else if (systemId.startsWith("jar:"))
        {
            URL url = new URL(systemId);
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            JarEntry jarEntry = conn.getJarEntry();
            if (jarEntry == null)
            {
                log.fatal("JAR entry '" + systemId + "' not found.");
            }
            //_jarFile.getInputStream(jarEntry);
            stream = conn.getJarFile().getInputStream(jarEntry);
        }
        else
        {
            if (_externalContext == null)
            {
                stream = ClassUtils.getResourceAsStream(systemId);
            }
            else
            {
                if (systemId.startsWith("file:")) {
                    systemId = systemId.substring(7); // remove file://
                }
                stream = _externalContext.getResourceAsStream(systemId);
            }
        }

        InputSource is = new InputSource(stream);
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        is.setEncoding("ISO-8859-1");
        return is;
    }

}
