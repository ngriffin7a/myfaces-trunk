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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.config.RuntimeConfig;
import net.sourceforge.myfaces.config.element.NavigationCase;
import net.sourceforge.myfaces.config.element.NavigationRule;
import net.sourceforge.myfaces.util.HashMapUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.31  2004/08/26 08:00:58  manolito
 * add current queryString to url on redirect
 *
 * Revision 1.30  2004/07/07 08:34:57  mwessendorf
 * removed unused import-statements
 *
 * Revision 1.29  2004/07/07 00:25:04  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.28  2004/07/01 22:05:14  mwessendorf
 * ASF switch
 *
 * Revision 1.27  2004/06/16 23:02:21  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.26.2.1  2004/06/16 02:07:22  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.26  2004/05/18 12:02:28  manolito
 * getActionURL and getResourceURL must not call encodeActionURL or encodeResourceURL
 *
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
    
    private Map _navigationCases = null;
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
        NavigationCase navigationCase = null;

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
                Object req = externalContext.getRequest();
                if (req instanceof HttpServletRequest)
                {
                    String queryString = ((HttpServletRequest)req).getQueryString();
                    if (queryString != null)
                    {
                        redirectPath = redirectPath + '?' + queryString;
                    }
                }
                else
                {
                    //TODO: What about Portlet requests?
                }
                try
                {
                    externalContext.redirect(externalContext.encodeActionURL(redirectPath));
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

    private NavigationCase calcMatchingNavigationCase(List casesList, String actionRef, String outcome)
    {
        for (int i = 0, size = casesList.size(); i < size; i++)
        {
            NavigationCase caze = (NavigationCase)casesList.get(i);
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
        if (_navigationCases == null)
        {
            synchronized(this) 
            {
                if (_navigationCases == null)
                {
                    ExternalContext externalContext = facesContext.getExternalContext();
                    RuntimeConfig runtimeConfig = RuntimeConfig.getCurrentInstance(externalContext);
        
                    Collection rules = runtimeConfig.getNavigationRules();
                    int rulesSize = rules.size();
                    Map cases = new HashMap(HashMapUtils.calcCapacity(rulesSize));
                    List wildcardKeys = new ArrayList();

                    for (Iterator iterator = rules.iterator(); iterator.hasNext();)
                    {
                        NavigationRule rule = (NavigationRule) iterator.next();
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
                            list = new ArrayList(rule.getNavigationCases());
                            cases.put(fromViewId, list);
                            if (fromViewId.endsWith(ASTERISK))
                            {
                                wildcardKeys.add(fromViewId);
                            }
                        } else {
                            list.addAll(rule.getNavigationCases());
                        }
        
                    }
                    Collections.sort(wildcardKeys, new KeyComparator());
                        
                    synchronized (cases)
                    {
                        // We do not really need this sychronization at all, but this
                        // gives us the peace of mind that some good optimizing compiler
                        // will not rearrange the execution of the assignment to an 
                        // earlier time, before all init code completes
                        _navigationCases = cases;
                        _wildcardKeys = wildcardKeys;
                    }
                }
            }
        }
        return _navigationCases;
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
