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
package net.sourceforge.myfaces.config;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesConfigEntityResolver
    implements EntityResolver
{
    private ServletContext _servletContext = null;
    private JarFile _jarFile = null;

    public FacesConfigEntityResolver(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    public FacesConfigEntityResolver(JarFile jarFile)
    {
        _jarFile = jarFile;
    }

    public InputSource resolveEntity(String publicId,
                                     String systemId)
        throws SAXException, IOException
    {
        InputStream stream;
        if (systemId.equals("http://java.sun.com/dtd/web-facesconfig_1_0.dtd"))
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            stream = loader.getResourceAsStream("net/sourceforge/myfaces/resource/web-facesconfig_1_0.dtd");
        }
        else if (_servletContext != null)
        {
            stream = _servletContext.getResourceAsStream(systemId);
        }
        else if (_jarFile != null)
        {
            JarEntry jarEntry = _jarFile.getJarEntry(systemId);
            stream = _jarFile.getInputStream(jarEntry);
        }
        else
        {
            return null;
        }

        InputSource is = new InputSource(stream);
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        is.setEncoding("ISO-8859-1");
        return is;
    }

}
