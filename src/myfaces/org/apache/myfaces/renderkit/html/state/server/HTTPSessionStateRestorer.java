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
package net.sourceforge.myfaces.renderkit.html.state.server;

import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfoUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspBeanInfo;
import net.sourceforge.myfaces.renderkit.html.state.StateRestorer;
import net.sourceforge.myfaces.MyFacesConfig;

import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTTPSessionStateRestorer
    implements StateRestorer
{
    protected static final String PREVIOUS_TREE_REQUEST_ATTR
        = HTTPSessionStateRestorer.class.getName() + ".PREVIOUS_TREE";

    public void restoreState(FacesContext facesContext) throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();

        //get Session
        HttpSession session = request.getSession(false);
        if (session == null)
        {
            return;
        }

        //Locale
        Locale locale = (Locale)session.getAttribute(HTTPSessionStateSaver.LOCALE_SESSION_ATTR);
        if (locale != null)
        {
            facesContext.setLocale(locale);
            session.removeAttribute(HTTPSessionStateSaver.LOCALE_SESSION_ATTR);
        }

        //Tree
        Tree currentTree = facesContext.getTree();
        Tree savedTree = (Tree)session.getAttribute(HTTPSessionStateSaver.TREE_SESSION_ATTR);
        if (savedTree != null)
        {
            if (savedTree.getTreeId().equals(currentTree.getTreeId()))
            {
                //same treeId, set restored tree as new tree in context
                facesContext.setTree(savedTree);
            }
            session.removeAttribute(HTTPSessionStateSaver.TREE_SESSION_ATTR);
            facesContext.getServletRequest().setAttribute(PREVIOUS_TREE_REQUEST_ATTR,
                                                          savedTree);

            //autocreate request scope beans
            if (MyFacesConfig.isAutoCreateRequestScopeBeans(facesContext.getServletContext()))
            {
                Iterator it = JspInfo.getJspBeanInfos(facesContext,
                                                      facesContext.getTree().getTreeId());
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry)it.next();
                    JspInfoUtils.checkModelInstance(facesContext,
                                                    (JspBeanInfo)entry.getValue());
                }
            }
        }

        //Model values
        restoreModelValues(facesContext, session);
    }


    private void restoreModelValues(FacesContext facesContext,
                                    HttpSession session)
    {
        Map modelValuesMap = (Map)session.getAttribute(HTTPSessionStateSaver.MODEL_VALUES_MAP_SESSION_ATTR);
        if (modelValuesMap != null)
        {
            for (Iterator it = modelValuesMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                String modelRef = (String)entry.getKey();
                JspInfoUtils.checkModelInstance(facesContext, modelRef);
                facesContext.setModelValue(modelRef, entry.getValue());
            }
            session.removeAttribute(HTTPSessionStateSaver.MODEL_VALUES_MAP_SESSION_ATTR);
        }
    }


    public Tree getPreviousTree(FacesContext facesContext)
    {
        return (Tree)facesContext.getServletRequest()
                        .getAttribute(PREVIOUS_TREE_REQUEST_ATTR);
    }



    /*
    public void restoreComponentState(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        HttpSession session = request.getSession(false);
        if (session == null)
        {
            return;
        }
        Tree savedTree = (Tree)session.getAttribute(HTTPSessionStateSaver.TREE_SESSION_ATTR);
        if (savedTree == null || savedTree == facesContext.getTree())
        {
            return;
        }

        String uniqueId = JspInfo.getUniqueComponentId(uiComponent);
        UIComponent find = JspInfo.findComponentByUniqueId(savedTree,
                                                           uniqueId);
        if (find != null)
        {
            //remove null attributes
            for (Iterator it = uiComponent.getAttributeNames(); it.hasNext();)
            {
                String attrName = (String)it.next();
                if (find.getAttribute(attrName) == null)
                {
                    uiComponent.setAttribute(attrName, null);
                }
            }

            //copy attributes
            for (Iterator it = find.getAttributeNames(); it.hasNext();)
            {
                String attrName = (String)it.next();
                if (!attrName.equals(CommonComponentAttributes.PARENT_ATTR))
                {
                    PropertyDescriptor pd = BeanUtils.findPropertyDescriptor(uiComponent, attrName);
                    if (pd != null)
                    {
                        BeanUtils.setBeanPropertyValue(uiComponent, pd, find.getAttribute(attrName));
                    }
                    else
                    {
                        uiComponent.setAttribute(attrName, find.getAttribute(attrName));
                    }
                }
            }

            //remove children
            for (int i = 0; i < uiComponent.getChildCount(); i++)
            {
                uiComponent.removeChild(0);
            }

            //copy children
            for (int i = 0; i < find.getChildCount(); i++)
            {
                uiComponent.addChild(find.getChild(i));
            }
        }
    }
    */


}
