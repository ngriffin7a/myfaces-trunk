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
package net.sourceforge.myfaces.webapp.filter;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Robert J. Lebowitz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class WelcomeFileHandler extends DefaultHandler {
    private Vector welcomeFiles; 
    private boolean fileFlag = false;
    private String[] files;
    private StringBuffer sb = new StringBuffer();

    /**
     * Creates a new WelcomeFileHandler object.
     */
    public WelcomeFileHandler() {
        super();
    }

    /**
     *
     *  Method called with each start element in an XML document
     * @param ns The namespace associated with this element
     * @param local The local name of this element
     * @param qName The qualified name of this element
     * @param atts Attributes associated with this element
     * @throws SAXException 
     *
     */
    public void startElement(String ns, String local, String qName,
        Attributes atts) throws SAXException {
        if (qName.equals("welcome-file-list")) {
            welcomeFiles = new Vector();
        }

        if (qName.equals("welcome-file")) {
        	sb.setLength(0);
            fileFlag = true;
        }

        super.startElement(ns, local, qName, atts);
    }

    /**
     * Method called with each end element in an XML Document
     * @param ns The namespace associated with this element
     * @param local The local name of this element
     * @param qName The qualified name of this element
     * @throws SAXException
     *
     */
    public void endElement(String ns, String local, String qName)
        throws SAXException {
        if (qName.equals("welcome-file-list")) {
            files = new String[welcomeFiles.size()];
            welcomeFiles.toArray(files);
            welcomeFiles = null;
        }

        if (qName.equals("welcome-file")) {
        	welcomeFiles.add(sb.toString());
        	sb.setLength(0);
            fileFlag = false;
        }
        super.endElement(ns, local, qName);
    }

    /**
     *
     * Method used to examine, modify or extract text in body of an element
     * @param ch character array containing the tag's body information.
     * @param start starting index of the body text in the character array.
     * @param length length of the text found in the body.
     * @throws SAXException 
     *
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        if (fileFlag) {
            sb.append(ch, start, length);
        }
        super.characters(ch, start, length);
    }

    /**
     * Accessor method used to get the array of welcome files.
     * @return The string array of welcome files.
     *
     */
    public String[] getWelcomeFiles() {
        return files;
    }
}
