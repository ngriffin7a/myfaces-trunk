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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
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
        _facesContext = null;
        _pageContext = null;
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
            /*
            //FacesServlet saves the FacesContext as request attribute:
            _facesContext = (FacesContext)_pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                                                    PageContext.REQUEST_SCOPE);
            */
            _facesContext = FacesContext.getCurrentInstance();
            if (_facesContext == null)
            {
                throw new IllegalStateException("No faces context!?");
            }
        }
        return _facesContext;
    }


    //property helpers

    protected void setComponentPropertyString(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue.toString(), true));
    }

    protected void setComponentPropertyBoolean(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }

        if (attrValue instanceof Boolean)
        {
            _attributes.add(new Attribute(attrName,
                                          attrValue,
                                          true));
        }
        else
        {
            _attributes.add(new Attribute(attrName,
                                          Boolean.valueOf(attrValue.toString()),
                                          true));
        }
    }

    protected void setRendererAttributeString(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue.toString(), false));
    }

    protected void setRendererAttributeBoolean(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }

        if (attrValue instanceof Boolean)
        {
            _attributes.add(new Attribute(attrName,
                                          attrValue,
                                          false));
        }
        else
        {
            _attributes.add(new Attribute(attrName,
                                          Boolean.valueOf(attrValue.toString()),
                                          false));
        }
    }

    protected void setRendererAttributeInteger(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }

        if (attrValue instanceof Integer)
        {
            _attributes.add(new Attribute(attrName,
                                          attrValue,
                                          false));
        }
        else if (attrValue instanceof Number)
        {
            _attributes.add(new Attribute(attrName,
                                          new Integer(((Number)attrValue).intValue()),
                                          false));
        }
        else
        {
            _attributes.add(new Attribute(attrName,
                                          new Integer(attrValue.toString()),
                                          false));
        }
    }


    protected static class Attribute
    {
        public String name;
        public Object value;
        public boolean isComponentProperty;

        public Attribute(String name, Object value, boolean isComponentProperty)
        {
            this.name = name;
            this.value = value;
            this.isComponentProperty = isComponentProperty;
        }
    }


    /**
     * @param uiComponent
     */
    protected void overrideProperties(UIComponent uiComponent)
    {
        if (_attributes != null)
        {
            if (_tag.getCreated())
            {
                //component was just created, so no need to check for old values
                //before setting the attributes
                for (Iterator it = _attributes.iterator(); it.hasNext();)
                {
                    Attribute attr = (Attribute)it.next();
                    if (attr.isComponentProperty)
                    {
                        setComponentProperty(uiComponent, attr.name, attr.value);
                    }
                    else
                    {
                        uiComponent.setAttribute(attr.name, attr.value);
                    }
                }
            }
            else
            {
                //component is NOT new, so we must only set (override) values
                //that are null
                for (Iterator it = _attributes.iterator(); it.hasNext();)
                {
                    Attribute attr = (Attribute)it.next();
                    if (attr.isComponentProperty)
                    {
                        if (attr.name.equals(CommonComponentAttributes.VALUE_ATTR))
                        {
                            overrideComponentValue(uiComponent,
                                                   attr.value);
                        }
                        else
                        {
                            overrideComponentProperty(uiComponent,
                                                      attr.name,
                                                      attr.value);
                        }
                    }
                    else
                    {
                        //override attribute
                        if (uiComponent.getAttribute(attr.name) == null)
                        {
                            uiComponent.setAttribute(attr.name, attr.value);
                        }
                    }
                }
            }
        }
    }


    private void setComponentProperty(UIComponent uiComponent,
                                      String propertyName,
                                      Object propertyValue)
    {
        boolean errorOccured = false;

        //Try bean property setter first
        PropertyDescriptor pd = BeanUtils.findPropertyDescriptor(uiComponent,
                                                                 propertyName);
        if (pd != null &&
            pd.getWriteMethod() != null)
        {
            try
            {
                BeanUtils.setBeanPropertyValue(uiComponent, pd, propertyValue);
            }
            catch (Exception e)
            {
                LogUtil.getLogger().warning("Exception in property setter of component " + UIComponentUtils.toString(uiComponent) + ": " + e.getMessage() + ". Attribute will be set directly.");
                errorOccured = true;
            }
        }
        else
        {
            //Component does not have a matching bean property!
            LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(uiComponent) + " does not have a valid property setter method for property '" + propertyName + "'.");
            errorOccured = true;
        }

        if (errorOccured)
        {
            //Alternativly set by attribute name:
            uiComponent.setAttribute(propertyName, propertyValue);
        }
    }



    private void overrideComponentProperty(UIComponent uiComponent,
                                           String propertyName,
                                           Object propertyValue)
    {
        boolean errorOccured = false;

        //Try bean property setter first
        PropertyDescriptor pd = BeanUtils.findPropertyDescriptor(uiComponent,
                                                                 propertyName);
        if (pd != null &&
            pd.getReadMethod() != null &&
            pd.getWriteMethod() != null)
        {
            try
            {
                if (BeanUtils.getBeanPropertyValue(uiComponent, pd) == null)
                {
                    BeanUtils.setBeanPropertyValue(uiComponent, pd, propertyValue);
                }
            }
            catch (Exception e)
            {
                LogUtil.getLogger().warning("Exception in property getter or setter of component " + UIComponentUtils.toString(uiComponent) + ": " + e.getMessage() + ". Attribute will be set directly.");
                errorOccured = true;
            }
        }
        else
        {
            //Component does not have a matching bean property!
            LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(uiComponent) + " does not have valid property setter and getter methods for property '" + propertyName + "'.");
            errorOccured = true;
        }

        if (errorOccured)
        {
            //Alternativly set by attribute name:
            if (uiComponent.getAttribute(propertyName) == null)
            {
                uiComponent.setAttribute(propertyName, propertyValue);
            }
        }
    }


    private void overrideComponentValue(UIComponent uiComponent,
                                        Object propertyValue)
    {
        //We must deal with value attribute special, because the value is always
        //set to null after updating the model. So we must do the null check for
        //the currentValue instead!

        Object currentValue;
        try
        {
            currentValue = uiComponent.currentValue(getFacesContext());
        }
        catch (Exception e)
        {
            //Exception occured, perhaps model bean does not yet exist?
            LogUtil.getLogger().warning("Exception occured getting currentValue of component " + UIComponentUtils.toString(uiComponent) + ": " + e.getMessage());
            currentValue = null;
        }

        if (currentValue == null)
        {
            uiComponent.setValue(propertyValue);
        }
    }



    /**
     * @return component, that was found or created. null, if not handled
     */
    protected UIComponent findComponent()
    {
        int mode = MyFacesConfig.getStateSavingMode(getFacesContext().getServletContext());
        if (mode != MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED &&
            mode != MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED)
        {
            //no "client minimized" mode, standard way of finding and creating components is ok
            return null;
        }

        if (_tag.getId() != null)
        {
            //hardcoded id, nothing special must be done --> default method of jsf-api works ok
            return null;
        }

        //We must locate the component, that corresponds to the current tag
        //in the parsed tree.
        //First we create a temporary component...
        UIComponent tempComp = _tag.createComponent();
        //...remember that we have created it (for overrideProperties)...
        ((MyFacesTagBaseIF)_tag).setCreated(true);
        //...and set the hardcoded attributes...
        ((MyFacesTagBaseIF)_tag).overrideProperties(tempComp);

        //Find an corresponding component in parsed tree and add the
        //new component if it does not yet exist in the current tree
        return findoutComponentIdAndAdd(getFacesContext(),
                                        _tag,
                                        tempComp);
    }


    protected static UIComponent findoutComponentIdAndAdd(FacesContext facesContext,
                                                          FacesTag facesTag,
                                                          UIComponent newComponent)
    {
        LogUtil.getLogger().entering(Level.FINEST);
        LogUtil.printComponentToConsole(newComponent, "compToFind");

        //determine current parent
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
                throw new IllegalStateException("Component with clientId '" + parentClientId + "' not found in parsed tree.");
            }
        }

        //Find corresponding child in parsed tree
        UIComponent parsedChild = findParsedChild(facesContext,
                                                  facesTag,
                                                  parsedParent,
                                                  parentClientId,
                                                  newComponent);
        if (parsedChild == null)
        {
            throw new IllegalStateException("FacesTag " + facesTag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
        }

        //Parsed component found, get id
        String id = parsedChild.getComponentId();
        facesTag.setId(id);

        //Component already in tree?
        UIComponent findComp = parent.findComponent(id);
        if (findComp != null)
        {
            ((MyFacesTagBaseIF)facesTag).setCreated(false);
            return findComp;
        }
        else
        {
            //add component to tree
            newComponent.setComponentId(id);
            parent.addChild(newComponent);
            ((MyFacesTagBaseIF)facesTag).setCreated(true);
            return newComponent;
        }
    }


    private static UIComponent findParsedChild(FacesContext facesContext,
                                               FacesTag facesTag,
                                               UIComponent parsedParent,
                                               String parentClientId,
                                               UIComponent newComponent)
    {
        UIComponent foundParsedComp = null;
        int foundIdx = 0;

        int lastChildIdx = getLastChildIndexForParent(facesContext,
                                                      parentClientId);
        int startSearchAt = lastChildIdx + 1;
        int childAndFacetCount = UIComponentUtils.getFacetAndChildCount(parsedParent);

        //search from component next to last found child
        for (int i = startSearchAt; i < childAndFacetCount; i++)
        {
            UIComponent child = UIComponentUtils.getFacetOrChild(parsedParent, i);
            if (equalsParsedChild(facesTag, child, newComponent))
            {
                //found corresponding parsed component
                foundParsedComp = child;
                foundIdx = i;
                break;
            }
        }

        if (foundParsedComp == null && startSearchAt > 0)
        {
            //search from first child in case this tag is within an iteration tag
            for (int i = 0; i < startSearchAt; i++)
            {
                UIComponent child = UIComponentUtils.getFacetOrChild(parsedParent, i);
                if (equalsParsedChild(facesTag, child, newComponent))
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
            if (foundIdx + 1 < childAndFacetCount)
            {
                UIComponent child = UIComponentUtils.getFacetOrChild(parsedParent,
                                                                     foundIdx + 1);
                if (equalsParsedChild(facesTag, child, newComponent))
                {
                    UIComponent found = UIComponentUtils.getFacetOrChild(parsedParent,
                                                                         foundIdx);
                    Object[] jspPos = (Object[])found.getAttribute(JspInfo.JSP_POSITION_ATTR);
                    String pos = "";
                    if (jspPos != null)
                    {
                        pos = " in file '" + jspPos[0] + "' (line " + jspPos[1] + " - " + jspPos[2] + ")";
                    }
                    LogUtil.getLogger().warning("Component " + UIComponentUtils.toString(newComponent) + pos + " is ambigous. This component must have an id when it is rendered conditional (i.e. within if-Block in JSP, or within conditional JSTL-tag).");
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
                String componentId = compToCompare.getComponentId();
                if (componentId == null || !componentId.equals(hardcodedId))
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
