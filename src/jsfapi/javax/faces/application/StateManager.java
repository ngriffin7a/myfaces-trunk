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
package javax.faces.application;



/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class StateManager
{
    public static final String STATE_SAVING_METHOD_PARAM_NAME = "javax.faces.STATE_SAVING_METHOD";
    public static final String STATE_SAVING_METHOD_CLIENT = "client";
    public static final String STATE_SAVING_METHOD_SERVER = "server";
    private Boolean _savingStateInClient = null;

    public abstract StateManager.SerializedView saveSerializedView(javax.faces.context.FacesContext context);

    protected abstract Object getTreeStructureToSave(javax.faces.context.FacesContext context);

    protected abstract Object getComponentStateToSave(javax.faces.context.FacesContext context);

    public abstract void writeState(javax.faces.context.FacesContext context,
                                    StateManager.SerializedView state)
            throws java.io.IOException;

    public abstract javax.faces.component.UIViewRoot restoreView(javax.faces.context.FacesContext context,
                                                                 String viewId,
                                                                 String renderKitId);

    protected abstract javax.faces.component.UIViewRoot restoreTreeStructure(javax.faces.context.FacesContext context,
                                                                             String viewId,
                                                                             String renderKitId);

    protected abstract void restoreComponentState(javax.faces.context.FacesContext context,
                                                  javax.faces.component.UIViewRoot viewRoot,
                                                  String renderKitId);

    public boolean isSavingStateInClient(javax.faces.context.FacesContext context)
    {
        if (_savingStateInClient != null) return _savingStateInClient.booleanValue();
        String stateSavingMethod = context.getExternalContext().getInitParameter(STATE_SAVING_METHOD_PARAM_NAME);
        if (stateSavingMethod == null)
        {
            _savingStateInClient = Boolean.FALSE; //Specs 10.1.3: default server saving
            context.getExternalContext().log("No state saving method defined, assuming default server state saving");
        }
        else if (stateSavingMethod.equals(STATE_SAVING_METHOD_CLIENT))
        {
            _savingStateInClient = Boolean.TRUE;
        }
        else if (stateSavingMethod.equals(STATE_SAVING_METHOD_SERVER))
        {
            _savingStateInClient = Boolean.FALSE;
        }
        else
        {
            _savingStateInClient = Boolean.FALSE; //Specs 10.1.3: default server saving
            context.getExternalContext().log("Illegal state saving method '" + stateSavingMethod + "', default server state saving will be used");
        }
        return _savingStateInClient.booleanValue();
    }


    public static class SerializedView
    {
        private Object _structure;
        private Object _state;

        public SerializedView(Object structure, Object state)
        {
            _structure = structure;
            _state = state;
        }

        public Object getStructure()
        {
            return _structure;
        }

        public Object getState()
        {
            return _state;
        }
    }
}
