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
import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.tree.TreeImpl;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * JspInfo is a helper class that returns useful static information on a JSP. Static means
 * prior to rendering the page.<br>
 * These infos are:
 * <ul>
 *  <li>The full faces tree of all common in that JSP.</li>
 *  <li>The type of each bean on that page as given in the "class" attribute of the useBean declaration.</li>
 *  <li>The FacesTag, that is responsible to create a component.</li>
 * <ul>
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspInfo
{
    public static final String CREATOR_TAG_CLASS_ATTR = JspInfo.class.getName() + ".CREATOR_TAG_CLASS";
    public static final String JSP_POSITION_ATTR = JspInfo.class.getName() + ".JSP_POSITION";
    public static final String HARDCODED_ID_ATTR = JspInfo.class.getName() + ".HARDCODED_ID";

    private Tree _tree = null;
    private String _filePath = null;
    private long _lastModified = 0;
    private Map _jspBeanInfosMap = new HashMap();
    private List _saveStateComponents = new ArrayList();
    private Map _componentMap = new HashMap();
    private byte[] _serializedTree = null;

    public JspInfo(Tree tree)
    {
        _tree = tree;
    }

    public Tree getTree()
    {
        return _tree;
    }

    public Tree getTreeClone()
    {
        if (_serializedTree == null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(_tree);
                oos.close();
                baos.close();
            }
            catch (IOException e)
            {
                LogUtil.getLogger().severe(e.getMessage());
                throw new RuntimeException(e);
            }
            _serializedTree = baos.toByteArray();
        }

        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(_serializedTree);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Tree)ois.readObject();
        }
        catch (IOException e)
        {
            LogUtil.getLogger().severe(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            LogUtil.getLogger().severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setJspBeanInfo(String beanId, JspBeanInfo jspBeanInfo)
    {
        _jspBeanInfosMap.put(beanId, jspBeanInfo);
    }

    public JspBeanInfo getJspBeanInfo(String beanId)
    {
        return (JspBeanInfo)_jspBeanInfosMap.get(beanId);
    }


    /**
     * Returns an Iterator over the entry Set (!) of the parsed JspBeanInfos.
     * @return
     */
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

    public Map getComponentMap()
    {
        return _componentMap;
    }

    public String getFilePath(ServletContext servletContext)
    {
        if (_filePath == null)
        {
            ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
            ServletMapping sm = smf.getServletMapping(servletContext);
            _filePath = sm.mapTreeIdToFilename(servletContext, _tree.getTreeId());
        }
        return _filePath;
    }

    public boolean isModified(ServletContext servletContext)
    {
        String filePath = getFilePath(servletContext);
        URLConnection urlConn = null;
        try
        {
            URL url = servletContext.getResource(filePath);
            urlConn = url.openConnection();
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
        //System.out.println("urlConn.getLastModified() = " + urlConn.getLastModified());
        //System.out.println("_lastModified = " + _lastModified);
        return urlConn.getLastModified() > _lastModified;
    }

    public void setLastModified(long lastModified)
    {
        _lastModified = lastModified;
    }






    public static Tree getTree(FacesContext facesContext,
                               String treeId)
    {
        return getJspInfo(facesContext, treeId).getTree();
    }

    public static Tree getTreeClone(FacesContext facesContext,
                                    String treeId)
    {
        return getJspInfo(facesContext, treeId).getTreeClone();
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


    /*
    public static UIComponentTag getCreatorTag(UIComponent uiComponent)
    {
        return (UIComponentTag)uiComponent.getAttribute(JspInfo.CREATOR_TAG_ATTR);
    }
    */



    public static Iterator getUISaveStateComponents(FacesContext facesContext,
                                                    String treeId)
    {
        return getJspInfo(facesContext, treeId).getUISaveStateComponents();
    }

    public static Map getComponentMap(FacesContext facesContext,
                                      String treeId)
    {
        return getJspInfo(facesContext, treeId).getComponentMap();
    }


    private static final String LAST_JSP_INFO_REQUEST_ATTR
        = JspInfo.class.getName() + ".LAST_JSP_INFO";

    private static JspInfo getJspInfo(FacesContext facesContext,
                                      String treeId)
    {
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();

        //Try the last JspInfo in this request
        JspInfo jspInfo = (JspInfo)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(LAST_JSP_INFO_REQUEST_ATTR);
        if (jspInfo != null &&
            jspInfo.getTree().getTreeId().equals(treeId))
        {
            return jspInfo;
        }

        Map jspInfoMap = getJspInfoMap(servletContext);
        jspInfo = (JspInfo)jspInfoMap.get(treeId);

        //Check for modification
        if (jspInfo != null &&
            MyFacesConfig.isCheckJspModification(servletContext) &&
            jspInfo.isModified(servletContext))
        {
            LogUtil.getLogger().info("JSP file '" + jspInfo.getFilePath(servletContext) + "' was modified, reparsing.");
            jspInfo = null;
        }

        if (jspInfo == null)
        {
            if (MyFacesConfig.isDisableJspParser(servletContext))
            {
                LogUtil.getLogger().info("JSP parsing is disabled, JspInfo cannot be applied.");
                jspInfo = new JspInfo(new TreeImpl(treeId));
            }
            else
            {
                JspTreeParser parser = new JspTreeParser(servletContext);
                parser.parse(treeId);
                jspInfo = parser.getJspInfo();
            }
            jspInfoMap.put(treeId, jspInfo);
        }

        ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(LAST_JSP_INFO_REQUEST_ATTR,
                                                      jspInfo);
        return jspInfo;
    }


    private static final String JSP_INFO_MAP_CONTEXT_ATTR
        = JspInfo.class.getName() + ".JSP_INFO_MAP";

    private static Map getJspInfoMap(ServletContext servletContext)
    {
        synchronized (servletContext)
        {
            Map map = (Map)servletContext.getAttribute(JSP_INFO_MAP_CONTEXT_ATTR);
            if (map == null)
            {
                map = new WeakHashMap();
                servletContext.setAttribute(JSP_INFO_MAP_CONTEXT_ATTR, map);
            }
            return map;
        }
    }


}
