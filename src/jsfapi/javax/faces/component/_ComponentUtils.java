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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentUtils
{
    private _ComponentUtils() {}

    static UIComponent findParentNamingContainer(UIComponent component,
                                                 boolean returnRootIfNotFound)
    {
        UIComponent parent = component.getParent();
        if (returnRootIfNotFound && parent == null)
        {
            return component;
        }
        while (parent != null)
        {
            if (parent instanceof NamingContainer) return parent;
            if (returnRootIfNotFound)
            {
                UIComponent nextParent = parent.getParent();
                if (nextParent == null)
                {
                    return parent;  //Root
                }
                parent = nextParent;
            }
            else
            {
                parent = parent.getParent();
            }
        }
        return null;
    }

    static UIComponent getRootComponent(UIComponent component)
    {
        UIComponent parent;
        for(;;)
        {
            parent = component.getParent();
            if (parent == null) return component;
            component = parent;
        }
    }

    static UIComponent findComponent(UIComponent findBase, String id)
    {
        if (id.equals(findBase.getId()))
        {
            return findBase;
        }

        for (Iterator it = findBase.getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent childOrFacet = (UIComponent)it.next();
            if (!(childOrFacet instanceof NamingContainer))
            {
                UIComponent find = findComponent(childOrFacet, id);
                if (find != null) return find;
            }
            else if (id.equals(childOrFacet.getId()))
            {
                return childOrFacet;
            }
        }

        return null;
    }


    static void callValidators(FacesContext context, UIInput input, Object convertedValue)
    {
        Validator[] validators = input.getValidators();
        for (int i = 0; i < validators.length; i++)
        {
            Validator validator = validators[i];
            try
            {
                validator.validate(context, input, convertedValue);
            }
            catch (ValidatorException e)
            {
                input.setValid(false);
                FacesMessage facesMessage = e.getFacesMessage();
                if (facesMessage != null)
                {
                    context.addMessage(input.getClientId(context), facesMessage);
                }
                else
                {
                    //TODO: specification? add a general message?
                }
                //TODO: specification? should we abort validation immediately
            }
        }

        MethodBinding validatorBinding = input.getValidator();
        if (validatorBinding != null)
        {
            try
            {
                validatorBinding.invoke(context,
                                        new Object[] {context, input, convertedValue});
            }
            catch (EvaluationException e)
            {
                input.setValid(false);
                Throwable cause = e.getCause();
                if (cause instanceof ValidatorException)
                {
                    FacesMessage facesMessage = ((ValidatorException)cause).getFacesMessage();
                    if (facesMessage != null)
                    {
                        context.addMessage(input.getClientId(context), facesMessage);
                    }
                    else
                    {
                        //TODO: specification? add a general message?
                    }
                    //TODO: specification? should we abort validation immediately
                }
                else
                {
                    throw e;
                }
            }
        }
    }


}
