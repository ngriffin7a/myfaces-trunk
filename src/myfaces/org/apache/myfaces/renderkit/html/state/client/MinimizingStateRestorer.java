/**
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
package net.sourceforge.myfaces.renderkit.html.state.client;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * StateRestorer that restores state info saved by the MinimizingStateSaver.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
//FIXME
public class MinimizingStateRestorer
    extends ClientStateRestorer
{
    public UIViewRoot getPreviousTree(FacesContext facesContext)
    {
        throw new UnsupportedOperationException();
    }

    public void restoreState(FacesContext facesContext) throws IOException
    {
        throw new UnsupportedOperationException();
    }

//    private static final Log log = LogFactory.getLog(MinimizingStateRestorer.class);
//
//    private static final String STATE_MAP_REQUEST_ATTR = MinimizingStateRestorer.class.getName() + ".STATE_MAP";
//
//    protected static final String PREVIOUS_TREE_REQUEST_ATTR
//        = MinimizingStateRestorer.class.getName() + ".PREVIOUS_TREE";
//
//    public void restoreState(FacesContext facesContext) throws IOException
//    {
//        String requestTreeId = facesContext.getViewRoot().getViewId();
//        Map stateMap = getStateMap(facesContext);
//
//        String previousTreeId = getStateParameter(stateMap,
//                                                  MinimizingStateSaver.TREE_ID_REQUEST_PARAM);
//        if (previousTreeId != null && requestTreeId.equals(previousTreeId))
//        {
//            //recreate beans of "request" scope
//            recreateRequestScopeBeans(facesContext);
//
//            //restore model beans and values:
//            restoreModelValues(facesContext, stateMap, false);
//
//            //restore previous tree
//            UIViewRoot tree = restorePreviousTree(facesContext, stateMap, requestTreeId);
//            facesContext.setTree(tree);
//
//            //restore listeners:
//            restoreListeners(facesContext, stateMap);
//        }
//        else
//        {
//            //restore global model beans and values:
//            restoreModelValues(facesContext, stateMap, true);
//        }
//
//        restoreLocale(facesContext, stateMap);
//    }
//
//    protected Tree restorePreviousTree(FacesContext facesContext,
//                                       Map stateMap,
//                                       String treeId)
//    {
//        //restore "hint" about unrendered components
//        Set unrendererComponents = restoreUnrenderedComponents(stateMap);
//
//        /*
//        //restore tree
//        TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
//        Tree tree = tf.getTree(facesContext, treeId);
//
//        Tree staticTree = JspInfo.getTree(facesContext, tree.getTreeId());
//        TreeCopier treeCopier = new TreeCopier(facesContext);
//        treeCopier.setOverwriteAttributes(false);
//        treeCopier.setOverwriteComponents(false);
//        treeCopier.setIgnoreComponents(unrendererComponents);
//        treeCopier.copyTree(staticTree, tree);
//
//        //restore component states
//        for (Iterator it = TreeUtils.treeIterator(tree); it.hasNext();)
//        {
//            UIComponent comp = (UIComponent)it.next();
//            restoreComponent(facesContext, stateMap, comp);
//        }
//        */
//
//        //restore tree
//        Tree tree = JspInfo.getTreeClone(facesContext, treeId);
//
//        //restore component states and remove previously unrendered components
//        traverseTree(facesContext, stateMap, unrendererComponents, tree.getRoot());
//
//        ((ServletRequest)facesContext.getExternalContext().getRequest())
//            .setAttribute(PREVIOUS_TREE_REQUEST_ATTR, tree);
//        return tree;
//    }
//
//
//    private void traverseTree(FacesContext facesContext,
//                              Map stateMap,
//                              Set unrendererComponents,
//                              UIComponent uiComponent)
//    {
//        restoreComponent(facesContext, stateMap, uiComponent);
//
//        for (int i = 0, len = uiComponent.getChildCount(); i < len; i++)
//        {
//            UIComponent child = uiComponent.getChild(i);
//            String clientId = child.getClientId(facesContext);
//            if (unrendererComponents.contains(clientId))
//            {
//                UIComponentUtils.removeChild(uiComponent, i);
//                i--;
//                len--;
//            }
//            else
//            {
//                traverseTree(facesContext, stateMap, unrendererComponents, child);
//            }
//        }
//
//        List facetsToBeRemoved = null;
//        for (Iterator it = uiComponent.getFacetNames(); it.hasNext(); )
//        {
//            String facetName = (String)it.next();
//            UIComponent child = uiComponent.getFacet(facetName);
//            String clientId = child.getClientId(facesContext);
//            if (unrendererComponents.contains(clientId))
//            {
//                //we must not remove via Iterator,
//                //because we cannot be sure that it (properly) supports the remove method
//                if (facetsToBeRemoved == null)
//                {
//                    facetsToBeRemoved = new ArrayList();
//                }
//                facetsToBeRemoved.add(facetName);
//            }
//            else
//            {
//                traverseTree(facesContext, stateMap, unrendererComponents, child);
//            }
//        }
//
//        if (facetsToBeRemoved != null)
//        {
//            for (int i = 0; i < facetsToBeRemoved.size(); i++)
//            {
//                UIComponentUtils.removeFacet(uiComponent,
//                                             (String)facetsToBeRemoved.get(i));
//            }
//        }
//    }
//
//
//
//    protected Map getStateMap(FacesContext facesContext)
//    {
//        Map map = (Map)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(STATE_MAP_REQUEST_ATTR);
//        if (map == null)
//        {
//            map = restoreStateMap(facesContext);
//            ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(STATE_MAP_REQUEST_ATTR, map);
//        }
//        return map;
//    }
//
//    protected Map restoreStateMap(FacesContext facesContext)
//    {
//        return ((ServletRequest)facesContext.getExternalContext().getRequest()).getParameterMap();
//    }
//
//    protected String getStateParameter(Map stateMap, String paramName)
//    {
//        String[] values = (String[])stateMap.get(paramName);
//        if (values == null || values.length == 0)
//        {
//            return null;
//        }
//
//        if (values.length == 1)
//        {
//            return values[0];
//        }
//        else //(values.length > 1)
//        {
//            StringBuffer buf = new StringBuffer();
//            for (int i = 0; i < values.length; i++)
//            {
//                if (i > 0)
//                {
//                    buf.append(',');
//                }
//                buf.append(values[i]);
//            }
//            return buf.toString();
//        }
//    }
//
//    protected String getStateParameterValueAsString(Object paramValue)
//    {
//        if (paramValue instanceof String[])
//        {
//            return StringArrayConverter.getAsString((String[])paramValue,
//                                                    false);
//        }
//        else
//        {
//            return (String)paramValue;
//        }
//    }
//
//
//
//    /**
//     * The model reference for each component is checked. If a model bean does not
//     * yet exist it is automatically created in the defined scope.
//     * @param facesContext
//     * @param stateMap
//     * @param uiComponent
//     * @throws javax.faces.FacesException
//     */
//    protected void restoreComponent(FacesContext facesContext,
//                                    Map stateMap,
//                                    UIComponent uiComponent)
//        throws FacesException
//    {
//        //Set valid as default
//        if (uiComponent.isValid())
//        {
//            //component is valid by default, so no need to set explicitly
//        }
//        else
//        {
//            //MinimizingStateSaver always saves a "valid" == false, so it will be
//            //set to saved state later anyway.
//            uiComponent.setValid(true);
//        }
//
//        //restore properties
//        for (Iterator it = stateMap.entrySet().iterator(); it.hasNext();)
//        {
//            Map.Entry entry = (Map.Entry)it.next();
//            String propName = RequestParameterNames.restoreUIComponentStateParameterPropName(facesContext,
//                                                                                             uiComponent,
//                                                                                             (String)entry.getKey());
//            if (propName != null)
//            {
//                restoreComponentProperty(facesContext,
//                                         uiComponent,
//                                         propName,
//                                         entry.getValue());
//            }
//        }
//
//        //restore attributes
//        for (Iterator it = stateMap.entrySet().iterator(); it.hasNext();)
//        {
//            Map.Entry entry = (Map.Entry)it.next();
//            String attrName = RequestParameterNames.restoreUIComponentStateParameterAttrName(facesContext,
//                                                                                                  uiComponent,
//                                                                                                  (String)entry.getKey());
//            if (attrName != null)
//            {
//                restoreComponentAttribute(facesContext,
//                                          uiComponent,
//                                          attrName,
//                                          entry.getValue());
//            }
//        }
//
//        //Not any longer needed when we have cloned the full static tree
//        //registerTagCreatedActionListeners(facesContext, stateMap, uiComponent);
//
//        //restore validators:
//        restoreComponentValidators(facesContext, stateMap, uiComponent);
//    }
//
//
//    protected void restoreComponentProperty(FacesContext facesContext,
//                                            UIComponent uiComponent,
//                                            String propName,
//                                            Object paramValue)
//    {
//        String strValue = getStateParameterValueAsString(paramValue);
//        PropertyDescriptor propertyDescriptor
//            = BeanUtils.findBeanPropertyDescriptor(uiComponent,
//                                               propName);
//        if (propertyDescriptor == null)
//        {
//            throw new IllegalArgumentException("No property descriptor found for property '" + propName + "' of component " + uiComponent.getClientId(facesContext));
//        }
//
//        //is it a null value?
//        if (strValue.equals(MinimizingStateSaver.NULL_DUMMY_VALUE))
//        {
//            BeanUtils.setBeanPropertyValue(uiComponent, propertyDescriptor, null);
//            return;
//        }
//
//        //Find proper converter to convert back from external String
//        Converter conv;
//        if (uiComponent instanceof UIOutput &&
//            propName.equals(MyFacesUIOutput.VALUE_PROP))
//        {
//            conv = ConverterUtils.findValueConverter(facesContext,
//                                                     (UIOutput)uiComponent);
//        }
//        else if (uiComponent instanceof UISelectOne &&
//                propName.equals(MyFacesUISelectOne.SELECTED_VALUE_PROP))
//        {
//            conv = ConverterUtils.findValueConverter(facesContext,
//                                                     (UIOutput)uiComponent);
//        }
//        else
//        {
//            conv = ConverterUtils.findConverter(propertyDescriptor.getPropertyType());
//        }
//
//        Object objValue;
//        if (conv != null)
//        {
//            //we have a converter, so MinimizingStateSaver did convert to String
//            if (conv instanceof StringArrayConverter &&
//                paramValue instanceof String[])
//            {
//                //no need to convert the String value back to StringArray
//                objValue = paramValue;
//            }
//            else
//            {
//                try
//                {
//                    objValue = StateUtils.convertStringToObject(facesContext,
//                                                                conv,
//                                                                strValue);
//                }
//                catch (ConverterException e)
//                {
//                    log.error("Value of attribute " + propName + " will be lost, because of converter exception restoring state of component " + UIComponentUtils.toString(uiComponent) + ".", e);
//                    return;
//                }
//            }
//        }
//        else
//        {
//            //we have no converter, so MinimizingStateSaver did serialize the value
//            try
//            {
//                objValue = ConverterUtils.deserializeAndDecodeBase64(strValue);
//            }
//            catch (FacesException e)
//            {
//                log.error("Value of attribute " + propName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because of exception during deserialization.", e);
//                return;
//            }
//        }
//
//        BeanUtils.setBeanPropertyValue(uiComponent, propertyDescriptor, objValue);
//    }
//
//
//
//    protected void restoreComponentAttribute(FacesContext facesContext,
//                                             UIComponent uiComponent,
//                                             String attrName,
//                                             Object paramValue)
//    {
//        String strValue = getStateParameterValueAsString(paramValue);
//
//        //is it a null value?
//        if (strValue.equals(MinimizingStateSaver.NULL_DUMMY_VALUE))
//        {
//            uiComponent.getAttributes().put(attrName, null);
//            return;
//        }
//
//        //Find proper converter to convert back from external String
//        Converter conv = HTMLRenderer.findConverterForAttribute(facesContext,
//                                                                uiComponent,
//                                                                attrName);
//
//        Object objValue;
//        if (conv != null)
//        {
//            //we have a converter, so MinimizingStateSaver did convert to String
//            if (conv instanceof StringArrayConverter &&
//                paramValue instanceof String[])
//            {
//                //no need to convert the String value back to StringArray
//                objValue = paramValue;
//            }
//            else
//            {
//                try
//                {
//                    objValue = StateUtils.convertStringToObject(facesContext,
//                                                                conv,
//                                                                strValue);
//                }
//                catch (ConverterException e)
//                {
//                    log.error("Value of attribute " + attrName + " will be lost, because of converter exception restoring state of component " + UIComponentUtils.toString(uiComponent) + ".", e);
//                    return;
//                }
//            }
//        }
//        else
//        {
//            //we have no converter, so MinimizingStateSaver did serialize the value
//            try
//            {
//                objValue = ConverterUtils.deserializeAndDecodeBase64(strValue);
//            }
//            catch (FacesException e)
//            {
//                log.error("Value of attribute " + attrName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because of exception during deserialization.", e);
//                return;
//            }
//        }
//
//        uiComponent.getAttributes().put(attrName, objValue);
//    }
//
//
//
//    protected void restoreModelValues(FacesContext facesContext,
//                                      Map stateMap,
//                                      boolean onlyGlobal)
//    {
//        Iterator it = JspInfo.getUISaveStateComponents(facesContext,
//                                                       facesContext.getTree().getTreeId());
//        while (it.hasNext())
//        {
//            UISaveState uiSaveState = (UISaveState)it.next();
//            if (!onlyGlobal ||
//                uiSaveState.isGlobal())
//            {
//                restoreModelValue(facesContext, stateMap, uiSaveState);
//            }
//        }
//    }
//
//    public void restoreModelValue(FacesContext facesContext, Map stateMap, UISaveState uiSaveState)
//    {
//        String valueRef = uiSaveState.getValueRef();
//        if (valueRef == null)
//        {
//            throw new FacesException("UISaveState " + uiSaveState.getComponentId() + " has no model reference");
//        }
//
//        //Check model instance and create, if it does not exist:
//        /*
//        --> already happend in recreateRequestScopeBeans
//
//        JspInfoUtils.checkModelInstance(facesContext, modelRef);
//        */
//
//        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
//        ValueBinding vb = af.getApplication().getValueBinding(valueRef);
//
//        String paramName = RequestParameterNames.getModelValueStateParameterName(valueRef);
//        String paramValue = getStateParameter(stateMap, paramName);
//        if (paramValue != null)
//        {
//            if (paramValue.equals(MinimizingStateSaver.NULL_DUMMY_VALUE))
//            {
//                vb.setValue(facesContext, null);
//                return;
//            }
//
//            Object propValue;
//            Converter conv = ConverterUtils.findValueConverter(facesContext, uiSaveState);
//            if (conv != null)
//            {
//                try
//                {
//                    propValue = StateUtils.convertStringToObject(facesContext,
//                                                                 conv,
//                                                                 paramValue);
//                }
//                catch (ConverterException e)
//                {
//                    throw new FacesException("Error restoring state of model value " + valueRef + ": Converter exception!", e);
//                }
//            }
//            else
//            {
//                propValue = ConverterUtils.deserializeAndDecodeBase64(paramValue);
//            }
//
//            vb.setValue(facesContext, propValue);
//        }
//    }
//
//
//    protected void restoreLocale(FacesContext facesContext, Map stateMap)
//    {
//        String localeVal = getStateParameter(stateMap, MinimizingStateSaver.LOCALE_REQUEST_PARAM);
//        if (localeVal != null)
//        {
//            StringTokenizer st = new StringTokenizer(localeVal, MinimizingStateSaver.LOCALE_REQUEST_PARAM_DELIMITER);
//            String language = "";
//            String country = "";
//            String variant = "";
//            if (st.hasMoreTokens())
//            {
//                language = st.nextToken();
//            }
//            if (st.hasMoreTokens())
//            {
//                country = st.nextToken();
//            }
//            if (st.hasMoreTokens())
//            {
//                variant = st.nextToken();
//            }
//            facesContext.setLocale(new Locale(language, country, variant));
//        }
//    }
//
//
//    protected void restoreListeners(FacesContext facesContext, Map stateMap)
//    {
//        for (Iterator it = stateMap.entrySet().iterator(); it.hasNext();)
//        {
//            Map.Entry entry = (Map.Entry)it.next();
//            String paramName = (String)entry.getKey();
//            RequestParameterNames.ListenerParameterInfo info
//                = RequestParameterNames.getListenerParameterInfo(paramName);
//            if (info != null)
//            {
//                UIComponent root = facesContext.getTree().getRoot();
//                String paramValue = getStateParameter(stateMap, paramName);
//                FacesListener listener;
//                if (info.serializedListener)
//                {
//                    //deserialize Listener
//                    listener = (FacesListener)ConverterUtils.deserializeAndDecodeBase64(paramValue);
//                }
//                else
//                {
//                    //Listener is another component, paramValue is the uniqueId
//                    listener = (FacesListener)root.findComponent(paramValue);
//                    if (listener == null)
//                    {
//                        log.error("Could not find listener component for restored listener of type " + info.listenerType);
//                        continue;
//                    }
//                }
//
//                UIComponent uiComponent = root.findComponent(info.clientId);
//                if (uiComponent == null)
//                {
//                    log.error("Could not find component " + info.clientId + " to add restored listener of type " + listener.getClass().getName());
//                    continue;
//                }
//
//                if (info.listenerType.equals(MinimizingStateSaver.LISTENER_TYPE_ACTION))
//                {
//                    if (uiComponent instanceof UICommand)
//                    {
//                        ((UICommand)uiComponent).addActionListener((ActionListener)listener);
//                    }
//                    else
//                    {
//                        throw new FacesException("Expected UICommand component.");
//                    }
//                }
//                else if (info.listenerType.equals(MinimizingStateSaver.LISTENER_TYPE_VALUE_CHANGED))
//                {
//                    if (uiComponent instanceof UIInput)
//                    {
//                        ((UIInput)uiComponent).addValueChangedListener((ValueChangedListener)listener);
//                    }
//                    else
//                    {
//                        throw new FacesException("Expected UIInput component.");
//                    }
//                }
//                else
//                {
//                    throw new IllegalArgumentException(info.listenerType);
//                }
//
//            }
//        }
//    }
//
//
//
//
//    protected Set restoreUnrenderedComponents(Map stateMap)
//    {
//        Object obj = stateMap.get(MinimizingStateSaver.UNRENDERED_COMPONENTS_REQUEST_PARAM);
//        if (obj == null)
//        {
//            return Collections.EMPTY_SET;
//        }
//
//        String[] values;
//        if (obj instanceof String)
//        {
//            values = new String[] {(String)obj};
//        }
//        else
//        {
//            values = (String[])obj;
//        }
//
//        if (values.length == 0)
//        {
//            return Collections.EMPTY_SET;
//        }
//
//        Set set = new HashSet();
//        if (values.length == 1)
//        {
//            StringTokenizer st = new StringTokenizer(values[0], ",");
//            while(st.hasMoreTokens())
//            {
//                set.add(st.nextToken());
//            }
//            return set;
//        }
//        else
//        {
//            for (int i = 0; i < values.length; i++)
//            {
//                set.add(values[i]);
//            }
//            return set;
//        }
//    }
//
//    public Tree getPreviousTree(FacesContext facesContext)
//    {
//        ServletRequest request = (ServletRequest)facesContext.getExternalContext().getRequest();
//        Tree tree = (Tree)request.getAttribute(PREVIOUS_TREE_REQUEST_ATTR);
//        if (tree != null)
//        {
//            return tree;
//        }
//
//        Map stateMap = getStateMap(facesContext);
//        String previousTreeId = getStateParameter(stateMap,
//                                                  MinimizingStateSaver.TREE_ID_REQUEST_PARAM);
//        if (previousTreeId == null)
//        {
//            return null;
//        }
//
//        return restorePreviousTree(facesContext, stateMap, previousTreeId);
//    }
//
//
//
//    protected void restoreComponentValidators(FacesContext facesContext,
//                                              Map stateMap,
//                                              UIComponent uiComponent)
//    {
//        List toBeRemoved = null;
//
//        for (Iterator it = stateMap.entrySet().iterator(); it.hasNext();)
//        {
//            Map.Entry entry = (Map.Entry)it.next();
//            String paramName = (String)entry.getKey();
//            int valIdx = RequestParameterNames.restoreValidatorIdx(facesContext,
//                                                                   uiComponent,
//                                                                   paramName);
//            if (valIdx >= 0)
//            {
//                Validator oldVal = restoreValidator(facesContext,
//                                                    stateMap,
//                                                    uiComponent,
//                                                    valIdx,
//                                                    getStateParameterValueAsString(entry.getValue()));
//                if (toBeRemoved == null) toBeRemoved = new ArrayList();
//                toBeRemoved.add(oldVal);
//            }
//        }
//
//        if (toBeRemoved != null)
//        {
//            for (Iterator it = toBeRemoved.iterator(); it.hasNext();)
//            {
//                uiComponent.removeValidator((Validator)it.next());
//            }
//        }
//    }
//
//    protected Validator restoreValidator(FacesContext facesContext,
//                                         Map stateMap,
//                                         UIComponent uiComponent,
//                                         int validatorIdx,
//                                         String validatorClass)
//    {
//        Validator oldValidator = null;
//        int idx = 0;
//        for (Iterator it = uiComponent.getValidators(); it.hasNext(); idx++)
//        {
//            if (idx == validatorIdx)
//            {
//                oldValidator = (Validator)it.next();
//                break;
//            }
//            else
//            {
//                it.next();
//            }
//        }
//
//        if (validatorClass.length() == 0)
//        {
//            return oldValidator;
//        }
//
//        Validator newValidator = null;
//        try
//        {
//            Class valClass = Class.forName(validatorClass, true, Thread.currentThread().getContextClassLoader());
//            newValidator = (Validator)valClass.newInstance();
//        }
//        catch (ClassNotFoundException e)
//        {
//            throw new RuntimeException(e);
//        }
//        catch (InstantiationException e)
//        {
//            throw new RuntimeException(e);
//        }
//        catch (IllegalAccessException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//        PropertyDescriptor[] propDescriptors = BeanUtils.getBeanInfo(newValidator).getPropertyDescriptors();
//        for (int i = 0; i < propDescriptors.length; i++)
//        {
//            PropertyDescriptor propDescr = propDescriptors[i];
//            if (propDescr.getReadMethod() != null &&
//                propDescr.getWriteMethod() != null)
//            {
//                restoreValidatorProperty(facesContext,
//                                         stateMap,
//                                         uiComponent,
//                                         validatorIdx,
//                                         newValidator,
//                                         propDescr);
//            }
//        }
//
//        uiComponent.addValidator(newValidator);
//
//        return oldValidator;
//    }
//
//
//    protected void restoreValidatorProperty(FacesContext facesContext,
//                                            Map stateMap,
//                                            UIComponent uiComponent,
//                                            int validatorIdx,
//                                            Validator validator,
//                                            PropertyDescriptor propDescr)
//    {
//        String paramName = RequestParameterNames.getComponentValidatorPropParameterName(facesContext,
//                                                                                        uiComponent,
//                                                                                        validatorIdx,
//                                                                                        propDescr.getName());
//        String strValue = getStateParameter(stateMap, paramName);
//
//        if (strValue.equals(MinimizingStateSaver.NULL_DUMMY_VALUE))
//        {
//            BeanUtils.setBeanPropertyValue(validator, propDescr, null);
//        }
//        else if (propDescr.getPropertyType().equals(String.class))
//        {
//            BeanUtils.setBeanPropertyValue(validator, propDescr, strValue);
//        }
//        else
//        {
//            Converter converter = ConverterUtils.getConverter(propDescr.getPropertyType());
//            Object objValue = StateUtils.convertStringToObject(facesContext,
//                                                               converter,
//                                                               strValue);
//            BeanUtils.setBeanPropertyValue(validator, propDescr, objValue);
//        }
//    }


}
