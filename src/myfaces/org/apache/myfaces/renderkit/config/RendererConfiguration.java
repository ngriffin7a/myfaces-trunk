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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RendererConfiguration
{
    private static final String ATTRIBUTE_DESCRIPION_RESOURCE =
            "net.sourceforge.myfaces.renderkit.config".replace('.','/') + "/AttributeDescriptor.xml";
    private static final String RENDERER_RESOURCE =
            "net.sourceforge.myfaces.renderkit.config".replace('.','/') + "/Renderer.xml";

    private static final String ELEMENT_ATTR_DESCR = "attrdesc";
    private static final String ELEMENT_COLLECTION = "collection";
    private static final String ELEMENT_DEFINITION = "definition";
    private static final String ELEMENT_REHDERER = "renderer";
    private static final String ELEMENT_ATTRIBUTE = "attribute";
    private static final String ELEMENT_ATTRIBUTES = "attributes";
    private static final String ELEMENT_COMPONENT = "component";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_COLLECTION = "collection";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_VALUE = "value";
    private static final String ATTRIBUTE_DESCRIPTOR = "attributeDescriptor";
    private static final String ATTRIBUTE_DESCRIPTOR_ARRAY = "collection";

    private static final int TYPE_ATTRIBUTE_MAP = 0;
    private static final int TYPE_COLLECTION_MAP = 1;
    private static final int TYPE_RENDERER_MAP = 2;
    private static final int TYPE_COMPONENT_MAP = 3;

    private static final Object CLASS_LOCK = RendererConfiguration.class;

    public static AttributeDescriptor[] getAttributeDescriptors(FacesContext context, String rendererType)
    {
        Map rendererMap = getRendererMap();
        ArrayList list = (ArrayList)rendererMap.get(rendererType);
        if (list == null || list.size() == 0)
        {
            return new AttributeDescriptor[] {};
        }
        return (AttributeDescriptor[])list.toArray(new AttributeDescriptor[] {});
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

    private static Map _rendererMap = null;
    private static Map _componentMap = null;

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
                    // TODO: log
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                    return _attributeMap;
                }
            }
        }
        switch (type)
        {
            case TYPE_ATTRIBUTE_MAP: return _attributeMap;
            case TYPE_COLLECTION_MAP: return _collectionMap;
            case TYPE_RENDERER_MAP: return _rendererMap;
            case TYPE_COMPONENT_MAP: return _componentMap;
            default: throw new IllegalArgumentException("Type " + type + " unknokn.");
        }
    }

    private static void initRendererMaps()
        throws DocumentParseException
    {
        _rendererMap = new HashMap();
        _componentMap = new HashMap();

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
                ArrayList attributeDescriptions = new ArrayList();
                Element renderer = (Element)definitionChild;
                String rendererType = renderer.getAttribute(ATTR_TYPE);

                NodeList rendererChilds = renderer.getChildNodes();
                for (int y = 0, sizeY = rendererChilds.getLength(); y < sizeY; y++)
                {
                    Node rendererChild = rendererChilds.item(y);
                    if (rendererChild.getNodeType() == Node.ELEMENT_NODE &&
                        rendererChild.getNodeName().equals(ELEMENT_ATTRIBUTE))
                    {
                        Element attribute = (Element)rendererChild;
                        String attributeName = attribute.getAttribute(ATTR_NAME);
                        if (attributeName != null && attributeName.length() > 0)
                        {
                            AttributeDescriptor descr = (AttributeDescriptor)attributeMap.get(attributeName);
                            if (descr != null)
                            {
                                attributeDescriptions.add(descr);
                            }
                            else
                            {
                                //TODO: log
                            }
                        }
                    }
                    else if (rendererChild.getNodeType() == Node.ELEMENT_NODE &&
                          rendererChild.getNodeName().equals(ELEMENT_ATTRIBUTES))
                    {
                        Element attribute = (Element)rendererChild;
                        String collectionName = attribute.getAttribute(ATTR_COLLECTION);
                        if (collectionName != null && collectionName.length() > 0)
                        {
                            ArrayList descrList = (ArrayList)collectionMap.get(collectionName);
                            if (descrList != null)
                            {
                                attributeDescriptions.addAll(descrList);
                            }
                            else
                            {
                                //TODO: log
                            }
                        }
                    }
                }
                _rendererMap.put(rendererType, attributeDescriptions);
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
            Node n = childNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                if (n.getNodeName().equals(ELEMENT_ATTR_DESCR))
                {
                    Element e = (Element)n;
                    String attributeName = e.getAttribute(ATTR_NAME);
                    if (attributeName != null && attributeName.length() > 0)
                    {
                        String className = e.getAttribute(ATTR_TYPE);
                        Class c = null;
                        try
                        {
                            c = RendererConfiguration.class.getClassLoader().loadClass(className);
                        } catch (ClassNotFoundException e1)
                        {
                            //TODO: log
                        }
                        _attributeMap.put(attributeName,
                                          c != null ? new AttrDescrImpl(attributeName, c) : new AttrDescrImpl(attributeName));
                    }

                }
                else if (n.getNodeName().equals(ELEMENT_COLLECTION))
                {
                    Element e = (Element)n;
                    String collectionName = e.getAttribute(ATTR_NAME);
                    String collectionType = e.getAttribute(ATTR_TYPE);
                    if (collectionName != null && collectionName.length() > 0 && collectionType != null)
                    {
                        String value = e.getAttribute(ATTR_VALUE);
                        if (value != null)
                        {
                            StringTokenizer tokenizer = new StringTokenizer(value, ",");
                            ArrayList descrList = new ArrayList(tokenizer.countTokens());
                            if (collectionType.equals(ATTRIBUTE_DESCRIPTOR))
                            {
                                for (int y = 0; tokenizer.hasMoreTokens(); y++)
                                {
                                    String attributeName = tokenizer.nextToken();
                                    AttributeDescriptor descr = (AttributeDescriptor)_attributeMap.get(attributeName);
                                    if (descr == null)
                                    {
                                        // TODO: log
                                    }
                                    else
                                    {
                                        descrList.add(descr);
                                    }
                                }
                            }
                            else if (collectionType.equals(ATTRIBUTE_DESCRIPTOR_ARRAY))
                            {
                                for (int y = 0; tokenizer.hasMoreTokens(); y++)
                                {
                                    String attributeName = tokenizer.nextToken();
                                    ArrayList descrArray = (ArrayList)_collectionMap.get(attributeName);
                                    if (descrArray != null && descrArray.size() > 0)
                                    {
                                        descrList.addAll(descrArray);
                                    }
                                }
                            }
                            else
                            {
                                // TODO: log
                            }
                            _collectionMap.put(collectionName, descrList);
                        }
                    }
                }
            }
        }
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

        map = getRendererMap();
        keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext(); )
        {
            String rendererName = (String)it.next();
            System.out.println("Renderer: " + rendererName);
            ArrayList descrList = (ArrayList)map.get(rendererName);
            for (int i = 0, size = descrList.size(); i < size; i++)
            {
                AttributeDescriptor descr = (AttributeDescriptor)descrList.get(i);
                System.out.println("   Attribute: " + descr.getName() + " type: " + descr.getType().getName());
            }
        }

        AttributeDescriptor[] descr = getAttributeDescriptors(null, "Image");
        System.out.println("--> Renderer: Image");
        for (int i = 0; i < descr.length; i++)
        {
            System.out.println("   Attribute: " + descr[i].getName() + " type: " + descr[i].getType().getName());
        }

    }

}
