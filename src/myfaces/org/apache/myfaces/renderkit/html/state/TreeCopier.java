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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.webapp.FacesTag;
import java.util.Iterator;
import java.util.Set;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TreeCopier
{
    private FacesContext _facesContext;
    private boolean _overwriteComponents = false;
    private boolean _overwriteAttributes = false;
    private Set _ignoreComponents = null;

    public TreeCopier(FacesContext facesContext)
    {
        _facesContext = facesContext;
    }

    public void setOverwriteAttributes(boolean overwriteAttributes)
    {
        _overwriteAttributes = overwriteAttributes;
    }

    public void setOverwriteComponents(boolean overwriteComponents)
    {
        _overwriteComponents = overwriteComponents;
    }

    /**
     * @param ignoreComponents  Set of uniqueIds of components that should not be copied
     */
    public void setIgnoreComponents(Set ignoreComponents)
    {
        _ignoreComponents = ignoreComponents;
    }


    public void copyTree(Tree fromTree, Tree toTree)
    {
        copyComponent(fromTree.getRoot(), toTree, toTree.getRoot());
    }

    /*
    public void copySubTree(UIComponent fromComponent, UIComponent toComponent)
    {
        copyComponent(fromComponent, toComponent);
    }
    */


    /**
     * @param fromComp          source components, where attributes and children should be copied from
     * @param toComp            destination component, where attributes and children should be copied to
     */
    protected void copyComponent(UIComponent fromComp,
                                 Tree toTree,
                                 UIComponent toComp)
    {
        copyAttributes(fromComp, toComp);

        int childIndex = 0;
        for (Iterator it = fromComp.getChildren(); it.hasNext(); childIndex++)
        {
            UIComponent child = (UIComponent)it.next();
            String uniqueId = JspInfo.getUniqueComponentId(child);

            if (_ignoreComponents != null &&
                _ignoreComponents.contains(uniqueId))
            {
                continue;
            }

            UIComponent clone;

            try
            {
                //destination component already exists?
                //clone = toComp.findComponent(child.getComponentId());
                clone = JspInfo.findComponentByUniqueId(toTree, uniqueId);
            }
            catch (Exception e)
            {
                clone = null;
            }

            if (_overwriteComponents && clone != null)
            {
                clone.getParent().removeChild(clone);
                clone = null;
            }

            if (clone == null)
            {
                clone = cloneComponent(child);

                clone.setComponentId(child.getComponentId());
                toComp.addChild(childIndex, clone);

                copyComponent(child, toTree, clone);    //Recursion
            }
        }
    }

    private UIComponent cloneComponent(UIComponent toBeCloned)
    {
        FacesTag tag = JspInfo.getCreatorTag(toBeCloned);
        if (tag != null)
        {
            UIComponent clone = tag.createComponent();
            return clone;
        }

        LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(toBeCloned) + " has no creator tag attribute.");

        try
        {
            return (UIComponent)toBeCloned.getClass().newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }


    protected void copyAttributes(UIComponent fromComp,
                                  UIComponent toComp)
    {
        for (Iterator it = fromComp.getAttributeNames(); it.hasNext();)
        {
            String attrName = (String)it.next();
            if (_overwriteAttributes || toComp.getAttribute(attrName) == null)
            {
                Object attrValue = fromComp.getAttribute(attrName);

                //TODO: always try bean property setter first!
                if (attrName.equals(CommonComponentAttributes.COMPONENT_ID_ATTR))
                {
                    toComp.setComponentId((String)attrValue);
                }
                else
                {
                    toComp.setAttribute(attrName, attrValue);
                }
            }
        }
    }

}
