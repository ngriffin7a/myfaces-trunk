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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.DataRenderer;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.SecretRenderer;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.taglib.core.ActionListenerTag;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedListener;
import javax.faces.tree.Tree;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.regex.Pattern;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateSaver
{
    public static final int URL_ENCODING = 1;
    public static final int HIDDEN_INPUTS_ENCODING = 2;

    private static final String URL_TOKEN = "__URLSTATE__";
    private static final String HIDDEN_INPUTS_TOKEN = "__HIDDENINPUTSSTATE__";

    private static final Pattern URL_TOKEN_PATTERN = Pattern.compile(URL_TOKEN);
    private static final Pattern HIDDEN_INPUTS_TOKEN_PATTERN = Pattern.compile(HIDDEN_INPUTS_TOKEN);

    private static final String STATE_MAP_REQUEST_ATTR = StateSaver.class.getName() + ".STATE_MAP";

    private static final Set IGNORE_ATTRIBUTES = new HashSet();
    static
    {
        IGNORE_ATTRIBUTES.add(CommonComponentAttributes.PARENT_ATTR);
        IGNORE_ATTRIBUTES.add(CommonComponentAttributes.COMPONENT_ID_ATTR);
        IGNORE_ATTRIBUTES.add(UIComponent.CLIENT_ID_ATTR);
    }

    public void init(FacesContext facesContext) throws IOException
    {
    }

    public void encodeState(FacesContext facesContext, int encodingType) throws IOException
    {
        if (MyFacesConfig.isStateEncodingOnTheFly())
        {
            Map stateMap = getStateMap(facesContext);
            ResponseWriter writer = facesContext.getResponseWriter();
            switch (encodingType)
            {
                case URL_ENCODING:
                    writeUrlState(writer, stateMap);
                    break;
                case HIDDEN_INPUTS_ENCODING:
                    writeHiddenInputsState(writer, stateMap);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal encoding type " + encodingType);
            }
        }
        else
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            switch (encodingType)
            {
                case URL_ENCODING:
                    writer.write(URL_TOKEN);
                    break;
                case HIDDEN_INPUTS_ENCODING:
                    writer.write(HIDDEN_INPUTS_TOKEN);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal encoding type " + encodingType);
            }
        }
    }

    public void release(FacesContext facesContext)
        throws IOException
    {
        if (MyFacesConfig.isStateEncodingOnTheFly())
        {
            return; //nothing to do
        }

        BodyContent bodyContent = (BodyContent)facesContext.getServletRequest()
                                    .getAttribute(StateRenderer.BODY_CONTENT_REQUEST_ATTR);
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }

        Map stateMap = getStateMap(facesContext);

        StringWriter urlWriter = new StringWriter();
        writeUrlState(urlWriter, stateMap);

        StringWriter hiddenInputsWriter = new StringWriter();
        writeHiddenInputsState(hiddenInputsWriter, stateMap);

        String body = bodyContent.getString();

        body = URL_TOKEN_PATTERN.matcher(body).replaceAll(urlWriter.toString());
        body = HIDDEN_INPUTS_TOKEN_PATTERN.matcher(body).replaceAll(hiddenInputsWriter.toString());

        bodyContent.getEnclosingWriter().write(body);
    }


    protected Map getStateMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest().getAttribute(STATE_MAP_REQUEST_ATTR);
        if (map == null)
        {
            map = new HashMap();
            saveResponseTreeId(facesContext, map);
            saveComponents(facesContext, map);
            saveModelValues(facesContext, map);
            saveLocale(facesContext, map);
            facesContext.getServletRequest()
                .setAttribute(STATE_MAP_REQUEST_ATTR, map);
        }
        return map;
    }


    protected void saveResponseTreeId(FacesContext facesContext, Map stateMap)
    {
        saveParameter(stateMap,
                      StateRenderer.TREE_ID_REQUEST_PARAM,
                      facesContext.getTree().getTreeId());
    }


    protected void saveComponents(FacesContext facesContext, Map stateMap)
    {
        LogUtil.getLogger().entering("StateSaver", "saveComponents");

        Iterator treeIt = TreeUtils.treeIterator(facesContext.getTree());
        while(treeIt.hasNext())
        {
            UIComponent comp = (UIComponent)treeIt.next();
            saveComponentAttributes(facesContext, stateMap, comp);
            saveListeners(facesContext, stateMap, comp);
        }

        LogUtil.getLogger().exiting("StateSaver", "saveComponents");
    }


    protected void saveComponentAttributes(FacesContext facesContext,
                                           Map stateMap,
                                           UIComponent uiComponent)
    {
        //Find corresponding component in parsed tree
        UIComponent parsedComp = findCorrespondingParsedComponent(facesContext,
                                                                  uiComponent);
        if (parsedComp == null)
        {
            LogUtil.getLogger().warning("Corresponding parsed component not found for component " + uiComponent.getClientId(facesContext));
        }

        //Remember all seen attributes of current component, so that
        //we can find "missing attributes" (i.e attributes that were set to null)
        //later
        Set visitedAttributes = new HashSet();

        //step through all attributes of component
        for (Iterator compIt = uiComponent.getAttributeNames(); compIt.hasNext();)
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
        // Save them by means of a special dummy value
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
            //If valid == false, we must save it, because StateRestorer
            //always assumes true as the default.
            //Since normally all common are valid, this can minimize
            //the number of saved attributes.
            saveComponentAttribute(facesContext,
                                   stateMap,
                                   uiComponent,
                                   CommonComponentAttributes.VALID_ATTR,
                                   Boolean.FALSE,
                                   null);
        }


        /*
        TODO: save currentValue and restore model value on restore ?
        Saving the currentValue for an UIOutput has side-effects!
        The value is restored, although it was null. currentValue then wont
        function properly, because it always would return the local value!

        if (!valueSeen
        && !isIgnoreValue(comp))
        {
        Object currValue = comp.currentValue(facesContext);
        if (currValue != null)
        {
        saveComponentValue(facesContext, stateMap, comp, currValue);
        }
        }
        */
    }


    protected UIComponent findCorrespondingParsedComponent(FacesContext facesContext,
                                                           UIComponent uiComponent)
    {
        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());
        return JspInfo.findComponentByUniqueId(parsedTree,
                                               JspInfo.getUniqueComponentId(uiComponent));
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
            //No need to save "valid" if true, because StateRestorer
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
                          StateRenderer.NULL_DUMMY_VALUE);
            return;
        }

        //convert attribute value to String
        String strValue;
        Converter conv = findConverterForAttribute(facesContext,
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
                LogUtil.getLogger().severe("Value of attribute " + attrName + " will be lost, because of converter exception saving state of component " + uiComponent.getClientId(facesContext) + ".");
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
                    strValue = ConverterUtils.serialize(attrValue);
                }
                catch (FacesException e)
                {
                    LogUtil.getLogger().severe("Value of attribute " + attrName + " of component " + uiComponent.getClientId(facesContext) + " will be lost, because of exception during serialization: " + e.getMessage());
                    return;
                }
            }
            else
            {
                LogUtil.getLogger().severe("Value of attribute " + attrName + " of component " + uiComponent.getClientId(facesContext) + " will be lost, because it is of non-serializable type: " + attrValue.getClass().getName());
                return;
            }
        }

        saveParameter(stateMap,
                      RequestParameterNames.getUIComponentStateParameterName(facesContext,
                                                                             uiComponent,
                                                                             attrName),
                      strValue);
    }


    protected Converter findConverterForAttribute(FacesContext facesContext,
                                                  UIComponent uiComponent,
                                                  String attrName)
    {
        if (attrName.equals(CommonComponentAttributes.VALUE_ATTR))
        {
            return ConverterUtils.findValueConverter(facesContext,
                                                     uiComponent);
        }
        else
        {
            return ConverterUtils.findAttributeConverter(facesContext,
                                                         uiComponent,
                                                         attrName);
        }
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
                paramValue = ConverterUtils.serialize(propValue);
            }

            saveParameter(stateMap,
                          RequestParameterNames.getModelValueStateParameterName(modelRef),
                          paramValue);
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


    protected void writeUrlState(Writer writer, Map stateMap) throws IOException
    {
        for (Iterator entries = stateMap.entrySet().iterator(); entries.hasNext();)
        {
            Map.Entry entry = (Map.Entry)entries.next();

            writer.write('&');  //we assume that there were previous parameters
            writer.write((String)entry.getKey());
            writer.write('=');
            writer.write(HTMLRenderer.urlEncode((String)entry.getValue()));
        }
    }

    protected void writeHiddenInputsState(Writer writer, Map stateMap) throws IOException
    {
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
        if (attrName.startsWith("net.sourceforge.myfaces."))
        {
            return true;
        }
        else if (attrName.startsWith("javax."))
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
            UIComponent parent = comp.getParent();
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
            String localeValue = locale.getLanguage() + StateRenderer.LOCALE_REQUEST_PARAM_DELIMITER +
                                 locale.getCountry() + StateRenderer.LOCALE_REQUEST_PARAM_DELIMITER +
                                 locale.getVariant();
            saveParameter(stateMap, StateRenderer.LOCALE_REQUEST_PARAM, localeValue);
        }
    }


    protected void saveListeners(FacesContext facesContext,
                                 Map stateMap,
                                 UIComponent uiComponent)
    {
        List[] listeners = UIComponentUtils.getListeners(uiComponent);
        if (listeners != null)
        {
            Set tagCreatedActionListenersSet
                = (Set)facesContext.getServletRequest()
                    .getAttribute(ActionListenerTag.TAG_CREATED_ACTION_LISTENERS_SET_ATTR);
            for (Iterator it = PhaseId.VALUES.iterator(); it.hasNext();)
            {
                PhaseId phaseId = (PhaseId)it.next();
                List phaseListeners = listeners[phaseId.getOrdinal()];
                if (phaseListeners != null)
                {
                    savePhaseListeners(facesContext,
                                       stateMap,
                                       uiComponent,
                                       tagCreatedActionListenersSet,
                                       phaseListeners);
                }
            }
        }
    }

    protected void savePhaseListeners(FacesContext facesContext,
                                      Map stateMap,
                                      UIComponent uiComponent,
                                      Set tagCreatedActionListenersSet,
                                      List phaseListeners)
    {
        for (Iterator it = phaseListeners.iterator(); it.hasNext();)
        {
            FacesListener facesListener = (FacesListener)it.next();

            if (tagCreatedActionListenersSet != null &&
                tagCreatedActionListenersSet.contains(facesListener))
            {
                //Listener was created by a "f:action_listener" tag
                //and can automatically be restored by StateRestorer.
                return;
            }

            String listenerType;
            if (facesListener instanceof ActionListener)
            {
                listenerType = StateRenderer.LISTENER_TYPE_ACTION;
            }
            else if (facesListener instanceof ValueChangedListener)
            {
                listenerType = StateRenderer.LISTENER_TYPE_VALUE_CHANGED;
            }
            else
            {
                //TODO: Support for common Listeners: find "addFooListener" method via reflection...
                LogUtil.getLogger().warning("Unsupported Listener type " + facesListener.getClass().getName());
                continue;
            }

            if (facesListener instanceof UIComponent)
            {
                //Listener is a component, so we only need to save the clientId
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
                String paramValue = ConverterUtils.serialize(facesListener);
                saveParameter(stateMap, paramName, paramValue);
            }
        }

    }


}
