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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;

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
        //FacesContext facesContext = FacesContext.getCurrentInstance();

        if (_attributes != null)
        {
            for (Iterator it = _attributes.iterator(); it.hasNext();)
            {
                Attribute attr = (Attribute)it.next();
                /*
                if (facesContext != null
                    && attr.isComponentAttribute
                    && attr.name.equals(CommonComponentAttributes.VALUE_ATTR))
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
                else */
                if (attr.isComponentAttribute)
                {
                    //Try bean property setter first
                    PropertyDescriptor pd = BeanUtils.findPropertyDescriptor(uiComponent,
                                                                             attr.name);
                    if (pd != null &&
                        pd.getReadMethod() != null &&
                        pd.getWriteMethod() != null)
                    {
                        try
                        {
                            if (BeanUtils.getBeanPropertyValue(uiComponent, pd) == null)
                            {
                                BeanUtils.setBeanPropertyValue(uiComponent, pd, attr.value);
                            }
                        }
                        catch (Exception e)
                        {
                            LogUtil.getLogger().warning("Exception in property getter or setter of component " + UIComponentUtils.toString(uiComponent) + ": " + e.getMessage() + ". Attribute will be set directly.");
                            //Alternativly set by attribute name:
                            if (uiComponent.getAttribute(attr.name) == null)
                            {
                                uiComponent.setAttribute(attr.name, attr.value);
                            }
                        }
                    }
                    else
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
            //TODO: Optimize: check if component exists and add it to current parent if not
        }
        else
        {
            throw new IllegalStateException("FacesTag " + _tag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
            /*
            LogUtil.getLogger().severe("FacesTag " + _tag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
            return null;
            */
        }
    }


    protected static UIComponent findComponentInParsedTree(FacesContext facesContext,
                                                           FacesTag facesTag,
                                                           UIComponent compToFind)
    {
        LogUtil.getLogger().entering(Level.FINEST);
        LogUtil.printComponentToConsole(compToFind, "compToFind");

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
        String parentClientId;
        UIComponent parsedParent;
        if (parentFacesTag == null)
        {
            parent = facesContext.getTree().getRoot();
            parentClientId = "";
            Tree parsedTree = JspInfo.getTree(facesContext,
                                              facesContext.getTree().getTreeId());
            parsedParent = parsedTree.getRoot();
        }
        else
        {
            //only parent tags that have a component are searched in loop above
            parent = parentFacesTag.getComponent();
            parentClientId = parent.getClientId(facesContext);
            Map componentMap = JspInfo.getComponentMap(facesContext,
                                                       facesContext.getTree().getTreeId());
            parsedParent = (UIComponent)componentMap.get(parentClientId);
            if (parsedParent == null)
            {
                throw new IllegalStateException("Component with clientId " + parentClientId + " not found in parsed tree.");
                /*
                Tree parsedTree = JspInfo.getTree(facesContext,
                                                  facesContext.getTree().getTreeId());
                parsedParent = parsedTree.getRoot().findComponent(parentClientId);
                if (parsedParent == null)
                {
                    throw new IllegalStateException("Corresponding parsed parent for parent " + parent + " not found!");
                }
                */
            }
        }

        return findParsedChild(facesContext,
                               facesTag,
                               parsedParent,
                               parentClientId,
                               compToFind);
    }


    private static UIComponent findParsedChild(FacesContext facesContext,
                                               FacesTag facesTag,
                                               UIComponent parsedParent,
                                               String parentClientId,
                                               UIComponent compToFind)
    {
        UIComponent foundParsedComp = null;
        int foundIdx = 0;

        int lastChildIdx = getLastChildIndexForParent(facesContext,
                                                      parentClientId);
        int startSearchAt = lastChildIdx + 1;

        int childCount = parsedParent.getChildCount();
        for (int i = startSearchAt; i < childCount; i++)
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
            //Check if next component would also match --> ambigous component
            if (foundIdx + 1 < childCount)
            {
                UIComponent child = parsedParent.getChild(foundIdx + 1);
                if (equalsParsedChild(facesTag, child, compToFind))
                {
                    LogUtil.getLogger().warning("Component " + UIComponentUtils.toString(compToFind) + " is ambigous. This component must have an id when it is rendered conditional (i.e. within if-Block in JSP, or within conditional JSTL-tag).");
                }
            }

            rememberChildIndexForParent(facesContext, parentClientId, foundIdx);
        }
        else
        {
            LogUtil.getLogger().exiting("Not found!", Level.FINEST);
        }

        return foundParsedComp;
    }



    private static final String LAST_PARSED_CHILD_INDEX_MAP
        = MyFacesTagHelper.class.getName() + ".LAST_PARSED_CHILD_INDEX_MAP";
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

    private static void rememberChildIndexForParent(FacesContext facesContext,
                                                    String parsedParentClientId,
                                                    int childIdx)
    {
        getLastParsedChildIndexMap(facesContext).put(parsedParentClientId,
                                                     new Integer(childIdx));
    }

    private static int getLastChildIndexForParent(FacesContext facesContext,
                                                  String parsedParentClientId)
    {
        Integer idxObj = (Integer)getLastParsedChildIndexMap(facesContext)
                                .get(parsedParentClientId);
        if (idxObj == null)
        {
            return -1;
        }
        else
        {
            return idxObj.intValue();
        }
    }




    protected static boolean equalsParsedChild(FacesTag facesTag,
                                               UIComponent parsedChild,
                                               UIComponent compToCompare)
    {
        LogUtil.getLogger().entering(Level.FINEST);
        LogUtil.printComponentToConsole(parsedChild, "parsedChild");

        Tag creatorTag = (Tag)parsedChild.getAttribute(JspInfo.CREATOR_TAG_ATTR);
        if (!(creatorTag.getClass().getName().equals(facesTag.getClass().getName())))
        {
            //different tag class --> certainly not the component to be found
            return false;
        }

        //check all hardcoded attributes for equality
        if (!isEqualAttributes(parsedChild.getAttributeNames(),
                               parsedChild,
                               compToCompare))
        {
            return false;
        }

        return true;
    }


    private static boolean isEqualAttributes(Iterator attributes,
                                             UIComponent parsedChild,
                                             UIComponent compToCompare)
    {
        while (attributes.hasNext())
        {
            String attrName = (String)attributes.next();
            if (attrName.equals(JspInfo.HARDCODED_ID_ATTR))
            {
                String hardcodedId = (String)parsedChild.getAttribute(attrName);
                if (!compToCompare.getComponentId().equals(hardcodedId))
                {
                    LogUtil.getLogger().finest("      diff: hardcoded id / " + compToCompare.getComponentId() + " <> " + hardcodedId);
                    return false;
                }
            }
            else
            {
                if (attrName.equals("componentId") ||
                    attrName.equals("clientId") ||
                    attrName.equals("parent") ||
                    UIComponentUtils.isInternalAttribute(attrName))
                {
                    // - component to find has no componentId or clientId yet
                    // - parent must not be compared
                    // - internal attributes must not be compared
                }
                else
                {
                    Object parsedValue = parsedChild.getAttribute(attrName);
                    Object actualValue = compToCompare.getAttribute(attrName);
                    if (parsedValue == null)
                    {
                        if (actualValue != null)
                        {
                            LogUtil.getLogger().finest("      diff: " + attrName + " / " + actualValue + " <> " + parsedValue);
                            return false;
                        }
                    }
                    else if (actualValue == null ||
                             !parsedValue.equals(actualValue))
                    {
                        LogUtil.getLogger().finest("      diff: " + attrName + " / " + actualValue + " <> " + parsedValue);
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
