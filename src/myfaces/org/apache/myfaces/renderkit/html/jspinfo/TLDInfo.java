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

import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JspCompilationContext;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JasperException;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.compiler.TagLibraryInfoImpl;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TLDInfo
{
    private JspCompilationContext _jspCompilationContext = null;
    private String _uri = null;
    private TagLibraryInfo _tagLibraryInfo = null;

    private TLDInfo(ServletContext servletContext, String uri)
    {
        _jspCompilationContext = new MyJspCompilationContext(servletContext);
        _uri = uri;
    }

    public TagLibraryInfo getTagLibraryInfo()
    {
        if (_tagLibraryInfo == null)
        {
            try
            {
                _tagLibraryInfo = new TagLibraryInfoImpl(_jspCompilationContext,
                                                         "dummy",
                                                         _uri);
            }
            catch (JasperException e)
            {
                LogUtil.getLogger().severe("Unable to get TagLibraryInfo for library '" + _uri + "'.");
                throw new RuntimeException(e);
            }
        }
        return _tagLibraryInfo;
    }



    /**
     * TODO: Cache in ServletContext
     * @param servletContext
     * @param taglibURI
     * @return
     */
    public static TagLibraryInfo getTagLibraryInfo(ServletContext servletContext,
                                                   String taglibURI)
    {
        return new TLDInfo(servletContext, taglibURI).getTagLibraryInfo();
    }

    public static TagInfo getTagInfo(ServletContext servletContext,
                                     String taglibURI,
                                     String tagName)
        throws IllegalArgumentException
    {
        TagLibraryInfo tagLibraryInfo = getTagLibraryInfo(servletContext,
                                                          taglibURI);
        TagInfo[] tags = tagLibraryInfo.getTags();
        for (int i = 0; i < tags.length; i++)
        {
            if (tags[i].getTagName().equals(tagName))
            {
                return tags[i];
            }
        }
        throw new IllegalArgumentException("TagInfo for tag '" + tagName + "' in TagLibrary '" + taglibURI + "' not found.");
    }

    public static TagAttributeInfo getTagAttributeInfo(ServletContext servletContext,
                                                       String taglibURI,
                                                       String tagName,
                                                       String attributeName)
    {
        TagInfo tagInfo = getTagInfo(servletContext, taglibURI, tagName);
        TagAttributeInfo[] attrs = tagInfo.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            if (attrs[i].getName().equals(attributeName))
            {
                return attrs[i];
            }
        }
        throw new IllegalArgumentException("TagAttributeInfo for attribute '" + attributeName + "' of Tag '" + tagName + "' in TagLibrary '" + taglibURI + "' not found.");
    }
}
