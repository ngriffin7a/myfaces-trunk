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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.NavigationCaseConfig;
import net.sourceforge.myfaces.config.NavigationRuleConfig;

import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.tree.TreeFactory;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationHandlerImpl
    extends NavigationHandler
{
    private static Map _cazes;
    private static List _wildcardKeys = new ArrayList();

    private static final String ASTERISK = "*";

    public void handleNavigation(FacesContext facesContext,
                                 String actionRef,
                                 String outcome)
    {
        if (outcome == null)
        {
            throw new NullPointerException("Parameter outcome must not be null");
        }

        String treeId = facesContext.getTree().getTreeId();
        Map casesMap = getNavigationCases(facesContext);

        String newTreeId = null;

        // Excact match
        List casesList = (List)casesMap.get(treeId);
        if (casesList != null)
        {
            newTreeId = getTreeId(casesList, actionRef, outcome);
        }

        if (newTreeId == null)
        {
            // Wildcard match
            List keys = getSortedWildcardKeys();
            for (int i = 0, size = keys.size(); i < size && newTreeId == null; i++)
            {
                String fromTreeId = (String)keys.get(i);
                if (fromTreeId.length() > 2)
                {
                    String prefix = fromTreeId.substring(0, fromTreeId.length() - 2);
                    if (treeId.startsWith(prefix))
                    {
                        casesList = (List)casesMap.get(fromTreeId);
                        if (casesList != null)
                        {
                            newTreeId = getTreeId(casesList, actionRef, outcome);
                        }
                    }
                }
                else
                {
                    casesList = (List)casesMap.get(fromTreeId);
                    if (casesList != null)
                    {
                        newTreeId = getTreeId(casesList, actionRef, outcome);
                    }
                }
            }
        }

        if (newTreeId != null)
        {
            TreeFactory treeFactory = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            facesContext.setTree(treeFactory.getTree(facesContext, newTreeId));
        }
    }

    private String getTreeId(List casesList, String actionRef, String outcome)
    {
        for (int i = 0, size = casesList.size(); i < size; i++)
        {
            NavigationCaseConfig caze = (NavigationCaseConfig)casesList.get(i);
            String cazeOutcome = caze.getFromOutcome();
            String cazeActionRef = caze.getFromActionRef();

            if (!((cazeOutcome != null && !cazeOutcome.equals(outcome)) ||
                (cazeActionRef != null && !cazeActionRef.equals(actionRef))))
            {
                return caze.getToTreeId();
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
            ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(servletContext);
            FacesConfig fc = fcf.getFacesConfig(servletContext);

            List rules = fc.getNavigationRuleConfigList();
            int sizei = rules.size();
            _cazes = new HashMap(sizei);

            for (int i = 0; i < sizei; i++)
            {
                NavigationRuleConfig rule = (NavigationRuleConfig)rules.get(i);
                List cazes = rule.getNavigationCaseConfigList();
                int sizej = cazes.size();

                String fromTreeId = rule.getFromTreeId();
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
