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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesFactoryFinder;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageUtils
{
    public static MessageFactory getMessageFactory(FacesContext facesContext)
    {
        return MyFacesFactoryFinder.getMessageFactory(facesContext.getExternalContext());
    }

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object[] args)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return getMessageFactory(facesContext).getMessage(facesContext,
                                                          facesContext.getViewRoot().getLocale(),
                                                          severity,
                                                          messageId,
                                                          args);
    }

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object[] args,
                                          FacesContext facesContext)
    {
        return getMessageFactory(facesContext).getMessage(facesContext,
                                                          facesContext.getViewRoot().getLocale(),
                                                          severity,
                                                          messageId,
                                                          args);
    }

    public static FacesMessage getMessage(FacesMessage.Severity severity,
                                          String messageId,
                                          Object arg1)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return getMessageFactory(facesContext).getMessage(facesContext,
                                                          facesContext.getViewRoot().getLocale(),
                                                          severity,
                                                          messageId,
                                                          new Object[]{arg1});
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args)
    {
        addMessage(severity, messageId, args, null, FacesContext.getCurrentInstance());
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  FacesContext facesContext)
    {
        addMessage(severity, messageId, args, null, facesContext);
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  String forClientId)
    {
        addMessage(severity, messageId, args, forClientId, FacesContext.getCurrentInstance());
    }

    public static void addMessage(FacesMessage.Severity severity,
                                  String messageId,
                                  Object[] args,
                                  String forClientId,
                                  FacesContext facesContext)
    {
        facesContext.addMessage(forClientId,
                                getMessage(severity, messageId, args, facesContext));
    }
}
