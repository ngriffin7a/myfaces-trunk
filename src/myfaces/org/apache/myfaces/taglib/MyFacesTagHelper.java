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
import net.sourceforge.myfaces.component.UIRoot;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.logging.Level;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesTagHelper
{
    private MyFacesTagBaseIF _tag;
    private Set _attributes = null;
    protected PageContext _pageContext;
    protected FacesContext _facesContext;

    private boolean _parentComponentOk = false;
    private UIComponent _parentComponent;

    private UIComponent _newComponent;

    private Boolean _suppressed;



    MyFacesTagHelper(MyFacesTagBaseIF tag)
    {
        _tag = tag;
    }

    public void release()
    {
        _attributes = null;
        _facesContext = null;
        _pageContext = null;
        _parentComponentOk = false;
        _parentComponent = null;
        _newComponent = null;
        _suppressed = null;
    }


    public boolean isComponentVisible()
    {
        return HTMLRenderer.isComponentVisible(getFacesContext(),
                                               _tag.getComponent());
    }


    public boolean isSuppressed()
    {
        if (_suppressed == null)
        {
            UIComponent parent = getParentComponent();
            while (parent != null)
            {
                if (parent.getRendersChildren())
                {
                    _suppressed = Boolean.TRUE;
                    return true;
                }
                parent = parent.getParent();
            }

            _suppressed = Boolean.valueOf(!isComponentVisible());
        }
        return _suppressed.booleanValue();
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
            _facesContext = FacesContext.getCurrentInstance();
            if (_facesContext == null)
            {
                throw new IllegalStateException("No faces context!?");
            }
        }
        return _facesContext;
    }


    //property helpers

    protected void setComponentPropertyObject(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, true));
    }

    protected void setComponentPropertyString(String attrName, Object attrValue)
    {
        setComponentPropertyObject(attrName, attrValue.toString());
    }

    protected void setComponentPropertyBoolean(String attrName, Object attrValue)
    {
        if (attrValue instanceof Boolean)
        {
            setComponentPropertyObject(attrName, attrValue);
        }
        else
        {
            setComponentPropertyObject(attrName,
                                       Boolean.valueOf(attrValue.toString()));
        }
    }


    protected void setRendererAttributeObject(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, false));
    }

    protected void setRendererAttributeString(String attrName, Object attrValue)
    {
        setRendererAttributeObject(attrName, attrValue.toString());
    }

    protected void setRendererAttributeBoolean(String attrName, Object attrValue)
    {
        if (attrValue instanceof Boolean)
        {
            setRendererAttributeObject(attrName, attrValue);
        }
        else
        {
            setRendererAttributeObject(attrName,
                                       Boolean.valueOf(attrValue.toString()));
        }
    }

    protected void setRendererAttributeInteger(String attrName, Object attrValue)
    {
        if (attrValue instanceof Integer)
        {
            setRendererAttributeObject(attrName, attrValue);
        }
        else if (attrValue instanceof Number)
        {
            setRendererAttributeObject(attrName,
                                       new Integer(((Number)attrValue).intValue()));
        }
        else
        {
            setRendererAttributeObject(attrName,
                                       new Integer(attrValue.toString()));
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
                        if (attr.name.equals(net.sourceforge.myfaces.component.MyFacesUIOutput.VALUE_PROP) &&
                            uiComponent instanceof UIOutput)
                        {
                            overrideComponentValue((UIOutput)uiComponent,
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
        //boolean errorOccured = false;

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
                //errorOccured = true;
            }
        }
        else
        {
            //Component does not have a matching bean property!
            LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(uiComponent) + " does not have a valid property setter method for property '" + propertyName + "'.");
            //errorOccured = true;
        }

        /*
        if (errorOccured)
        {
            //Alternativly set by attribute name:
            uiComponent.setAttribute(propertyName, propertyValue);
        }
        */
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


    private void overrideComponentValue(UIOutput uiComponent,
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
        /*
        int mode = MyFacesConfig.getStateSavingMode((ServletContext)getFacesContext().getExternalContext().getContext());
        if (mode != MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED &&
            mode != MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED)
        {
            //no "client minimized" mode, standard way of finding and creating components is ok
            return null;
        }
        */

        String id = _tag.getId();
        if (id == null)
        {
            id = findoutComponentId();
        }

        //Component already in tree?
        UIComponent parentComp = getParentComponent();
        UIComponent findComp;
        if (parentComp == null)
        {
            //Root
            findComp = getFacesContext().getTree().getRoot();
        }
        else
        {
            findComp = parentComp.findComponent(id);
        }

        if (findComp != null)
        {
            _tag.setCreated(false);
            return findComp;
        }
        else
        {
            //add component to tree
            UIComponent newComponent = getNewComponent();
            newComponent.setComponentId(id);
            String facetName = _tag.getFacetName();
            if (facetName == null)
            {
                parentComp.addChild(newComponent);
            }
            else
            {
                UIComponentUtils.addFacet(parentComp, facetName, newComponent);
            }
            return newComponent;
        }
    }


    protected String findoutComponentId()
    {
        LogUtil.getLogger().entering(Level.FINEST);

        FacesContext facesContext = getFacesContext();

        UIComponent parentComp = getParentComponent();
        if (parentComp == null)
        {
            return UIRoot.ROOT_COMPONENT_ID;
        }

        UIComponent parsedParent;
        String parentClientId;
        if (parentComp.getParent() == null)
        {
            parentClientId = "";    //Root has no clientId
            Tree parsedTree = JspInfo.getTree(facesContext,
                                              facesContext.getTree().getTreeId());
            parsedParent = parsedTree.getRoot();
        }
        else
        {
            parentClientId = parentComp.getClientId(facesContext);
            Map componentMap = JspInfo.getComponentMap(facesContext,
                                                       facesContext.getTree().getTreeId());
            parsedParent = (UIComponent)componentMap.get(parentClientId);
            if (parsedParent == null)
            {
                throw new IllegalStateException("Component with clientId '" + parentClientId + "' not found in parsed tree.");
            }
        }

        //We create a temporary component...
        UIComponent newComponent = getNewComponent();

        //Find corresponding child in parsed tree
        UIComponent parsedChild = findParsedChild(parsedParent,
                                                  parentClientId,
                                                  newComponent);
        if (parsedChild == null)
        {
            throw new IllegalStateException("FacesTag " + _tag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
        }

        //Parsed component found, get id
        String id = parsedChild.getComponentId();
        _tag.setId(id);

        LogUtil.getLogger().exiting(Level.FINEST);
        return id;
    }


    protected UIComponent getParentComponent()
    {
        if (!_parentComponentOk)
        {
            //determine current parent
            Tag parentTag = _tag.getParent();
            while (parentTag != null &&
                   (!(parentTag instanceof UIComponentTag) ||
                    (((UIComponentTag)parentTag).getComponent() == null)))
            {
                parentTag = parentTag.getParent();
            }
            if (parentTag == null)
            {
                _parentComponent = null;
            }
            else
            {
                //only parent tags that have a component are searched in loop above
                _parentComponent = ((UIComponentTag)parentTag).getComponent();
            }

            _parentComponentOk = true;
        }
        return _parentComponent;
    }


    private UIComponent getNewComponent()
    {
        if (_newComponent == null)
        {
            //First we create a temporary component...
            String componentType = UIComponentTagHacks.getComponentType(_tag);
            ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            _newComponent = af.getApplication().getComponent(componentType);
            //...remember that we have created it (for overrideProperties)...
            _tag.setCreated(true);
            //...and set the hardcoded attributes...
            _tag.overrideProperties(_newComponent);
        }
        return _newComponent;
    }


    private UIComponent findParsedChild(UIComponent parsedParent,
                                        String parentClientId,
                                        UIComponent newComponent)
    {
        FacesContext facesContext = getFacesContext();

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
            if (equalsParsedChild(child, newComponent))
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
                if (equalsParsedChild(child, newComponent))
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
                if (equalsParsedChild(child, newComponent))
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
        Map map = (Map)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(LAST_PARSED_CHILD_INDEX_MAP);
        if (map == null)
        {
            map = new HashMap();
            ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(LAST_PARSED_CHILD_INDEX_MAP,
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




    protected boolean equalsParsedChild(UIComponent parsedChild,
                                        UIComponent compToCompare)
    {
        LogUtil.getLogger().entering(Level.FINEST);
        LogUtil.printComponentToConsole(parsedChild, "parsedChild");

        String creatorTagClass = (String)parsedChild.getAttribute(JspInfo.CREATOR_TAG_CLASS_ATTR);
        if (!(creatorTagClass.equals(_tag.getClass().getName())))
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
