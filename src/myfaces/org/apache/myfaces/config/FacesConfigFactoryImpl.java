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
package net.sourceforge.myfaces.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesConfigFactoryImpl
    extends FacesConfigFactoryBase
{
    public void parseFacesConfig(FacesConfig facesConfig,
                                 InputStream in) throws IOException, FacesException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setValidating(true);
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw new FacesException(e);
        }
        db.setEntityResolver(new MyER());

        Document document;
        try
        {
            document = db.parse(in);
        }
        catch (SAXException e)
        {
            throw new FacesException(e);
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }

        Element facesConfigElem = document.getDocumentElement();
        if (!facesConfigElem.getNodeName().equals("faces-config"))
        {
            throw new FacesException("Root is not a faces-config element ?!");
        }

        parseChildren(facesConfig, facesConfigElem.getChildNodes());
    }


    public static class MyER implements EntityResolver
    {
        public InputSource resolveEntity(String publicId,
                                         String systemId)
            throws SAXException, IOException
        {
            if (systemId.equals("http://java.sun.com/dtd/web-facesconfig_1_0.dtd"))
            {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream stream = loader.getResourceAsStream("net/sourceforge/myfaces/resource/web-facesconfig_1_0.dtd");
                return new InputSource(stream);
            }
            else
            {
                return null;
            }
        }
    }

    public void parseChildren(Object obj, NodeList nodeList)
    {
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                setProperty(obj, (Element)n);
            }
            else
            {
                System.out.println("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
            }
        }
    }

    public void setProperty(Object obj, Element elem)
    {
        //System.out.println("setProperty " + obj + " : " + elem);
        String propName = resolvePropertyName(elem.getNodeName());

        //Look for setXxx or addXxx method
        BeanInfo beanInfo = null;
        try
        {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        }
        catch (IntrospectionException e)
        {
            throw new FacesException(e);
        }
        String methodNameMiddle = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);

        Object[][] searchPatterns = {
            {"add" + methodNameMiddle + "Config", Config.class},
            {"set" + methodNameMiddle + "Config", Config.class},
            {"add" + methodNameMiddle, String.class},
            {"set" + methodNameMiddle, String.class},
            {"add" + methodNameMiddle, Object.class},
            {"set" + methodNameMiddle, Object.class},
        };

        Method method = null;

        for (int i = 0; i < searchPatterns.length; i++)
        {
            method = findWriteMethod(beanInfo,
                                     (String)searchPatterns[i][0],
                                     (Class)searchPatterns[i][1]);
            if (method != null) break;
        }

        if (method == null)
        {
            //throw new FacesException("Object " + obj + " has no set or add method for property '" + propName + "'.");
            System.out.println("Object " + obj + " has no set or add method for property '" + propName + "'.");
            return;
        }

        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1)
        {
            throw new FacesException("Object " + obj + " has illegal set or add method for property '" + propName + "'.");
        }

        setProperty(obj, elem, paramTypes[0], method);
    }


    private Method findWriteMethod(BeanInfo beanInfo, String methodName, Class paramType)
    {
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        for (int i = 0; i < methodDescriptors.length; i++)
        {
            String name = methodDescriptors[i].getName();
            if (name.equals(methodName))
            {
                Method method = methodDescriptors[i].getMethod();
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1)
                {
                    continue;
                }

                if (paramType.isAssignableFrom(paramTypes[0]))
                {
                    return method;
                }
            }
        }
        return null;
    }


    private void setProperty(Object obj, Element elem, Class propType, Method propWriteMethod)
    {
        if (String.class.isAssignableFrom(propType))
        {
            invoke(obj, propWriteMethod, getElementText(elem));
        }
        else if (Config.class.isAssignableFrom(propType))
        {
            Config config = (Config)instantiate(propType);
            parseChildren(config, elem.getChildNodes());
            invoke(obj, propWriteMethod, config);
        }
        else
        {
            //Assume class name
            String type = getElementText(elem);
            Class clazz = null;
            try
            {
                clazz = Class.forName(type);
            }
            catch (ClassNotFoundException e)
            {
                throw new FacesException(e);
            }
            invoke(obj, propWriteMethod, instantiate(clazz));
        }
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
            else
            {
                throw new FacesException("Unexpected node type " + n.getNodeType());
            }
        }
        return buf.toString();
    }


    private static Object instantiate(Class clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }
    }


    private static void invoke(Object obj, Method method, Object arg)
    {
        try
        {
            method.invoke(obj, new Object[] {arg});
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new FacesException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new FacesException(e);
        }
    }


    public static String resolvePropertyName(String elemName)
    {
        boolean ucase = false;
        StringBuffer buf = new StringBuffer(elemName.length());
        for (int i = 0, len = elemName.length(); i < len; i++)
        {
            char c = elemName.charAt(i);
            if (c == '-')
            {
                ucase = true;
            }
            else
            {
                if (ucase)
                {
                    buf.append(Character.toUpperCase(c));
                    ucase = false;
                }
                else
                {
                    buf.append(c);
                }
            }
        }
        return buf.toString();
    }




    public static void main (String[]  args)
	{
        try
        {
            //InputStream stream = new FileInputStream("C:\\Develop\\myfaces.sourceforge\\myfaces\\src\\myfaces\\faces-config.xml");
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("faces-config.xml");
            FacesConfigFactoryBase cp = new FacesConfigFactoryImpl();
            FacesConfig fc = new FacesConfig();
            cp.parseFacesConfig(fc, stream);
            System.out.println(fc);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (FacesException e)
        {
            throw new RuntimeException(e);
        }
    }



}
