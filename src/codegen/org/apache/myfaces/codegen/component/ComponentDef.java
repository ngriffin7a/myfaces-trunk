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
package net.sourceforge.myfaces.codegen.component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentDef
{
    //private static final Log log = LogFactory.getLog(Component.class);

    public static final String[] IMPLICIT_PACKAGES = {"java.util",
                                                      "java.lang",
                                                      "javax.faces.el",
                                                      "javax.faces.context",
                                                      "javax.faces.convert"};

    private String _qualifiedClassName;
    private String _baseClassName;
    private String _componentType;
    private String _componentFamily;
    private String _rendererType;
    private Map _fieldsMap;
    private boolean _generateStateMethods;
    private boolean _generateConstructor = true;

    public ComponentDef()
    {
    }

    public ComponentDef(String className, String baseClassName, String componentType)
    {
        _qualifiedClassName = className;
        _baseClassName = baseClassName;
        _componentType = componentType;
    }

    public void setQualifiedClassName(String qualifiedClassName)
    {
        _qualifiedClassName = qualifiedClassName;
    }

    public String getComponentFamily()
    {
        return _componentFamily;
    }

    public void setComponentFamily(String componentFamily)
    {
        _componentFamily = componentFamily;
    }

    public String getPackage()
    {
        int dot = _qualifiedClassName.lastIndexOf('.');
        return _qualifiedClassName.substring(0, dot);
    }

    public String getClassName()
    {
        int dot = _qualifiedClassName.lastIndexOf('.');
        return _qualifiedClassName.substring(dot + 1);
    }

    public String getQualifiedClassName()
    {
        return _qualifiedClassName;
    }

    public String getBaseClassName()
    {
        return _baseClassName;
    }

    public void setBaseClassName(String baseClassName)
    {
        _baseClassName = baseClassName;
    }

    public Collection getFields()
    {
        return _fieldsMap != null ? _fieldsMap.values() : Collections.EMPTY_LIST;
    }

    public void addField(FieldDef field)
    {
        if (_fieldsMap == null)
        {
            _fieldsMap = new LinkedHashMap();
        }
        _fieldsMap.put(field.getName(), field);
    }

    public FieldDef getField(String fieldName)
    {
        return (FieldDef)_fieldsMap.get(fieldName);
    }


    public List getImportPackages()
    {
        return Arrays.asList(IMPLICIT_PACKAGES);
    }


    public String getComponentType()
    {
        return _componentType;
    }

    public void setComponentType(String componentType)
    {
        _componentType = componentType;
    }

    public String getRendererType()
    {
        return _rendererType;
    }

    public void setRendererType(String rendererType)
    {
        _rendererType = rendererType;
    }

    public boolean isGenerateStateMethods()
    {
        return _generateStateMethods;
    }

    public void setGenerateStateMethods(boolean generateStateMethods)
    {
        _generateStateMethods = generateStateMethods;
    }


    public Collection getPropertyFields()
    {
        List lst = new ArrayList();
        for (Iterator it = getFields().iterator(); it.hasNext(); )
        {
            FieldDef field = (FieldDef)it.next();
            if (field.isGenerateProperty())
            {
                lst.add(field);
            }
        }
        return lst;
    }

    public Collection getSaveStateFields()
    {
        List lst = new ArrayList();
        int counter = 1;
        for (Iterator it = getFields().iterator(); it.hasNext(); )
        {
            FieldDef field = (FieldDef)it.next();
            if (field.isSaveState())
            {
                field.setSaveStateFieldIndex(counter++);
                lst.add(field);
            }
        }
        return lst;
    }

    public int getSaveStateFieldsCountPlusOne()
    {
        return getSaveStateFields().size() + 1;
    }

    public boolean isGenerateConstructor()
    {
        return _generateConstructor;
    }

    public void setGenerateConstructor(boolean generateConstructor)
    {
        _generateConstructor = generateConstructor;
    }


    public void toXml(PrintWriter writer) throws IOException
    {
        writer.print  ("<component");
        if (!isGenerateConstructor()) writer.print(" generateConstructor=\"false\"");
        if (!isGenerateStateMethods()) writer.print(" generateStateMethods=\"false\"");
        writer.println(">");
        writer.println("    <component-class>" + _qualifiedClassName + "</component-class>");
        writer.println("    <base-class>" + _baseClassName + "</base-class>");
        if (_componentType != null)
        {
            writer.println("    <component-type>" + _componentType + "</component-type>");
        }

        if (_componentFamily != null)
        {
            writer.println("    <component-family>" + _componentFamily + "</component-family>");
        }

        if (_rendererType != null)
        {
            writer.println("    <renderer-type>" + _rendererType + "</renderer-type>");
        }

        Collection fields = getFields();
        for (Iterator iterator = fields.iterator(); iterator.hasNext();)
        {
            FieldDef f = (FieldDef)iterator.next();
            f.toXml(writer);
        }
        writer.println("</component>");
    }

}
