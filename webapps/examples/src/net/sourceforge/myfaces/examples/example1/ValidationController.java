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

        UIInput number2 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        validators = number2.getValidators();
        if (validators == null || validators.length == 0)
        {
            number2.addValidator(new LongRangeValidator(50, 20));
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

        UIInput number2 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        validators = number2.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                number2.removeValidator(validator);
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



    public String getNumber1ValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LongRangeValidator)validators[0]).getMinimum();
            long max = ((LongRangeValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + ")";
        }
        else
        {
            return "";
        }
    }

    public String getNumber2ValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LongRangeValidator)validators[0]).getMinimum();
            long max = ((LongRangeValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + ")";
        }
        else
        {
            return "";
        }
    }

    public String getTextValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LengthValidator)validators[0]).getMinimum();
            long max = ((LengthValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + " chars)";
        }
        else
        {
            return "";
        }
    }

}
