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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentDefinitionParser
{
    //private static final Log log = LogFactory.getLog(ComponentDefinitionParser.class);

    public ComponentDef parse(File xmlFile)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setIgnoringComments(true);
            dbf.setNamespaceAware(false);
            dbf.setValidating(false);

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource(xmlFile.getAbsolutePath());

            Document document = db.parse(is);
            return parseComponent(document.getDocumentElement());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private ComponentDef parseComponent(Element componentElem)
    {
        String componentClass = getChildElementText(componentElem, "component-class");
        String baseClass = getChildElementText(componentElem, "base-class");
        String componentType = getChildElementText(componentElem, "component-type");
        String componentFamily = getChildElementText(componentElem, "component-family");
        String rendererType = getChildElementText(componentElem, "renderer-type");

        ComponentDef component = new ComponentDef(componentClass, baseClass, componentType);
        component.setRendererType(rendererType);
        component.setComponentFamily(componentFamily);

        String generateConstructor = componentElem.getAttribute("generateConstructor");
        if (generateConstructor != null && generateConstructor.length() > 0)
        {
            component.setGenerateConstructor(Boolean.valueOf(generateConstructor).booleanValue());
        }

        NodeList fields = componentElem.getElementsByTagName("field");
        for (int i = 0, len = fields.getLength(); i < len; i++)
        {
            Element field = (Element)fields.item(i);
            component.addField(createField(field));
        }

        return component;
    }


    private FieldDef createField(Element fieldElem)
    {
        String fieldName = getChildElementText(fieldElem, "name");
        FieldDef field =  new FieldDef(fieldName);

        String fieldType = getChildElementText(fieldElem, "type");
        field.setQualifiedType(fieldType);

        String defaultValue = getChildElementText(fieldElem, "default-value");
        if (defaultValue != null) field.setDefaultValue(defaultValue);

        String proprietary = fieldElem.getAttribute("proprietary");
        if (proprietary != null && proprietary.length() > 0)
        {
            field.setProprietary(Boolean.valueOf(proprietary).booleanValue());
        }

        String generateProperty = fieldElem.getAttribute("generateProperty");
        if (generateProperty != null && generateProperty.length() > 0)
        {
            field.setGenerateProperty(Boolean.valueOf(generateProperty).booleanValue());
        }

        String generateSetter = fieldElem.getAttribute("generateSetter");
        if (generateSetter != null && generateSetter.length() > 0)
        {
            field.setGenerateSetter(Boolean.valueOf(generateSetter).booleanValue());
        }

        String saveState = fieldElem.getAttribute("saveState");
        if (saveState != null && saveState.length() > 0)
        {
            field.setSaveState(Boolean.valueOf(saveState).booleanValue());
        }

        return field;
    }


    private String getChildElementText(Element elem, String childElementName)
    {
        NodeList nodeList = elem.getElementsByTagName(childElementName);
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node child = nodeList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                return getElementText((Element)child);
            }
        }
        return null;
    }


    private String getElementText(Element elem)
    {
        StringBuffer buf = new StringBuffer();
        NodeList nodeList = elem.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.TEXT_NODE)
            {
                buf.append(n.getNodeValue());
            }
            else if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                buf.append(getElementText((Element)n));
            }
        }
        return buf.toString();
    }




}
