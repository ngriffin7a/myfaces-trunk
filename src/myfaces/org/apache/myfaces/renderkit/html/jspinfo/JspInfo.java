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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.webapp.FacesTag;
import java.util.*;

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
    public static final String CREATOR_TAG_ATTR = JspInfo.class.getName() + ".CREATOR_TAG";
    public static final String ACTION_LISTENERS_TYPE_LIST_ATTR = JspInfo.class.getName() + ".LISTENERS";

    private Tree _tree = null;
    private Map _jspBeanInfosMap = new HashMap();
    //private Map _creatorTagsMap = new HashMap();
    private List _saveStateComponents = new ArrayList();

    public JspInfo(Tree tree)
    {
        _tree = tree;
    }

    public Tree getTree()
    {
        return _tree;
    }


    public void setJspBeanInfo(String beanId, JspBeanInfo jspBeanInfo)
    {
        _jspBeanInfosMap.put(beanId, jspBeanInfo);
    }

    public JspBeanInfo getJspBeanInfo(String beanId)
    {
        return (JspBeanInfo)_jspBeanInfosMap.get(beanId);
    }

    public Iterator getJspBeanInfos()
    {
        return _jspBeanInfosMap.entrySet().iterator();
    }


    /*
    public void setCreatorTag(String compoundId, FacesTag tag)
    {
        _creatorTagsMap.put(compoundId, tag);
    }

    public FacesTag getCreatorTag(String compoundId)
    {
        return (FacesTag)_creatorTagsMap.get(compoundId);
    }
    */


    public void addUISaveStateComponent(UIComponent uiSaveState)
    {
        _saveStateComponents.add(uiSaveState);
    }

    public Iterator getUISaveStateComponents()
    {
        return _saveStateComponents.iterator();
    }






    public static Tree getTree(FacesContext facesContext,
                                     String treeId)
    {
        return getJspInfo(facesContext, treeId).getTree();
    }

    public static JspBeanInfo getJspBeanInfo(FacesContext facesContext,
                                             String treeId,
                                             String beanId)
    {
        return getJspInfo(facesContext, treeId).getJspBeanInfo(beanId);
    }

    public static Iterator getJspBeanInfos(FacesContext facesContext,
                                           String treeId)
    {
        return getJspInfo(facesContext, treeId).getJspBeanInfos();
    }

    public static FacesTag getCreatorTag(UIComponent uiComponent)
    {
        return (FacesTag)uiComponent.getAttribute(JspInfo.CREATOR_TAG_ATTR);
    }

    public static List getActionListenersTypeList(UIComponent uiComponent)
    {
        return (List)uiComponent.getAttribute(JspInfo.ACTION_LISTENERS_TYPE_LIST_ATTR);
    }


    public static Iterator getUISaveStateComponents(FacesContext facesContext,
                                                    String treeId)
    {
        return getJspInfo(facesContext, treeId).getUISaveStateComponents();
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
            jspInfo = parser.getJspInfo();
            jspInfoMap.put(treeId, jspInfo);
        }
        return jspInfo;
    }


    private static final String JSP_INFO_MAP_ATTR = JspInfo.class.getName() + ".JSP_INFO_MAP";

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
