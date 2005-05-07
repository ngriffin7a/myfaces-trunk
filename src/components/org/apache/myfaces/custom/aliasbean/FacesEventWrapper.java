/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.aliasbean;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * The aliasBean tag allows you to link a fictive bean to a real bean.
 * 
 * Let's suppose you have a subform you use often but with different beans.
 * <br/>The aliasBean allows you to design the subform with a fictive bean and
 * to include it in all the pages where you use it. You just need to make an
 * alias to the real bean named after the fictive bean before invoking the
 * fictive bean. <br/>This making it possible to have a library of reusable
 * generic subforms.
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 */

class FacesEventWrapper extends FacesEvent {
    private FacesEvent _wrappedFacesEvent;

    public FacesEventWrapper(FacesEvent facesEvent, AliasBeansScope redirectComponent) {
        super(redirectComponent);
        _wrappedFacesEvent = facesEvent;
    }
	
    public FacesEventWrapper(FacesEvent facesEvent, AliasBean redirectComponent) {
        super(redirectComponent);
        _wrappedFacesEvent = facesEvent;
    }

    public PhaseId getPhaseId() {
        return _wrappedFacesEvent.getPhaseId();
    }

    public void setPhaseId(PhaseId phaseId) {
        _wrappedFacesEvent.setPhaseId(phaseId);
    }

    public void queue() {
        _wrappedFacesEvent.queue();
    }

    public String toString() {
        return _wrappedFacesEvent.toString();
    }

    public boolean isAppropriateListener(FacesListener faceslistener) {
        return _wrappedFacesEvent.isAppropriateListener(faceslistener);
    }

    public void processListener(FacesListener faceslistener) {
        _wrappedFacesEvent.processListener(faceslistener);
    }

    public FacesEvent getWrappedFacesEvent() {
        return _wrappedFacesEvent;
    }
}