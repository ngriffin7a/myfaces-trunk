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
package net.sourceforge.myfaces.application.cbp;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * @author Dimitry D'hondt
 * @author Anton Koinov
 * 
 * Utilities for the ViewHandler and StateManager selectors.
 */
public class SelectorUtils
{
    /**
     * Determines the package where the descriptor classes are located.
     * 
     * @param context
     * @return
     */
    public static String getDescriptorPackage(FacesContext context)
    {
        String ret = ""; // Default package.

        // Try to determine descriptor package...
        String temp = context.getExternalContext().getInitParameter(
            "net.sourceforge.smile.descriptor.package");
        if (temp != null)
        {
            ret = temp;
            if (!ret.endsWith("."))
            {
                ret = ret + ".";
            }
        }

        return ret;
    }

    /**
     * Determines the postfix for the descriptor class.
     * 
     * @param context
     * @return
     */
    public static String getDescriptorPostfix(FacesContext context)
    {
        // Try to determine descriptor package...
        String pacakge = context.getExternalContext().getInitParameter(
            "net.sourceforge.smile.descriptor.postfix");
        return (pacakge != null) ? pacakge : "";
    }

    /**
     * This operation determines if the supplied viewId maps to a CBP page.
     * This is basically done by determining if any of the CBT packages contain
     * classes that correspond to the name of the supplied viewId.
     * 
     * @param viewId
     * @return
     */
    public static boolean isPageJSP(FacesContext facesContext, String viewId)
    {
        return (viewId != null)
            && getDescriptorClassNameForViewId(facesContext, viewId) == null;
    }

    /**
     * This operation determines if the supplied viewRoot maps to a CBP page.
     * This is basically done by determining if any of the CBT packages contain
     * classes that correspond to the name of the supplied viewId.
     * 
     * @param viewRoot
     * @return
     */
    public static boolean isPageJSP(FacesContext facesContext, UIViewRoot viewRoot)
    {
        return (viewRoot != null)
            ? isPageJSP(facesContext, viewRoot.getViewId()) : false;
    }

    /**
     * This operation looks at the current component tree, and determines if it
     * is a CBP page, or JSP page.
     * 
     * @return
     */
    public static boolean isCurrentPageJSP(FacesContext facesContext)
    {
        UIViewRoot viewRoot = getCurrentViewRoot(facesContext);
        return (viewRoot != null)
            ? isPageJSP(facesContext, viewRoot) : false;
    }

    /**
     * This function is responsible for finding the descriptor class for a
     * given viewId. The policy may change over time, like supporting multiple
     * packages or more flexible mapping of viewIds to descriptor classes,etc..
     * 
     * @param viewId
     * @return the descriptor class for a given viewId, or null if no
     *         descriptor found.
     */
    public static Class getDescriptorClassNameForViewId(FacesContext facesContext, String viewId)
    {
        // TODO : We should implement a configurable scheme with more than one
        // package.
        if (viewId.endsWith(".jsf") || viewId.endsWith(".jsp"))
        {
            int startIndex = viewId.charAt(0) == '/' ? 1 : 0;
            String shortClassName = viewId.substring(startIndex, viewId.length() - 4);
            String className = getDescriptorPackage(facesContext) + shortClassName
                + getDescriptorPostfix(facesContext);
            
            try
            {
                return ClassUtils.classForName_(className);
            }
            catch (ClassNotFoundException e)
            {
                return null;
            }
        }

        return null;
    }


    /**
     * Determines the current ViewRoot.
     * 
     * @return
     */
    public static UIViewRoot getCurrentViewRoot(FacesContext facesContext)
    {
        return (UIViewRoot) facesContext.getExternalContext().getSessionMap()
            .get(net.sourceforge.myfaces.application.cbp.CbpStateManagerImpl.SESSION_KEY_CURRENT_VIEW);
    }
}
