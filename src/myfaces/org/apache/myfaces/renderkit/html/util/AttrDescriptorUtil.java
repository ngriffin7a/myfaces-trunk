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
package net.sourceforge.myfaces.renderkit.html.util;



/**
 * Utilities to create AttributeDescriptors from a TLD.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 * @deprecated
 */
public class AttrDescriptorUtil
{
    private AttrDescriptorUtil() {}

    /**
     * Gets the TagAttributeInfo from the given attribute in the specified
     * taglib and returns a new AttributeDescriptor with the given
     * renderer attribute name and the determined type.
     * @param servletContext
     * @param taglibURI
     * @param tagName
     * @param tagAttributeName
     * @param rendererAttributeName
     * @return a new AttributeDescriptor
     */
    /*
    public static AttributeDescriptor getAttributeDescriptor(ServletContext servletContext,
                                                             String taglibURI,
                                                             String tagName,
                                                             String tagAttributeName,
                                                             String rendererAttributeName)
    {
        final TagAttributeInfo tagAttributeInfo
            = TLDInfo.getTagAttributeInfo(servletContext,
                                          taglibURI,
                                          tagName,
                                          tagAttributeName);
        Class clazz;
        String type = tagAttributeInfo.getTypeName();
        if (type == null)
        {
            clazz = String.class;
        }
        else
        {
            try
            {
                clazz = Class.forName(type);
            }
            catch (ClassNotFoundException e)
            {
                LogUtil.getLogger().severe("Attribute '" + tagAttributeName + "' of tag '" + tagName + "' has illegal type:" + e.getMessage());
                clazz = Object.class;
            }
        }

        return new AttrDescriptorImpl(rendererAttributeName, clazz);
    }
    */


    /**
     * Identical to {@link #getAttributeDescriptor(javax.servlet.ServletContext, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * but for the (usual) case where the renderer attribute name is identical to the
     * tag attribute name.
     * @param servletContext
     * @param taglibURI
     * @param tagName
     * @param attributeName
     * @return
     */
    /*
    public static AttributeDescriptor getAttributeDescriptor(ServletContext servletContext,
                                                             String taglibURI,
                                                             final String tagName,
                                                             final String attributeName)
    {
        return getAttributeDescriptor(servletContext,
                                      taglibURI,
                                      tagName,
                                      attributeName,
                                      attributeName);
    }
    */



}
