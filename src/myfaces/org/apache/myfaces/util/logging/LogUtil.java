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
package net.sourceforge.myfaces.util.logging;

import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.component.UIComponentUtils;

import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.component.UIComponent;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;


/**
 * Utilities (shortcuts) for logging.
 *
 * TODO: Convenient methods for debugging, that automatically log
 * current method and class (be means of Exception and StackTraceElement).
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LogUtil
{
    public static final String LOGGER_NAME = "MyFacesLogger";
    private static final Handler CONSOLE_HANDER = new ConsoleHandler();
    private static MyFacesLogger _logger = null;

    private LogUtil() {}

    public static MyFacesLogger getLogger()
    {
        if (_logger == null)
        {
            //_logger = Logger.getLogger(LOGGER_NAME);
            _logger = new MyFacesLogger();
            synchronized (_logger)
            {
                if (_logger.getHandlers().length == 0)
                {
                    _logger.addHandler(CONSOLE_HANDER);
                }
                _logger.setUseParentHandlers(false);
            }
        }
        return _logger;
    }


    public static void printTreeToConsole(String additionalMsg)
    {
        printTreeToConsole(null, additionalMsg);
    }

    public static void printTreeToConsole(Tree tree, String additionalMsg)
    {
        if (getLogger().getLevel().intValue() <= Level.FINEST.intValue())
        {
            if (tree == null)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext == null)
                {
                    getLogger().info("Could not print tree to console (no FacesContext).");
                }
                tree = facesContext.getTree();
            }

            System.out.println("========================================");
            if (additionalMsg != null)
            {
                System.out.println(additionalMsg);
            }
            TreeUtils.printTree(tree);
            System.out.println("========================================");
        }
    }

    public static void printComponentToConsole(UIComponent component)
    {
        printComponentToConsole(component, null);
    }

    public static void printComponentToConsole(UIComponent component, String label)
    {
        if (getLogger().getLevel().intValue() <= Level.FINEST.intValue())
        {
            if (label != null)
            {
                System.out.print(label);
                System.out.print(": ");
            }
            System.out.println(UIComponentUtils.toString(component));
        }
    }

}
