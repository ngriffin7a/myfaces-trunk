/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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

import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterException;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class StateRestorer
{

    public void restoreState(FacesContext facesContext) throws IOException
    {
        Tree requestTree = facesContext.getRequestTree();
        Map stateMap = restoreStateMap(facesContext);

        String previousTreeId = getStateParameter(stateMap, StateRenderer.TREE_ID_REQUEST_PARAM);
        if (previousTreeId != null && requestTree.getTreeId().equals(previousTreeId))
        {
            //restore tree
            Tree staticTree = JspInfo.getStaticTree(facesContext, requestTree.getTreeId());
            TreeCopier treeCopier = new TreeCopier(facesContext);
            treeCopier.setOverwriteAttributes(false);
            treeCopier.setOverwriteComponents(false);
            treeCopier.copyStaticTree(staticTree, requestTree);

            for (Iterator it = TreeUtils.treeIterator(requestTree); it.hasNext();)
            {
                UIComponent comp = (UIComponent)it.next();
                restoreComponent(facesContext, stateMap, comp);
            }

            //remember that the request tree is a restored tree
            requestTree.getRoot().setAttribute(StateRenderer.RESTORED_TREE_ATTR,
                                               Boolean.TRUE);
        }
        else
        {
            //remember that the request tree is a new tree
            requestTree.getRoot().setAttribute(StateRenderer.RESTORED_TREE_ATTR,
                                               Boolean.FALSE);
        }

        //decode model beans
        restoreModelValues(facesContext, stateMap);
    }


    protected Map restoreStateMap(FacesContext facesContext)
    {
        return facesContext.getServletRequest().getParameterMap();
    }

    protected String getStateParameter(Map stateMap, String attrName)
    {
        String[] values = (String[])stateMap.get(attrName);
        if (values == null || values.length == 0)
        {
            return null;
        }

        if (values.length == 1)
        {
            return values[0];
        }
        else //(values.length > 1)
        {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < values.length; i++)
            {
                if (i > 0)
                {
                    buf.append(',');
                }
                buf.append(values[i]);
            }
            return buf.toString();
        }
    }


    protected void restoreComponent(FacesContext facesContext, Map stateMap, UIComponent uiComponent)
        throws FacesException
    {
        //restore value:
        String savedValue = getStateParameter(stateMap,
                                              RequestParameterNames
                                                .getUIComponentStateParameterName(uiComponent,
                                                                                  MyFacesComponent.VALUE_ATTR));
        if (savedValue != null)
        {
            Converter conv = ConverterUtils.findConverter(facesContext, uiComponent);
            if (conv != null)
            {
                UIComponentUtils.convertAndSetValue(facesContext,
                                                             uiComponent,
                                                             savedValue,
                                                             conv,
                                                             false);    //no error message
            }
            else
            {
                Object obj = ConverterUtils.deserialize(savedValue);
                UIComponentUtils.setComponentValue(uiComponent, obj);
            }
        }

        //restore other attributes:
        for (Iterator it = stateMap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            String attrName = RequestParameterNames.restoreUIComponentStateParameterAttributeName(uiComponent,
                                                                            (String)entry.getKey());
            if (attrName != null && !attrName.equals(MyFacesComponent.VALUE_ATTR))
            {
                Object paramValue = entry.getValue();

                if (attrName.equals(MyFacesComponent.STRING_VALUE_ATTR))
                {
                    if (paramValue instanceof String[])
                    {
                        paramValue = StringArrayConverter.getAsString((String[])paramValue);
                    }
                    uiComponent.setAttribute(MyFacesComponent.STRING_VALUE_ATTR, paramValue);
                    uiComponent.setValue(null);
                    uiComponent.setValid(false);
                    continue;
                }

                Converter conv = null;
                try
                {
                    Class c = BeanUtils.getBeanPropertyType(uiComponent, attrName);
                    conv = ConverterUtils.findConverter(facesContext, c);
                }
                catch (FacesException e)
                {
                    LogUtil.getLogger().warning("Component " + uiComponent.getCompoundId() + " does not have a getter method for attribute '" + attrName + "'.");
                }

                Object objValue;
                if (conv != null)
                {
                    if (conv instanceof StringArrayConverter)
                    {
                        if (paramValue instanceof String[])
                        {
                            objValue = paramValue;
                        }
                        else
                        {
                            objValue = new String[] {(String)paramValue};
                        }
                    }
                    else
                    {
                        try
                        {
                            if (paramValue instanceof String[])
                            {
                                paramValue = StringArrayConverter.getAsString((String[])paramValue);
                            }
                            objValue = conv.getAsObject(facesContext,
                                                        facesContext.getResponseTree().getRoot(), //dummy UIComponent
                                                        (String)paramValue);
                        }
                        catch (ConverterException e)
                        {
                            throw new FacesException("Error restoring state of attribute '" + attrName + "' of component " + uiComponent.getCompoundId() + ": Converter exception!", e);
                        }
                    }
                }
                else
                {
                    if (paramValue instanceof String[])
                    {
                        paramValue = StringArrayConverter.getAsString((String[])paramValue);
                    }
                    objValue = ConverterUtils.deserialize((String)paramValue);
                }

                uiComponent.setAttribute(attrName, objValue);
            }
        }
    }


    protected void restoreModelValues(FacesContext facesContext, Map stateMap)
    {
        Tree staticTree = JspInfo.getStaticTree(facesContext,
                                                facesContext.getRequestTree().getTreeId());
        for (Iterator it = StateRenderer.getUISaveStateIterator(staticTree); it.hasNext();)
        {
            UIComponent uiSaveState = (UIComponent)it.next();
            restoreModelValue(facesContext, stateMap, uiSaveState);
        }
    }

    public void restoreModelValue(FacesContext facesContext, Map stateMap, UIComponent uiSaveState)
    {
        String modelRef = uiSaveState.getModelReference();
        if (modelRef == null)
        {
            throw new FacesException("UISaveState " + uiSaveState.getComponentId() + " has no model reference");
        }
        String paramName = RequestParameterNames.getModelValueStateParameterName(modelRef);
        String paramValue = getStateParameter(stateMap, paramName);
        if (paramValue != null)
        {
            Object propValue;
            Converter conv = ConverterUtils.findConverter(facesContext, uiSaveState);
            if (conv != null)
            {
                try
                {
                    propValue = conv.getAsObject(facesContext, uiSaveState, paramValue);
                }
                catch (ConverterException e)
                {
                    throw new FacesException("Error restoring state of model value " + modelRef + ": Converter exception!", e);
                }
            }
            else
            {
                propValue = ConverterUtils.deserialize(paramValue);
            }

            facesContext.setModelValue(modelRef, propValue);
        }
    }


}
