/*
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

import net.sourceforge.myfaces.MyFacesBaseTest;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/26 11:24:15  manolito
 * new NavigationHandlerImplTest and some junit refactoring
 *
 */
public class NavigationHandlerImplTest
         extends MyFacesBaseTest
{
    //private static final Log log = LogFactory.getLog(NavigationHandlerImplTest.class);

    public NavigationHandlerImplTest(String name)
    {
        super(name);
    }

    public void test()
    {
        NavigationHandler navigationHandler = _application.getNavigationHandler();

        UIViewRoot viewRoot = new UIViewRoot();

        // case 1: no from-view-id
        viewRoot.setViewId("/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{anyaction}", "outcome1");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page1.jsp");

        // case 2: from-view-id = *, with from-outcome, no from-action
        viewRoot.setViewId("/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{anyaction}", "outcome2");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page2.jsp");

        // case 3: from-view-id = *, with from-outcome and from-action
        viewRoot.setViewId("/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{action3}", "outcome3");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page3.jsp");

        // case 4: from-view-id = *, no from-outcome, with from-action
        viewRoot.setViewId("/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{action4}", "anyoutcome");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page4.jsp");

        // case 5: exact from-view-id match, with from-outcome, no from-action
        viewRoot.setViewId("/from5.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{anyaction}", "outcome5");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page5.jsp");

        // case 6: wildcard from-view-id match, with from-outcome, no from-action
        viewRoot.setViewId("/context6/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{anyaction}", "outcome6");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/page6.jsp");

        // no match
        viewRoot.setViewId("/anycontext/anypage.jsp");
        _facesContext.setViewRoot(viewRoot);
        navigationHandler.handleNavigation(_facesContext, "#{anyaction}", "anyoutcome");
        assertEquals(_facesContext.getViewRoot().getViewId(), "/anycontext/anypage.jsp");

    }

}
