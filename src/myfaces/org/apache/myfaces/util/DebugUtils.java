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
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


/**
 * Utilities for logging.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DebugUtils
{
    private static final Log log = LogFactory.getLog(DebugUtils.class);

    //Attributes that should not be printed
    private static final HashSet IGNORE_ATTRIBUTES;
    static
    {
        IGNORE_ATTRIBUTES = new HashSet();
        IGNORE_ATTRIBUTES.add("attributes");
        IGNORE_ATTRIBUTES.add("children");
        IGNORE_ATTRIBUTES.add("childCount");
        IGNORE_ATTRIBUTES.add("class");
        IGNORE_ATTRIBUTES.add("facets");
        IGNORE_ATTRIBUTES.add("facetsAndChildren");
        IGNORE_ATTRIBUTES.add("parent");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.COMPONENT_IDS");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.FACET_NAMES");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.CURRENT_VIEW_ROOT");
    }

    private static final String JSF_COMPONENT_PACKAGE = "javax.faces.component.";
    private static final String MYFACES_COMPONENT_PACKAGE = "net.sourceforge.myfaces.component.";


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



    public static void traceView(String additionalMsg)
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
            if (viewRoot == null)
            {
                log.error("Cannot not print view to console (no ViewRoot in FacesContext).");
                return;
            }

            traceView(additionalMsg, viewRoot);
        }
    }

    /**
     * Be careful, when using this version of traceView:
     * Some component properties (e.g. getRenderer) assume, that there is a
     * valid viewRoot set in the FacesContext!
     * @param additionalMsg
     * @param viewRoot
     */
    private static void traceView(String additionalMsg, UIViewRoot viewRoot)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        if (additionalMsg != null)
        {
            ps.println(additionalMsg);
        }
        ps.println("========================================");
        printView(viewRoot, ps);
        ps.println("========================================");
        ps.close();
        log.trace(baos.toString());
    }

    public static void printView(UIViewRoot uiViewRoot, PrintStream stream)
    {
        printComponent(uiViewRoot, stream, 0, true, null);
    }

    public static void printComponent(UIComponent comp, PrintStream stream)
    {
        printComponent(comp, stream, 0, false, null);
    }

    private static void printComponent(UIComponent comp,
                                       PrintStream stream,
                                       int indent,
                                       boolean withChildrenAndFacets,
                                       String facetName)
    {
        printIndent(stream, indent);
        stream.print('<');

        String compType = comp.getClass().getName();
        if (compType.startsWith(JSF_COMPONENT_PACKAGE))
        {
            compType = compType.substring(JSF_COMPONENT_PACKAGE.length());
        }
        else if (compType.startsWith(MYFACES_COMPONENT_PACKAGE))
        {
            compType = compType.substring(MYFACES_COMPONENT_PACKAGE.length());
        }
        stream.print(compType);

        printAttribute(stream, "id", comp.getId());

        if (facetName != null)
        {
            printAttribute(stream, "facetName", facetName);
        }

        for (Iterator it = comp.getAttributes().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            if (!"id".equals(entry.getKey()))
            {
                printAttribute(stream, entry.getKey(), entry.getValue());
            }
        }

        //HACK: comp.getAttributes() only returns attributes, that are NOT backed
        //by a corresponding component property. So, we must explicitly get the
        //available properties by Introspection:
        BeanInfo beanInfo = BeanUtils.getBeanInfo(comp);
        PropertyDescriptor propDescriptors[] = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propDescriptors.length; i++)
        {
            String name = propDescriptors[i].getName();
            if (!"id".equals(name))
            {
                ValueBinding vb = comp.getValueBinding(name);
                if (vb != null)
                {
                    printAttribute(stream, name, vb.getExpressionString());
                }
                else
                {
                    Object value = comp.getAttributes().get(name);
                    if (value != null)
                    {
                        printAttribute(stream, name, value);
                    }
                }
            }
        }

        //TODO: listeners, ...

        if (withChildrenAndFacets)
        {
            int childCount = comp.getChildCount();
            Map facetsMap = comp.getFacets();
            if (childCount > 0 || !facetsMap.isEmpty())
            {
                stream.println('>');

                if (childCount > 0)
                {
                    for (Iterator it = comp.getChildren().iterator(); it.hasNext(); )
                    {
                        UIComponent child = (UIComponent)it.next();
                        printComponent(child, stream, indent + 1, true, null);
                    }
                }

                for (Iterator it = facetsMap.entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry entry = (Map.Entry)it.next();
                    printComponent((UIComponent)entry.getValue(),
                                   stream, indent + 1, true,
                                   (String)entry.getKey());
                }

                printIndent(stream, indent);
                stream.print("</");
                stream.print(compType);
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
                                       Object name,
                                       Object value)
    {
        if (IGNORE_ATTRIBUTES.contains(name)) return;
        stream.print(' ');
        stream.print(name.toString());
        stream.print("=\"");
        if (value != null)
        {
            if (value instanceof UIComponent)
            {
                stream.print("[id:");
                stream.print(((UIComponent)value).getId());
                stream.print("]");
            }
            else
            {
                stream.print(value.toString());
            }
        }
        else
        {
            stream.print("NULL");
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
