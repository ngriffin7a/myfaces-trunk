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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.component.MyFacesUIOutput;
import net.sourceforge.myfaces.component.UIComponentHacks;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.ext.UISaveState;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.DataRenderer;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.SecretRenderer;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.StaticFacesListener;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.validator.Validator;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedListener;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.*;

/**
 * StateSaver for the "client_minimized" state saving mode.
 * Only those (dynamical) tree and component infos are saved, that could not
 * otherwise be restored by parsing the JSP.
 * Parameters are saved in "plaintext", so convenient for debugging purposes.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MinimizingStateSaver
    extends ClientStateSaver
{
    private static final String STATE_MAP_REQUEST_ATTR = MinimizingStateSaver.class.getName() + ".STATE_MAP";

    protected static final String TREE_ID_REQUEST_PARAM = "_tId";

    protected static final String LISTENER_TYPE_ACTION = "Action";
    protected static final String LISTENER_TYPE_VALUE_CHANGED = "ValueChanged";

    protected static final String NULL_DUMMY_VALUE = "__NULL__";

    protected static final String UNRENDERED_COMPONENTS_REQUEST_PARAM = "_unrendered";

    protected static final String LOCALE_REQUEST_PARAM = "_locale";
    protected static final String LOCALE_REQUEST_PARAM_DELIMITER = "_";

    private static final Set IGNORE_ATTRIBUTES = new HashSet();
    static
    {
        IGNORE_ATTRIBUTES.add(CommonComponentProperties.CLIENT_ID_ATTR);
    }

    private static final Set IGNORE_PROPERTIES = new HashSet();
    static
    {
        IGNORE_PROPERTIES.add(CommonComponentProperties.PARENT_PROP);
        IGNORE_PROPERTIES.add(CommonComponentProperties.CLIENT_ID_PROP);

        //we must save the "valid" attribute

        //we must save the "componentId" because:
        //      - static componentIds are in parsed tree and are not saved anyway.
        //      - we cannot be sure that a dynamically created componentId is
        //        the same when the tree is getting restored.
    }

    protected Map getStateMap(FacesContext facesContext)
    {
        Map map = (Map)((ServletRequest)facesContext.getExternalContext().getRequest())
                            .getAttribute(STATE_MAP_REQUEST_ATTR);
        if (map == null)
        {
            map = new HashMap();
            ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(STATE_MAP_REQUEST_ATTR,
                                                          map);
            saveResponseTreeId(facesContext, map);
            saveComponents(facesContext, map);
            saveModelValues(facesContext, map);
            saveLocale(facesContext, map);
        }
        return map;
    }


    protected void saveResponseTreeId(FacesContext facesContext, Map stateMap)
    {
        saveParameter(stateMap,
                      TREE_ID_REQUEST_PARAM,
                      facesContext.getTree().getTreeId());
    }


    protected void saveComponents(FacesContext facesContext, Map stateMap)
    {
        LogUtil.getLogger().entering("MinimizingStateSaver", "saveComponents");

        //Remember all seen components of current tree, so that
        //we can find "missing components" later (i.e. components that are in
        //the parsed tree, but have not been rendered)
        Set visitedComponents = new HashSet();

        Iterator treeIt = TreeUtils.treeIterator(facesContext.getTree());
        while(treeIt.hasNext())
        {
            UIComponent comp = (UIComponent)treeIt.next();
            //Find corresponding component in parsed tree
            UIComponent parsedComp = findCorrespondingParsedComponent(facesContext,
                                                                      comp);
            if (parsedComp == null)
            {
                LogUtil.getLogger().warning("Corresponding parsed component not found for component " + comp.getClientId(facesContext));
            }
            saveComponentProperties(facesContext, stateMap, comp, parsedComp);
            saveComponentAttributes(facesContext, stateMap, comp, parsedComp);
            saveListeners(facesContext, stateMap, comp, parsedComp);
            saveValidators(facesContext, stateMap, comp, parsedComp);
            visitedComponents.add(comp.getClientId(facesContext));
        }

        saveUnrenderedComponents(facesContext, stateMap, visitedComponents);

        LogUtil.getLogger().exiting("MinimizingStateSaver", "saveComponents");
    }


    /**
     * Save "hint" for all components, that are in the parsed tree
     * but not in the current tree.
     */
    protected void saveUnrenderedComponents(FacesContext facesContext,
                                            Map stateMap,
                                            Set visitedComponents)
    {
        StringBuffer unrenderedComponents = new StringBuffer();

        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());
        saveUnrenderedComponents(facesContext,
                                 parsedTree.getRoot(),
                                 visitedComponents,
                                 unrenderedComponents);

        if (unrenderedComponents.length() > 0)
        {
            saveParameter(stateMap,
                          UNRENDERED_COMPONENTS_REQUEST_PARAM,
                          unrenderedComponents.toString());
        }
    }


    private void saveUnrenderedComponents(FacesContext facesContext,
                                          UIComponent parsedComp,
                                          Set visitedComponents,
                                          StringBuffer buf)
    {
        String clientId = parsedComp.getClientId(facesContext);
        if (!visitedComponents.contains(clientId))
        {
            if (buf.length() > 0)
            {
                buf.append(',');
            }
            buf.append(clientId);

            //No need to save "unrendered" state of children
            //because they are "unrendered" implicitly
            return;
        }

        for (Iterator it = parsedComp.getFacetsAndChildren(); it.hasNext();)
        {
            //Recursion:
            saveUnrenderedComponents(facesContext,
                                     (UIComponent)it.next(),
                                     visitedComponents,
                                     buf);
        }
    }


    protected void saveComponentProperties(FacesContext facesContext,
                                           Map stateMap,
                                           UIComponent uiComponent,
                                           UIComponent parsedComp)
    {
        //step through all properties of component
        BeanInfo beanInfo = BeanUtils.getBeanInfo(uiComponent);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            //Only save properties that are get- and setable
            if (propertyDescriptor.getReadMethod() != null &&
                propertyDescriptor.getWriteMethod() != null)
            {
                saveComponentProperty(facesContext,
                                      stateMap,
                                      uiComponent,
                                      propertyDescriptor,
                                      parsedComp);
            }
        }
    }

    protected void saveComponentProperty(FacesContext facesContext,
                                         Map stateMap,
                                         UIComponent uiComponent,
                                         PropertyDescriptor propertyDescriptor,
                                         UIComponent parsedComponent)
    {
        String propName = propertyDescriptor.getName();

        if (isIgnoreProperty(uiComponent, propName))
        {
            return;
        }

        if (propName.equals(CommonComponentProperties.VALID_PROP) &&
            uiComponent.isValid())
        {
            //No need to save "valid" if true, because MinimizingStateRestorer
            //assumes true as default anyway.
            return;
        }

        Object propValue = null;
        try
        {
            propValue = BeanUtils.getBeanPropertyValue(uiComponent,
                                                       propertyDescriptor);
        }
        catch (Exception e)
        {
            LogUtil.getLogger().warning("Exception getting property '" + propName + "' of component '" + uiComponent.getClientId(facesContext) + "': " + e.getMessage());
            return;
        }

        //compare current value to static value in parsed component
        if (parsedComponent != null)
        {
            Object parsedValue = BeanUtils.getBeanPropertyValue(parsedComponent,
                                                                propertyDescriptor);
            if ((parsedValue != null && parsedValue.equals(propValue)) ||
                (parsedValue == null && propValue == null))
            {
                //current value identical to hardcoded value --> no need to save
                return;
            }
        }

        //is null value?
        if (propValue == null)
        {
            saveParameter(stateMap,
                          RequestParameterNames.getUIComponentStateParameterPropName(facesContext,
                                                                                     uiComponent,
                                                                                     propName),
                          NULL_DUMMY_VALUE);
            return;
        }

        //convert attribute value to String
        Converter conv;
        if (uiComponent instanceof UIOutput &&
            propName.equals(MyFacesUIOutput.VALUE_PROP))
        {
            conv = ConverterUtils.findValueConverter(facesContext,
                                                     (UIOutput)uiComponent);
        }
        else
        {
            conv = ConverterUtils.findConverter(propertyDescriptor.getPropertyType());
        }

        String strValue;
        if (conv != null)
        {
            //lucky, we have a converter  :-)
            try
            {
                //TODO: Whenever using a converter for state saving we should wrap the facesContext to return a fixed locale and apply a dummy component
                strValue = conv.getAsString(facesContext, uiComponent, propValue);
            }
            catch (ConverterException e)
            {
                LogUtil.getLogger().severe("Value of attribute " + propName + " will be lost, because of converter exception saving state of component " + UIComponentUtils.toString(uiComponent) + ".");
                return;
            }
        }
        else
        {
            //damn, we could not find a converter  :-(
            if (propValue instanceof Serializable)
            {
                try
                {
                    strValue = ConverterUtils.serializeAndEncodeBase64(propValue);
                }
                catch (FacesException e)
                {
                    LogUtil.getLogger().severe("Value of attribute " + propName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because of exception during serialization: " + e.getMessage());
                    return;
                }
            }
            else
            {
                LogUtil.getLogger().severe("Value of attribute " + propName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because it is of non-serializable type: " + propValue.getClass().getName());
                return;
            }
        }

        saveParameter(stateMap,
                      RequestParameterNames.getUIComponentStateParameterPropName(facesContext,
                                                                                 uiComponent,
                                                                                 propName),
                      strValue);
    }



    protected void saveComponentAttributes(FacesContext facesContext,
                                           Map stateMap,
                                           UIComponent uiComponent,
                                           UIComponent parsedComp)
    {
        //Remember all seen attributes of current component, so that
        //we can find "missing attributes" (i.e attributes that were set to null)
        //later
        Set visitedAttributes = new HashSet();

        //step through all attributes of component

        //HACK: copy to ArrayList to prevent ConcurrentModificationException
        List attrNames = new ArrayList();
        for (Iterator compIt = uiComponent.getAttributeNames(); compIt.hasNext();)
        {
            attrNames.add(compIt.next());
        }
        for (Iterator compIt = attrNames.iterator(); compIt.hasNext();)
        {
            String attrName = (String)compIt.next();
            Object attrValue = uiComponent.getAttribute(attrName);

            saveComponentAttribute(facesContext,
                                   stateMap,
                                   uiComponent,
                                   attrName,
                                   attrValue,
                                   parsedComp);

            visitedAttributes.add(attrName);
        }

        // Save all attributes, that are set in the parsed tree
        // but not in the current tree (= removed attributes).
        // Save them by means of a special null dummy value
        if (parsedComp != null)
        {
            for (Iterator parsedCompIt = parsedComp.getAttributeNames(); parsedCompIt.hasNext();)
            {
                String attrName = (String)parsedCompIt.next();
                if (!visitedAttributes.contains(attrName))
                {
                    saveComponentAttribute(facesContext,
                                           stateMap,
                                           uiComponent,
                                           attrName,
                                           null,
                                           null);
                    visitedAttributes.add(attrName);
                }
            }
        }
    }


    protected UIComponent findCorrespondingParsedComponent(FacesContext facesContext,
                                                           UIComponent uiComponent)
    {
        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());
        return parsedTree.getRoot().findComponent(uiComponent.getClientId(facesContext));
    }


    /**
     * @param facesContext
     * @param stateMap
     * @param uiComponent
     * @param attrName
     * @param attrValue
     * @param parsedComponent
     */
    protected void saveComponentAttribute(FacesContext facesContext,
                                          Map stateMap,
                                          UIComponent uiComponent,
                                          String attrName,
                                          Object attrValue,
                                          UIComponent parsedComponent)
    {
        if (isIgnoreAttribute(uiComponent, attrName))
        {
            return;
        }

        //compare current value to static value in parsed component
        if (parsedComponent != null)
        {
            Object parsedValue = parsedComponent.getAttribute(attrName);
            if ((parsedValue != null && parsedValue.equals(attrValue)) ||
                (parsedValue == null && attrValue == null))
            {
                //current value identical to hardcoded value --> no need to save
                return;
            }
        }

        //is null value?
        if (attrValue == null)
        {
            saveParameter(stateMap,
                          RequestParameterNames.getUIComponentStateParameterAttrName(facesContext,
                                                                                     uiComponent,
                                                                                     attrName),
                          NULL_DUMMY_VALUE);
            return;
        }

        //convert attribute value to String
        String strValue;
        Converter conv = UIComponentUtils.findConverterForAttribute(facesContext,
                                                                    uiComponent,
                                                                    attrName);
        if (conv != null)
        {
            //lucky, we have a converter  :-)
            try
            {
                strValue = conv.getAsString(facesContext, uiComponent, attrValue);
            }
            catch (ConverterException e)
            {
                LogUtil.getLogger().severe("Value of attribute " + attrName + " will be lost, because of converter exception saving state of component " + UIComponentUtils.toString(uiComponent) + ".");
                return;
            }
        }
        else
        {
            //damn, we could not find a converter  :-(
            if (attrValue instanceof Serializable)
            {
                try
                {
                    strValue = ConverterUtils.serializeAndEncodeBase64(attrValue);
                }
                catch (FacesException e)
                {
                    LogUtil.getLogger().severe("Value of attribute " + attrName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because of exception during serialization: " + e.getMessage());
                    return;
                }
            }
            else
            {
                LogUtil.getLogger().severe("Value of attribute " + attrName + " of component " + UIComponentUtils.toString(uiComponent) + " will be lost, because it is of non-serializable type: " + attrValue.getClass().getName());
                return;
            }
        }

        saveParameter(stateMap,
                      RequestParameterNames.getUIComponentStateParameterAttrName(facesContext,
                                                                                 uiComponent,
                                                                                 attrName),
                      strValue);
    }


    protected void saveModelValues(FacesContext facesContext, Map stateMap)
    {
        Iterator it = JspInfo.getUISaveStateComponents(facesContext,
                                                       facesContext.getTree().getTreeId());
        while (it.hasNext())
        {
            UISaveState uiSaveState = (UISaveState)it.next();
            saveModelValue(facesContext, stateMap, uiSaveState);
        }
    }

    protected void saveModelValue(FacesContext facesContext,
                                  Map stateMap,
                                  UISaveState uiSaveState)
    {
        String valueRef = uiSaveState.getValueRef();
        if (valueRef == null)
        {
            throw new FacesException("UISaveState " + uiSaveState.getComponentId() + " has no model reference");
        }

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ValueBinding vb = af.getApplication().getValueBinding(valueRef);

        Object propValue = vb.getValue(facesContext);
        if (propValue != null)
        {
            String paramValue;
            Converter conv = ConverterUtils.findValueConverter(facesContext, uiSaveState);
            if (conv != null)
            {
                try
                {
                    paramValue = conv.getAsString(facesContext, uiSaveState, propValue);
                }
                catch (ConverterException e)
                {
                    throw new FacesException("Error saving state of model value " + valueRef + ": Converter exception!", e);
                }
            }
            else
            {
                paramValue = ConverterUtils.serializeAndEncodeBase64(propValue);
            }

            saveParameter(stateMap,
                          RequestParameterNames.getModelValueStateParameterName(valueRef),
                          paramValue);
        }
        else
        {
            saveParameter(stateMap,
                          RequestParameterNames.getModelValueStateParameterName(valueRef),
                          NULL_DUMMY_VALUE);
        }
    }


    protected void saveParameter(Map map,
                                 String paramName,
                                 String paramValue)
    {
        if (map.get(paramName) != null)
        {
            //throw new IllegalStateException("Duplicate state parameter " + paramName);
            LogUtil.getLogger().warning("Duplicate state parameter " + paramName);
        }
        map.put(paramName, paramValue);
    }


    protected void writeUrlState(FacesContext facesContext, Writer writer) throws IOException
    {
        Map stateMap = getStateMap(facesContext);

        for (Iterator entries = stateMap.entrySet().iterator(); entries.hasNext();)
        {
            Map.Entry entry = (Map.Entry)entries.next();

            writer.write('&');  //we assume that there were previous parameters
            writer.write((String)entry.getKey());
            writer.write('=');
            writer.write(HTMLRenderer.urlEncode((String)entry.getValue()));
        }
    }

    protected void writeHiddenInputsState(FacesContext facesContext, Writer writer) throws IOException
    {
        Map stateMap = getStateMap(facesContext);

        for (Iterator entries = stateMap.entrySet().iterator(); entries.hasNext();)
        {
            Map.Entry entry = (Map.Entry)entries.next();

            writer.write("\n<input type=\"hidden\" name=\"");
            writer.write((String)entry.getKey());
            writer.write("\" value=\"");
            writer.write(HTMLEncoder.encode((String)entry.getValue(), false, false));
            writer.write("\">");
        }
    }


    protected boolean isIgnoreAttribute(UIComponent comp, String attrName)
    {
        if (UIComponentUtils.isInternalAttribute(attrName))
        {
            return true;
        }
        else
        {
            return IGNORE_ATTRIBUTES.contains(attrName);
        }
    }

    protected boolean isIgnoreProperty(UIComponent comp, String propName)
    {
        if (comp instanceof UIOutput &&
            propName.equals(MyFacesUIOutput.VALUE_PROP))
        {
            return isIgnoreValue((UIOutput)comp);
        }
        else
        {
            return IGNORE_PROPERTIES.contains(propName);
        }
    }

    protected boolean isIgnoreValue(UIOutput comp)
    {
        //Secret with redisplay == false?
        String rendererType = comp.getRendererType();
        if (rendererType != null && rendererType.equals(SecretRenderer.TYPE))
        {
            Boolean redisplay = (Boolean)comp.getAttribute(SecretRenderer.REDISPLAY_ATTR);
            if (redisplay == null ||   //because false is default
                !redisplay.booleanValue())
            {
                //value must not be redisplayed, so also no (visual) state saving must occur
                return true;
            }
        }

        //is it a DataRenderer variable (= "var" attribute of a UIPanel) ?
        String modelRef = comp.getValueRef();
        if (modelRef != null)
        {
            UIComponent parent = comp.getParent();
            while (parent != null)
            {
                if (parent instanceof UIPanel)
                {
                    String var = (String)parent.getAttribute(DataRenderer.VAR_ATTR);
                    if (var != null)
                    {
                        if (modelRef.equals(var) ||
                            modelRef.startsWith(var + "."))
                        {
                            return true;
                        }
                    }
                }
                parent = parent.getParent();
            }
        }

        return false;
    }


    protected void saveLocale(FacesContext facesContext, Map stateMap)
    {
        Locale locale = facesContext.getLocale();
        if (!(locale.equals(Locale.getDefault())))
        {
            String localeValue = locale.getLanguage() + LOCALE_REQUEST_PARAM_DELIMITER +
                                 locale.getCountry() + LOCALE_REQUEST_PARAM_DELIMITER +
                                 locale.getVariant();
            saveParameter(stateMap, LOCALE_REQUEST_PARAM, localeValue);
        }
    }


    protected void saveListeners(FacesContext facesContext,
                                 Map stateMap,
                                 UIComponent uiComponent,
                                 UIComponent parsedComp)
    {
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ActionListener actionListener = af.getApplication().getActionListener();

        List[] listeners = UIComponentHacks.getListeners(uiComponent);
        List[] staticListeners = UIComponentHacks.getListeners(parsedComp);
        if (listeners != null)
        {
            for (Iterator it = PhaseId.VALUES.iterator(); it.hasNext();)
            {
                PhaseId phaseId = (PhaseId)it.next();
                List phaseListeners = listeners[phaseId.getOrdinal()];
                List staticPhaseListeners = null;
                if (staticListeners != null)
                {
                    staticPhaseListeners = staticListeners[phaseId.getOrdinal()];
                }
                if (phaseListeners != null)
                {
                    savePhaseListeners(facesContext,
                                       stateMap,
                                       uiComponent,
                                       phaseListeners,
                                       staticPhaseListeners,
                                       actionListener);
                }
            }
        }
    }

    protected void savePhaseListeners(FacesContext facesContext,
                                      Map stateMap,
                                      UIComponent uiComponent,
                                      List phaseListeners,
                                      List staticPhaseListeners,
                                      ActionListener applicationActionListener)
    {
        for (Iterator it = phaseListeners.iterator(); it.hasNext();)
        {
            FacesListener facesListener = (FacesListener)it.next();

            if (isStaticListener(facesListener, staticPhaseListeners))
            {
                return;
            }

            String listenerType;
            if (facesListener instanceof ActionListener)
            {
                listenerType = LISTENER_TYPE_ACTION;
                if (uiComponent instanceof UICommand &&
                    facesListener == applicationActionListener)
                {
                    //default ActionListener for this UICommand will be
                    //automatically registered again in "reconstituteComponentTree" phase
                    return;
                }
            }
            else if (facesListener instanceof ValueChangedListener)
            {
                listenerType = LISTENER_TYPE_VALUE_CHANGED;
            }
            else
            {
                //TODO: Support for common Listeners: find "addFooListener" method via reflection...
                LogUtil.getLogger().warning("Unsupported Listener type " + facesListener.getClass().getName());
                continue;
            }

            if (facesListener instanceof UIComponent)
            {
                //Listener is a component, so we only need to save the uniqueId
                String paramName = RequestParameterNames.getComponentListenerParameterName(facesContext,
                                                                                           uiComponent,
                                                                                           listenerType);
                String paramValue = ((UIComponent)facesListener).getClientId(facesContext);
                saveParameter(stateMap, paramName, paramValue);
            }
            else
            {
                //Listener is of unknown class, so we must serialize it
                String paramName = RequestParameterNames.getSerializableListenerParameterName(facesContext,
                                                                                              uiComponent,
                                                                                              listenerType);
                String paramValue = ConverterUtils.serializeAndEncodeBase64(facesListener);
                saveParameter(stateMap, paramName, paramValue);
            }
        }

    }


    protected boolean isStaticListener(FacesListener facesListener,
                                       List staticPhaseListeners)
    {
        if (facesListener instanceof StaticFacesListener)
        {
            //Listener was created by JspTreeParser
            //and can automatically be restored by MinimizingStateRestorer.
            return true;
        }

        if (staticPhaseListeners == null)
        {
            return false;
        }

        String listenerType = facesListener.getClass().getName();
        for (Iterator it = staticPhaseListeners.iterator(); it.hasNext();)
        {
            StaticFacesListener staticFacesListener = (StaticFacesListener)it.next();
            String staticType = staticFacesListener.getWrappedListener().getClass().getName();
            if (listenerType.equals(staticType))
            {
                return true;
            }
        }

        return false;
    }



    protected void saveValidators(FacesContext facesContext,
                                  Map stateMap,
                                  UIComponent uiComponent,
                                  UIComponent parsedComp)
    {
        Iterator parsedIt;
        if (parsedComp != null)
        {
            parsedIt = parsedComp.getValidators();
        }
        else
        {
            parsedIt = Collections.EMPTY_LIST.iterator();
        }
        Iterator it = uiComponent.getValidators();
        int parsedValIdx = 0;
        for (; parsedIt.hasNext() && it.hasNext(); parsedValIdx++)
        {
            Validator validator = (Validator)it.next();
            Validator parsedValidator = (Validator)parsedIt.next();
            if (validator != parsedValidator &&
                !equalValidators(validator, parsedValidator))
            {
                saveValidator(facesContext, stateMap, uiComponent, parsedValIdx, validator);
            }
        }

        if (parsedIt.hasNext())
        {
            //Remember removed validators
            for (; parsedIt.hasNext(); parsedValIdx++)
            {
                parsedIt.next();
                saveParameter(stateMap,
                              RequestParameterNames.getComponentValidatorClassParameterName(facesContext,
                                                                                            uiComponent,
                                                                                            parsedValIdx),
                              "");
            }
        }
        else if (it.hasNext())
        {
            //Save additional validators
            for (; it.hasNext(); parsedValIdx++)
            {
                Validator validator = (Validator)it.next();
                saveValidator(facesContext, stateMap, uiComponent, parsedValIdx, validator);
            }
        }
    }

    protected boolean equalValidators(Validator val1, Validator val2)
    {
        if (!val1.getClass().equals(val2.getClass()))
        {
            return false;
        }

        PropertyDescriptor[] propDescriptors = BeanUtils.getBeanInfo(val1).getPropertyDescriptors();
        for (int i = 0; i < propDescriptors.length; i++)
        {
            PropertyDescriptor propdDescr = propDescriptors[i];
            if (propdDescr.getReadMethod() != null &&
                propdDescr.getWriteMethod() != null)
            {
                Object propVal1 = BeanUtils.getBeanPropertyValue(val1, propdDescr);
                Object propVal2 = BeanUtils.getBeanPropertyValue(val2, propdDescr);
                if (propVal1 == null && propVal2 == null)
                {
                    continue;
                }
                else if (propVal1 == null && propVal2 != null ||
                         propVal1 != null && propVal2 == null ||
                         !propVal1.equals(propVal2))
                {
                    return false;
                }
            }
        }

        return true;
    }

    protected void saveValidator(FacesContext facesContext,
                                 Map stateMap,
                                 UIComponent uiComponent,
                                 int valIdx,
                                 Validator validator)
    {
        saveParameter(stateMap,
                      RequestParameterNames.getComponentValidatorClassParameterName(facesContext,
                                                                                    uiComponent,
                                                                                    valIdx),
                      validator.getClass().getName());

        PropertyDescriptor[] propDescriptors = BeanUtils.getBeanInfo(validator).getPropertyDescriptors();
        for (int i = 0; i < propDescriptors.length; i++)
        {
            PropertyDescriptor propDescr = propDescriptors[i];
            if (propDescr.getReadMethod() != null &&
                propDescr.getWriteMethod() != null)
            {
                String propName = propDescr.getName();
                Object propVal = BeanUtils.getBeanPropertyValue(validator, propDescr);
                if (propVal != null)
                {
                    saveParameter(stateMap,
                                  RequestParameterNames.getComponentValidatorPropParameterName(facesContext,
                                                                                               uiComponent,
                                                                                               valIdx,
                                                                                               propName),
                                  propVal.toString());  //TODO: Use converters to convert to String
                }
                else
                {
                    saveParameter(stateMap,
                                  RequestParameterNames.getComponentValidatorPropParameterName(facesContext,
                                                                                               uiComponent,
                                                                                               valIdx,
                                                                                               propName),
                                  NULL_DUMMY_VALUE);
                }
            }
        }
    }


    protected void saveValidatorProperty(FacesContext facesContext,
                                         Map stateMap,
                                         UIComponent uiComponent,
                                         int valIdx,
                                         Validator validator,
                                         PropertyDescriptor propDescr)
    {
        String paramName = RequestParameterNames.getComponentValidatorPropParameterName(facesContext,
                                                                                        uiComponent,
                                                                                        valIdx,
                                                                                        propDescr.getName());
        Object objVal = BeanUtils.getBeanPropertyValue(validator, propDescr);

        if (objVal == null)
        {
            saveParameter(stateMap,
                          paramName,
                          NULL_DUMMY_VALUE);
        }
        else if (propDescr.getPropertyType().equals(String.class))
        {
            saveParameter(stateMap,
                          paramName,
                          (String)objVal);
        }
        else
        {
            Converter converter = ConverterUtils.getConverter(propDescr.getPropertyType());
            String strValue = converter.getAsString(facesContext,
                                                    facesContext.getTree().getRoot(),   //dummy component
                                                    objVal);
            BeanUtils.setBeanPropertyValue(validator, propDescr, strValue);
        }

    }

}
