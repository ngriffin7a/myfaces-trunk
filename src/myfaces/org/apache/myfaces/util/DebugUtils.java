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
package net.sourceforge.myfaces.util;

import net.sourceforge.myfaces.util.bean.BeanUtils;
import org.apache.commons.logging.Log;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;


/**
 * Utilities for logging.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DebugUtils
{
    private DebugUtils() 
    {
        // hide from public access
    }

    public static void assertError(boolean condition, Log log, String msg)
            throws FacesException
    {
        if (!condition)
        {
            log.error(msg);
            throw new FacesException(msg);
        }
    }

    public static void assertFatal(boolean condition, Log log, String msg)
        throws FacesException
    {
        if (!condition)
        {
            log.fatal(msg);
            throw new FacesException(msg);
        }
    }



    public static void traceView(Log log, String additionalMsg)
    {
        if (log.isTraceEnabled())
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext == null)
            {
                log.error("Cannot not print view to console (no FacesContext).");
                return;
            }
            UIViewRoot viewRoot = facesContext.getViewRoot();

            traceView(log, additionalMsg, viewRoot);
        }
    }

    public static void traceView(Log log, String additionalMsg, UIViewRoot viewRoot)
    {
        if (log.isTraceEnabled())
        {
            log.trace("========================================");
            if (additionalMsg != null)
            {
                log.trace(additionalMsg);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            printView(viewRoot, new PrintStream(baos));
            log.trace(baos.toString());
            log.trace("========================================");
        }
    }


    public static void printView(UIViewRoot uiViewRoot)
    {
        printView(uiViewRoot, System.out);
    }

    public static void printView(UIViewRoot uiViewRoot, PrintStream stream)
    {
        printComponent(uiViewRoot, stream, 0, true);
    }

    public static void printComponent(UIComponent comp, PrintStream stream)
    {
        printComponent(comp, stream, 0, false);
    }

    private static void printComponent(UIComponent comp,
                                       PrintStream stream,
                                       int indent,
                                       boolean recursive)
    {
        printIndent(stream, indent);
        stream.print('<');
        stream.print(comp.getClass().getName());

        /*
        stream.print(" component=\"");
        stream.print(comp);
        stream.print("\"");
        */

        //printProperty(stream, comp, CommonComponentProperties.COMPONENT_ID_ATTR, "id");
        //printProperty(stream, comp, CommonComponentProperties.RENDERER_TYPE_ATTR, "rendererType");
        /*
        if (comp instanceof UIOutput)
        {
            //printProperty(stream, comp, CommonComponentProperties.VALUE_PROP);
            //printProperty(stream, comp, CommonComponentProperties.VALUE_REF_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UIOutput.VALUE_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UIOutput.VALUE_REF_PROP);
        }
        if (comp instanceof UICommand)
        {
            printProperty(stream, comp, net.sourceforge.myfaces.component.UICommand.ACTION_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UICommand.ACTION_REF_PROP);
        }
        */

        BeanInfo beanInfo = BeanUtils.getBeanInfo(comp);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if (propertyDescriptor.getReadMethod() != null &&
                propertyDescriptor.getWriteMethod() != null)
            {
                printProperty(stream, comp, propertyDescriptor.getName());
            }
        }

        //FIXME
        /*
        for (Iterator it = comp.getAttributeNames(); it.hasNext();)
        {
            String attrName = (String)it.next();
            printAttribute(stream, comp, attrName);
        }
        */

        /*
        if (comp instanceof UICommand)
        {
            List[] listeners = (((UICommand)comp).getListeners());
            if (listeners != null)
            {
                stream.println();
                stream.println(listeners.length + " Listeners");
            }
        }
        */

        if (recursive)
        {
            Iterator children = comp.getFacetsAndChildren();
            if (children.hasNext())
            {
                stream.println('>');
                while (children.hasNext())
                {
                    UIComponent child = (UIComponent)children.next();
                    printComponent(child, stream, indent + 1, true);
                }
                printIndent(stream, indent);
                stream.print("</");
                stream.print(comp.getClass().getName());
                stream.println('>');
            }
            else
            {
                stream.println("/>");
            }
        }
        else
        {
            stream.println("/>");
        }
    }

    private static void printAttribute(PrintStream stream,
                                       UIComponent comp,
                                       String attrName)
    {
        printAttribute(stream, comp, attrName, attrName);
    }

    private static void printAttribute(PrintStream stream,
                                       UIComponent comp,
                                       String attrName,
                                       String prettyAttrName)
    {
        Object v = comp.getAttributes().get(attrName);
        if (v != null)
        {
            stream.print(' ');
            stream.print(prettyAttrName);
            stream.print("=\"");
            stream.print(v.toString());
            stream.print('"');
        }
    }

    private static void printProperty(PrintStream stream,
                                      UIComponent comp,
                                      String propName)
    {
        printProperty(stream, comp, propName, propName);
    }

    private static void printProperty(PrintStream stream,
                                       UIComponent comp,
                                       String propName,
                                       String prettyPropName)
    {
        Object v = null;
        try
        {
            v = BeanUtils.getBeanPropertyValue(comp, propName);
            if (v == null) v = "NULL";
        }
        catch (Exception e)
        {
        }
        stream.print(' ');
        stream.print(prettyPropName);
        stream.print("=\"");
        if (v != null)
        {
            stream.print(v.toString());
        }
        else
        {
            stream.print('?');
        }
        stream.print('"');
    }

    private static void printIndent(PrintStream stream, int depth)
    {
        for (int i = 0; i < depth; i++)
        {
            stream.print("  ");
        }
    }
}
