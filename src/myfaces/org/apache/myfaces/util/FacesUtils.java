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
package net.sourceforge.myfaces.util;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.MessageResources;
import javax.faces.el.ValueBinding;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Utility class for access to JavaServer Faces classes.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesUtils {
    //~ Methods ----------------------------------------------------------------

    public static Application getApplication() {
        return ((ApplicationFactory)FactoryFinder.getFactory(
            "javax.faces.application.ApplicationFactory")).getApplication();
    }

    public static MessageResources getMessageResources(String name) {
        return getApplication().getMessageResources(name);
    }

    public static HttpServletRequest getRequest() {
        return getRequest(FacesContext.getCurrentInstance());
    }

    public static HttpServletRequest getRequest(FacesContext ctx) {
        return (HttpServletRequest)ctx.getExternalContext().getRequest();
    }

    public static HttpSession getSession() {
        return getSession(FacesContext.getCurrentInstance());
    }

    public static HttpSession getSession(FacesContext ctx) {
        return getRequest(ctx).getSession();
    }

    public static Tree getTree(String treeId) {
        return getTreeFactory().getTree(
            FacesContext.getCurrentInstance(), treeId);
    }

    public static Tree getTree(FacesContext ctx, String treeId) {
        return getTreeFactory().getTree(ctx, treeId);
    }

    public static Tree getTree(
        FacesContext ctx, TreeFactory treeFactory, String treeId) {
        return treeFactory.getTree(ctx, treeId);
    }

    public static Tree getTree(TreeFactory treeFactory, String treeId) {
        return treeFactory.getTree(FacesContext.getCurrentInstance(), treeId);
    }

    public static TreeFactory getTreeFactory() {
        return (TreeFactory)FactoryFinder.getFactory(
            "javax.faces.tree.TreeFactory");
    }

    public static ValueBinding getValueBinding(String valueRef) {
        return getApplication().getValueBinding(valueRef);
    }

    public static void setValueRef(
        Application app, FacesContext ctx, String valueRef, Object value) {
        app.getValueBinding(valueRef).setValue(ctx, value);
    }

    public static void setValueRef(
        Application app, String valueRef, Object value) {
        app.getValueBinding(valueRef).setValue(
            FacesContext.getCurrentInstance(), value);
    }

    public static void setValueRef(
        FacesContext ctx, String valueRef, Object value) {
        getApplication().getValueBinding(valueRef).setValue(ctx, value);
    }

    public static void setValueRef(String valueRef, Object value) {
        getValueBinding(valueRef).setValue(
            FacesContext.getCurrentInstance(), value);
    }

    public static Object getValueRef(
        Application app, FacesContext ctx, String valueRef) {
        return app.getValueBinding(valueRef).getValue(ctx);
    }

    public static Object getValueRef(Application app, String valueRef) {
        return app.getValueBinding(valueRef).getValue(
            FacesContext.getCurrentInstance());
    }

    public static Object getValueRef(FacesContext ctx, String valueRef) {
        return getValueBinding(valueRef).getValue(ctx);
    }

    public static Object getValueRef(String valueRef) {
        return getValueBinding(valueRef).getValue(
            FacesContext.getCurrentInstance());
    }
}
