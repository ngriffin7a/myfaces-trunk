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
package net.sourceforge.myfaces.component;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesUICommand
    extends javax.faces.component.UICommand
{
//    public static final String COMMAND_NAME_PROP = "commandName";
    public static final String ACTION_PROP = "action";
//    public static final String ACTION_REF_PROP = "actionRef";
    public static final String IMMEDIATE_ACTION_PROP = "immediateAction";

    private boolean _immediateAction;

    public MyFacesUICommand()
    {
        //setValid(true);
    }

    public List[] getListeners()
    {
        //FIXME
        //return listeners;
        return null;
    }


    public boolean isImmediateAction()
    {
        return _immediateAction;
    }

    public void setImmediateAction(boolean immediateAction)
    {
        _immediateAction = immediateAction;
    }

    public boolean broadcast(FacesEvent event, PhaseId phaseId) throws AbortProcessingException
    {
        if (isImmediateAction())
        {
            if (phaseId == PhaseId.APPLY_REQUEST_VALUES &&
                event instanceof ActionEvent &&
                event.getSource() == this)
            {
                //Item was clicked --> render immediatly
                ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                Application application = af.getApplication();
                application.getActionListener().processAction((ActionEvent)event);

                //handle other listeners
                // FIXME
                //super.broadcast(event, phaseId);

                //go to render phase directly
                FacesContext.getCurrentInstance().renderResponse();
                return false;
            }
        }
        //FIXME
        //return super.broadcast(event, phaseId);
        return false;
    }


//------------------------------------------------------------------------------

    public String getClientId(FacesContext context)
    {
        return UIComponentUtils.getClientId(context, this);
    }

    public void addFacet(String facetName, UIComponent facet)
    {
        //FIXME
        //super.addFacet(facetName, facet);
        UIComponentUtils.ensureComponentInNamingContainer(facet);
    }

//------------------------------------------------------------------------------
}
