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
package net.sourceforge.myfaces.examples.listexample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class SimpleCountryAction
{
    private static final Log log = LogFactory.getLog(SimpleCountryAction.class);

    public void initCountryForm(ActionEvent actionevent) throws AbortProcessingException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Long id = null;
        for (Iterator it = actionevent.getComponent().getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                if (name.equals("id"))
                {
                    id = (Long)((UIParameter)child).getValue();
                }
            }
        }

        if (id == null)
        {
            log.fatal("No id parameter given.");
            throw new AbortProcessingException("No id parameter given.");
        }

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        VariableResolver vr = af.getApplication().getVariableResolver();
        SimpleCountryForm form = (SimpleCountryForm)vr.resolveVariable(facesContext, "countryForm");
        form.setId(id.longValue());
    }
}
