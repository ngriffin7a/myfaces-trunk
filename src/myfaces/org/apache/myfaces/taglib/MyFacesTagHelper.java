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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.tree.Tree;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesTagHelper
{
    private static final Log log = LogFactory.getLog(MyFacesTagHelper.class);

    private MyFacesTagBaseIF _tag;
    private Set _attributes = null;
    protected FacesContext _facesContext;
    protected PageContext _pageContext;

    private boolean _parentComponentOk = false;
    private UIComponent _parentComponent;

    private UIComponent _newComponent;

    private Boolean _suppressed;

    private String _rendererType = null;



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


    private static final String UI_COMPONENT_TAG_STACK_ATTR
        = MyFacesTagHelper.class.getName() + ".UI_COMPONENT_TAG_STACK";

    protected Stack getUIComponentTagStack()
    {
        ServletRequest request = (ServletRequest)getFacesContext().getExternalContext().getRequest();
        Stack stack = (Stack)request.getAttribute(UI_COMPONENT_TAG_STACK_ATTR);
        if (stack == null)
        {
            stack = new Stack();
            request.setAttribute(UI_COMPONENT_TAG_STACK_ATTR, stack);
        }
        return stack;
    }

    public UIComponentTag getParentUIComponentTag()
    {
        Stack stack = getUIComponentTagStack();
        if (stack.isEmpty())
        {
            return null;
        }

        UIComponentTag tag = (UIComponentTag)stack.peek();
        if (tag == _tag)
        {
            //This tag is already on the stack, so we must return the second top
            if (stack.size() < 2)
            {
                return null;
            }
            tag = (UIComponentTag)stack.get(stack.size() - 2);
        }

        return tag;
    }


    public void setRendererType(String rendererType)
    {
        _rendererType = rendererType;
    }

    public String getRendererType()
    {
        if (_rendererType == null)
        {
            return _tag.getDefaultRendererType();
        }
        else
        {
            return _rendererType;
        }
    }



    //property helpers

    private void internalSetComponentProperty(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, true));
    }

    protected void setComponentPropertyObject(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        internalSetComponentProperty(attrName, val);
    }

    protected void setComponentPropertyString(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        internalSetComponentProperty(attrName, val.toString());
    }

    protected void setComponentPropertyBoolean(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        if (val instanceof Boolean)
        {
            internalSetComponentProperty(attrName, val);
        }
        else
        {
            internalSetComponentProperty(attrName,
                                         Boolean.valueOf(val.toString()));
        }
    }

    protected void setComponentPropertyInteger(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        if (val instanceof Integer)
        {
            setComponentPropertyObject(attrName, val);
        }
        else if (val instanceof Number)
        {
            setComponentPropertyObject(attrName,
                                       new Integer(((Number)val).intValue()));
        }
        else
        {
            setComponentPropertyObject(attrName,
                                       new Integer(val.toString()));
        }
    }



    private void internalSetRendererAttribute(String attrName, Object attrValue)
    {
        if (_attributes == null)
        {
            _attributes = new HashSet();
        }
        _attributes.add(new Attribute(attrName, attrValue, false));
    }

    protected void setRendererAttributeObject(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        internalSetRendererAttribute(attrName, val);
    }

    protected void setRendererAttributeString(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        internalSetRendererAttribute(attrName, val.toString());
    }

    protected void setRendererAttributeBoolean(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        if (val instanceof Boolean)
        {
            internalSetRendererAttribute(attrName, val);
        }
        else
        {
            internalSetRendererAttribute(attrName,
                                         Boolean.valueOf(val.toString()));
        }
    }

    protected void setRendererAttributeInteger(String attrName, Object attrValue)
    {
        Object val = evaluateSimpleELExpression(attrValue);
        if (val instanceof Integer)
        {
            setRendererAttributeObject(attrName, val);
        }
        else if (val instanceof Number)
        {
            setRendererAttributeObject(attrName,
                                       new Integer(((Number)val).intValue()));
        }
        else
        {
            setRendererAttributeObject(attrName,
                                       new Integer(val.toString()));
        }
    }


    protected Object evaluateSimpleELExpression(Object attrValue)
    {
        /*
        if (attrValue != null &&
            attrValue instanceof String &&
            ((String)attrValue).startsWith("${") &&
            ((String)attrValue).endsWith("}"))
        {
            String expr = ((String)attrValue).substring(2, ((String)attrValue).length() - 1);
            ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            ValueBinding vb = af.getApplication().getValueBinding(expr);
            return vb.getValue(getFacesContext());
        }
        else
        {
            return attrValue;
        }
        */

        if (attrValue == null || !(attrValue instanceof String))
        {
            return attrValue;
        }

        String strValue = (String)attrValue;
        int dollarIdx = strValue.indexOf("${");
        if (dollarIdx == -1)
        {
            return strValue;
        }

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = af.getApplication();

        StringBuffer buf = new StringBuffer(strValue.length());
        int endIdx = -1;
        while (dollarIdx != -1)
        {
            buf.append(strValue.substring(endIdx + 1, dollarIdx));

            endIdx = strValue.indexOf('}', dollarIdx + 2);
            if (endIdx == -1)
            {
                log.error("Illegal EL expression '" + strValue + "'.");
                buf.append(strValue.substring(dollarIdx));
                return buf.toString();
            }

            String expr = strValue.substring(dollarIdx + 2, endIdx);
            if (expr.length() > 1 &&
                (expr.startsWith("'") && expr.endsWith("'")) ||
                (expr.startsWith("\"") && expr.endsWith("\"")))
            {
                //Quoted literal
                buf.append(expr.substring(1, expr.length() - 1));
            }
            else
            {
                ValueBinding binding = application.getValueBinding(expr);
                Object obj = binding.getValue(getFacesContext());
                buf.append(obj.toString());     //TODO: more sophisticated method for String conversion --> what says JSP Spec. 2.0 ?
            }

            dollarIdx = strValue.indexOf("${", endIdx + 1);
        }
        buf.append(strValue.substring(endIdx + 1));

        return buf.toString();
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
                        uiComponent.getAttributes().put(attr.name, attr.value);
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
                        if (uiComponent.getAttributes().put(attr.name) == null)
                        {
                            uiComponent.getAttributes().put(attr.name, attr.value);
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
        //Try bean property setter first
        PropertyDescriptor pd = BeanUtils.findBeanPropertyDescriptor(uiComponent,
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
                log.warn("Exception in property setter of component " + UIComponentUtils.toString(uiComponent) + " - attribute will be set directly.", e);
            }
        }
        else
        {
            //Component does not have a matching bean property!
            log.error("Component " + UIComponentUtils.toString(uiComponent) + " does not have a valid property setter method for property '" + propertyName + "'.");
        }
    }



    private void overrideComponentProperty(UIComponent uiComponent,
                                           String propertyName,
                                           Object propertyValue)
    {
        boolean errorOccured = false;

        //Try bean property setter first
        PropertyDescriptor pd = BeanUtils.findBeanPropertyDescriptor(uiComponent,
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
                log.warn("Exception in property getter or setter of component " + UIComponentUtils.toString(uiComponent) + " - attribute will be set directly.", e);
                errorOccured = true;
            }
        }
        else
        {
            //Component does not have a matching bean property!
            log.error("Component " + UIComponentUtils.toString(uiComponent) + " does not have valid property setter and getter methods for property '" + propertyName + "'.");
            errorOccured = true;
        }

        if (errorOccured)
        {
            //Alternativly set by attribute name:
            if (uiComponent.getAttributes().get(propertyName) == null)
            {
                uiComponent.getAttributes().put(propertyName, propertyValue);
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
            log.error("Exception occured getting currentValue of component " + UIComponentUtils.toString(uiComponent) + ".", e);
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
        String id = _tag.getId();
        if (id == null)
        {
            //no id attribute in tag, so we must find out the automatic id
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
            //We could find the component by a call to parentComp.findComponent(id)
            //but we also need the child index to adapt the member variable childIndex
            //in the UIComponentTag class.
            //This is necessary, because there could be a mixture of tags that directly extend the
            //UIComponentTag from the JSF API and tags that extend the MyFacesTag. To keep the
            //JSF APIs method of finding corresponding components for tags working, we must do
            //this hack.
            findComp = null;
            for (int i = 0; i < parentComp.getChildCount(); i++)
            {
                UIComponent c = parentComp.getChild(i);
                if (id.equals(c.getComponentId()))
                {
                    //child with if was found
                    findComp = c;
                    //childIndex is needed for tags that extend UIComponentTag directly
                    UIComponentTagHacks.setChildIndex(_tag.getParentUIComponentTag(), i + 1);
                    break;
                }
            }
            if (findComp == null)
            {
                //Perhaps a facet?
                for (Iterator it = parentComp.getFacetNames(); it.hasNext(); )
                {
                    UIComponent c = parentComp.getFacet((String)it.next());
                    if (id.equals(c.getComponentId()))
                    {
                        findComp = c;
                        break;
                    }
                }
            }
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


    /**
     * Find out the automatic component id by searching the parsed tree for the corresponding
     * component.
     */
    protected String findoutComponentId()
    {
        if (log.isTraceEnabled()) log.trace("entering findoutComponentId in MyFacesTagHelper");

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
            throw new IllegalStateException("UIComponentTag " + _tag.getClass().getName() + ": Corresponding component in parsed tree could not be found!");
        }

        //Parsed component found, get id
        String id = parsedChild.getComponentId();
        _tag.setId(id);

        if (log.isTraceEnabled()) log.trace("exiting findoutComponentId in MyFacesTagHelper");
        return id;
    }


    protected UIComponent getParentComponent()
    {
        if (!_parentComponentOk)
        {
            //determine current parent
            /*
            Tag parentTag = _tag.getParent();
            while (parentTag != null &&
                   (!(parentTag instanceof UIComponentTag) ||
                    (((UIComponentTag)parentTag).getComponent() == null)))
            {
                parentTag = parentTag.getParent();
            }
            */
            Tag parentTag = _tag.getParentUIComponentTag();
            while (parentTag != null &&
                   (!(parentTag instanceof UIComponentTag) ||
                    (((UIComponentTag)parentTag).getComponent() == null)))
            {
                if (parentTag instanceof MyFacesTagBaseIF)
                {
                    parentTag = ((MyFacesTagBaseIF)parentTag).getParentUIComponentTag();
                }
                else
                {
                    parentTag = parentTag.getParent();
                }
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
                    Object[] jspPos = (Object[])found.getAttributes().get(JspInfo.JSP_POSITION_ATTR);
                    String pos = "";
                    if (jspPos != null)
                    {
                        pos = " in file '" + jspPos[0] + "' (line " + jspPos[1] + " - " + jspPos[2] + ")";
                    }
                    log.warn("Component " + UIComponentUtils.toString(newComponent) + pos + " is ambigous. This component must have an id when it is rendered conditional (i.e. within if-Block in JSP, or within conditional JSTL-tag).");
                }
            }

            rememberChildIndexForParent(facesContext, parentClientId, foundIdx);
        }
        else
        {
            if (log.isTraceEnabled()) log.trace("exiting findParsedChild in MyFacesTagHelper: not found!");
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
        if (log.isTraceEnabled())
        {
            log.trace("entering equalsParsedChild in MyFacesTagHelper");
            log.trace("parsedChild: " + UIComponentUtils.toString(parsedChild));
        }

        String creatorTagClass = (String)parsedChild.getAttributes().get(JspInfo.CREATOR_TAG_CLASS_ATTR);
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
                String hardcodedId = (String)parsedChild.getAttributes().get(attrName);
                String componentId = compToCompare.getComponentId();
                if (componentId == null || !componentId.equals(hardcodedId))
                {
                    if (log.isTraceEnabled()) log.trace("      diff: hardcoded id / " + compToCompare.getComponentId() + " <> " + hardcodedId);
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
                    Object parsedValue = parsedChild.getAttributes().get(attrName);
                    Object actualValue = compToCompare.getAttributes().get(attrName);
                    if (parsedValue == null)
                    {
                        if (actualValue != null)
                        {
                            if (log.isTraceEnabled()) log.trace("      diff: " + attrName + " / " + actualValue + " <> " + parsedValue);
                            return false;
                        }
                    }
                    else if (actualValue == null ||
                             !parsedValue.equals(actualValue))
                    {
                        if (log.isTraceEnabled()) log.trace("      diff: " + attrName + " / " + actualValue + " <> " + parsedValue);
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
