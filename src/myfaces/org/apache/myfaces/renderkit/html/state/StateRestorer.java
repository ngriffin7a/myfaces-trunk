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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterException;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspBeanInfo;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateRestorer
{
    private static final String STATE_MAP_REQUEST_ATTR = StateRestorer.class.getName() + ".STATE_MAP";

    public void restoreState(FacesContext facesContext) throws IOException
    {
        Tree requestTree = facesContext.getRequestTree();
        Map stateMap = getStateMap(facesContext);

        String previousTreeId = getStateParameter(stateMap,
                                                  StateRenderer.TREE_ID_REQUEST_PARAM);
        if (previousTreeId != null && requestTree.getTreeId().equals(previousTreeId))
        {
            //restore model beans and values:
            restoreModelValues(facesContext, stateMap);

            //restore tree
            Tree staticTree = JspInfo.getTree(facesContext, requestTree.getTreeId());
            TreeCopier treeCopier = new TreeCopier(facesContext);
            treeCopier.setOverwriteAttributes(false);
            treeCopier.setOverwriteComponents(false);
            treeCopier.copyTree(staticTree, requestTree);

            //restore component states
            for (Iterator it = TreeUtils.treeIterator(requestTree); it.hasNext();)
            {
                UIComponent comp = (UIComponent)it.next();
                restoreComponent(facesContext, stateMap, comp);
            }
        }

    }

    protected Map getStateMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest().getAttribute(STATE_MAP_REQUEST_ATTR);
        if (map == null)
        {
            map = restoreStateMap(facesContext);
            facesContext.getServletRequest().setAttribute(STATE_MAP_REQUEST_ATTR, map);
        }
        return map;
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


    /**
     * The model reference for each component is checked. If a model bean does not
     * yet exist it is automatically created in the defined scope.
     * @param facesContext
     * @param stateMap
     * @param uiComponent
     * @throws FacesException
     */
    protected void restoreComponent(FacesContext facesContext,
                                    Map stateMap,
                                    UIComponent uiComponent)
        throws FacesException
    {
        //Check model instance and create it, if it does not exist:
        String modelRef = uiComponent.getModelReference();
        if (modelRef != null)
        {
            checkModelInstance(facesContext, modelRef);
        }

        //restore value:
        String savedValue = getStateParameter(stateMap,
                                              RequestParameterNames
                                                .getUIComponentStateParameterName(uiComponent,
                                                                                  CommonComponentAttributes.VALUE_ATTR));
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
            if (attrName != null && !attrName.equals(CommonComponentAttributes.VALUE_ATTR))
            {
                Object paramValue = entry.getValue();

                if (attrName.equals(CommonComponentAttributes.STRING_VALUE_ATTR))
                {
                    if (paramValue instanceof String[])
                    {
                        paramValue = StringArrayConverter.getAsString((String[])paramValue);
                    }
                    uiComponent.setAttribute(CommonComponentAttributes.STRING_VALUE_ATTR, paramValue);
                    uiComponent.setValue(null);
                    uiComponent.setValid(false);
                    continue;
                }

                Converter conv = null;
                try
                {
                    Class c = BeanUtils.getBeanPropertyType(uiComponent, attrName);
                    conv = ConverterUtils.findConverter(facesContext.getServletContext(), c);
                }
                catch (IllegalArgumentException e)
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
        Iterator it = JspInfo.getUISaveStateComponents(facesContext,
                                                       facesContext.getRequestTree().getTreeId());
        while (it.hasNext())
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

        //Check model instance and create, if it does not exist:
        checkModelInstance(facesContext, modelRef);

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


    public void restoreComponentState(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        Map stateMap = getStateMap(facesContext);
        restoreComponent(facesContext, stateMap, uiComponent);
    }



    protected void checkModelInstance(FacesContext facesContext, String modelRef)
    {
        String modelId;
        int dot = modelRef.indexOf('.');
        if (dot == -1)    //TODO: Handle also ${} style!
        {
            modelId = modelRef;
        }
        else
        {
            modelId = modelRef.substring(0, dot);
        }

        JspBeanInfo jspBeanInfo = JspInfo.getJspBeanInfo(facesContext,
                                                         facesContext.getRequestTree().getTreeId(),
                                                         modelId);
        if (jspBeanInfo == null)
        {
            //There is no JspBeanInfo for that model bean
            // - either because of a typing error --> not our problem... :-)
            // - or the referenced model object is the variable of a DataRenderer
            // - or the object is created elsewhere (i.e. not via jsp:useBean)
            // --> so, we can do nothing other than ignore this issue
            return;
        }

        if (findModelBean(facesContext, modelId, jspBeanInfo.getScope()) == null)
        {
            //Create bean instance
            if (jspBeanInfo.getClassName() == null &&
                jspBeanInfo.getBeanName() == null)
            {
                throw new IllegalArgumentException("Incomplete JspBeanInfo for model " + modelId);
            }

            Object bean = jspBeanInfo.instantiate();
            switch (jspBeanInfo.getScope())
            {
                case PageContext.PAGE_SCOPE:
                    throw new IllegalArgumentException("Page scope is not supported!");

                case PageContext.REQUEST_SCOPE:
                    facesContext.getServletRequest().setAttribute(modelId, bean);
                    break;

                case PageContext.SESSION_SCOPE:
                    ServletRequest servletRequest = facesContext.getServletRequest();
                    if (servletRequest instanceof HttpServletRequest)
                    {
                        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
                        session.setAttribute(modelId, bean);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Session scope is not allowed because ServletRequest is not a HttpServletRequest!");
                    }
                    break;

                case PageContext.APPLICATION_SCOPE:
                    facesContext.getServletContext().setAttribute(modelId, bean);
                    break;

                default:
                    throw new IllegalArgumentException("Illegal scope " + jspBeanInfo.getScope());
            }
        }
    }

    private Object findModelBean(FacesContext facesContext, String id, int scope)
    {
        switch (scope)
        {
            case PageContext.PAGE_SCOPE:
                throw new IllegalArgumentException("Page scope is not supported!");

            case PageContext.REQUEST_SCOPE:
                return facesContext.getServletRequest().getAttribute(id);

            case PageContext.SESSION_SCOPE:
                ServletRequest servletRequest = facesContext.getServletRequest();
                if (servletRequest instanceof HttpServletRequest)
                {
                    HttpSession session = ((HttpServletRequest)servletRequest).getSession(false);
                    if (session == null)
                    {
                        return null;
                    }
                    else
                    {
                        return session.getAttribute(id);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("Session scope is not allowed because ServletRequest is not a HttpServletRequest!");
                }

            case PageContext.APPLICATION_SCOPE:
                return facesContext.getServletContext().getAttribute(id);

            default:
                throw new IllegalArgumentException("Unknown scope " + scope);
        }
    }

}
