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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.NavigationCaseConfig;
import net.sourceforge.myfaces.config.NavigationRuleConfig;
import net.sourceforge.myfaces.util.HashMapUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.25  2004/05/10 04:49:26  dave0000
 * small optimization improvements
 *
 * Revision 1.24  2004/04/26 11:28:16  manolito
 * global navigation-rule with no from-view-id NPE bug
 *
 */
public class NavigationHandlerImpl
    extends NavigationHandler
{
    private static final Log log = LogFactory.getLog(NavigationHandlerImpl.class);

    private static final String ASTERISK = "*";
    
    private Map _cazes = null;
    private List _wildcardKeys = new ArrayList();

    public NavigationHandlerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New NavigationHandler instance created");        
    }

    public void handleNavigation(FacesContext facesContext, String fromAction, String outcome)
    {
        if (outcome == null)
        {
            // stay on current ViewRoot
            return;
        }

        String viewId = facesContext.getViewRoot().getViewId();
        Map casesMap = getNavigationCases(facesContext);
        NavigationCaseConfig navigationCase = null;

        List casesList = (List)casesMap.get(viewId);
        if (casesList != null)
        {
            // Exact match?
            navigationCase = calcMatchingNavigationCase(casesList, fromAction, outcome);
        }

        if (navigationCase == null)
        {
            // Wildcard match?
            List keys = getSortedWildcardKeys();
            for (int i = 0, size = keys.size(); i < size; i++)
            {
                String fromViewId = (String)keys.get(i);
                if (fromViewId.length() > 2)
                {
                    String prefix = fromViewId.substring(0, fromViewId.length() - 2);
                    if (viewId.startsWith(prefix))
                    {
                        casesList = (List)casesMap.get(fromViewId);
                        if (casesList != null)
                        {
                            navigationCase = calcMatchingNavigationCase(casesList, fromAction, outcome);
                            if (navigationCase != null) break;
                        }
                    }
                }
                else
                {
                    casesList = (List)casesMap.get(fromViewId);
                    if (casesList != null)
                    {
                        navigationCase = calcMatchingNavigationCase(casesList, fromAction, outcome);
                        if (navigationCase != null) break;
                    }
                }
            }
        }

        if (navigationCase != null)
        {
            if (log.isTraceEnabled())
            {
                log.trace("handleNavigation fromAction=" + fromAction + " outcome=" + outcome +
                          " toViewId =" + navigationCase.getToViewId() +
                          " redirect=" + navigationCase.isRedirect());
            }
            if (navigationCase.isRedirect())
            {
                ExternalContext externalContext = facesContext.getExternalContext();
                ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
                String redirectPath = viewHandler.getActionURL(facesContext, navigationCase.getToViewId());
                try
                {
                    externalContext.redirect(redirectPath);
                }
                catch (IOException e)
                {
                    throw new FacesException(e.getMessage(), e);
                }
                facesContext.responseComplete();
            }
            else
            {
                ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
                //create new view
                UIViewRoot viewRoot = viewHandler.createView(facesContext, navigationCase.getToViewId());
                facesContext.setViewRoot(viewRoot);
                facesContext.renderResponse();
            }
        }
        else
        {
            // no navigationcase found, stay on current ViewRoot
            if (log.isTraceEnabled())
            {
                log.trace("handleNavigation fromAction=" + fromAction + " outcome=" + outcome +
                          " no matching navigation-case found, staying on current ViewRoot");
            }
        }
    }

    private NavigationCaseConfig calcMatchingNavigationCase(List casesList, String actionRef, String outcome)
    {
        for (int i = 0, size = casesList.size(); i < size; i++)
        {
            NavigationCaseConfig caze = (NavigationCaseConfig)casesList.get(i);
            String cazeOutcome = caze.getFromOutcome();
            String cazeActionRef = caze.getFromAction();
            if ((cazeOutcome == null || cazeOutcome.equals(outcome)) &&
                (cazeActionRef == null || cazeActionRef.equals(actionRef)))
            {
                return caze;
            }
        }
        return null;
    }

    private List getSortedWildcardKeys()
    {
        return _wildcardKeys;
    }

    private Map getNavigationCases(FacesContext facesContext)
    {
        if (_cazes == null)
        {
            synchronized(this) 
            {
                if (_cazes == null)
                {
                    ExternalContext externalContext = facesContext.getExternalContext();
                    FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
                    FacesConfig fc = fcf.getFacesConfig(externalContext);
        
                    List rules = fc.getNavigationRuleConfigList();
                    int rulesSize = rules.size();
                    Map cases = new HashMap(HashMapUtils.calcCapacity(rulesSize));
                    List wildcardKeys = new ArrayList();
        
                    for (int i = 0; i < rulesSize; i++)
                    {
                        NavigationRuleConfig rule = (NavigationRuleConfig)rules.get(i);
                        List cazeList = rule.getNavigationCaseConfigList();
                        int sizej = cazeList.size();
        
                        String fromViewId = rule.getFromViewId();
        
                        //specification 7.4.2 footnote 4 - missing fromViewId is allowed:
                        if (fromViewId == null)
                        {
                            fromViewId = ASTERISK;
                        }
                        else
                        {
                            fromViewId = fromViewId.trim();
                        }
        
                        List list = (List) cases.get(fromViewId);
                        if (list == null)
                        {
                            list = new ArrayList(sizej);
                            cases.put(fromViewId, list);
                            if (fromViewId.endsWith(ASTERISK))
                            {
                                wildcardKeys.add(fromViewId);
                            }
                        }
        
                        for (int j = 0; j < sizej; j++)
                        {
                            NavigationCaseConfig navcase = (NavigationCaseConfig)cazeList.get(j);
                            list.add(navcase);
                        }
                    }
                    Collections.sort(wildcardKeys, new KeyComparator());
                        
                    synchronized (cases)
                    {
                        // We do not really need this sychronization at all, but this
                        // gives us the peace of mind that some good optimizing compiler
                        // will not rearrange the execution of the assignment to an 
                        // earlier time, before all init code completes
                        _cazes = cases;
                        _wildcardKeys = wildcardKeys;
                    }
                }
            }
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
