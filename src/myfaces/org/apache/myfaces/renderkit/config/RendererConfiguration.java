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
package net.sourceforge.myfaces.renderkit.config;

import net.sourceforge.myfaces.renderkit.attr.AttrDescrImpl;
import net.sourceforge.myfaces.util.logging.LogUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.AttributeDescriptor;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RendererConfiguration
{
    private static final String ATTRIBUTE_DESCRIPION_RESOURCE =
            "net.sourceforge.myfaces.renderkit.config.".replace('.','/') + "AttributeDescriptor.xml";
    private static final String RENDERER_RESOURCE =
            "net.sourceforge.myfaces.renderkit.config.".replace('.','/') + "Renderer.xml";

    private static final String ELEMENT_ATTR_DESCR  = "attrdesc";
    private static final String ELEMENT_ATTRIBUTE   = "attribute";
    private static final String ELEMENT_ATTRIBUTES  = "attributes";
    private static final String ELEMENT_DEFINITION  = "definition";
    private static final String ELEMENT_COLLECTION  = "collection";
    private static final String ELEMENT_COLLECTIONS = "collections";
    private static final String ELEMENT_COMPONENT   = "uiComponent";
    private static final String ELEMENT_REHDERER    = "renderer";


    private static final String ATTR_NAME           = "name";
    private static final String ATTR_COLLECTION     = "collection";
    private static final String ATTR_TYPE           = "type";
    private static final String ATTR_REF            = "ref";
    private static final String ATTR_COLLECTION_REF = "collectionRef";

    private static final int TYPE_ATTRIBUTE_MAP = 0;
    private static final int TYPE_COLLECTION_MAP = 1;
    private static final int TYPE_RENDERER_MAP = 2;

    private static final Object CLASS_LOCK = RendererConfiguration.class;

    public static Map getAttributeDescriptor(String rendererType)
    {
        Map rendererMap = getRendererMap();
        return (Map)rendererMap.get(rendererType);
    }

    private static final Map getAttributeMap()
    {
        return getMap(TYPE_ATTRIBUTE_MAP);
    }

    private static final Map getCollectionMap()
    {
        return getMap(TYPE_COLLECTION_MAP);
    }

    private static final Map getRendererMap()
    {
        return getMap(TYPE_RENDERER_MAP);
    }

    private static Map _rendererComponentMap = null;

    private static Map _attributeMap = null;
    private static Map _collectionMap = null;
    private static final Map getMap(int type)
    {
        synchronized(CLASS_LOCK)
        {
            if (_attributeMap == null)
            {
                try
                {
                    initAttributeMaps();
                    initRendererMaps();
                }
                catch (DocumentParseException e)
                {
                    _attributeMap = new HashMap(0);
                    LogUtil.getLogger().warning("Error parsing Document. " + e.getMessage() + ".");
                    return _attributeMap;
                }
            }
        }
        switch (type)
        {
            case TYPE_ATTRIBUTE_MAP: return _attributeMap;
            case TYPE_COLLECTION_MAP: return _collectionMap;
            case TYPE_RENDERER_MAP: return _rendererComponentMap;
            default: throw new IllegalArgumentException("Type " + type + " unknokn.");
        }
    }

    private static void initRendererMaps()
        throws DocumentParseException
    {
        _rendererComponentMap = new HashMap();
        Map rendererMap = new HashMap();

        Map attributeMap = getMap(TYPE_ATTRIBUTE_MAP);
        Map collectionMap = getMap(TYPE_COLLECTION_MAP);

        Document doc = DomUtil.readDcoument(RENDERER_RESOURCE);
        Element defElement = DomUtil.getElementByTagName(doc, ELEMENT_DEFINITION);
        NodeList childNodes = defElement.getChildNodes();
        for (int i = 0, size = childNodes.getLength(); i < size; i++)
        {
            Node definitionChild = childNodes.item(i);
            if (definitionChild.getNodeType() == Node.ELEMENT_NODE &&
                definitionChild.getNodeName().equals(ELEMENT_REHDERER))
            {
                ArrayList rendererAttrDescrList = new ArrayList();
                Element renderer = (Element)definitionChild;
                String rendererType = renderer.getAttribute(ATTR_TYPE);

                NodeList rendererChilds = renderer.getChildNodes();
                for (int y = 0, sizeY = rendererChilds.getLength(); y < sizeY; y++)
                {
                    Node rendererChild = rendererChilds.item(y);
                    if (rendererChild.getNodeType() == Node.ELEMENT_NODE &&
                        rendererChild.getNodeName().equals(ELEMENT_ATTRIBUTE))
                    {
                        // attribute
                        Element attribute = (Element)rendererChild;
                        addAttribute(attribute,  attributeMap, rendererAttrDescrList);
                    }
                    else if (rendererChild.getNodeType() == Node.ELEMENT_NODE &&
                          rendererChild.getNodeName().equals(ELEMENT_ATTRIBUTES))
                    {
                        // attributes
                        Element attributes = (Element)rendererChild;
                        addAttributes(attributes, collectionMap, rendererAttrDescrList);
                    }
                    else if (rendererChild.getNodeType() == Node.ELEMENT_NODE &&
                             rendererChild.getNodeName().equals(ELEMENT_COMPONENT))
                    {
                        Element component = (Element)rendererChild;
                        String componentType = component.getAttribute(ATTR_TYPE);
                        if (componentType != null && componentType.length() > 0)
                        {
                            rendererMap.put(componentType, getComponentMap(component, attributeMap, collectionMap, rendererAttrDescrList));
                        }
                        else
                        {
                            LogUtil.getLogger().warning("Attribute " + ATTR_TYPE + " was expected with Element " +
                                                        component.getNodeName() + " (ElementType = " + ELEMENT_COMPONENT + ".");
                        }
                    }
                    else if (rendererChild.getNodeType() == Node.ELEMENT_NODE)
                    {
                        LogUtil.getLogger().warning("Unknokn Element found: " + rendererChild.getNodeName() + ".");
                    }
                }
                _rendererComponentMap.put(rendererType, rendererMap);
            }
        }
    }


    private static void initAttributeMaps()
        throws DocumentParseException
    {
        _attributeMap = new HashMap();
        _collectionMap = new HashMap();

        Document doc = DomUtil.readDcoument(ATTRIBUTE_DESCRIPION_RESOURCE);
        Element defElement = DomUtil.getElementByTagName(doc, ELEMENT_DEFINITION);
        NodeList childNodes = defElement.getChildNodes();
        for (int i = 0, size = childNodes.getLength(); i < size; i++)
        {
            Node definitionNode = childNodes.item(i);
            if (definitionNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element definitionChild = (Element)definitionNode;
                if (definitionChild.getNodeName().equals(ELEMENT_ATTR_DESCR))
                {
                    // attredesc
                    String attributeName = definitionChild.getAttribute(ATTR_NAME);
                    addAttributeDescriptor(definitionChild, _attributeMap, attributeName);
                }
                else if (definitionNode.getNodeName().equals(ELEMENT_COLLECTION))
                {
                    // collection
                    String collectionName = definitionChild.getAttribute(ATTR_NAME);
                    ArrayList attributeDescriptorList = new ArrayList();

                    NodeList collectionChilds = definitionChild.getChildNodes();
                    for (int y = 0, sizeY = collectionChilds.getLength(); y < sizeY; y++)
                    {
                        Node collectionNodeChild = collectionChilds.item(y);
                        if (collectionNodeChild.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element collectionChild = (Element)collectionNodeChild;
                            if (collectionChild.getNodeName().equals(ELEMENT_COLLECTIONS))
                            {
                                addCollections(collectionChild, attributeDescriptorList);
                            }
                            else if (collectionChild.getNodeName().equals(ELEMENT_ATTR_DESCR))
                            {
                                addAttributeDescriptor(collectionChild, attributeDescriptorList);
                            }
                            else if (collectionChild.getNodeName().equals(ELEMENT_ATTRIBUTE))
                            {
                                addAttribute(collectionChild, _attributeMap, attributeDescriptorList);
                            }
                            else if (collectionChild.getNodeName().equals(ELEMENT_ATTRIBUTES))
                            {
                                addAttributes(collectionChild, _collectionMap, attributeDescriptorList);
                            }
                            else
                            {
                                LogUtil.getLogger().warning("Unknokn Element found: " + definitionChild.getNodeName() + ".");
                            }
                        }
                    }
                    _collectionMap.put(collectionName, attributeDescriptorList);
                }
            }
        }
    }

    private static Map getComponentMap(Element e, Map attributeMap, Map collectionMap, ArrayList rendererAttributeList)
    {
        if (!e.getNodeName().equals(ELEMENT_COMPONENT))
        {
            throw new IllegalArgumentException("Element must be of type " + ELEMENT_COMPONENT);
        }

        ArrayList attributeDescriptorList = new ArrayList();

        NodeList componentChilds = e.getChildNodes();
        for (int i = 0, size = componentChilds.getLength(); i < size; i++)
        {
            Node componentChild = componentChilds.item(i);
            if (componentChild.getNodeType() == Node.ELEMENT_NODE &&
                componentChild.getNodeName().equals(ELEMENT_ATTRIBUTE))
            {
                // attribute
                Element attribute = (Element)componentChild;
                addAttribute(attribute,  attributeMap, attributeDescriptorList);
            }
            else if (componentChild.getNodeType() == Node.ELEMENT_NODE &&
                  componentChild.getNodeName().equals(ELEMENT_ATTRIBUTES))
            {
                // attributes
                Element attributes = (Element)componentChild;
                addAttributes(attributes, collectionMap, attributeDescriptorList);
            }
        }

        for (int i = 0, size= rendererAttributeList.size(); i < size; i++)
        {
            attributeDescriptorList.add(rendererAttributeList.get(i));
        }

        Map map = new HashMap(attributeDescriptorList.size());
        for (int i = 0, size = attributeDescriptorList.size(); i < size; i++)
        {
            AttributeDescriptor descr = (AttributeDescriptor)attributeDescriptorList.get(i);
            map.put(descr.getName(), descr);
        }
        return map;
    }

    private static void addAttribute(Element attribute, Map attributeMap, List attributeDescriptorList)
    {
        // attribute
        String attributeName = attribute.getAttribute(ATTR_REF);
        if (attributeName != null && attributeName.length() > 0)
        {
            AttributeDescriptor descr = (AttributeDescriptor)attributeMap.get(attributeName);
            if (descr != null)
            {
                attributeDescriptorList.add(descr);
            }
            else
            {
                LogUtil.getLogger().warning("AttributeDescriptor for Attribute with name " + attributeName +" not found.");
            }
        }
    }

    private static void addAttributes(Element attribute, Map collectionMap, List attributeDescriptorList)
    {
        String collectionName = attribute.getAttribute(ATTR_COLLECTION_REF);
        if (collectionName != null && collectionName.length() > 0)
        {
            ArrayList descrList = (ArrayList)collectionMap.get(collectionName);
            if (descrList != null)
            {
                attributeDescriptorList.addAll(descrList);
            }
            else
            {
                LogUtil.getLogger().warning("AttributeDescriptors-Collection with name " + collectionName +" not found.");
            }
        }
    }

    private static void addCollections(Element e, List attributeDescriptorList)
    {
        if (!e.getNodeName().equals(ELEMENT_COLLECTIONS))
        {
            throw new IllegalArgumentException("Element must be of type " + ELEMENT_COLLECTIONS);
        }
        Map collectionMap = getCollectionMap();

        String value = DomUtil.getText(e);
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        while (tokenizer.hasMoreTokens())
        {
            String collectionName = tokenizer.nextToken().trim();
            ArrayList list = (ArrayList)collectionMap.get(collectionName);
            if (list != null && list.size() > 0)
            {
                attributeDescriptorList.addAll(list);
            }
        }
    }

    private static void addAttributeDescriptor(Element e, List list)
    {
        AttributeDescriptor descr = getAttributeDescriptor(e);
        if (descr != null)
        {
            list.add(descr);
        }
    }

    private static void addAttributeDescriptor(Element e, Map map, String key)
    {
        AttributeDescriptor descr = getAttributeDescriptor(e);
        if (descr != null)
        {
            map.put(key, descr);
        }
    }

    private static AttributeDescriptor getAttributeDescriptor(Element e)
    {
        if (!e.getNodeName().equals(ELEMENT_ATTR_DESCR))
        {
            throw new IllegalArgumentException("Element must be of type " + ELEMENT_ATTR_DESCR);
        }

        String attributeName = e.getAttribute(ATTR_NAME);
        if (attributeName != null && attributeName.length() > 0)
        {
            String className = e.getAttribute(ATTR_TYPE);
            Class c = null;
            if (className != null && className.length() > 0)
            {
                try
                {
                    c = RendererConfiguration.class.getClassLoader().loadClass(className);
                }
                catch (ClassNotFoundException e1)
                {
                    LogUtil.getLogger().warning("Class not found " + className + ".");
                    return null;
                }
            }
            return c != null ? new AttrDescrImpl(attributeName, c) : new AttrDescrImpl(attributeName);
        }
        return null;
    }

    public static void main(String[] argv)
    {
        Map map = getAttributeMap();
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext(); )
        {
            AttributeDescriptor descr = (AttributeDescriptor)map.get(it.next());
            System.out.println("Attribute: " + descr.getName() + " type: " + descr.getType().getName());
        }

        map = getCollectionMap();
        keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext(); )
        {
            String collectionName = (String)it.next();
            System.out.println("Collection: " + collectionName);
            ArrayList descrList = (ArrayList)map.get(collectionName);
            for (int i = 0, size = descrList.size(); i < size; i++)
            {
                AttributeDescriptor descr = (AttributeDescriptor)descrList.get(i);
                System.out.println("   Attribute: " + descr.getName() + " type: " + descr.getType().getName());
            }
        }

        System.out.println("Renderer: Image");
        Map rendererMap = getAttributeDescriptor("Image");
        keySet = rendererMap.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext(); )
        {
            String componentType = (String)it.next();
            System.out.println("   Component: " + componentType);
            Map attributeMap = (Map)rendererMap.get(componentType);
            keySet = attributeMap.keySet();
            for (Iterator attrIt = keySet.iterator(); attrIt.hasNext(); )
            {
                AttributeDescriptor descr = (AttributeDescriptor)attributeMap.get(attrIt.next());
                System.out.println("       Attribute: " + descr.getName() + " type: " + descr.getType().getName());
            }
        }
    }

}

