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
package net.sourceforge.myfaces.renderkit.html.jspinfo;

import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JasperException;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JspCompilationContext;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.compiler.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class JspViewParser
{
    private static final Log log = LogFactory.getLog(JspViewParser.class);

    private String _topFileEncoding = "ISO-8859-1";
    private JspCompilationContext _jspCompilationContext = null;
    private MyParseEventListener _parseEventListener = null;
    private Stack _baseDirStack = new Stack();
    private ServletContext _servletContext = null;
    private JspInfo _jspInfo = null;

    public JspViewParser(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    public ServletContext getServletContext()
    {
        return _servletContext;
    }

    public JspInfo getJspInfo()
    {
        return _jspInfo;
    }

    protected void init(String viewId)
    {
        UIViewRoot viewRoot = new UIViewRoot();
        viewRoot.setViewId(viewId);
        viewRoot.setRenderKitId(RenderKitFactory.DEFAULT_RENDER_KIT);

        _jspInfo = new JspInfo(viewRoot);

        _jspCompilationContext = new MyJspCompilationContext(_servletContext);
        _parseEventListener = new MyParseEventListener(this,
                                                       _jspCompilationContext,
                                                       _jspInfo);
    }


    public void parse(String treeId)
        throws FacesException
    {
        init(treeId);

        String topFileName = _jspInfo.getFilePath(_servletContext);
        InputStream stream;
        try
        {
            URL url = _servletContext.getResource(topFileName);
            if (url == null)
            {
                throw new FacesException("JspTreeParser: File '" + topFileName + "' not found.");
            }
            URLConnection urlConn = url.openConnection();
            _jspInfo.setLastModified(urlConn.getLastModified());
            stream = urlConn.getInputStream();
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }

        if (log.isDebugEnabled()) log.debug("Parsing JSP file '" + topFileName + "'.");
        parseFile(topFileName, stream);
    }


    protected void parseIncludeFile(String fileName)
        throws FacesException
    {
        String absFileName = resolveFileName(fileName);
        InputStream stream = _servletContext.getResourceAsStream(absFileName);
        if (stream == null)
        {
            throw new FacesException("File " + absFileName + " not found.");
        }
        parseFile(absFileName, stream);
    }

    protected void parseIncludePage(String pageName)
        throws FacesException
    {
        if (pageName.startsWith("<%"))
        {
            log.warn("Cannot parse dynamically included page '" + pageName + "'.");
            return;
        }

        pageName = pageName.replace('\\', '/');
        boolean isAbsolute = pageName.startsWith("/");
        if (!isAbsolute)
        {
            log.warn("Relative includes with <jsp:include> are not supported yet!");
            return;
        }

        String absFileName = pageName;
        InputStream stream = _servletContext.getResourceAsStream(absFileName);
        if (stream == null)
        {
            throw new FacesException("File " + absFileName + " not found.");
        }
        parseFile(absFileName, stream);
    }

    private void parseFile(String absFileName,
                           InputStream stream)
        throws FacesException
    {
        String baseDir = absFileName.substring(0,
                                               absFileName.lastIndexOf("/") + 1);
        _baseDirStack.push(baseDir);
        try
        {
            //TODO: find out encoding

            InputStreamReader isr = null;
            try
            {
                isr = new InputStreamReader(stream, _topFileEncoding);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new FacesException(e);
            }

            try
            {
                Parser p = new Parser(_jspCompilationContext,
                                      absFileName,
                                      _topFileEncoding,
                                      isr,
                                      _parseEventListener);
                p.parse();
            }
            catch (JasperException e)
            {
                throw new FacesException(e);
            }
            catch (java.io.FileNotFoundException e)
            {
                throw new FacesException(e);
            }
            finally
            {
                try
                {
                    isr.close();
                }
                catch (IOException e)
                {
                    throw new FacesException(e);
                }
            }
        }
        finally
        {
            _baseDirStack.pop();
        }
    }



    /**
     * Copyright (c) 1999 The Apache Software Foundation.
     * (Copied from jasper's ParserController)
     * Modified by manolito: push is done parseFile
     *
     * Resolve the name of the file and update
     * baseDirStack() to keep track ot the current
     * base directory for each included file.
     * The 'root' file is always an 'absolute' path,
     * so no need to put an initial value in the
     * baseDirStack.
     */
    private String resolveFileName(String inFileName)
    {
        String fileName = inFileName.replace('\\', '/');
        boolean isAbsolute = fileName.startsWith("/");
        fileName = isAbsolute
            ? fileName
            : (String)_baseDirStack.peek() + fileName;
        //String baseDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
        //_baseDirStack.push(baseDir);
        return fileName;
    }

}
