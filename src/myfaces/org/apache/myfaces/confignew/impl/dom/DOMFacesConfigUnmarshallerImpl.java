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
 * Revision 1.2  2004/07/01 22:05:15  mwessendorf
 * ASF switch
 *
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
