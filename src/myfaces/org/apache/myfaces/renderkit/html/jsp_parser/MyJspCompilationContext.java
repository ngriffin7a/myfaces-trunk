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
package net.sourceforge.myfaces.renderkit.html.jsp_parser;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.Compiler;
import org.apache.jasper.compiler.JspReader;
import org.apache.jasper.compiler.ServletWriter;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyJspCompilationContext
        implements JspCompilationContext
{
    private ServletContext _servletContext;

    public MyJspCompilationContext(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    public Compiler createCompiler() throws JasperException
    {
        throw new UnsupportedOperationException();
    }

    public ClassLoader getClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }

    public String getClassPath()
    {
        throw new UnsupportedOperationException();
    }

    public String getContentType()
    {
        throw new UnsupportedOperationException();
    }

    public String getJavacOutputDir()
    {
        throw new UnsupportedOperationException();
    }

    public String getJspFile()
    {
        throw new UnsupportedOperationException();
    }

    public Options getOptions()
    {
        throw new UnsupportedOperationException();
    }

    public String getOutputDir()
    {
        throw new UnsupportedOperationException();
    }

    public JspReader getReader()
    {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String path)
    {
        return _servletContext.getRealPath(path);
    }

    public URL getResource(String res) throws MalformedURLException
    {
        return _servletContext.getResource(res);
    }

    public InputStream getResourceAsStream(String res)
    {
        return _servletContext.getResourceAsStream(res);
    }

    public String getServletClassName()
    {
        throw new UnsupportedOperationException();
    }

    public String getServletJavaFileName()
    {
        throw new UnsupportedOperationException();
    }

    public String getServletPackageName()
    {
        throw new UnsupportedOperationException();
    }

    public String[] getTldLocation(String uri) throws JasperException
    {
        throw new UnsupportedOperationException();
    }

    public ServletWriter getWriter()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isErrorPage()
    {
        throw new UnsupportedOperationException();
    }

    public boolean keepGenerated()
    {
        throw new UnsupportedOperationException();
    }

    public String resolveRelativeUri(String uri)
    {
        throw new UnsupportedOperationException();
    }

    public void setContentType(String contentType)
    {
        throw new UnsupportedOperationException();
    }

    public void setErrorPage(boolean isErrPage)
    {
        throw new UnsupportedOperationException();
    }

    public void setReader(JspReader reader)
    {
        throw new UnsupportedOperationException();
    }

    public void setServletClassName(String servletClassName)
    {
        throw new UnsupportedOperationException();
    }

    public void setServletJavaFileName(String servletJavaFileName)
    {
        throw new UnsupportedOperationException();
    }

    public void setServletPackageName(String servletPackageName)
    {
        throw new UnsupportedOperationException();
    }

    public void setWriter(ServletWriter writer)
    {
        throw new UnsupportedOperationException();
    }
}
