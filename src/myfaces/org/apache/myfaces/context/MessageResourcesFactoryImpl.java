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
package net.sourceforge.myfaces.context;

import javax.faces.context.MessageResources;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Implementation of a javax.faces.context.MessageResourcesFactory (see Spec. JSF.5.7).
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageResourcesFactoryImpl
{
    private HashMap _messageResources = new HashMap();

    private static final String FACES_API_MESSAGES_RESOURCE = "net.sourceforge.myfaces.context.resource.FacesApiMessages";
    private static final String FACES_IMPL_MESSAGES_RESOURCE = "net.sourceforge.myfaces.context.resource.FacesImplMessages";

    public MessageResourcesFactoryImpl()
    {
        addMessageResources(MessageResources.FACES_API_MESSAGES,
                            new MessageResourcesResourceBundleImpl(FACES_API_MESSAGES_RESOURCE));
        addMessageResources(MessageResources.FACES_IMPL_MESSAGES,
                            new MessageResourcesResourceBundleImpl(FACES_IMPL_MESSAGES_RESOURCE));
    }

    public void addMessageResources(String id, MessageResources messageResources)
    {
        synchronized (_messageResources)
        {
            if (_messageResources.put(id, messageResources) != null)
            {
                throw new RuntimeException("Duplicate MessageResources " + id);
            }
        }
    }

    public MessageResources getMessageResources(String id)
        throws IllegalArgumentException
    {
        MessageResources msgRes = (MessageResources)_messageResources.get(id);
        if (msgRes == null)
        {
            throw new IllegalArgumentException("No MessageResources with id '" + id + "' found!");
        }
        return msgRes;
    }

    public Iterator getMessageResourcesIds()
    {
        return _messageResources.keySet().iterator();
    }
}
