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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspTreeParser;

import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.webapp.FacesTag;
import javax.faces.component.UIComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * JspInfo is a helper class that returns useful static information on a JSP. Static means
 * prior to rendering the page.<br>
 * These infos are:
 * <ul>
 *  <li>The full faces tree of all components in that JSP.</li>
 *  <li>The type of each bean on that page as given in the "class" attribute of the useBean declaration.</li>
 *  <li>The FacesTag, that is responsible to create a component.</li>
 * <ul>
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspInfo
{
    private Tree _staticTree;
    private Map _beanTypesMap;
    private Map _creatorTagsMap;

    public JspInfo(Tree staticTree, Map beanTypesMap, Map creatorTagsMap)
    {
        _staticTree = staticTree;
        _beanTypesMap = beanTypesMap;
        _creatorTagsMap = creatorTagsMap;
    }

    public Tree getStaticTree()
    {
        return _staticTree;
    }

    public Map getBeanTypesMap()
    {
        return _beanTypesMap;
    }

    public Map getCreatorTagsMap()
    {
        return _creatorTagsMap;
    }



    private static final String JSP_INFO_MAP_ATTR = JspInfo.class.getName() + ".JSP_INFO_MAP";

    public static Tree getStaticTree(FacesContext facesContext,
                                     String treeId)
    {
        return getJspInfo(facesContext, treeId).getStaticTree();
    }

    public static String getBeanType(FacesContext facesContext,
                                     String treeId,
                                     String beanId)
    {
        Map beanTypesMap = getJspInfo(facesContext, treeId).getBeanTypesMap();
        if (beanTypesMap == null)
        {
            LogUtil.getLogger().severe("No beanTypesMap found for tree " + treeId);
            return null;
        }
        else
        {
            return (String)beanTypesMap.get(beanId);
        }
    }

    public static FacesTag getCreatorTag(FacesContext facesContext,
                                         String treeId,
                                         String compoundId)
    {
        Map creatorTagsMap = getJspInfo(facesContext, treeId).getCreatorTagsMap();
        if (creatorTagsMap == null)
        {
            LogUtil.getLogger().severe("No creatorTagsMap found for tree " + treeId);
            return null;
        }
        else
        {
            return (FacesTag)creatorTagsMap.get(compoundId);
        }
    }

    public static FacesTag getCreatorTag(FacesContext facesContext,
                                         String treeId,
                                         UIComponent uiComponent)
    {
        return getCreatorTag(facesContext, treeId, uiComponent.getCompoundId());
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
                                  parser.getBeanClassesMap(),
                                  parser.getCreatorTagsMap());
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
