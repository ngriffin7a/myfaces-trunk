/*
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
package net.sourceforge.myfaces.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.NavigationCaseConfig;
import net.sourceforge.myfaces.config.NavigationRuleConfig;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class NavigationHandlerImpl
    extends NavigationHandler
{
    private static final Log log = LogFactory.getLog(NavigationHandlerImpl.class);

    private static final String ASTERISK = "*";
    
    private static Map _cazes;
    private static List _wildcardKeys = new ArrayList();

    public NavigationHandlerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New NavigationHandler instance created");        
    }

    public void handleNavigation(FacesContext facesContext, String fromAction, String outcome)
    {
        if(outcome == null)
        {
            // stay on current ViewRoot
            return;
        }
        
        String viewId = facesContext.getViewRoot().getViewId();
        Map casesMap = getNavigationCases(facesContext);
        String newViewId = null;

        // Exact match
        List casesList = (List)casesMap.get(viewId);
        if (casesList != null)
        {
            newViewId = getViewId(casesList, fromAction, outcome);
        }

        if (newViewId == null)
        {
            // Wildcard match
            List keys = getSortedWildcardKeys();
            for (int i = 0, size = keys.size(); i < size && newViewId == null; i++)
            {
                String fromTreeId = (String)keys.get(i);
                if (fromTreeId.length() > 2)
                {
                    String prefix = fromTreeId.substring(0, fromTreeId.length() - 2);
                    if (viewId.startsWith(prefix))
                    {
                        casesList = (List)casesMap.get(fromTreeId);
                        if (casesList != null)
                        {
                            newViewId = getViewId(casesList, fromAction, outcome);
                            if (newViewId != null) break;
                        }
                    }
                }
                else
                {
                    casesList = (List)casesMap.get(fromTreeId);
                    if (casesList != null)
                    {
                        newViewId = getViewId(casesList, fromAction, outcome);
                        if (newViewId != null) break;
                    }
                }
            }
        }

        if (newViewId != null)
        {
            ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
            facesContext.setViewRoot(viewHandler.createView(facesContext, newViewId));
        }
        // Otherwise stay on current ViewRoot
    }

    private String getViewId(List casesList, String actionRef, String outcome)
    {
        for (int i = 0, size = casesList.size(); i < size; i++)
        {
            NavigationCaseConfig caze = (NavigationCaseConfig)casesList.get(i);
            String cazeOutcome = caze.getFromOutcome();
            String cazeActionRef = caze.getFromActionRef();
            if ((cazeOutcome == null || cazeOutcome.equals(outcome)) &&
                (cazeActionRef == null || cazeActionRef.equals(actionRef)))
            {
                return caze.getToViewId();
            }
        }
        return null;
    }

    private List getSortedWildcardKeys()
    {
        return _wildcardKeys;
    }

    private synchronized Map getNavigationCases(FacesContext facesContext)
    {
        if (_cazes == null)
        {
            ExternalContext externalContext = facesContext.getExternalContext();
            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
            FacesConfig fc = fcf.getFacesConfig(externalContext);

            List rules = fc.getNavigationRuleConfigList();
            int rulesSize = rules.size();
            _cazes = new HashMap(rulesSize);

            for (int i = 0; i < rulesSize; i++)
            {
                NavigationRuleConfig rule = (NavigationRuleConfig)rules.get(i);
                List cazes = rule.getNavigationCaseConfigList();
                int sizej = cazes.size();

                String fromTreeId = rule.getFromViewId();
                List list = (List)_cazes.get(fromTreeId);
                if (list == null)
                {
                    list = new ArrayList(sizej);
                    _cazes.put(fromTreeId, list);
                    if (fromTreeId.endsWith(ASTERISK))
                    {
                        _wildcardKeys.add(fromTreeId);
                    }
                }

                for (int j = 0; j < sizej; j++)
                {
                    NavigationCaseConfig navcase = (NavigationCaseConfig)cazes.get(j);
                    list.add(navcase);
                }
            }
            Collections.sort(_wildcardKeys, new KeyComparator());
        }
        return _cazes;
    }

    private static final class KeyComparator
        implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            return -(((String)o1).compareTo((String)o2));
        }
    }
}
