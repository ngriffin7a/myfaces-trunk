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
package net.sourceforge.myfaces.util;

import net.sourceforge.myfaces.el.VariableResolverImpl;

import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;


/**
 * Utility class for access to JavaServer Faces classes.
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesUtils
{
    //~ Static fields/initializers -----------------------------------------------------------------

    protected static final VariableResolver _variableResolver = new VariableResolverImpl();

    //~ Methods ------------------------------------------------------------------------------------

    public static Application getApplication()
    {
        return ((ApplicationFactory) FactoryFinder.getFactory(
            "javax.faces.application.ApplicationFactory")).getApplication();
    }

    public static String getPathInfo(FacesContext ctx)
    {
        return ctx.getExternalContext().getRequestPathInfo();
    }

    public static String getPathInfo()
    {
        return getPathInfo(FacesContext.getCurrentInstance());
    }

    public static Renderer getRenderer(String rendererType)
    {
        return getRenderer(FacesContext.getCurrentInstance(), rendererType);
    }

    public static Renderer getRenderer(FacesContext ctx, String rendererType)
    {
        return ((RenderKitFactory) FactoryFinder.getFactory("javax.faces.render.RenderKitFactory")).getRenderKit(
            ctx.getViewRoot().getRenderKitId()).getRenderer(rendererType);
    }

    public static Map getRequestMap()
    {
        return getRequestMap(FacesContext.getCurrentInstance());
    }

    public static Map getRequestMap(FacesContext ctx)
    {
        return ctx.getExternalContext().getRequestMap();
    }

    public static Map getRequestParameterMap()
    {
        return getRequestParameterMap(FacesContext.getCurrentInstance());
    }

    public static Map getRequestParameterMap(FacesContext ctx)
    {
        return ctx.getExternalContext().getRequestParameterMap();
    }

    public static Object getSession(boolean create)
    {
        return getSession(FacesContext.getCurrentInstance(), create);
    }

    public static Object getSession(FacesContext ctx, boolean create)
    {
        return ctx.getExternalContext().getSession(create);
    }

    public static Map getSessionMap()
    {
        return getSessionMap(FacesContext.getCurrentInstance());
    }

    public static Map getSessionMap(FacesContext ctx)
    {
        return ctx.getExternalContext().getSessionMap();
    }

    public static boolean isValueBinding(String s)
    {
        s = s.trim();
        return s.startsWith("#{") && s.endsWith("}");
    }

    public static void setValueRef(Application app, String valueRef, Object value)
    {
        app.createValueBinding(valueRef).setValue(FacesContext.getCurrentInstance(), value);
    }

    public static void setValueRef(FacesContext ctx, String valueRef, Object value)
    {
        ctx.getApplication().createValueBinding(valueRef).setValue(ctx, value);
    }

    public static void setValueRef(String valueRef, Object value)
    {
        createValueBinding(valueRef).setValue(FacesContext.getCurrentInstance(), value);
    }

    public static Object getValueRef(Application app, String valueRef)
    {
        return app.createValueBinding(valueRef).getValue(FacesContext.getCurrentInstance());
    }

    public static Object getValueRef(FacesContext ctx, String valueRef)
    {
        return createValueBinding(ctx, valueRef).getValue(ctx);
    }

    public static Object getValueRef(String valueRef)
    {
        return createValueBinding(valueRef).getValue(FacesContext.getCurrentInstance());
    }

    //FIXME

    /*
    public static Tree getTree(String treeId)
    {
        return getTreeFactory().getViewRoot(FacesContext.getCurrentInstance(), treeId);
    }

    public static Tree getTree(FacesContext ctx, String treeId)
    {
        return getTreeFactory().getViewRoot(ctx, treeId);
    }

    public static Tree getTree(FacesContext ctx, TreeFactory treeFactory, String treeId)
    {
        return treeFactory.getViewRoot(ctx, treeId);
    }

    public static Tree getTree(TreeFactory treeFactory, String treeId)
    {
        return treeFactory.getViewRoot(FacesContext.getCurrentInstance(), treeId);
    }


    public static TreeFactory getTreeFactory()
    {
        return (TreeFactory) FactoryFinder.getFactory("javax.faces.tree.TreeFactory");
    }
    */
    public static ValueBinding createValueBinding(String valueRef)
    {
        return getApplication().createValueBinding(valueRef);
    }

    public static ValueBinding createValueBinding(FacesContext ctx, String valueRef)
    {
        return ctx.getApplication().createValueBinding(valueRef);
    }

    public static Object resolveVariable(String name)
    {
        return _variableResolver.resolveVariable(FacesContext.getCurrentInstance(), name);
    }

    public static Object resolveVariable(FacesContext ctx, String name)
    {
        return _variableResolver.resolveVariable(ctx, name);
    }
}
