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

import net.sourceforge.myfaces.confignew.FacesConfigUnmarshaller;
import net.sourceforge.myfaces.util.xml.MyFacesErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Concrete implementation of a FacesConfigUnmarshaller that uses a DOM Parser for
 * unmarshalling the faces config.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/17 14:28:27  manolito
 * new configuration concept
 *
 */
public class DOMFacesConfigUnmarshallerImpl
        implements FacesConfigUnmarshaller
{
    private static final Log log = LogFactory.getLog(DOMFacesConfigUnmarshallerImpl.class);

    private FacesConfigEntityResolver _entityResolver;

    public DOMFacesConfigUnmarshallerImpl(ExternalContext externalContext)
    {
        _entityResolver = new FacesConfigEntityResolver(externalContext);
    }

    public Object getFacesConfig(InputStream in,
                                 String systemId)
    {
        if (in == null) throw new NullPointerException("InputStream must not be null.");
        if (systemId == null) throw new NullPointerException("systemId must not be null.");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setValidating(true);    //TODO: config parameter
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
        db.setEntityResolver(_entityResolver);
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

        return facesConfigElem;
    }
}
