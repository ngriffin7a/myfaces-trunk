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
package net.sourceforge.myfaces.webapp.webxml;

import net.sourceforge.myfaces.util.xml.MyFacesErrorHandler;
import net.sourceforge.myfaces.util.xml.XmlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author gem (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class WebXmlParser
{
    private static final Log log = LogFactory.getLog(WebXmlParser.class);

    private static final String WEB_XML_PATH = "/WEB-INF/web.xml";
    private static final String DEFAULT_ENCODING = "ISO-8859-1";

    private ExternalContext _context;
    private WebXml _webXml;

    public WebXmlParser(ExternalContext context)
    {
        _context = context;
    }

    public WebXml parse()
    {
        _webXml = new WebXml();

        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setValidating(true);
            dbf.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new _EntityResolver());
            db.setErrorHandler(new MyFacesErrorHandler(log));

            InputSource is = createInputSource(null, WEB_XML_PATH);

            Document document = db.parse(is);

            Element webAppElem = document.getDocumentElement();
            if (webAppElem == null ||
                !webAppElem.getNodeName().equals("web-app"))
            {
                throw new FacesException("No valid web-app root element found!");
            }

            readWebApp(webAppElem);

            return _webXml;
        }
        catch (Exception e)
        {
            log.fatal("Unable to parse web.xml", e);
            throw new FacesException(e);
        }
    }


    public InputSource createInputSource(String publicId, String systemId)
        throws IOException
    {
        InputStream inStream = _context.getResourceAsStream(systemId);
        if (inStream == null)
        {
            throw new IOException("Unable to find resource " + systemId);
        }
        InputSource is = new InputSource(inStream);
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        is.setEncoding(DEFAULT_ENCODING);
        return is;
    }


    public class _EntityResolver implements EntityResolver
    {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
        {
            if (systemId == null)
            {
                throw new UnsupportedOperationException("systenId must not be null");
            }

            if (systemId.equals("http://java.sun.com/dtd/web-app_2_2.dtd"))
            {
                return createInputSource(publicId, "javax/servlet/resources/web-app_2_2.dtd");
            }
            else if (systemId.equals("http://java.sun.com/dtd/web-app_2_3.dtd"))
            {
                return createInputSource(publicId, "javax/servlet/resources/web-app_2_3.dtd");
            }
            else
            {
                return createInputSource(publicId, systemId);
            }
        }
    }


    private void readWebApp(Element webAppElem)
    {
        NodeList nodeList = webAppElem.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                if (n.getNodeName().equals("servlet"))
                {
                    readServlet((Element)n);
                }
                if (n.getNodeName().equals("servlet-mapping"))
                {
                    readServletMapping((Element)n);
                }
            }
            else
            {
                if (log.isDebugEnabled()) log.debug("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
            }
        }
    }

    private void readServlet(Element servletElem)
    {
        String servletName = null;
        String servletClass = null;
        NodeList nodeList = servletElem.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                if (n.getNodeName().equals("servlet-name"))
                {
                    servletName = XmlUtils.getElementText((Element)n);
                }
                else if (n.getNodeName().equals("servlet-class"))
                {
                    servletClass = XmlUtils.getElementText((Element)n).trim();
                }
                else
                {
                    if (log.isWarnEnabled()) log.warn("Ignored element '" + n.getNodeName() + "' as child of '" + servletElem.getNodeName() + "'.");
                }
            }
            else
            {
                if (log.isDebugEnabled()) log.debug("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
            }
        }
        _webXml.addServlet(servletName, servletClass);
    }


    private void readServletMapping(Element servletMappingElem)
    {
        String servletName = null;
        String urlPattern = null;
        NodeList nodeList = servletMappingElem.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++)
        {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                if (n.getNodeName().equals("servlet-name"))
                {
                    servletName = XmlUtils.getElementText((Element)n);
                }
                else if (n.getNodeName().equals("url-pattern"))
                {
                    urlPattern = XmlUtils.getElementText((Element)n).trim();
                }
                else
                {
                    if (log.isWarnEnabled()) log.warn("Ignored element '" + n.getNodeName() + "' as child of '" + servletMappingElem.getNodeName() + "'.");
                }
            }
            else
            {
                if (log.isDebugEnabled()) log.debug("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
            }
        }
        _webXml.addServletMapping(servletName, urlPattern);
    }

}
