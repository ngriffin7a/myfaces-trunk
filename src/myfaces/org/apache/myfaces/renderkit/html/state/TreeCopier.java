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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.taglib.MyFacesTagExtension;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.jsp.tagext.Tag;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TreeCopier
{
    public static final String CREATOR_TAG_ATTR = TreeCopier.class.getName() + ".CREATOR_TAG";
    public static final String HARDCODED_VALUE_ATTR = TreeCopier.class.getName() + ".HARDCODED_VALUE";

    private FacesContext _facesContext;
    private boolean _overwriteComponents = false;
    private boolean _overwriteAttributes = false;

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


    public void copyTree(Tree fromTree, Tree toTree)
    {
        copyComponent(fromTree.getRoot(), toTree.getRoot(), false);
    }

    public void copyStaticTree(Tree fromTree, Tree toTree)
    {
        copyComponent(fromTree.getRoot(), toTree.getRoot(), true);
    }


    public void copySubTree(UIComponent fromComponent, UIComponent toComponent)
    {
        copyComponent(fromComponent, toComponent, false);
    }

    public void copyStaticSubTree(UIComponent fromComponent, UIComponent toComponent)
    {
        copyComponent(fromComponent, toComponent, true);
    }


    protected void copyComponent(UIComponent fromComp,
                                 UIComponent toComp,
                                 boolean staticAttributeConversions)
    {
        copyAttributes(fromComp, toComp, staticAttributeConversions);

        int childIndex = 0;
        for (Iterator it = fromComp.getChildren(); it.hasNext(); childIndex++)
        {
            UIComponent child = (UIComponent)it.next();
            UIComponent clone;

            try
            {
                //destination component already exists?
                //TODO: Optimize by Set "destinationChildren"
                clone = toComp.findComponent(child.getComponentId());
            }
            catch (IllegalArgumentException e)
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
                MyFacesTagExtension tag
                    = (MyFacesTagExtension)child.getAttribute(CREATOR_TAG_ATTR);
                if (tag != null)
                {
                    clone = tag.createComponent();
                }
                else
                {
                    LogUtil.getLogger().severe("Component " + child.getCompoundId() + " has no '" + CREATOR_TAG_ATTR + "' attribute!");
                    try
                    {
                        clone = (UIComponent)child.getClass().newInstance();
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

                clone.setComponentId(child.getComponentId());
                toComp.addChild(childIndex, clone);

                copyComponent(child, clone, staticAttributeConversions);    //Recursion
            }
        }
    }


    protected void copyAttributes(UIComponent fromComp,
                                  UIComponent toComp,
                                  boolean staticAttributeConversions)
    {
        for (Iterator it = fromComp.getAttributeNames(); it.hasNext();)
        {
            String attrName = (String)it.next();
            if (_overwriteAttributes || toComp.getAttribute(attrName) == null)
            {
                Object attrValue = fromComp.getAttribute(attrName);
                if (staticAttributeConversions)
                {
                    if (attrName.equals(HARDCODED_VALUE_ATTR))
                    {
                        UIComponentUtils.convertAndSetValue(_facesContext,
                                                            toComp,
                                                            (String)attrValue,
                                                            false); //no converter error message
                    }
                    //else if (attrValue instanceof String)
                    else if (!isIgnoreAttribute(attrName))
                    {
                        Tag tag = (Tag)fromComp.getAttribute(CREATOR_TAG_ATTR);
                        UIComponentUtils.convertAndSetAttribute(_facesContext,
                                                                toComp,
                                                                attrName,
                                                                (String)attrValue,
                                                                false, //do not deserialize
                                                                tag);
                    }
                    /*
                    else
                    {
                    toComp.setAttribute(attrName, attrValue);
                    }
                    */
                }
                else
                {
                    toComp.setAttribute(attrName, attrValue);
                }
            }
        }
    }



    protected boolean isIgnoreAttribute(String attrName)
    {
        if (attrName.startsWith("net.sourceforge.myfaces."))
        {
            return true;
        }
        else if (attrName.startsWith("javax."))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
