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
package net.sourceforge.myfaces.application;

import javax.faces.FactoryFinder;
import javax.faces.application.Action;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ActionListenerImpl
    implements ActionListener
{
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException
    {
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = af.getApplication();

        FacesContext facesContext = FacesContext.getCurrentInstance();

        String actionRef;
        String outcome;

        UICommand uiCommand = (UICommand)actionEvent.getComponent();
        if (uiCommand.getAction() != null)
        {
            actionRef = null;
            outcome = uiCommand.getAction();
        }
        else
        {
            actionRef = uiCommand.getActionRef();
            if (actionRef == null)
            {
                //throw new IllegalArgumentException("Component " + uiCommand.getClientId(facesContext) + " does not have an action or actionRef property!");
                return;
            }

            Object actionObj = application.getValueBinding(actionRef).getValue(facesContext);
            if (actionObj == null || !(actionObj instanceof Action))
            {
                throw new IllegalArgumentException("Referenced value '" + actionRef + "' is not a valid Action!");
            }

            Action action = (Action)actionObj;
            outcome = action.invoke();
        }

        NavigationHandler navigationHandler = application.getNavigationHandler();
        navigationHandler.handleNavigation(facesContext, actionRef, outcome);
    }

    public PhaseId getPhaseId()
    {
        return PhaseId.INVOKE_APPLICATION;
    }
}
