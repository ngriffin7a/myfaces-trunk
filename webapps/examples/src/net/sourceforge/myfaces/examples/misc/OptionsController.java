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
package net.sourceforge.myfaces.examples.misc;

import javax.faces.FactoryFinder;
import javax.faces.application.Action;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class OptionsController
{
    /*
    public boolean setLocale(FacesContext facesContext, CommandEvent commandEvent)
    {
        OptionsForm form = (OptionsForm)facesContext.getModelValue("optionsForm");
        facesContext.setLocale(form.getLocale());
        return false;
    }
    */

    public Action getLocaleAction()
    {
        return new Action() {
            public String invoke()
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                VariableResolver vr = af.getApplication().getVariableResolver();
                OptionsForm form = (OptionsForm)vr.resolveVariable(facesContext, "optionsForm");
                facesContext.setLocale(form.getLocale());
                return "ok";
            }
        };
    }

}
