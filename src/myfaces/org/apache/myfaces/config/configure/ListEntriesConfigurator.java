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
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.ListEntriesConfig;
import net.sourceforge.myfaces.config.ValueBindingExpression;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListEntriesConfigurator
{
    //private static final Log log = LogFactory.getLog(ManagedBeanConfigurator.class);

    private ListEntriesConfig _listEntriesConfig;

    public ListEntriesConfigurator(ListEntriesConfig listEntriesConfig)
    {
        _listEntriesConfig = listEntriesConfig;
    }

    public void configure(FacesContext facesContext, List list)
    {
        list.clear();   //TODO: ok, to clear?
        if (_listEntriesConfig.isContainsValueBindings())
        {
            // copy one by one
            for (Iterator it = _listEntriesConfig.getValues().iterator(); it.hasNext();)
            {
                Object value = it.next();
                if (value instanceof ValueBindingExpression)
                {
                    ValueBinding vb = ((ValueBindingExpression)value).getValueBinding(facesContext,
                                                                                      _listEntriesConfig.getValueClass());
                    list.add(vb.getValue(facesContext));
                }
                else
                {
                    list.add(value);
                }
            }
        }
        else
        {
            list.addAll(_listEntriesConfig.getValues());
        }
    }
}

