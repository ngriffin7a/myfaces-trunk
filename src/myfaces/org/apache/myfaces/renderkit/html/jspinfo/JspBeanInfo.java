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
package net.sourceforge.myfaces.renderkit.html.jspinfo;

import net.sourceforge.myfaces.util.logging.LogUtil;

import java.beans.Beans;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspBeanInfo
{
    private String _id;
    private String _className;
    private String _beanName;
    private int _scope;

    /**
     *
     * @param id            id of the bean
     * @param className     can be null
     * @param beanName      can be null
     * @param scope         on of the scopes defined in PageContext
     */
    public JspBeanInfo(String id, String className, String beanName, int scope)
    {
        _id = id;
        _className = className;
        _beanName = beanName;
        _scope = scope;
    }

    public String getId()
    {
        return _id;
    }

    public String getClassName()
    {
        return _className;
    }

    public String getBeanName()
    {
        return _beanName;
    }

    public int getScope()
    {
        return _scope;
    }


    public Object instantiate()
    {
        if (_className != null)
        {
            try
            {
                Class clazz = Class.forName(_className);
                return clazz.newInstance();
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if (_beanName != null)
        {
            try
            {
                return Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                         _beanName);
            }
            catch (java.io.IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            //As stated in JSP Spec. the bean object must be present in the given scope
            //if class and beanName attributes are not specified.
            //--> we cannot instantiate this bean
            LogUtil.getLogger().severe("Cannot instantiate bean " + _id + " without class or beanName.");
            return null;
        }
    }

}
