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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.component.UIComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.webapp.FacesTag;
import javax.faces.FacesException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ActionListenerTag
    extends TagSupport
{
    private String _type = null;

    public ActionListenerTag()
    {
    }

    public void setType(String type)
    {
        _type = type;
    }


    public int doStartTag() throws JspException
    {
        //Find parent FacesTag
        Tag parent = getParent();
        while (parent != null && !(parent instanceof FacesTag))
        {
            parent = parent.getParent();
        }
        if (parent == null)
        {
            throw new JspException("action_listener has no FacesTag ancestor");
        }

        FacesTag facesTag = (FacesTag)parent;

        if (facesTag.getCreated())
        {
            //Component was just created, so we add the Listener
            UIComponent component = facesTag.getComponent();
            addActionListener(component, _type);
        }

        return Tag.SKIP_BODY;
    }



    private static final String ADD_ACTION_LISTENER_METHOD_NAME = "addActionListener";
    private static final Class[] ADD_ACTION_LISTENER_METHOD_PARAMS = new Class[]{ActionListener.class};

    public static void addActionListener(UIComponent uiComponent,
                                         String type)
        throws FacesException
    {
        try
        {
            Method method = uiComponent.getClass().getMethod(ADD_ACTION_LISTENER_METHOD_NAME,
                                                             ADD_ACTION_LISTENER_METHOD_PARAMS);
            if (method == null)
            {
                LogUtil.getLogger().severe("Component " + UIComponentUtils.toString(uiComponent)
                                           + " has no " + ADD_ACTION_LISTENER_METHOD_NAME + " method.");
            }
            else
            {
                Class c = Class.forName(type);
                ActionListener al = (ActionListener)c.newInstance();
                method.invoke(uiComponent, new Object[]{al});

                getTagCreatedListenersSet().add(al);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new FacesException(e);
        }
        catch (SecurityException e)
        {
            throw new FacesException(e);
        }
        catch (InstantiationException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new FacesException(e);
        }
    }


    public static final String TAG_CREATED_ACTION_LISTENERS_SET_ATTR
        = ActionListenerTag.class.getName() + ".TAG_CREATED_LISTENERS_SET";
    private static final Set getTagCreatedListenersSet()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Set set = (Set)facesContext.getServletRequest().getAttribute(TAG_CREATED_ACTION_LISTENERS_SET_ATTR);
        if (set == null)
        {
            set = new HashSet();
            facesContext.getServletRequest().setAttribute(TAG_CREATED_ACTION_LISTENERS_SET_ATTR, set);
        }
        return set;
    }

}
