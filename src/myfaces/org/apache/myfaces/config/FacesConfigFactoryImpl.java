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
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.xml.MyFacesErrorHandler;
import net.sourceforge.myfaces.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.20  2004/07/01 22:05:08  mwessendorf
 * ASF switch
 *
 * Revision 1.19  2004/05/03 09:24:34  manolito
 * _facesConfig member must not be static
 *
 */
public class FacesConfigFactoryImpl
    extends FacesConfigFactoryBase
{
    private static final Log log = LogFactory.getLog(FacesConfigFactoryImpl.class);
    private Map _propPatternCache;
    private FacesConfig _facesConfig;

    public void parseFacesConfig(FacesConfig facesConfig,
                                 InputStream in,
                                 String systemId,
                                 EntityResolver entityResolver) throws FacesException
    {
        _facesConfig = facesConfig;
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
        db.setErrorHandler(new MyFacesErrorHandler(log));

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

        _propPatternCache = new HashMap();
        parseChildren(facesConfig, facesConfigElem.getChildNodes());
        _propPatternCache = null; // free memory
    }


    private void parseChildren(Object obj, NodeList nodeList)
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
                log.warn("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
            }
        }
    }


    private static final Class[] CONFIG_PARAM = new Class[] {Config.class};
    private static final Class[] STRING_PARAM = new Class[] {String.class};
    private static final Class[] OBJECT_PARAM = new Class[] {Object.class};
    private static final Class[] LANG_AND_STRING_PARAM = new Class[] {String.class, String.class};

    private void setProperty(Object obj, Element elem)
    {
        if (log.isTraceEnabled()) log.trace("setProperty " + obj + " : " + elem);

        Class beanClass = obj.getClass();
        String nodeName = resolvePropertyName(elem.getNodeName());
        Object[][] searchPatterns = (Object[][]) _propPatternCache.get(nodeName);
        if (searchPatterns == null) {
            String propName = resolvePropertyName(nodeName);
            String methodNameMiddle = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
            searchPatterns = new Object [][] {
                    {"add" + methodNameMiddle + "Config", CONFIG_PARAM},
                    {"set" + methodNameMiddle + "Config", CONFIG_PARAM},
                    {"add" + methodNameMiddle, LANG_AND_STRING_PARAM},
                    {"add" + methodNameMiddle, STRING_PARAM},
                    {"set" + methodNameMiddle, STRING_PARAM},
                    {"add" + methodNameMiddle, OBJECT_PARAM},
                    {"set" + methodNameMiddle, OBJECT_PARAM}
            };

            _propPatternCache.put(nodeName, searchPatterns);
        }

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
            //throw new FacesException("Class " + beanClass + " has no set or add method for property '" + nodeName + "'.");
            log.error("Class " + beanClass + " has no set or add method for '" + nodeName + "'.");
            return;
        }

        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 1)
        {
            setProperty(obj, elem, paramTypes[0], method);
        }
        else if (paramTypes.length == 2)
        {
            // FIXME: maybe should call getAttributeNS()?
            String language = elem.getAttribute("xml:lang");
            invokeWithLang(obj, method, language, XmlUtils.getElementText(elem));
        }
        else
        {
            throw new FacesException("Object " + obj + " has illegal set or add method for '" + nodeName + "'.");
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
            invoke(obj, propWriteMethod, XmlUtils.getElementText(elem).trim());
        }
        else if (Config.class.isAssignableFrom(propType))
        {
            Config config = (Config)instantiate(propType);
            parseChildren(config, elem.getChildNodes());
            try {
                invoke(obj, propWriteMethod, config);
            } catch(Throwable t) {
                log.error("Error invoking method: " + propWriteMethod + "; for element: " + elem, t);
            }
        }
        else
        {
            // Assume class name
            String type = XmlUtils.getElementText(elem).trim();
            Class clazz = ClassUtils.classForName(type);
            invoke(obj, propWriteMethod, instantiate(clazz));
        }
    }


    private Object instantiate(Class clazz)
    {
        try
        {
            // Check to see if the class follows the decorator pattern, too bad all this reflection is in here
            if(ViewHandler.class.isAssignableFrom(clazz)){
                log.debug("Decorating view handler: " + clazz);
                // Now check if decorator pattern is followed
                try {
                    Constructor c = clazz.getConstructor(new Class[]{ViewHandler.class});
                    return c.newInstance(new ViewHandler[]{_facesConfig.getApplicationConfig().getViewHandler()});
                } catch (NoSuchMethodException e) {
                    // then not a decorator so instantiate normally
                } catch (SecurityException e) {
                    throw new FacesException("Unable to instantiate: " + clazz, e);
                }
            }
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new FacesException("Unable to instantiate:" + clazz, e);
        }
  }


    private void invoke(Object obj, Method method, Object arg)
    {
        try
        {
            method.invoke(obj, new Object[] {arg});
        }
        catch (Exception e)
        {
            throw new FacesException("base: " + obj.getClass() + "; method: " + method + "; arg: " + arg.getClass(), e);
        }
    }


    private void invokeWithLang(Object obj, Method method, String lang, Object arg)
    {
        try
        {
            method.invoke(obj, new Object[] {lang, arg});
        }
        catch (Exception e)
        {
            throw new FacesException("base: " + obj.getClass() + "; method: " + method + "; arg: " + arg.getClass(), e);
        }
    }


    public String resolvePropertyName(String elemName)
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
}
