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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.tree.Tree;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.Serializable;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesTagHelper
    implements Serializable
{
    private FacesTag _tag;
    private Set _attributes = null;
    protected PageContext _pageContext;
    protected FacesContext _facesContext;

    MyFacesTagHelper(FacesTag tag)
    {
        _tag = tag;
    }

    public void release()
    {
        _attributes = null;
    }


    //JSF Spec.


    //subclass helpers
    public void setPageContext(PageContext pageContext)
    {
        _pageContext = pageContext;
    }

    protected PageContext getPageContext()
    {
        return _pageContext;
    }

    protected FacesContext getFacesContext()
    {
        if (_facesContext == null)
        {
            //FacesServlet saves the FacesContext as request attribute:
            _facesContext = (FacesContext)_pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                                                    PageContext.REQUEST_SCOPE);
            if (_facesContext == null)
            {
                throw new IllegalStateException("No faces context!?");
            }
        }
        return _facesContext;
    }


    //property helpers

    protected void setComponentAttribute(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, true));
    }

    protected void setComponentAttribute(String attrName, boolean attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName,
                                      attrValue ? Boolean.TRUE : Boolean.FALSE,
                                      true));
    }

    protected void setRendererAttribute(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, false));
    }

    protected void setRendererAttribute(String attrName, boolean attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName,
                                      attrValue ? Boolean.TRUE : Boolean.FALSE,
                                      false));
    }



    protected static class Attribute
    {
        public String name;
        public Object value;
        public boolean isComponentAttribute;

        public Attribute(String name, Object value, boolean isComponentAttribute)
        {
            this.name = name;
            this.value = value;
            this.isComponentAttribute = isComponentAttribute;
        }
    }


    /**
     * TODO: Rethink this logic. Better do a findOutAttributeType and
     *       convert to proper type regardless of value or common attribute.
     *
     * @param uiComponent
     */
    protected void overrideProperties(UIComponent uiComponent)
    {
        FacesContext facesContext = null;
        if (getPageContext() != null)
        {
            facesContext = (FacesContext)getPageContext()
                                    .getAttribute("javax.faces.context.FacesContext",
                                                  PageContext.REQUEST_SCOPE);
        }

        if (_attributes != null)
        {
            for (Iterator it = _attributes.iterator(); it.hasNext();)
            {
                Attribute attr = (Attribute)it.next();
                if (facesContext != null
                    && attr.isComponentAttribute
                    && attr.name.equals(CommonComponentAttributes.VALUE_ATTR))
                {
                    if (uiComponent.currentValue(facesContext) == null)
                    {
                        Object rtValue = attr.value;
                        if (rtValue instanceof String)
                        {
                            Converter conv = ConverterUtils.findValueConverter(facesContext,
                                                                          uiComponent);
                            if (conv != null)
                            {
                                UIComponentUtils.convertAndSetValue(facesContext,
                                                                    uiComponent,
                                                                    (String)rtValue,
                                                                    conv,
                                                                    false);    //No error message
                            }
                            else
                            {
                                UIComponentUtils.setComponentValue(uiComponent, rtValue);
                            }
                        }
                        else
                        {
                            UIComponentUtils.setComponentValue(uiComponent, rtValue);
                        }
                    }
                }
                else if (attr.isComponentAttribute)
                {
                    //Try bean property setter first
                    try
                    {
                        if (BeanUtils.getBeanPropertyValue(uiComponent, attr.name) == null)
                        {
                            BeanUtils.setBeanPropertyValue(uiComponent, attr.name, attr.value);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        //Component does not have this property!
                        LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(uiComponent) + " does not have valid property setter and getter methods for property '" + attr.name + "'.");
                        //Alternativly set by attribute name:
                        if (uiComponent.getAttribute(attr.name) == null)
                        {
                            uiComponent.setAttribute(attr.name, attr.value);
                        }
                    }
                }
                else
                {
                    if (uiComponent.getAttribute(attr.name) == null)
                    {
                        uiComponent.setAttribute(attr.name, attr.value);
                    }
                }
            }
        }
    }




    protected String getIdFromParsedTree(UIComponent component)
    {
        UIComponent parsedComp = findComponentInParsedTree(getFacesContext(),
                                                           _tag,
                                                           component);
        if (parsedComp != null)
        {
            return parsedComp.getComponentId();
        }
        else
        {
            LogUtil.getLogger().warning("FacesTag " + _tag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
            return null;
        }
    }

    private static final String PARSED_COMPONENT_ATTR
        = MyFacesTagHelper.class.getName() + ".PARSED_COMPONENT";

    private static final String LAST_PARSED_CHILD_INDEX_MAP
        = MyFacesTagHelper.class.getName() + ".LAST_PARSED_CHILD_INDEX_MAP";

    protected static UIComponent findComponentInParsedTree(FacesContext facesContext,
                                                           FacesTag facesTag,
                                                           UIComponent compToFind)
    {
        System.out.println("findComponentInParsedTree " + UIComponentUtils.toString(compToFind));

        //determine parent
        Tag parentTag = facesTag.getParent();
        while (parentTag != null &&
               (!(parentTag instanceof FacesTag) ||
                (((FacesTag)parentTag).getComponent() == null)))
        {
            parentTag = parentTag.getParent();
        }
        FacesTag parentFacesTag = (FacesTag)parentTag;
        UIComponent parent;
        UIComponent parsedParent;
        if (parentFacesTag == null)
        {
            parent = facesContext.getTree().getRoot();
            Tree parsedTree = JspInfo.getTree(facesContext,
                                              facesContext.getTree().getTreeId());
            parsedParent = parsedTree.getRoot();
        }
        else
        {
            parent = parentFacesTag.getComponent();
            //only parent tags that have a component are searched in loop above
            parsedParent = (UIComponent)parent.getAttribute(PARSED_COMPONENT_ATTR);
            if (parsedParent == null)
            {
                //parent was not created by a MyFacesTag
                //no parent or grand-parent or ... must have a null id:
                UIComponent check = parent;
                while (check != null)
                {
                    if (check.getComponentId() == null)
                    {
                        throw new IllegalStateException("Component " + UIComponentUtils.toString(check) + " was not created by a subclass of MyFacesTag and must therefore have a fixed component id!");
                    }
                    check = check.getParent();
                }
                Tree parsedTree = JspInfo.getTree(facesContext,
                                                  facesContext.getTree().getTreeId());
                parsedParent = parsedTree.getRoot().findComponent(parent.getClientId(facesContext));
                if (parsedParent == null)
                {
                    throw new IllegalStateException("Corresponding parsed parent for parent " + parent + " not found!");
                }
            }
        }

        int startSearchAt;
        String parentId = parsedParent.getComponentId();
        Map lastParsedChildMap = getLastParsedChildIndexMap(facesContext);
        Integer indexObj = (Integer)lastParsedChildMap.get(parentId);
        if (indexObj == null)
        {
            if (parsedParent.getChildCount() == 0)
            {
                throw new IllegalStateException("Corresponding parsed parent has no children?!");
            }
            startSearchAt = 0;
        }
        else
        {
            startSearchAt = indexObj.intValue() + 1;
        }

        UIComponent foundParsedComp = null;
        int foundIdx = 0;
        int len = parsedParent.getChildCount();
        for (int i = startSearchAt; i < len; i++)
        {
            UIComponent child = parsedParent.getChild(i);
            if (equalsParsedChild(facesTag, child, compToFind))
            {
                //found corresponding parsed component
                foundParsedComp = child;
                foundIdx = i;
                break;
            }
        }

        if (foundParsedComp == null && startSearchAt > 0)
        {
            for (int i = 0; i < startSearchAt; i++)
            {
                UIComponent child = parsedParent.getChild(i);
                if (equalsParsedChild(facesTag, child, compToFind))
                {
                    //found corresponding parsed component
                    foundParsedComp = child;
                    foundIdx = i;
                    break;
                }
            }
        }

        if (foundParsedComp != null)
        {
            //TODO: Check if there would also be another ambigous component
            compToFind.setAttribute(PARSED_COMPONENT_ATTR, foundParsedComp);
            lastParsedChildMap.put(parentId, new Integer(foundIdx));

            //find in current tree
            String componentId = foundParsedComp.getComponentId();
            if (componentId == null)
            {
                throw new IllegalStateException();
            }
            if (parent.findComponent(componentId) == null)
            {
                //add to parent
                compToFind.setComponentId(componentId);
                parent.addChild(compToFind);
            }
        }
        else
        {
            System.out.println("not found");
        }

        return foundParsedComp;
    }


    private static Map getLastParsedChildIndexMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest().getAttribute(LAST_PARSED_CHILD_INDEX_MAP);
        if (map == null)
        {
            map = new HashMap();
            facesContext.getServletRequest().setAttribute(LAST_PARSED_CHILD_INDEX_MAP,
                                                          map);
        }
        return map;
    }


    protected static boolean equalsParsedChild(FacesTag facesTag,
                                               UIComponent parsedChild,
                                               UIComponent compToCompare)
    {
        System.out.print("   equalsParsedChild ");
        TreeUtils.printComponent(parsedChild, System.out);

        Tag creatorTag = (Tag)parsedChild.getAttribute(JspInfo.CREATOR_TAG_ATTR);
        if (!(creatorTag.getClass().getName().equals(facesTag.getClass().getName())))
        {
            //different tag class --> certainly not the component to be found
            return false;
        }

        /*
        Won't function properly because overrideProperties leaves later changed attributes alone!
        for (Iterator it = parsedChild.getAttributeNames(); it.hasNext();)
        {
            String attrName = (String)it.next();
            if (attrName.equals("componentId") ||
                attrName.equals("clientId") ||
                attrName.equals("parent") ||
                UIComponentUtils.isInternalAttribute(attrName))
            {
                // - component to find has no componentId yet
                // - parent must not be compared
                // - internal attributes must not be compared
                continue;
            }
            Object value = parsedChild.getAttribute(attrName);
            if (!value.equals(compToCompare.getAttribute(attrName)))
            {
                System.out.println("      diff: " + attrName + " / " + compToCompare.getAttribute(attrName) + " <> " + value);
                return false;
            }
        }
        */
        return true;
    }


}
