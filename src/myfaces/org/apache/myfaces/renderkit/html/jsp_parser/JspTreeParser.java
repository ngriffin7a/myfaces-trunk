/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
import org.apache.jasper.compiler.Parser;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class JspTreeParser
{
    private ServletContext _servletContext = null;
    private Tree _tree = null;
    private String _topFileEncoding = "ISO-8859-1";
    private Map _beanClassesMap = null;
//    private Set _variables = null;
    private JspCompilationContext _jspCompilationContext = null;
    private MyParseEventListener _parseEventListener = null;
    private Stack baseDirStack = new Stack();

    public JspTreeParser(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    public Tree getTree()
    {
        return _tree;
    }

    public Map getBeanClassesMap()
    {
        return _beanClassesMap;
    }

    protected void init(String treeId)
    {
        TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
        _tree = tf.getTree(_servletContext, treeId);

        _beanClassesMap = new HashMap();

        _jspCompilationContext = new MyJspCompilationContext(_servletContext);
        _parseEventListener = new MyParseEventListener(this,
                                                       _jspCompilationContext,
                                                       _tree,
                                                       _beanClassesMap);
    }


    public void parse(String treeId)
        throws FacesException
    {
        init(treeId);
        parseFile(treeId);
    }


    protected void parseFile(String fileName)
        throws FacesException
    {
        String absFileName = resolveFileName(fileName);

        InputStream stream = _servletContext.getResourceAsStream(absFileName);
        if (stream == null)
        {
            throw new RuntimeException("File " + absFileName + " not found.");
        }

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
    }



    /**
     * Copyright (c) 1999 The Apache Software Foundation.
     * (Copied from jasper's ParserController)
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
            : (String)baseDirStack.peek() + fileName;
        String baseDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
        baseDirStack.push(baseDir);
        return fileName;
    }

}
