/*
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
package javax.faces.component;

import javax.faces.context.FacesContext;
import java.io.Serializable;


/**
 * see Javadoc of JSF Specification
 *
 * TODO
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class UIComponentBase
        extends UIComponent
{

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[0];
        //TODO
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        //TODO
    }



    Object wrapForStateSaving(FacesContext context, Object object)
    {
        if (object == null) return null;
        if (object instanceof StateHolder)
        {
            if (((StateHolder)object).isTransient())
            {
                return null;
            }
            else
            {
                return new StateHolderWrapper(object.getClass(),
                                              ((StateHolder)object).saveState(context));
            }
        }
        else if (object instanceof Serializable)
        {
            return object;
        }
        else
        {
            throw new IllegalArgumentException("Must be StateHolder or Serializable");
        }
    }

    Object unwrapForStateRestoring(FacesContext context, Object object)
    {
        if (object == null) return null;
        if (object instanceof StateHolderWrapper)
        {
            Class clazz = ((StateHolderWrapper)object).getClazz();
            StateHolder stateHolder = null;
            try
            {
                stateHolder = (StateHolder)clazz.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            stateHolder.restoreState(context, ((StateHolderWrapper)object).getWrapped());
            return stateHolder;
        }
        else
        {
            return object;
        }
    }


    private static class StateHolderWrapper
            implements Serializable
    {
        private Class _class;
        private Object _wrapped;

        public StateHolderWrapper(Class aClass, Object wrapped)
        {
            _class = aClass;
            _wrapped = wrapped;
        }

        public Object getWrapped()
        {
            return _wrapped;
        }

        public Class getClazz()
        {
            return _class;
        }
    }



    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------
    //------------------ GENERATED CODE END ---------------------------------------
}
