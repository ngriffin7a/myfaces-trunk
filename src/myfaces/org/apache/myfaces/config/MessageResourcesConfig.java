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
package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.faces.context.FacesContext;
import javax.faces.context.MessageResources;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MessageResourcesConfig
    extends MessageResources
    implements Config
{
    private static final Log log = LogFactory.getLog(MessageResourcesConfig.class);

    private String _messageResourcesId;
    private String _messageResourcesClass;
    private Map _messageConfigMap;

    public String getMessageResourcesId()
    {
        return _messageResourcesId;
    }

    public void setMessageResourcesId(String messageResourcesId)
    {
        _messageResourcesId = messageResourcesId;
    }

    public String getMessageResourcesClass()
    {
        return _messageResourcesClass;
    }

    public void setMessageResourcesClass(String messageResourcesClass)
    {
        _messageResourcesClass = messageResourcesClass;
    }


    public void addMessageConfig(MessageConfig newMC)
    {
        String id = newMC.getMessageId();
        MessageConfig oldMC = (MessageConfig)getMessageConfigMap().get(id);
        if (oldMC == null)
        {
            getMessageConfigMap().put(id, newMC);
        }
        else
        {
            if (newMC.getDeclaredSeverity() != null &&
                oldMC.getDeclaredSeverity() != null &&
                newMC.getSeverity() != oldMC.getSeverity())
            {
                log.warn("Message '" + id + "' defined more than once with different severities.");
            }
            else if (newMC.getDeclaredSeverity() != null)
            {
                oldMC.setSeverity(newMC.getSeverity());
            }

            for (Iterator it = newMC.getSummaryMap().entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                oldMC.addSummary((String)entry.getKey(), (String)entry.getValue());
            }

            for (Iterator it = newMC.getDetailMap().entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                oldMC.addDetail((String)entry.getKey(), (String)entry.getValue());
            }
        }
    }

    public Map getMessageConfigMap()
    {
        if (_messageConfigMap == null)
        {
            _messageConfigMap = new HashMap();
        }
        return _messageConfigMap;
    }




    public Message getMessage(FacesContext facescontext, String s)
    {
        return internalGetMessage(facescontext, s, null);
    }

    public Message getMessage(FacesContext facescontext, String s, Object aobj[])
    {
        return internalGetMessage(facescontext, s, aobj);
    }

    public Message getMessage(FacesContext facescontext, String s, Object obj)
    {
        return internalGetMessage(facescontext, s, new Object[] {obj});
    }

    public Message getMessage(FacesContext facescontext, String s, Object obj, Object obj1)
    {
        return internalGetMessage(facescontext, s, new Object[] {obj, obj1});
    }

    public Message getMessage(FacesContext facescontext, String s, Object obj, Object obj1, Object obj2)
    {
        return internalGetMessage(facescontext, s, new Object[] {obj, obj1, obj2});
    }

    public Message getMessage(FacesContext facescontext, String s, Object obj, Object obj1, Object obj2, Object obj3)
    {
        return internalGetMessage(facescontext, s, new Object[] {obj, obj1, obj2, obj3});
    }

    private Message internalGetMessage(FacesContext facesContext, String msgId, Object[] args)
    {
        MessageConfig mc = (MessageConfig)getMessageConfigMap().get(msgId);
        if (mc == null)
        {
            log.error("Message with id '" + msgId + "' not found in MessageResources '" + _messageResourcesId + "'.");
            return new MessageImpl(Message.SEVERITY_ERROR,
                                   "Error " + msgId,
                                   "No detailed description for error + " + msgId);
        }

        return mc.getMessage(facesContext, args);
    }


    public MessageResources newMessageResources()
    {
        if (!(_messageConfigMap == null || _messageConfigMap.isEmpty()))
        {
            log.warn("Declared messages for MessageResources '" + _messageResourcesId + "' will be ignored, because I don't know how to add Messages to class '" + _messageResourcesClass + "'.");
        }
        return (MessageResources)ConfigUtil.newInstance(_messageResourcesClass);
    }

}
