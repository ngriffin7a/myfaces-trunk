/*
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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TreeStructureManager
{
    //private static final Log log = LogFactory.getLog(TreeStructureManager.class);

    //private FacesContext _facesContext;

    public TreeStructureManager()
    {
        //_facesContext = facesContext;
    }

    public Object buildTreeStructureToSave(UIViewRoot viewRoot)
    {
        return internalBuildTreeStructureToSave(viewRoot);
    }

    private TreeStructComponent internalBuildTreeStructureToSave(UIComponent component)
    {
        TreeStructComponent structComp = new TreeStructComponent(component.getClass().getName(),
                                                                 component.getId());

        //children
        if (component.getChildCount() > 0)
        {
            List childList = component.getChildren();
            List structChildList = new ArrayList();
            for (int i = 0, len = childList.size(); i < len; i++)
            {
                UIComponent child = (UIComponent)childList.get(i);
                if (!child.isTransient())
                {
                    TreeStructComponent structChild = internalBuildTreeStructureToSave(child);
                    structChildList.add(structChild);
                }
            }
            TreeStructComponent[] childArray = (TreeStructComponent[])structChildList.toArray(new TreeStructComponent[structChildList.size()]);
            structComp.setChildren(childArray);
        }

        //facets
        Map facetMap = component.getFacets();
        if (!facetMap.isEmpty())
        {
            List structFacetList = new ArrayList();
            for (Iterator it = facetMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                UIComponent child = (UIComponent)entry.getValue();
                if (!child.isTransient())
                {
                    String facetName = (String)entry.getKey();
                    TreeStructComponent structChild = internalBuildTreeStructureToSave(child);
                    structFacetList.add(new Object[] {facetName, structChild});
                }
            }
            Object[] facetArray = structFacetList.toArray(new Object[structFacetList.size()]);
            structComp.setFacets(facetArray);
        }

        return structComp;
    }


    public UIViewRoot restoreTreeStructure(Object treeStructRoot)
    {
        if (treeStructRoot instanceof TreeStructComponent)
        {
            return (UIViewRoot)internalRestoreTreeStructure((TreeStructComponent)treeStructRoot);
        }
        else
        {
            throw new IllegalArgumentException("TreeStructure of type " + treeStructRoot.getClass().getName() + " is not supported.");
        }
    }

    private UIComponent internalRestoreTreeStructure(TreeStructComponent treeStructComp)
    {
        String compClass = treeStructComp.getComponentClass();
        String compId = treeStructComp.getComponentId();
        UIComponent component = (UIComponent)ClassUtils.newInstance(compClass);
        component.setId(compId);

        //children
        TreeStructComponent[] childArray = treeStructComp.getChildren();
        if (childArray != null)
        {
            List childList = component.getChildren();
            for (int i = 0, len = childArray.length; i < len; i++)
            {
                UIComponent child = internalRestoreTreeStructure(childArray[i]);
                childList.add(child);
            }
        }

        //facets
        Object[] facetArray = treeStructComp.getFacets();
        if (facetArray != null)
        {
            Map facetMap = component.getFacets();
            for (int i = 0, len = facetArray.length; i < len; i++)
            {
                Object[] tuple = (Object[])facetArray[i];
                String facetName = (String)tuple[0];
                TreeStructComponent structChild = (TreeStructComponent)tuple[1];
                UIComponent child = internalRestoreTreeStructure(structChild);
                facetMap.put(facetName, child);
            }
        }

        return component;
    }


    public static class TreeStructComponent
            implements Serializable
    {
        private String _componentClass;
        private String _componentId;
        private TreeStructComponent[] _children = null;    // Array of children
        private Object[] _facets = null;                   // Array of Array-tuples with Facetname and TreeStructComponent

        TreeStructComponent(String componentClass, String componentId)
        {
            _componentClass = componentClass;
            _componentId = componentId;
        }

        public String getComponentClass()
        {
            return _componentClass;
        }

        public String getComponentId()
        {
            return _componentId;
        }

        void setChildren(TreeStructComponent[] children)
        {
            _children = children;
        }

        TreeStructComponent[] getChildren()
        {
            return _children;
        }

        Object[] getFacets()
        {
            return _facets;
        }

        void setFacets(Object[] facets)
        {
            _facets = facets;
        }
    }

}
