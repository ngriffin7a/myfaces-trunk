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
import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterException;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.SecretRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
        IGNORE_ATTRIBUTES.add(MyFacesComponent.PARENT_ATTR);
        IGNORE_ATTRIBUTES.add(MyFacesComponent.VALID_ATTR);
    }


    public void init(FacesContext facesContext) throws IOException
    {
        /*
        if (MyFacesConfig.isStateEncodingOnTheFly())
        {
            //state encoding on the fly (without tokens)
            //means that we must prebuild a new response tree from the parsed JspInfo
            String requestTreeId = facesContext.getRequestTree().getTreeId();
            Tree responseTree = facesContext.getResponseTree();
            String responseTreeId = responseTree.getTreeId();
            Boolean restored = (Boolean)responseTree.getRoot()
                                    .getAttribute(StateRenderer.RESTORED_TREE_ATTR);
            if (!requestTreeId.equals(responseTreeId) ||
                (restored == null || !restored.booleanValue()))
            {
                //we jump to a new tree, so let's prebuild the response tree...
                Tree staticTree = JspInfo.getStaticTree(facesContext,
                                                        responseTreeId);
                TreeCopier treeCopier = new TreeCopier(facesContext);
                treeCopier.setOverwriteAttributes(true);
                treeCopier.setOverwriteComponents(true);
                treeCopier.copyTree(staticTree, facesContext.getResponseTree());
            }
        }
        else
        {
            //state encoding with tokens
            //means we can create the components on the fly
            //BUT, what about components that rely on underlying child components?
            //e.g. a UIParameter under a Hyperlink-UICommand would not exist when the
            // HyperlinkRenderer would need it already during encodeBegin !?
            //So, should we always prebuild the tree from the static tree before rendering?
        }
        */
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
            facesContext.getServletRequest().setAttribute(STATE_MAP_REQUEST_ATTR, map);
        }
        return map;
    }


    protected void saveResponseTreeId(FacesContext facesContext, Map stateMap)
    {
        saveParameter(stateMap,
                      StateRenderer.TREE_ID_REQUEST_PARAM,
                      facesContext.getResponseTree().getTreeId());
    }


    protected void saveComponents(FacesContext facesContext, Map stateMap)
    {
        Iterator treeIt = TreeUtils.treeIterator(facesContext.getResponseTree());
        while (treeIt.hasNext())
        {
            UIComponent comp = (UIComponent)treeIt.next();
            boolean valueSeen = false;

            for (Iterator compIt = comp.getAttributeNames(); compIt.hasNext();)
            {
                String attrName = (String)compIt.next();
                Object attrValue = comp.getAttribute(attrName);
                if (attrValue != null && !isIgnoreAttribute(attrName))
                {
                    if (attrName.equals(MyFacesComponent.VALUE_ATTR) ||
                        attrName.equals(MyFacesComponent.STRING_VALUE_ATTR))
                    {
                        valueSeen = true;

                        if (isIgnoreValue(comp))
                        {
                            //ignore value
                            continue;
                        }

                        if (attrName.equals(MyFacesComponent.VALUE_ATTR))
                        {
                            saveComponentValue(facesContext, stateMap, comp, attrValue);
                        }
                        else //(attrName.equals(MyFacesComponent.STRING_VALUE_ATTR))
                        {
                            saveParameter(stateMap,
                                          RequestParameterNames
                                            .getUIComponentStateParameterName(comp,
                                                                              MyFacesComponent.STRING_VALUE_ATTR),
                                          (String)attrValue);
                        }
                    }
                    else
                    {
                        saveComponentAttribute(facesContext, stateMap, comp,
                                               attrName, attrValue);
                    }
                }
            }

            if (!valueSeen
                && MyFacesConfig.isAlwaysSaveComponentValue()
                && !isIgnoreValue(comp))
            {
                Object currValue = comp.currentValue(facesContext);
                if (currValue != null)
                {
                    saveComponentValue(facesContext, stateMap, comp, currValue);
                }
            }

        }

    }


    protected void saveComponentValue(FacesContext facesContext,
                                      Map stateMap,
                                      UIComponent uiComponent,
                                      Object attrValue)
    {
        Tree parsedTree = JspInfo.getStaticTree(facesContext,
                                                facesContext.getResponseTree().getTreeId());
        UIComponent parsedComp = null;
        try
        {
            parsedComp = parsedTree.getRoot().findComponent(uiComponent.getCompoundId());
        }
        catch (IllegalArgumentException e)
        {
            parsedComp = null;
        }
        if (parsedComp != null)
        {
            Object parsedValue = parsedComp.getAttribute(MyFacesComponent.VALUE_ATTR);
            if (parsedValue != null && parsedValue.equals(attrValue))
            {
                //current value identical to hardcoded value
                return;
            }
        }

        String strValue;
        Converter conv = ConverterUtils.findConverter(facesContext, uiComponent);
        if (conv != null)
        {
            try
            {
                strValue = conv.getAsString(facesContext, uiComponent, attrValue);
            }
            catch (ConverterException e)
            {
                throw new FacesException("Error saving state of value of component " + uiComponent.getCompoundId() + ": Converter exception!", e);
            }
        }
        else
        {
            if (attrValue instanceof Serializable)
            {
                strValue = ConverterUtils.serialize(attrValue);
            }
            else
            {
                LogUtil.getLogger().warning("Value of component " + uiComponent.getCompoundId() + " is not serializable - cannot save state!");
                return;
            }
        }

        saveParameter(stateMap,
                      RequestParameterNames.getUIComponentStateParameterName(uiComponent,
                                                                             MyFacesComponent.VALUE_ATTR),
                      strValue);
    }


    protected void saveComponentAttribute(FacesContext facesContext,
                                          Map stateMap,
                                          UIComponent uiComponent,
                                          String attrName,
                                          Object attrValue)
    {
        Tree parsedTree = JspInfo.getStaticTree(facesContext,
                                                facesContext.getResponseTree().getTreeId());
        UIComponent parsedComp = null;
        try
        {
            parsedComp = parsedTree.getRoot().findComponent(uiComponent.getCompoundId());
        }
        catch (IllegalArgumentException e)
        {
            parsedComp = null;
        }
        if (parsedComp != null)
        {
            Object parsedValue = parsedComp.getAttribute(attrName);
            if (parsedValue != null && parsedValue.equals(attrValue))
            {
                //current attribute identical to hardcoded attribute
                return;
            }
        }

        Converter conv = null;
        try
        {
            Class c = BeanUtils.getBeanPropertyType(uiComponent, attrName);
            if (c != null)
            {
                conv = ConverterUtils.findConverter(facesContext.getServletContext(), c);
            }
        }
        catch (IllegalArgumentException e)
        {
            //probably not a component attribute but a render dependent attribute
            LogUtil.getLogger().info("Component " + uiComponent.getCompoundId() + " does not have a getter method for attribute '" + attrName + "', assuming renderer dependent attribute.");
        }

        String strValue;
        if (conv != null)
        {
            try
            {
                strValue = conv.getAsString(facesContext,
                                            facesContext.getResponseTree().getRoot(), //dummy UIComponent
                                            attrValue);
            }
            catch (ConverterException e)
            {
                throw new FacesException("Error saving state of attribute '" + attrName + "' of component " + uiComponent.getCompoundId() + ": Converter exception!", e);
            }
        }
        else
        {
            if (attrValue instanceof Serializable)
            {
                strValue = ConverterUtils.serialize(attrValue);
            }
            else
            {
                LogUtil.getLogger().warning("Attribute '" + attrName + "' of component " + uiComponent.getCompoundId() + " is not serializable - cannot save state!");
                return;
            }

        }

        saveParameter(stateMap,
                      RequestParameterNames.getUIComponentStateParameterName(uiComponent, attrName),
                      strValue);
    }



    protected void saveModelValues(FacesContext facesContext, Map stateMap)
    {
        for (Iterator it = StateRenderer.getUISaveStateIterator(facesContext.getResponseTree()); it.hasNext();)
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
            Converter conv = ConverterUtils.findConverter(facesContext, uiSaveState);
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
            return IGNORE_ATTRIBUTES.contains(attrName);
        }
    }

    protected boolean isIgnoreValue(UIComponent comp)
    {
        //Secret with redisplay == false?
        if (comp.getComponentType().equals(SecretRenderer.TYPE))
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
        Boolean trans = (Boolean)comp.getAttribute(MyFacesComponent.TRANSIENT_ATTR);
        if (trans != null && trans.booleanValue())
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
                    String var = (String)parent.getAttribute(UIPanel.VAR_ATTR);
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

}
