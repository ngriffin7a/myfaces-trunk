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
package net.sourceforge.myfaces.renderkit.html.state.server;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspBeanInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfoUtils;
import net.sourceforge.myfaces.renderkit.html.state.ModelValueEntry;
import net.sourceforge.myfaces.renderkit.html.state.StateRestorer;
import net.sourceforge.myfaces.util.FacesUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Collection;
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
    //~ Static fields/initializers -----------------------------------------------------------------

    protected static final String PREVIOUS_TREE_REQUEST_ATTR =
        HTTPSessionStateRestorer.class.getName() + ".PREVIOUS_TREE";

    //~ Methods ------------------------------------------------------------------------------------

    public Tree getPreviousTree(FacesContext facesContext)
    {
        return (Tree) ((ServletRequest) facesContext.getExternalContext().getRequest())
                .getAttribute(PREVIOUS_TREE_REQUEST_ATTR);
    }

    public void restoreState(FacesContext facesContext)
    throws IOException
    {
        Map sessionMap = FacesUtils.getSessionMap(facesContext);

        if (sessionMap == null)
        {
            return;
        }

        //Locale
        Locale locale = (Locale) sessionMap.get(HTTPSessionStateSaver.LOCALE_SESSION_ATTR);

        if (locale != null)
        {
            facesContext.setLocale(locale);
            sessionMap.remove(HTTPSessionStateSaver.LOCALE_SESSION_ATTR);
        }

        //Tree
        Tree currentTree = facesContext.getTree();
        Tree savedTree = (Tree) sessionMap.get(HTTPSessionStateSaver.TREE_SESSION_ATTR);

        if (savedTree != null)
        {
            //autocreate request scope beans
            if (
                MyFacesConfig.isAutoCreateRequestScopeBeans(
                            ((ServletContext) facesContext.getExternalContext().getContext())))
            {
                Iterator it =
                    JspInfo.getJspBeanInfos(
                        facesContext,
                        facesContext.getTree().getTreeId());

                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    JspInfoUtils.checkModelInstance(facesContext, (JspBeanInfo) entry.getValue());
                }
            }

            if (savedTree.getTreeId().equals(currentTree.getTreeId()))
            {
                //same treeId, set restored tree as new tree in context
                facesContext.setTree(savedTree);

                //Model values
                restoreModelValues(facesContext, sessionMap, false);
            }
            else
            {
                //Global model values
                restoreModelValues(facesContext, sessionMap, true);
            }

            sessionMap.remove(HTTPSessionStateSaver.TREE_SESSION_ATTR);
            FacesUtils.getRequestMap(facesContext).put(PREVIOUS_TREE_REQUEST_ATTR, savedTree);
        }
    }

    private void restoreModelValues(FacesContext facesContext, Map sessionMap, boolean onlyGlobal)
    {
        if (
            !MyFacesConfig.isDisableJspParser(
                        ((ServletContext) facesContext.getExternalContext().getContext())))
        {
            Collection modelValuesColl =
                (Collection) sessionMap.get(HTTPSessionStateSaver.MODEL_VALUES_COLL_SESSION_ATTR);

            if (modelValuesColl != null)
            {
                for (Iterator it = modelValuesColl.iterator(); it.hasNext();)
                {
                    ModelValueEntry entry = (ModelValueEntry) it.next();

                    if (!onlyGlobal || entry.isGlobal())
                    {
                        String modelRef = entry.getModelReference();
                        JspInfoUtils.checkModelInstance(facesContext, modelRef);

                        ApplicationFactory af =
                            (ApplicationFactory) FactoryFinder.getFactory(
                                FactoryFinder.APPLICATION_FACTORY);
                        ValueBinding       vb = af.getApplication().getValueBinding(modelRef);
                        vb.setValue(
                            facesContext,
                            entry.getValue());
                    }
                }

                sessionMap.remove(HTTPSessionStateSaver.MODEL_VALUES_COLL_SESSION_ATTR);
            }
        }
    }

    /*
    public void restoreComponentState(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
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
                if (!attrName.equals(CommonComponentProperties.PARENT_ATTR))
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
            uiComponent.clearChildren();

            //copy children
            for (int i = 0; i < find.getChildCount(); i++)
            {
                uiComponent.addChild(find.getChild(i));
            }
        }
    }
    */
}
