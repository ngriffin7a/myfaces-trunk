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
package net.sourceforge.myfaces.examples.example1;

import javax.faces.application.Action;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.tree.Tree;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class ValidationController
    implements ActionListener
{
    public void processAction(ActionEvent event) throws AbortProcessingException
    {
        String commandName = event.getActionCommand();
        if (commandName.equals("enableVal"))
        {
            ENABLE_ACTION.invoke();
        }
        else
        {
            DISABLE_ACTION.invoke();
        }
    }

    public PhaseId getPhaseId()
    {
        return PhaseId.APPLY_REQUEST_VALUES;
    }


    private static final Action ENABLE_ACTION = new Action() {
        public String invoke()
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Tree tree = facesContext.getTree();

            UIInput number1 = (UIInput)tree.getRoot().findComponent("number1");
            number1.clearValidators();
            number1.addValidator(new LongRangeValidator(10, 1));

            UIInput text = (UIInput)tree.getRoot().findComponent("text");
            text.clearValidators();
            text.addValidator(new LengthValidator(7, 3));

            return "ok";
        }
    };

    private static final Action DISABLE_ACTION = new Action() {
        public String invoke()
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Tree tree = facesContext.getTree();

            UIInput number1 = (UIInput)tree.getRoot().findComponent("number1");
            number1.clearValidators();

            UIInput text = (UIInput)tree.getRoot().findComponent("text");
            text.clearValidators();

            return "ok";
        }
    };


}
