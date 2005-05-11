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
package org.apache.myfaces.renderkit.html;

import org.apache.myfaces.renderkit.MyfacesResponseStateManager;
import org.apache.myfaces.util.MyFacesObjectInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlResponseStateManager
        extends MyfacesResponseStateManager
{
    private static final Log log = LogFactory.getLog(HtmlResponseStateManager.class);

    private static final String TREE_PARAM = "jsf_tree";
    private static final String STATE_PARAM = "jsf_state";
    private static final String VIEWID_PARAM = "jsf_viewid";
    private static final String BASE64_TREE_PARAM = "jsf_tree_64";
    private static final String BASE64_STATE_PARAM = "jsf_state_64";
    private static final String ZIP_CHARSET = "ISO-8859-1";

    public void writeState(FacesContext facescontext,
                           StateManager.SerializedView serializedview) throws IOException
    {
        ResponseWriter responseWriter = facescontext.getResponseWriter();
        Object treeStruct = serializedview.getStructure();
        Object compStates = serializedview.getState();

        if (treeStruct != null)
        {
            if (treeStruct instanceof String)
            {
                responseWriter.startElement(HTML.INPUT_ELEM, null);
                responseWriter.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                responseWriter.writeAttribute(HTML.NAME_ATTR, TREE_PARAM, null);
                responseWriter.writeAttribute(HTML.VALUE_ATTR, treeStruct, null);
                responseWriter.endElement(HTML.INPUT_ELEM);
            }
            else
            {
                responseWriter.startElement(HTML.INPUT_ELEM, null);
                responseWriter.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                responseWriter.writeAttribute(HTML.NAME_ATTR, BASE64_TREE_PARAM, null);
                responseWriter.writeAttribute(HTML.VALUE_ATTR, encode64(treeStruct), null);
                responseWriter.endElement(HTML.INPUT_ELEM);
            }
        }
        else
        {
            log.error("No tree structure to be saved in client response!");
        }

        if (compStates != null)
        {
            if (compStates instanceof String)
            {
                responseWriter.startElement(HTML.INPUT_ELEM, null);
                responseWriter.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                responseWriter.writeAttribute(HTML.NAME_ATTR, STATE_PARAM, null);
                responseWriter.writeAttribute(HTML.VALUE_ATTR, compStates, null);
                responseWriter.endElement(HTML.INPUT_ELEM);
            }
            else
            {
                responseWriter.startElement(HTML.INPUT_ELEM, null);
                responseWriter.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                responseWriter.writeAttribute(HTML.NAME_ATTR, BASE64_STATE_PARAM, null);
                responseWriter.writeAttribute(HTML.VALUE_ATTR, encode64(compStates), null);
                responseWriter.endElement(HTML.INPUT_ELEM);
            }
        }
        else
        {
            log.error("No component states to be saved in client response!");
        }

        responseWriter.startElement(HTML.INPUT_ELEM, null);
        responseWriter.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        responseWriter.writeAttribute(HTML.NAME_ATTR, VIEWID_PARAM, null);
        responseWriter.writeAttribute(HTML.VALUE_ATTR, facescontext.getViewRoot().getViewId(), null);
        responseWriter.endElement(HTML.INPUT_ELEM);
    }

    /**
     * MyFaces extension
     * @param facescontext
     * @param serializedview
     * @throws IOException
     */
    public void writeStateAsUrlParams(FacesContext facescontext,
                                      StateManager.SerializedView serializedview) throws IOException
    {
        ResponseWriter responseWriter = facescontext.getResponseWriter();
        Object treeStruct = serializedview.getStructure();
        Object compStates = serializedview.getState();

        if (treeStruct != null)
        {
            if (treeStruct instanceof String)
            {
                writeStateParam(responseWriter, TREE_PARAM, (String)treeStruct);
            }
            else
            {
                writeStateParam(responseWriter, BASE64_TREE_PARAM, encode64(treeStruct));
            }
        }
        else
        {
            log.error("No tree structure to be saved in client response!");
        }

        if (compStates != null)
        {
            if (treeStruct != null)
            {
                responseWriter.write('&');
            }
            if (compStates instanceof String)
            {
                writeStateParam(responseWriter, STATE_PARAM, (String)compStates);
            }
            else
            {
                writeStateParam(responseWriter, BASE64_STATE_PARAM, encode64(compStates));
            }
        }
        else
        {
            log.error("No component states to be saved in client response!");
        }

        if (treeStruct != null || compStates != null) {
            responseWriter.write('&');
        }
        writeStateParam(responseWriter, VIEWID_PARAM, facescontext.getViewRoot().getViewId());
    }


    private String encode64(Object obj)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream zos = new GZIPOutputStream(baos);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(obj);
            oos.close();
            zos.close();
            baos.close();
            Base64 base64Codec = new Base64();
            return new String(base64Codec.encode( baos.toByteArray() ), ZIP_CHARSET);
        }
        catch (IOException e)
        {
            log.fatal("Cannot encode Object with Base64", e);
            throw new FacesException(e);
        }
    }


    public Object getTreeStructureToRestore(FacesContext facescontext, String viewId)
    {
        Map reqParamMap = facescontext.getExternalContext().getRequestParameterMap();
        Object param = reqParamMap.get(VIEWID_PARAM);
        if (param == null || !param.equals(viewId))
        {
            //no saved state or state of different viewId
            return null;
        }

        param = reqParamMap.get(TREE_PARAM);
        if (param != null)
        {
            return param;
        }

        param = reqParamMap.get(BASE64_TREE_PARAM);
        if (param != null)
        {
            return decode64((String)param);
        }

        return null;
    }

    public Object getComponentStateToRestore(FacesContext facescontext)
    {
        Map reqParamMap = facescontext.getExternalContext().getRequestParameterMap();
        Object param = reqParamMap.get(STATE_PARAM);
        if (param != null)
        {
            return param;
        }

        param = reqParamMap.get(BASE64_STATE_PARAM);
        if (param != null)
        {
            return decode64((String)param);
        }

        return null;
    }

    private Object decode64(String s)
    {
        try
        {
        	Base64 base64Codec = new Base64();
            ByteArrayInputStream decodedStream = new ByteArrayInputStream( base64Codec.decode( s.getBytes(ZIP_CHARSET) ) );
            InputStream unzippedStream = new GZIPInputStream(decodedStream);
            ObjectInputStream ois = new MyFacesObjectInputStream(unzippedStream);
            Object obj = ois.readObject();
            ois.close();
            unzippedStream.close();
            decodedStream.close();
            return obj;
        }
        catch (IOException e)
        {
            log.fatal("Cannot decode Object from Base64 String", e);
            throw new FacesException(e);
        }
        catch (ClassNotFoundException e)
        {
            log.fatal("Cannot decode Object from Base64 String", e);
            throw new FacesException(e);
        }
    }



    private void writeStateParam(ResponseWriter writer, String name, String value)
        throws IOException
    {
        writer.write(name);
        writer.write('=');
        writer.write(URLEncoder.encode(value, writer.getCharacterEncoding()));
    }


}

