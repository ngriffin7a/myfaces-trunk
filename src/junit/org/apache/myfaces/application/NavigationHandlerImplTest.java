/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.application;

import org.apache.myfaces.MyFacesBaseTest;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 22:01:14  mwessendorf
 * ASF switch
 *
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
