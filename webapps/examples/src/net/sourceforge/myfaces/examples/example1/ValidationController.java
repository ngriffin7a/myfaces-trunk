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
package net.sourceforge.myfaces.examples.example1;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class ValidationController
{
    public String enableValidation()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators == null || validators.length == 0)
        {
            number1.addValidator(new LongRangeValidator(10, 1));
        }

        UIInput text = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        validators = text.getValidators();
        if (validators == null || validators.length == 0)
        {
            text.addValidator(new LengthValidator(7, 3));
        }

        return "ok";
    }

    public String disableValidation()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                number1.removeValidator(validator);
            }
        }

        UIInput text = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        validators = text.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                text.removeValidator(validator);
            }
        }

        return "ok";
    }

}
