/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package org.apache.myfaces.codegen.component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
            db.setEntityResolver(new _EntityResolver());

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

        String generateStateMethods = componentElem.getAttribute("generateStateMethods");
        if (generateStateMethods != null && generateStateMethods.length() > 0)
        {
            component.setGenerateStateMethods(Boolean.valueOf(generateStateMethods).booleanValue());
        }

        String generateUserRoleMethods = componentElem.getAttribute("generateUserRoleMethods");
        if (generateUserRoleMethods != null && generateUserRoleMethods.length() > 0)
        {
            component.setGenerateUserRoleMethods(Boolean.valueOf(generateUserRoleMethods).booleanValue());
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



    private InputSource createClassloaderInputSource(String publicId, String systemId)
        throws IOException
    {
        //InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(systemId);
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(systemId);
        if (inStream == null)
        {
            throw new IOException("Unable to find classloader resource " + systemId);
        }
        InputSource is = new InputSource(inStream);
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        is.setEncoding("ISO-8859-1");
        return is;
    }

    public class _EntityResolver implements EntityResolver
    {
        public InputSource resolveEntity(String publicId, String systemId) throws IOException
        {
            if (systemId == null)
            {
                throw new UnsupportedOperationException("systemId must not be null");
            }

            if (systemId.equals("http://myfaces.sourceforge.net/dtd/Component.dtd"))
            {
                //Load DTD from servlet.jar
                return createClassloaderInputSource(publicId, "net/sourceforge/myfaces/codegen/resource/Component.dtd");
            }

            throw new UnsupportedOperationException();
        }

    }



}
