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

import javax.faces.component.UIComponent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentInspector
{
    //private static final Log log = LogFactory.getLog(ComponentInspector.class);

    private static final Object[] EMPTY_ARGS = new Object[]{};

    private File _destdir;
    private boolean _overwrite;

    public ComponentInspector(File destdir, boolean overwrite)
    {
        _destdir = destdir;
        _overwrite = overwrite;
    }

    public void inspect(String componentClassName)
    {
        System.out.println("Inspecting " + componentClassName);
        try
        {
            ComponentDef oldComponentDefinition = null;
            
            File xmlFile = getDestFileFromClassName(componentClassName);
            if (xmlFile.exists())
            {
                if (!_overwrite)
                {
                    System.err.println(xmlFile + " not created (already exists)");
                    return;
                }
                else
                {
                    ComponentDefinitionParser parser = new ComponentDefinitionParser();
                    oldComponentDefinition = parser.parse(xmlFile);
                }
            }

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            writer.println("<?xml version=\"1.0\"?>");
            inspectSingleClass(writer, componentClassName, oldComponentDefinition);
            writer.close();
            stringWriter.close();

            FileWriter fileWriter = new FileWriter(xmlFile);
            fileWriter.write(stringWriter.getBuffer().toString());
            fileWriter.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private File getDestFileFromClassName(String className)
    {
        String destFileName = className.replace('.', '/') + ".xml";
        return new File(_destdir, destFileName);
    }

    private void inspectSingleClass(PrintWriter writer, String className,
                                    ComponentDef oldComponentDefinition)
    {
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            Class clazz;
            UIComponent component;
            try
            {
                clazz = Class.forName(className, true, classLoader);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            try
            {
                component = (UIComponent)clazz.newInstance();
            }
            catch (Exception e)
            {
                //abstract component?
                e.printStackTrace();
                component = null;
            }

            ComponentDef componentDef = new ComponentDef();
            if (oldComponentDefinition != null)
            {
                componentDef.setGenerateConstructor(oldComponentDefinition.isGenerateConstructor());
                componentDef.setGenerateStateMethods(oldComponentDefinition.isGenerateStateMethods());
            }

            componentDef.setQualifiedClassName(clazz.getName());
            componentDef.setBaseClassName(clazz.getSuperclass().getName());

            Field declaredField = getDeclaredField(clazz, "COMPONENT_TYPE");
            if (component != null && declaredField != null)
            {
                componentDef.setComponentType((String)declaredField.get(component));
            }

            declaredField = getDeclaredField(clazz, "COMPONENT_FAMILY");
            if (component != null && declaredField != null)
            {
                componentDef.setComponentFamily((String)declaredField.get(component));
            }

            if (component != null && component.getRendererType() != null)
            {
                componentDef.setRendererType(component.getRendererType());
            }

            Set baseClassProperties = new HashSet();
            addAllBaseClassProperties(clazz.getSuperclass(), baseClassProperties);

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++)
            {
                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                if (baseClassProperties.contains(propertyDescriptor.getName()))
                {
                    //property is declared in super class
                    continue;
                }

                if (propertyDescriptor.getWriteMethod() == null ||
                    propertyDescriptor.getReadMethod() == null)
                {
                    //Only create declaredField descriptors for read-write properties
                    continue;
                }

                FieldDef field = null;
                if (oldComponentDefinition != null)
                {
                    FieldDef oldField = oldComponentDefinition.getField(propertyDescriptor.getName());
                    if (oldField != null)
                    {
                        field = oldField;
                    }
                }

                if (field == null)
                {
                    field = new FieldDef(propertyDescriptor.getName());
                }

                field.setQualifiedType(propertyDescriptor.getPropertyType().getName());

                if (component != null)
                {
                    Method m = propertyDescriptor.getReadMethod();
                    String defaultValue = null;
                    try
                    {
                        defaultValue = m.invoke(component, EMPTY_ARGS).toString();
                    }
                    catch (Exception e)
                    {
                    }
                    if (defaultValue != null)
                    {
                        field.setDefaultValue(defaultValue);
                    }
                }

                componentDef.addField(field);
            }

            if (oldComponentDefinition != null)
            {
                Collection fields = oldComponentDefinition.getFields();
                for (Iterator it = fields.iterator(); it.hasNext();)
                {
                    FieldDef field = (FieldDef)it.next();
                    if (field.isProprietary())
                    {
                        componentDef.addField(field);
                    }
                }
            }

            componentDef.toXml(writer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private java.lang.reflect.Field getDeclaredField(Class clazz, String name)
    {
        try
        {
            return clazz.getDeclaredField(name);
        }
        catch (NoSuchFieldException e)
        {
            return null;
        }
    }


    private void addAllBaseClassProperties(Class baseClass, Set baseClassProperties)
        throws IntrospectionException
    {
        if (baseClass == null) return;

        BeanInfo beanInfo = Introspector.getBeanInfo(baseClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            baseClassProperties.add(propertyDescriptor.getName());
        }

        addAllBaseClassProperties(baseClass.getSuperclass(), baseClassProperties);
    }


    /*
    private static final String COMPONENT_PACKAGE = "javax.faces.component";
    private static final String[] COMPONENT_LIST = {"UIColumn",
                                                    "UICommand",
                                                    "UIData",
                                                    "UIForm",
                                                    "UIGraphic",
                                                    "UIInput",
                                                    "UIMessage",
                                                    "UIMessages",
                                                    "UINamingContainer",
                                                    "UIOutput",
                                                    "UIPanel",
                                                    "UIParameter",
                                                    "UISelectBoolean",
                                                    "UISelectItem",
                                                    "UISelectItems",
                                                    "UISelectMany",
                                                    "UISelectOne",
                                                    "UIViewRoot"};

    private static final String COMPONENT_HTML_PACKAGE = "javax.faces.component.html";
    private static final String[] COMPONENT_HTML_LIST = {"HtmlCommandButton",
                                                         "HtmlCommandLink",
                                                         "HtmlDataTable",
                                                         "HtmlForm",
                                                         "HtmlGraphicImage",
                                                         "HtmlInputHidden",
                                                         "HtmlInputSecret",
                                                         "HtmlInputText",
                                                         "HtmlInputTextarea",
                                                         "HtmlMessage",
                                                         "HtmlMessages",
                                                         "HtmlOutputFormat",
                                                         "HtmlOutputLabel",
                                                         "HtmlOutputLink",
                                                         "HtmlOutputText",
                                                         "HtmlPanelGrid",
                                                         "HtmlPanelGroup",
                                                         "HtmlSelectBooleanCheckbox",
                                                         "HtmlSelectManyCheckbox",
                                                         "HtmlSelectManyListbox",
                                                         "HtmlSelectManyMenu",
                                                         "HtmlSelectOneListbox",
                                                         "HtmlSelectOneMenu",
                                                         "HtmlSelectOneRadio"};
                                                         */

    public static void main(String[] args)
    {
        /*
        String componentDefFile = "C:\\Develop\\myfaces.sourceforge\\myfaces\\src\\codegen\\net\\sourceforge\\myfaces\\codegen\\resource\\Components.xml";
        File outfile = new File(componentDefFile);
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(outfile, false);

            ComponentInspector inspector = new ComponentInspector(fileWriter);
            inspector.addComponentClassNames(COMPONENT_PACKAGE, COMPONENT_LIST);
            inspector.addComponentClassNames(COMPONENT_HTML_PACKAGE, COMPONENT_HTML_LIST);
            inspector.inspect();

            fileWriter.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        */
    }

}
