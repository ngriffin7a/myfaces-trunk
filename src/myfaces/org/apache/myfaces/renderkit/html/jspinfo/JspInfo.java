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
import net.sourceforge.myfaces.tree.TreeUtils;

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

    private static final char COMPONENT_ID_SEPARATOR_CHAR = ':';

    private static final String UNIQUE_COMPONENT_ID = JspInfo.class.getName() + ".UNIQUE_COMPONENT";
    private static final char UNIQUE_COMPONENT_ID_SEPARATOR_CHAR = ':';


    private Tree _tree = null;
    private Map _jspBeanInfosMap = new HashMap();
    private List _saveStateComponents = new ArrayList();
    private boolean _clientIdsCreated = false;

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



    /*
    private void checkClientIds(UIComponent comp, int childIndex)
    {
        getStaticClientId(comp, childIndex);

        int childCnt = 0;
        for (Iterator it = comp.getFacetsAndChildren(); it.hasNext(); childCnt++)
        {
            checkClientIds((UIComponent)it.next(), childCnt);
        }
    }


    public static String getStaticClientId(UIComponent uiComponent,
                                           int childIndex)
    {
        String clientId = (String)uiComponent.getAttribute(UIComponent.CLIENT_ID_ATTR);
        if (clientId != null)
        {
            return clientId;
        }

        //Find namingContainer
        UIComponent parent = uiComponent.getParent();
        if (parent == null)
        {
            //we have got the root component
            if (!(uiComponent instanceof NamingContainer))
            {
                throw new FacesException("Root is no naming container?!");
            }

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(UIRoot.ROOT_COMPONENT_ID);
            }
            clientId = uiComponent.getComponentId();
        }
        else
        {
            //Find namingContainer
            UIComponent namingContainer = parent;
            while (!(namingContainer instanceof NamingContainer))
            {
                namingContainer = namingContainer.getParent();
                if (namingContainer == null)
                {
                    throw new FacesException("Root is no naming container?!");
                }
            }

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(generateComponentId(uiComponent,
                                                               childIndex));
            }

            if (namingContainer.getParent() == null)
            {
                //NamingContainer is root, so nothing to be prepended
                clientId = uiComponent.getComponentId();
            }
            else
            {
                String ncClientId = (String)namingContainer.getAttribute(UIComponent.CLIENT_ID_ATTR);
                if (ncClientId == null)
                {
                    //We assume, that the tree is traversed in normal order and
                    //that clientIds of parents already have been generated
                    throw new IllegalStateException("Parent does not have an clientId yet?!");
                }
                clientId = ncClientId + UIComponent.SEPARATOR_CHAR + uiComponent.getComponentId();
            }
        }

        uiComponent.setAttribute(UIComponent.CLIENT_ID_ATTR, clientId);
        return clientId;
    }
    */

    /**
     * We use our own algorithm to generate unique component IDs. This is,
     * to make sure that the dynamically created ID is the same as the ID
     * from the corresponding component in the parsed tree.
     */
    /*
    protected static String generateComponentId(UIComponent uiComponent,
                                                int childIndex)
    {
        UIComponent parent = uiComponent.getParent();
        if (parent == null)
        {
            //we have the root component
            String id = uiComponent.getComponentId();
            return id != null ? id : UIRoot.ROOT_COMPONENT_ID;
        }

        if (parent instanceof NamingContainer)
        {
            return new StringBuffer(COMPONENT_ID_SEPARATOR_CHAR)
                                            .append(childIndex).toString();
        }

        String parentId = parent.getComponentId();
        if (parentId == null)
        {
            //We assume, that the tree is traversed in normal order and
            //that componentIds of parents already have been generated
            throw new IllegalStateException("Parent does not have an componentId yet?!");
        }

        return parentId + COMPONENT_ID_SEPARATOR_CHAR + childIndex;
    }
    */



    public static String getUniqueComponentId(UIComponent uiComponent)
    {
        String uniqueId = (String)uiComponent.getAttribute(UNIQUE_COMPONENT_ID);
        if (uniqueId != null)
        {
            return uniqueId;
        }

        //find root
        UIComponent findRoot = uiComponent;
        while (findRoot.getParent() != null)
        {
            findRoot = findRoot.getParent();
        }

        //assign unique component ids:
        findRoot.setAttribute(UNIQUE_COMPONENT_ID, "");
        assignUniqueIdsToChildren(findRoot, "");

        return (String)uiComponent.getAttribute(UNIQUE_COMPONENT_ID);
    }

    private static void assignUniqueIdsToChildren(UIComponent parent,
                                                  String parentUniqueId)
    {
        int childIdx = 0;
        for (Iterator it = parent.getFacetsAndChildren(); it.hasNext(); childIdx++)
        {
            String uniqueId = parentUniqueId + UNIQUE_COMPONENT_ID_SEPARATOR_CHAR + childIdx;
            UIComponent comp = (UIComponent)it.next();
            comp.setAttribute(UNIQUE_COMPONENT_ID, uniqueId);
            assignUniqueIdsToChildren(comp, uniqueId);
        }
    }



    /**
     * TODO: Can we optimize by a HashMap
     */
    public static UIComponent findComponentByUniqueId(Tree tree, String uniqueId)
    {
        for (Iterator it = TreeUtils.treeIterator(tree); it.hasNext();)
        {
            UIComponent comp = (UIComponent)it.next();
            if (getUniqueComponentId(comp).equals(uniqueId))
            {
                return comp;
            }
        }
        return null;
    }



}
