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
package net.sourceforge.myfaces.example.controller;

import net.sourceforge.myfaces.example.model.QuotationForm;

import javax.faces.context.FacesContext;
import javax.faces.event.CommandEvent;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class QuotationController
{
    public void processEvent(FacesContext facesContext, CommandEvent commandEvent)
    {
        String commandName = commandEvent.getCommandName();
        if (commandName == null)
        {
            throw new IllegalArgumentException("Command must not be null.");
        }

        QuotationForm form = (QuotationForm)facesContext.getModelValue("q_form");

        if (commandName.equals("quotationOn"))
        {
            form.quote();
        }
        else if (commandName.equals("quotationOff"))
        {
            form.unquote();
        }
        else
        {
            throw new IllegalArgumentException("Unknown Command " + commandName);
        }
    }
}