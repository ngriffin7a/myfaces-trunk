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
package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.component.UICommand;
import net.sourceforge.myfaces.util.bean.BeanMethod;
import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.CommandEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date${DATE} ${TIME} $
 */
 public class DefaultApplicationHandler
        implements ApplicationHandler
{

    public boolean processEvent(FacesContext facesContext,
                                FacesEvent facesEvent)
    {
        if (facesEvent instanceof CommandEvent)
        {
            CommandEvent commandEvent = (CommandEvent)facesEvent;
            UIComponent uiComponent = commandEvent.getComponent();
            String commandRef = (String)uiComponent.getAttribute(UICommand.COMMAND_REFERENCE_ATTR);
            if (commandRef != null)
            {
                if (invokeControllerMethod(facesContext, commandRef, commandEvent))
                {
                    return true;
                }
            }
        }

        return false;
    }


    public static boolean invokeControllerMethod(FacesContext facesContext,
                                                 String commandRef,
                                                 CommandEvent commandEvent)
    {
        int i = commandRef.indexOf('.');
        if (i == -1)
        {
            throw new IllegalArgumentException("Illegal command reference");
        }

        String controllerId = commandRef.substring(0, i);
        String methodPath = commandRef.substring(i + 1);

        Object controllerObj = facesContext.getModelValue(controllerId);
        BeanMethod beanMethod
            = BeanUtils.findNestedBeanMethod(controllerObj, methodPath, "",
                                             new Class[] {FacesContext.class, CommandEvent.class});
        Object obj = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        if (method == null)
        {
            throw new RuntimeException("Controller object " + controllerId + " does not have a valid command method '" + methodPath + "'.");
        }

        Object retObj;
        try
        {
            retObj = method.invoke(obj, new Object[] {facesContext, commandEvent});
        }
        catch (SecurityException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new FacesException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new FacesException(e.getTargetException());
        }

        if (retObj == null)
        {
            //method is void
            return false; //TODO: false ok?
        }

        if (!(retObj instanceof Boolean))
        {
            throw new RuntimeException("Controller method '" + methodPath + "' of controller object " + controllerId + " does not have a boolean return value.");
        }

        return ((Boolean)retObj).booleanValue();
    }

}