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

package net.sourceforge.myfaces.confignew.impl.dom;

import net.sourceforge.myfaces.confignew.FacesConfigDispenser;
import net.sourceforge.myfaces.util.NullIterator;
import net.sourceforge.myfaces.util.ReverseListIterator;
import net.sourceforge.myfaces.util.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import java.util.*;

/**
 * Concrete implementation of the FacesConfigDispenser that belongs to the
 * {@link DOMFacesConfigUnmarshallerImpl} and is able to process DOM element nodes.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/17 14:28:27  manolito
 * new configuration concept
 *
 */
public class DOMFacesConfigDispenserImpl
        implements FacesConfigDispenser
{
    private static final Log log = LogFactory.getLog(DOMFacesConfigDispenserImpl.class);

    private Map _factories = new HashMap();
    private Map _applicationObjects = new HashMap();
    private String _defaultRenderKitId = null;
    private String _messageBundle = null;
    private String _defaultLocale = null;
    private List _supportedLocales = null;

    private Map _componentClasses = new HashMap();
    private Map _convertersById = new HashMap();
    private Map _convertersByClass = new HashMap();
    private Map _validatorClasses = new HashMap();


    public void feed(Object facesConfig)
    {
        if (!(facesConfig instanceof Element))
        {
            throw new IllegalArgumentException("Unsupported faces config type " + facesConfig.getClass().getName());
        }
        Element facesConfigElem = (Element)facesConfig;

        NodeList nodeList;

        // <!ELEMENT faces-config    ((application|factory|component|converter|managed-bean|navigation-rule|referenced-bean|render-kit|lifecycle|validator)*)>

        //application elements (there can be more than one!)
        nodeList = facesConfigElem.getElementsByTagName("application");
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            feedApplicationElement((Element)nodeList.item(i));
        }

        //factory elements (there can be more than one!)
        nodeList = facesConfigElem.getElementsByTagName("factory");
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            feedFactoryElement((Element)nodeList.item(i));
        }

        // component elements
        nodeList = facesConfigElem.getElementsByTagName("component");
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            feedComponentElement((Element)nodeList.item(i));
        }

        //TODO: converter elements
        //TODO: managed-bean elements
        //TODO: navigation-rule elements
        //TODO: referenced-bean elements
        //TODO: render-kit elements
        //TODO: lifecycle elements
        //TODO: validator elements
    }

    public void feedApplicationFactory(String factoryClassName)
    {
        addFactory(FactoryFinder.APPLICATION_FACTORY, factoryClassName);
    }

    public void feedFacesContextFactory(String factoryClassName)
    {
        addFactory(FactoryFinder.FACES_CONTEXT_FACTORY, factoryClassName);
    }

    public void feedLifecycleFactory(String factoryClassName)
    {
        addFactory(FactoryFinder.LIFECYCLE_FACTORY, factoryClassName);
    }

    public void feedRenderKitFactory(String factoryClassName)
    {
        addFactory(FactoryFinder.RENDER_KIT_FACTORY, factoryClassName);
    }


    private void feedFactoryElement(Element factoryElem)
    {
        // <!ELEMENT factory     ((application-factory|faces-context-factory|lifecycle-factory|render-kit-factory)*)>
        NodeList factoryChildNodes = factoryElem.getChildNodes();
        for (int i = 0, len = factoryChildNodes.getLength(); i < len; i++)
        {
            Node node = (Node)factoryChildNodes.item(i);
            if (node instanceof Element)
            {
                String factoryName = node.getLocalName();
                addFactory(factoryName, XmlUtils.getElementText((Element)node));
            }
        }
    }

    private void addFactory(String name, String className)
    {
        List lst = (List)_factories.get(name);
        if (lst == null)
        {
            lst = new ArrayList();
            _factories.put(name, lst);
        }
        lst.add(className);
    }

    private void feedApplicationElement(Element factoryElem)
    {
        // <!ELEMENT application     ((action-listener|default-render-kit-id|message-bundle|navigation-handler|view-handler|state-manager|property-resolver|variable-resolver|locale-config)*)>
        NodeList applicationChildNodes = factoryElem.getChildNodes();
        for (int i = 0, len = applicationChildNodes.getLength(); i < len; i++)
        {
            Node node = (Node)applicationChildNodes.item(i);
            if (node instanceof Element)
            {
                String nodeName = node.getLocalName();
                if (nodeName.equals("default-render-kit-id"))
                {
                    _defaultRenderKitId = XmlUtils.getElementText((Element)node);
                }
                else if (nodeName.equals("message-bundle"))
                {
                    _messageBundle = XmlUtils.getElementText((Element)node);
                }
                else if (nodeName.equals("locale-config"))
                {
                    feedLocaleConfig((Element)node);
                }
                else
                {
                    //one of the application Objects
                    List lst = (List)_applicationObjects.get(nodeName);
                    if (lst == null)
                    {
                        lst = new ArrayList();
                        _applicationObjects.put(nodeName, lst);
                    }
                    lst.add(XmlUtils.getElementText((Element)node));
                }
            }
        }
    }

    private void feedLocaleConfig(Element localeConfigElem)
    {
        // <!ELEMENT locale-config (default-locale?, supported-locale*)>
        NodeList childNodes = localeConfigElem.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++)
        {
            Node node = (Node)childNodes.item(i);
            if (node instanceof Element)
            {
                String nodeName = node.getLocalName();
                if (nodeName.equals("default-locale"))
                {
                    _defaultLocale = XmlUtils.getElementText((Element)node);
                }
                else if (nodeName.equals("supported-locale"))
                {
                    if (_supportedLocales == null)
                    {
                        _supportedLocales = new ArrayList();
                    }
                    _supportedLocales.add(XmlUtils.getElementText((Element)node));
                }
                else
                {
                    log.warn("Unknown locale-config child element " + nodeName);
                }
            }
        }
    }


    private void feedComponentElement(Element componentElement)
    {
        // <!ELEMENT component       (description*, display-name*, icon*, component-type, component-class, attribute*, property*, component-extension*)>
        String componentType = null;
        String componentClass = null;
        NodeList childNodes = componentElement.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++)
        {
            Node node = (Node)childNodes.item(i);
            if (node instanceof Element)
            {
                String nodeName = node.getLocalName();
                if (nodeName.equals("component-type"))
                {
                    componentType = XmlUtils.getElementText((Element)node);
                }
                else if (nodeName.equals("component-class"))
                {
                    componentClass = XmlUtils.getElementText((Element)node);
                }
            }
        }

        if (componentType == null || componentClass == null)
        {
            throw new FacesException("Error in faces config: component element without component-type and/or component-class");
        }

        _componentClasses.put(componentType, componentClass);
    }




    // FacesConfigDispenser interface getters

    public Iterator getApplicationFactoryIterator()
    {
        List lst = (List)_factories.get("application-factory");
        return lst == null ? NullIterator.instance() : lst.iterator();
    }

    public Iterator getFacesContextFactoryIterator()
    {
        List lst = (List)_factories.get("faces-context-factory");
        return lst == null ? NullIterator.instance() : lst.iterator();
    }

    public Iterator getLifecycleFactoryIterator()
    {
        List lst = (List)_factories.get("lifecycle-factory");
        return lst == null ? NullIterator.instance() : lst.iterator();
    }

    public Iterator getRenderKitFactoryIterator()
    {
        List lst = (List)_factories.get("renderkit-factory");
        return lst == null ? NullIterator.instance() : lst.iterator();
    }


    public Iterator getActionListenerIterator()
    {
        List lst = (List)_applicationObjects.get("action-listener");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public String getDefaultRenderKitId()
    {
        return _defaultRenderKitId;
    }

    public String getMessageBundle()
    {
        return _messageBundle;
    }

    public Iterator getNavigationHandlerIterator()
    {
        List lst = (List)_applicationObjects.get("navigation-handler");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public Iterator getViewHandlerIterator()
    {
        List lst = (List)_applicationObjects.get("view-handler");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public Iterator getStateManagerIterator()
    {
        List lst = (List)_applicationObjects.get("state-manager");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public Iterator getPropertyResolverIterator()
    {
        List lst = (List)_applicationObjects.get("property-resolver");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public Iterator getVariableResolverIterator()
    {
        List lst = (List)_applicationObjects.get("variable-resolver");
        return lst == null ? NullIterator.instance() : new ReverseListIterator(lst);
    }

    public String getDefaultLocale()
    {
        return _defaultLocale;
    }

    public Iterator getSupportedLocalesIterator()
    {
        return _supportedLocales == null
                ? NullIterator.instance()
                : _supportedLocales.iterator();
    }

    public Iterator getComponentTypes()
    {
        return _componentClasses.keySet().iterator();
    }

    public String getComponentClass(String componentType)
    {
        return (String)_componentClasses.get(componentType);
    }

    public Iterator getConverterIds()
    {
        return _convertersById.keySet().iterator();
    }

    public Iterator getConverterClasses()
    {
        return _convertersByClass.keySet().iterator();
    }

    public String getConverterClassById(String converterId)
    {
        return (String)_convertersById.get(converterId);
    }

    public String getConverterClassByClass(String className)
    {
        return (String)_convertersByClass.get(className);
    }

    public Iterator getValidatorIds()
    {
        return _validatorClasses.keySet().iterator();
    }

    public String getValidatorClass(String validatorId)
    {
        return (String)_validatorClasses.get(validatorId);
    }


    public Iterator getManagedBeans()
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    public Iterator getNavigationRules()
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    public Iterator getRenderKitIds()
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    public String getRenderKitClass(String renderKitId)
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    public Iterator getRenderers(String renderKitId)
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    public Iterator getLifecyclePhaseListeners()
    {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

}
