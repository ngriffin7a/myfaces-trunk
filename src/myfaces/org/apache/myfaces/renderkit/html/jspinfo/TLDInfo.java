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

import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JasperException;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JspCompilationContext;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.compiler.TagLibraryInfoImpl;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.compiler.TldLocationsCache;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TLDInfo
{
    private static final String TAGLIB_MAP_CONTEXT_ATTR
        = TLDInfo.class.getName() + ".TAGLIB_MAP";
    private static final Object LOCK = new Object();

    public static TagAttributeInfo getTagAttributeInfo(ServletContext servletContext,
                                                       String taglibURI,
                                                       String tagName,
                                                       String attributeName)
    {
        Map taglibMap;
        synchronized (LOCK)
        {
            taglibMap = (Map)servletContext.getAttribute(TAGLIB_MAP_CONTEXT_ATTR);
            if (taglibMap == null)
            {
                taglibMap = new WeakHashMap();
                servletContext.setAttribute(TAGLIB_MAP_CONTEXT_ATTR, taglibMap);
            }
        }

        Map tagMap;
        synchronized (taglibMap)
        {
            tagMap = (Map)taglibMap.get(taglibURI);
            if (tagMap == null)
            {
                tagMap = buildTagMap(servletContext, taglibURI);
                taglibMap.put(taglibURI, tagMap);
            }
        }

        Map tagAttributesMap = (Map)tagMap.get(tagName);
        if (tagAttributesMap == null)
        {
            throw new IllegalArgumentException("TagInfo for tag '" + tagName + "' in TagLibrary '" + taglibURI + "' not found.");
        }

        TagAttributeInfo tagAttributeInfo
            = (TagAttributeInfo)tagAttributesMap.get(attributeName);
        if (tagAttributeInfo == null)
        {
            throw new IllegalArgumentException("TagAttributeInfo for attribute '" + attributeName + "' of Tag '" + tagName + "' in TagLibrary '" + taglibURI + "' not found.");
        }
        return tagAttributeInfo;
    }

    private static Map buildTagMap(ServletContext servletContext,
                                   String taglibURI)
        throws IllegalArgumentException
    {
        TagLibraryInfo tagLibraryInfo = getTagLibraryInfo(servletContext,
                                                          taglibURI);
        TagInfo[] tags = tagLibraryInfo.getTags();
        Map tagMap = new HashMap(tags.length);
        for (int i = 0; i < tags.length; i++)
        {
            TagInfo ti = tags[i];
            TagAttributeInfo[] attrs = ti.getAttributes();
            Map tagAttributesMap = new HashMap(attrs.length);
            for (int j = 0; j < attrs.length; j++)
            {
                TagAttributeInfo tai = attrs[j];
                tagAttributesMap.put(tai.getName(), tai);
            }

            tagMap.put(ti.getTagName(), tagAttributesMap);
        }
        return tagMap;
    }

    public static TagLibraryInfo getTagLibraryInfo(ServletContext servletContext,
                                                   String taglibURI)
    {
        try
        {
            TldLocationsCache locCache = new TldLocationsCache(servletContext);
            String[] loc = locCache.getLocation(taglibURI);
            JspCompilationContext jspCompilationContext
                = new MyJspCompilationContext(servletContext);
            return new TagLibraryInfoImpl(jspCompilationContext,
                                          "dummy",
                                          taglibURI,
                                          loc);
        }
        catch (JasperException e)
        {
            LogUtil.getLogger().severe("Unable to get TagLibraryInfo for library '" + taglibURI + "': " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
