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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import net.sourceforge.myfaces.renderkit.attr.DocumentParseException;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DomUtil
{
    private static final SimpleDateFormat _dateformat
        = new SimpleDateFormat("yyyy-MM-dd");
    private static final NumberFormat _nubmerformat = NumberFormat.getInstance(Locale.US);

    //------------------------------------------------------------------------
    // DomUtil
    //------------------------------------------------------------------------
    public static Element getElementByTagName(Document doc, String name)
        throws DocumentParseException
    {
        NodeList list = doc.getElementsByTagName(name);
        if (list == null || list.getLength() != 1)
        {
            int anz = list == null ? 0 : list.getLength();
            throw new DocumentParseException("Element " + name + " kann nicht ermittelt werden. Anzahl gefundene Elemente: " + anz);
        }
        return (Element)list.item(0);
    }

    public static Element getElementByTagName(Node parent, String name)
        throws DocumentParseException
    {
        NodeList list = parent.getChildNodes();

        if (list == null || list.getLength() == 0)
        {
            throw new DocumentParseException("Element " + name + " kann ausgehend vom Knoten " + parent.getNodeName() + " nicht ermittelt werden. Anzahl gefundene Elemente: 0");
        }
        for (int i = 0, size = list.getLength(); i < size; i++)
        {
            Node n = list.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE &&
                n.getNodeName().equals(name))
            {
                return (Element)n;
            }
        }
        throw new DocumentParseException("Element " + name + " kann ausgehend vom Knoten " + parent.getNodeName() + " nicht ermittelt werden.");
    }


    public static BigDecimal getTextAsBigDecimalNoEx(Node node, String elementName, int scale)
    {
        try
        {
            Element e = getElementByTagName(node, elementName);
            if (e == null)
            {
                return null;
            }
            return getTextAsBigDecimal(e, scale);
        }
        catch (DocumentParseException ex)
        {
            return null;
        }
    }

    public static Date getTextAsDateNoEx(Node node, String elementName)
    {
        try
        {
            Element e = getElementByTagName(node, elementName);
            if (e == null)
            {
                return null;
            }
            return getTextAsDate(e);
        }
        catch (DocumentParseException ex)
        {
            return null;
        }
    }

    public static String getTextNoEx(Node node, String elementName)
    {
        try
        {
            Element e = getElementByTagName(node, elementName);
            if (e == null)
            {
                return null;
            }
            return getText(e);
        }
        catch (DocumentParseException ex)
        {
            return null;
        }
    }

    public static String getText(Node n)
    {
        StringBuffer buf = new StringBuffer();
        NodeList list = n.getChildNodes();
        for (int i = 0, size = list.getLength(); i < size; i++)
        {
            Node text = list.item(i);
            if (text.getNodeType() == Node.TEXT_NODE ||
                text.getNodeType() == Node.ENTITY_NODE ||
                text.getNodeType() == Node.ENTITY_REFERENCE_NODE)
            {
                buf.append(text.getNodeValue());
            }
        }
        return buf.toString();
    }

    public static Date getTextAsDate(Node n)
        throws DocumentParseException
    {
        NodeList list = n.getChildNodes();
        if (list.getLength()>0)
        {
            Node text = list.item(0);
            try
            {
                return _dateformat.parse(text.getNodeValue());
            }
            catch (ParseException ex)
            {
                throw new DocumentParseException(ex);
            }
        }
        return null;
    }

    public static BigDecimal getTextAsBigDecimal(Node n, int scale)
        throws DocumentParseException
    {
        NodeList list = n.getChildNodes();
        boolean negate = false;
        if (list.getLength()>0)
        {
            Node text = list.item(0);
            String value = text.getNodeValue();
            if (value.charAt(value.length()-1) == '-')
            {
                value = value.substring(0, value.length()-2);
                negate = true;
            }
            try
            {
                Number num = _nubmerformat.parse(text.getNodeValue());
                if (negate)
                {
                    BigDecimal big = new BigDecimal(-num.doubleValue());
                    return  scale >= 0 ? big.setScale(scale, BigDecimal.ROUND_HALF_UP) : big;
                }
                BigDecimal big = new BigDecimal(num.doubleValue());
                return scale >= 0 ? big.setScale(scale, BigDecimal.ROUND_HALF_UP) : big;
            }
            catch (ParseException ex)
            {
                throw new DocumentParseException(ex);
            }
        }
        return null;
    }

    public static String removeTrailingZeros(String value)
    {
        for (int i=0; i<value.length(); i++)
        {
            if (value.charAt(i) != '0')
            {
                return value.substring(i);
            }
        }
        return "0";
    }

    public static String appendLeadingZeros(String value, int len)
    {
        StringBuffer buf = new StringBuffer(value);
        while (buf.length() < len)
        {
            buf.insert(0, "0");
        }
        return buf.toString();
    }


    private static DocumentBuilderFactory _docbuilderFactory = null;
    /**
     *
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException FactoryConfigurationError
     */
    public static synchronized DocumentBuilder getDocumentBuilder()
        throws ParserConfigurationException
    {
        if (_docbuilderFactory == null)
        {
            _docbuilderFactory = DocumentBuilderFactory.newInstance();
        }
        return _docbuilderFactory.newDocumentBuilder();
    }

    public static Document readDcoument(String resourceName)
        throws DocumentParseException
    {
        ClassLoader loader = DomUtil.class.getClassLoader();
        InputStream stream = loader.getResourceAsStream(resourceName);
        if (stream == null)
        {
            throw new DocumentParseException("Template " + resourceName + " could not be found.");
        }
        BufferedReader reader;
        reader =
            new BufferedReader(new InputStreamReader(stream));
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(reader);

        DocumentBuilder parser = null;
        try
        {
            parser = DomUtil.getDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw new DocumentParseException(e);
        }

        try
        {
            return parser.parse(inputSource);
        }
        catch (SAXException e)
        {
            throw new DocumentParseException(e);
        }
        catch (IOException e)
        {
            throw new DocumentParseException(e);
        }
    }


}
