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

import net.sourceforge.myfaces.util.logging.LogUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.faces.FacesException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
                                 InputStream in,
                                 String systemId,
                                 EntityResolver entityResolver) throws IOException, FacesException
    {
        if (in == null)
        {
            throw new NullPointerException("InputStream must not be null.");
        }
        if (systemId == null)
        {
            throw new NullPointerException("systemId must not be null.");
        }
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
        db.setEntityResolver(entityResolver);
        db.setErrorHandler(ERROR_HANDLER);

        Document document;
        try
        {
            InputSource is = new InputSource(in);
            is.setEncoding("ISO-8859-1");
            is.setSystemId(systemId);
            document = db.parse(is);
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
        if (facesConfigElem == null ||
            !facesConfigElem.getNodeName().equals("faces-config"))
        {
            throw new FacesException("No valid faces-config root element found!");
        }

        parseChildren(facesConfig, facesConfigElem.getChildNodes());
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


    private static final Class[] CONFIG_PARAM = new Class[] {Config.class};
    private static final Class[] STRING_PARAM = new Class[] {String.class};
    private static final Class[] OBJECT_PARAM = new Class[] {Object.class};
    private static final Class[] LANG_AND_STRING_PARAM = new Class[] {String.class, String.class};

    public void setProperty(Object obj, Element elem)
    {
        //System.out.println("setProperty " + obj + " : " + elem);
        String propName = resolvePropertyName(elem.getNodeName());

        //Look for setXxx or addXxx method
        /*
        BeanInfo beanInfo = null;
        try
        {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        }
        catch (IntrospectionException e)
        {
            throw new FacesException(e);
        }
        */
        Class beanClass = obj.getClass();
        String methodNameMiddle = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);

        Object[][] searchPatterns = {
            {"add" + methodNameMiddle + "Config", CONFIG_PARAM},
            {"set" + methodNameMiddle + "Config", CONFIG_PARAM},
            {"add" + methodNameMiddle, LANG_AND_STRING_PARAM},
            {"add" + methodNameMiddle, STRING_PARAM},
            {"set" + methodNameMiddle, STRING_PARAM},
            {"add" + methodNameMiddle, OBJECT_PARAM},
            {"set" + methodNameMiddle, OBJECT_PARAM},
        };

        Method method = null;

        for (int i = 0; i < searchPatterns.length; i++)
        {
            method = findWriteMethod(beanClass,
                                     (String)searchPatterns[i][0],
                                     (Class[])searchPatterns[i][1]);
            if (method != null) break;
        }

        if (method == null)
        {
            //throw new FacesException("Object " + obj + " has no set or add method for property '" + propName + "'.");
            System.out.println("Object " + obj + " has no set or add method for property '" + propName + "'.");
            return;
        }

        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 1)
        {
            setProperty(obj, elem, paramTypes[0], method);
        }
        else if (paramTypes.length == 2)
        {
            String language = elem.getAttribute("xml:lang");
            invokeWithLang(obj, method, language, getElementText(elem));
        }
        else
        {
            throw new FacesException("Object " + obj + " has illegal set or add method for property '" + propName + "'.");
        }

    }


    private Method findWriteMethod(Class beanClass, String methodName, Class[] withParamTypes)
    {
        Method[] methods = beanClass.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            if (method.getName().equals(methodName))
            {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != withParamTypes.length)
                {
                    continue;
                }

                boolean found = true;
                for (int j = 0; j < paramTypes.length; j++)
                {
                    if (!withParamTypes[j].isAssignableFrom(paramTypes[j]))
                    {
                        found = false;
                        break;
                    }
                }

                if (found)
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

    private static void invokeWithLang(Object obj, Method method, String lang, Object arg)
    {
        try
        {
            method.invoke(obj, new Object[] {lang, arg});
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




    /*
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
    */


    private static final ErrorHandler ERROR_HANDLER = new ErrorHandler()
    {
        public void warning(SAXParseException exception)
            throws SAXException
        {
            LogUtil.getLogger().warning("SAXParseException: " + getMessage(exception));
        }

        public void error(SAXParseException exception)
            throws SAXException
        {
            LogUtil.getLogger().severe("SAXParseException: " + getMessage(exception));
        }

        public void fatalError(SAXParseException exception)
            throws SAXException
        {
            LogUtil.getLogger().severe("SAXParseException: " + getMessage(exception));
        }

        private String getMessage(SAXParseException exception)
        {
            StringBuffer buf = new StringBuffer();
            buf.append("URI=");
            buf.append(exception.getSystemId());
            buf.append(" Line=");
            buf.append(exception.getLineNumber());
            buf.append(" Column=");
            buf.append(exception.getColumnNumber());
            buf.append(":");
            buf.append(exception.getMessage());
            return buf.toString();
        }
    };


}
