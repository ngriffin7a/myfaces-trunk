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
package net.sourceforge.myfaces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.ViewHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LifecycleImpl
        extends Lifecycle
{
    private ApplicationHandler _applicationHandler = null;
    private ViewHandler _viewHandler = null;

    private static final int[] PHASE_IDS = {
        RECONSTITUTE_REQUEST_TREE_PHASE,
        APPLY_REQUEST_VALUES_PHASE,
        HANDLE_REQUEST_EVENTS_PHASE,
        PROCESS_VALIDATIONS_PHASE,
        UPDATE_MODEL_VALUES_PHASE,
        INVOKE_APPLICATION_PHASE,
        RENDER_RESPONSE_PHASE,
    };
    private List _phases = null;

    public LifecycleImpl()
    {
    }

    private void initPhases()
    {
        _phases = new ArrayList();
        _phases.add(new ReconstituteRequestTreePhase(this));
        _phases.add(new ApplyRequestValuesPhase(this));
        _phases.add(new HandleRequestEventsPhase(this));
        _phases.add(new ProcessValidationsPhase(this));
        _phases.add(new UpdateModelValuesPhase(this));
        _phases.add(new InvokeApplicationPhase(this));
        _phases.add(new RenderResponsePhase(this));
    }

    private Phase getPhaseByIndex(int phaseIdx)
    {
        if (_phases == null)
        {
            initPhases();
        }
        return (Phase)_phases.get(phaseIdx);
    }

    private Phase getPhaseById(int phaseId)
    {
        if (_phases == null)
        {
            initPhases();
        }
        for (int i = 0; i < PHASE_IDS.length; i++)
        {
            if (PHASE_IDS[i] == phaseId)
            {
                return (Phase)_phases.get(i);
            }
        }
        throw new IllegalArgumentException();
    }

    public void execute(FacesContext facescontext)
            throws FacesException
    {
        for (int i = 0; i < PHASE_IDS.length; i++)
        {
            Phase phase = getPhaseByIndex(i);
            int result = executePhase(facescontext, phase);
            if (result == Phase.GOTO_EXIT)
            {
                return;
            }
            else if (result == Phase.GOTO_RENDER)
            {
                executePhase(facescontext, getPhaseById(RENDER_RESPONSE_PHASE));
                return;
            }
        }
    }

    public int executePhase(FacesContext facescontext, Phase phase)
            throws FacesException
    {
        return phase.execute(facescontext);
    }

    public ApplicationHandler getApplicationHandler()
    {
        return _applicationHandler;
    }

    public ViewHandler getViewHandler()
    {
        if (_viewHandler == null)
        {
            _viewHandler = new ViewHandlerJspImpl();
        }
        return _viewHandler;
    }

    public void setApplicationHandler(ApplicationHandler applicationhandler)
    {
        _applicationHandler = applicationhandler;
    }

    public void setViewHandler(ViewHandler viewhandler)
    {
        _viewHandler = viewhandler;
    }

}
