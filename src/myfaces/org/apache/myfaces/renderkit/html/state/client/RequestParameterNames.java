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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.component.UIComponentUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RequestParameterNames
{
    private RequestParameterNames() {} //Utility class

    protected static String getModelValueStateParameterName(String modelRef)
    {
        return "SM_" + modelRef;
    }

    protected static String getUIComponentStateParameterPropName(FacesContext facesContext,
                                                                 UIComponent uiComponent,
                                                                 String propertyName)
    {
        return "SCP_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent) + "/" + propertyName;
    }

    protected static String restoreUIComponentStateParameterPropName(FacesContext facesContext,
                                                                     UIComponent uiComponent,
                                                                     String paramName)
    {
        String prefix = "SCP_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent) + "/";
        if (paramName.startsWith(prefix))
        {
            return paramName.substring(prefix.length());
        }
        else
        {
            return null;
        }
    }

    protected static String getUIComponentStateParameterAttrName(FacesContext facesContext,
                                                                 UIComponent uiComponent,
                                                                 String attributeName)
    {
        return "SCA_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent) + "/" + attributeName;
    }

    protected static String restoreUIComponentStateParameterAttrName(FacesContext facesContext,
                                                                     UIComponent uiComponent,
                                                                     String paramName)
    {
        String prefix = "SCA_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent) + "/";
        if (paramName.startsWith(prefix))
        {
            return paramName.substring(prefix.length());
        }
        else
        {
            return null;
        }
    }

    private static final String LISTENER_SERIAL_ATTR
        = RequestParameterNames.class.getName() + ".LISTENER_SERIAL";

    protected static String getComponentListenerParameterName(FacesContext facesContext,
                                                              UIComponent uiComponent,
                                                              String listenerType)
    {
        return "SLC_"   //SLC stands for "State of Listener of type Component"
                + listenerType
                //+ "_" + uiComponent.getClientId(facesContext)
                + "_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent)
                + "/" + getNextListenerSerial(facesContext);
    }

    protected static String getSerializableListenerParameterName(FacesContext facesContext,
                                                                 UIComponent uiComponent,
                                                                 String listenerType)
    {
        return "SLS_"   //SLS stands for "State of Listener of type Serializable"
                + listenerType
                //+ "_" + uiComponent.getClientId(facesContext)
                + "_" + UIComponentUtils.getUniqueComponentId(facesContext, uiComponent)
                + "/" + getNextListenerSerial(facesContext);
    }

    private static int getNextListenerSerial(FacesContext facesContext)
    {
        Integer serial = (Integer)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(LISTENER_SERIAL_ATTR);
        if (serial == null)
        {
            serial = new Integer(1);
        }
        else
        {
            serial = new Integer(serial.intValue() + 1);
        }
        ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(LISTENER_SERIAL_ATTR, serial);
        return serial.intValue();
    }

    protected static ListenerParameterInfo getListenerParameterInfo(String paramName)
    {
        ListenerParameterInfo info;
        if (paramName.startsWith("SLC_"))
        {
            info = new ListenerParameterInfo();
            info.serializedListener = false;
        }
        else if (paramName.startsWith("SLS_"))
        {
            info = new ListenerParameterInfo();
            info.serializedListener = true;
        }
        else
        {
            return null;
        }

        int underscoreIdx = paramName.indexOf('_', 4);
        if (underscoreIdx < 0) throw new FacesException("Curious listener state parameter.");
        info.listenerType = paramName.substring(4, underscoreIdx);

        int slashIdx = paramName.indexOf('/', underscoreIdx + 1);
        if (slashIdx < 0) throw new FacesException("Curious listener state parameter.");
        info.uniqueComponentId = paramName.substring(underscoreIdx + 1, slashIdx);

        return info;
    }


    public static class ListenerParameterInfo
    {
        public boolean serializedListener;
        public String listenerType;
        //public String clientId;
        public String uniqueComponentId;

        public ListenerParameterInfo()
        {
        }
    }


}
