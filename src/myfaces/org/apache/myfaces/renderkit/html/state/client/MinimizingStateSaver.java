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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.DataRenderer;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.SecretRenderer;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.StaticFacesListener;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedListener;
import javax.faces.tree.Tree;
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

    public static final String TRANSIENT_ATTR
        = StateRenderer.class.getName() + ".TRANSIENT";

    private static final Set IGNORE_ATTRIBUTES = new HashSet();
    static
    {
        IGNORE_ATTRIBUTES.add(CommonComponentAttributes.PARENT_ATTR);
        IGNORE_ATTRIBUTES.add(UIComponent.CLIENT_ID_ATTR);

        //we must save the "valid" attribute

        //we must save the "componentId" because:
        //      - static componentId are in parsed tree and are not saved anyway.
        //      - we cannot be sure that a dynamically created componentId is
        //        the same when the tree is getting restored.
    }


    protected Map getStateMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest()
                            .getAttribute(STATE_MAP_REQUEST_ATTR);
        if (map == null)
        {
            map = new HashMap();
            facesContext.getServletRequest().setAttribute(STATE_MAP_REQUEST_ATTR,
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
                LogUtil.getLogger().warning("Corresponding parsed component not found for component " + UIComponentUtils.getUniqueComponentId(facesContext, comp));
            }
            saveComponentAttributes(facesContext, stateMap, comp, parsedComp);
            saveListeners(facesContext, stateMap, comp, parsedComp);
            visitedComponents.add(UIComponentUtils.getUniqueComponentId(facesContext, comp));
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
        String uniqueId = UIComponentUtils.getUniqueComponentId(facesContext, parsedComp);
        if (!visitedComponents.contains(uniqueId))
        {
            if (buf.length() > 0)
            {
                buf.append(',');
            }
            buf.append(uniqueId);

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


        //enforce saving of "valid" attribute, if it is null and defaults to false
        if (!visitedAttributes.contains(CommonComponentAttributes.VALID_ATTR) &&
            !uiComponent.isValid())
        {
            //"valid" attribute not yet seen, so the internal attribute is null
            //but there is a default value when we use the property getter.
            //If valid == false, we must save it, because MinimizingStateRestorer
            //always assumes true as the default.
            //Since normally all common are valid, this can minimize
            //the number of saved attributes.
            saveComponentAttribute(facesContext,
                                   stateMap,
                                   uiComponent,
                                   CommonComponentAttributes.VALID_ATTR,
                                   Boolean.FALSE,
                                   null);
            visitedAttributes.add(CommonComponentAttributes.VALID_ATTR);
        }

        /*
        Problems! e.g. components with modelRef to a var
        if (!visitedAttributes.contains(CommonComponentAttributes.VALUE_ATTR) &&
            uiComponent.getModelReference() != null &&
            !UIComponentUtils.isTransient(uiComponent))
        {
            saveComponentAttribute(facesContext,
                                   stateMap,
                                   uiComponent,
                                   CommonComponentAttributes.VALUE_ATTR,
                                   uiComponent.currentValue(facesContext),
                                   null);
        }
        */
    }


    protected UIComponent findCorrespondingParsedComponent(FacesContext facesContext,
                                                           UIComponent uiComponent)
    {
        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());
        return UIComponentUtils.findComponentByUniqueId(facesContext,
                                                        parsedTree,
                                                        UIComponentUtils.getUniqueComponentId(facesContext, uiComponent));
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

        if (attrName.equals(CommonComponentAttributes.VALID_ATTR) &&
            uiComponent.isValid())
        {
            //No need to save "valid" if true, because MinimizingStateRestorer
            //assumes true as default anyway.
            return;
        }

        //is it Sun's troublesome "tagHash" attribute in the root?
        if (TagHashHack.isTagHashAttribute(uiComponent, attrName))
        {
            String strValue = TagHashHack.getAsStringToBeSaved(facesContext,
                                                               (Map)attrValue);
            if (strValue != null)
            {
                saveParameter(stateMap,
                              RequestParameterNames.getUIComponentStateParameterName(facesContext,
                                                                                     uiComponent,
                                                                                     attrName),
                              strValue);
            }
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
                          RequestParameterNames.getUIComponentStateParameterName(facesContext,
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
                      RequestParameterNames.getUIComponentStateParameterName(facesContext,
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
            UIComponent uiSaveState = (UIComponent)it.next();
            saveModelValue(facesContext, stateMap, uiSaveState);
        }
    }

    protected void saveModelValue(FacesContext facesContext,
                                     Map stateMap,
                                     UIComponent uiSaveState)
    {
        String modelRef = uiSaveState.getModelReference();
        if (modelRef == null)
        {
            throw new FacesException("UISaveState " + uiSaveState.getComponentId() + " has no model reference");
        }
        Object propValue = facesContext.getModelValue(modelRef);
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
                    throw new FacesException("Error saving state of model value " + modelRef + ": Converter exception!", e);
                }
            }
            else
            {
                paramValue = ConverterUtils.serializeAndEncodeBase64(propValue);
            }

            saveParameter(stateMap,
                          RequestParameterNames.getModelValueStateParameterName(modelRef),
                          paramValue);
        }
        else
        {
            saveParameter(stateMap,
                          RequestParameterNames.getModelValueStateParameterName(modelRef),
                          NULL_DUMMY_VALUE);
        }
    }


    protected void saveParameter(Map map,
                                 String paramName,
                                 String paramValue)
    {
        if (map.get(paramName) != null)
        {
            throw new IllegalStateException("Duplicate state parameter " + paramName);
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
        else if (attrName.equals(CommonComponentAttributes.VALUE_ATTR))
        {
            return isIgnoreValue(comp);
        }
        else
        {
            return IGNORE_ATTRIBUTES.contains(attrName);
        }
    }

    protected boolean isIgnoreValue(UIComponent comp)
    {
        //Secret with redisplay == false?
        String rendererType = comp.getRendererType();
        if (rendererType != null && rendererType.equals(SecretRenderer.TYPE))
        {
            Boolean redisplay = (Boolean)comp.getAttribute(SecretRenderer.REDISPLAY_ATTR);
            if (redisplay == null   //because false is default
                || !redisplay.booleanValue())
            {
                //value must not be redisplayed, so also no (visual) state saving must occur
                return true;
            }
        }

        //transient?
        if (UIComponentUtils.isTransient(comp))
        {
            return true;
        }

        //is it a DataRenderer variable (= "var" attribute of a UIPanel) ?
        String modelRef = comp.getModelReference();
        if (modelRef != null)
        {
            UIComponent parent = UIComponentUtils.getParentOrFacetOwner(comp);
            while (parent != null)
            {
                if (parent.getComponentType().equals(UIPanel.TYPE))
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
                parent = UIComponentUtils.getParentOrFacetOwner(parent);
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
        List[] listeners = UIComponentUtils.getListeners(uiComponent);
        List[] staticListeners = UIComponentUtils.getListeners(parsedComp);
        if (listeners != null)
        {
            /*
            Set tagCreatedActionListenersSet
                = (Set)facesContext.getServletRequest()
                    .getAttribute(ActionListenerTag.TAG_CREATED_ACTION_LISTENERS_SET_ATTR);
            */
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
                                       //tagCreatedActionListenersSet,
                                       phaseListeners,
                                       staticPhaseListeners);
                }
            }
        }
    }

    protected void savePhaseListeners(FacesContext facesContext,
                                      Map stateMap,
                                      UIComponent uiComponent,
                                      //Set tagCreatedActionListenersSet,
                                      List phaseListeners,
                                      List staticPhaseListeners)
    {
        for (Iterator it = phaseListeners.iterator(); it.hasNext();)
        {
            FacesListener facesListener = (FacesListener)it.next();

            if (isStaticListener(facesListener, staticPhaseListeners))
            {
                return;
            }

            /*
            if (tagCreatedActionListenersSet != null &&
                tagCreatedActionListenersSet.contains(facesListener))
            {
                //Listener was created by a "f:action_listener" tag
                //and can automatically be restored by MinimizingStateRestorer.
                return;
            }
            */

            String listenerType;
            if (facesListener instanceof ActionListener)
            {
                listenerType = LISTENER_TYPE_ACTION;
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
                String paramValue = UIComponentUtils.getUniqueComponentId(facesContext, (UIComponent)facesListener);
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

}
