package net.sourceforge.myfaces.util.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class XmlUtils
{
    private XmlUtils() 
    {
        // hide from public access
    }

    public static String getElementText(Element elem)
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
                //TODO see jsf-samples
                //throw new FacesException("Unexpected node type " + n.getNodeType());
            }
        }
        return buf.toString();
    }



    /**
     * Return content of child element with given tag name.
     * If more than one children with this name are present, the content of the last
     * element is returned.
     *
     * @param elem
     * @param childTagName
     * @return content of child element or null if no child element with this name was found
     */
    public static String getChildText(Element elem, String childTagName)
    {
        NodeList nodeList = elem.getElementsByTagName(childTagName);
        int len = nodeList.getLength();
        if (len == 0)
        {
            return null;
        }
        else
        {
            return getElementText((Element)nodeList.item(len - 1));
        }
   }


    /**
     * Return list of content Strings of all child elements with given tag name.
     * @param elem
     * @param childTagName
     * @return 
     */
    public static List getChildTextList(Element elem, String childTagName)
    {
        NodeList nodeList = elem.getElementsByTagName(childTagName);
        int len = nodeList.getLength();
        if (len == 0)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            List list = new ArrayList(len);
            for (int i = 0; i < len; i++)
            {
                list.add(getElementText((Element)nodeList.item(i)));
            }
            return list;
        }
   }

}
