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
package net.sourceforge.myfaces.renderkit.html.state;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.jsp_parser.JspTreeParser;

import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspInfo
{
    private Tree _staticTree;
    private Map _beanClassesMap;
    //private Set _variables;

    public JspInfo(Tree staticTree, Map beanClasses)
    {
        _staticTree = staticTree;
        _beanClassesMap = beanClasses;
    }

    public Tree getStaticTree()
    {
        return _staticTree;
    }

    public Map getBeanClassesMap()
    {
        return _beanClassesMap;
    }

    /*
    public Set getVariables()
    {
        return _variables;
    }
    */


    private static final String JSP_INFO_MAP_ATTR = JspInfo.class.getName() + ".JSP_INFO_MAP";

    public static Tree getStaticTree(FacesContext facesContext,
                                     String treeId)
    {
        return getJspInfo(facesContext, treeId).getStaticTree();
    }

    public static Map getBeanClassesMap(FacesContext facesContext,
                                        String treeId)
    {
        return getJspInfo(facesContext, treeId).getBeanClassesMap();
    }


    private static JspInfo getJspInfo(FacesContext facesContext,
                                      String treeId)
    {
        Map jspInfoMap = getJspInfoMap(facesContext);
        JspInfo jspInfo = (JspInfo)jspInfoMap.get(treeId);
        if (jspInfo == null)
        {
            JspTreeParser parser = new JspTreeParser(facesContext.getServletContext());
            parser.parse(treeId);
            jspInfo = new JspInfo(parser.getTree(),
                                  parser.getBeanClassesMap());
            jspInfoMap.put(treeId, jspInfo);
        }
        return jspInfo;
    }

    private static Map getJspInfoMap(FacesContext facesContext)
    {
        Map map;
        if (MyFacesConfig.isJspInfoApplicationCaching())
        {
            map = (Map)facesContext.getServletContext().getAttribute(JSP_INFO_MAP_ATTR);
        }
        else
        {
            map = (Map)facesContext.getServletRequest().getAttribute(JSP_INFO_MAP_ATTR);
        }
        if (map == null)
        {
            map = new HashMap();
            if (MyFacesConfig.isJspInfoApplicationCaching())
            {
                facesContext.getServletContext().setAttribute(JSP_INFO_MAP_ATTR, map);
            }
            else
            {
                facesContext.getServletRequest().setAttribute(JSP_INFO_MAP_ATTR, map);
            }
        }
        return map;
    }

}
