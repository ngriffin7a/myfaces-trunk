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
package net.sourceforge.myfaces.renderkit.html.state;

import net.sourceforge.myfaces.util.Base64;
import net.sourceforge.myfaces.util.MyFacesObjectInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;
import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author gem (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTMLResponseStateManager
        extends ResponseStateManager
{
    private static final Log log = LogFactory.getLog(HTMLResponseStateManager.class);

    private static final String TREE_PARAM = "jsf_tree";
    private static final String STATE_PARAM = "jsf_state";
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
                responseWriter.startElement("input", null);
                responseWriter.writeAttribute("type", "hidden", null);
                responseWriter.writeAttribute("name", TREE_PARAM, null);
                responseWriter.writeAttribute("value", treeStruct, null);
            }
            else
            {
                responseWriter.startElement("input", null);
                responseWriter.writeAttribute("type", "hidden", null);
                responseWriter.writeAttribute("name", BASE64_TREE_PARAM, null);
                responseWriter.writeAttribute("value", encode64(treeStruct), null);
            }
        }

        if (compStates != null)
        {
            if (compStates instanceof String)
            {
                responseWriter.startElement("input", null);
                responseWriter.writeAttribute("type", "hidden", null);
                responseWriter.writeAttribute("name", STATE_PARAM, null);
                responseWriter.writeAttribute("value", compStates, null);
            }
            else
            {
                responseWriter.startElement("input", null);
                responseWriter.writeAttribute("type", "hidden", null);
                responseWriter.writeAttribute("name", BASE64_STATE_PARAM, null);
                responseWriter.writeAttribute("value", encode64(compStates), null);
            }
        }
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
                responseWriter.writeURIAttribute(TREE_PARAM, treeStruct, null);
            }
            else
            {
                responseWriter.writeURIAttribute(BASE64_TREE_PARAM, encode64(treeStruct), null);
            }
        }

        if (compStates != null)
        {
            if (compStates instanceof String)
            {
                responseWriter.writeURIAttribute(STATE_PARAM, compStates, null);
            }
            else
            {
                responseWriter.writeURIAttribute(BASE64_STATE_PARAM, encode64(compStates), null);
            }
        }
    }


    private String encode64(Object obj)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = Base64.getEncoder(baos);
            OutputStream zos = new GZIPOutputStream(encStream);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(obj);
            oos.close();
            zos.close();
            encStream.close();
            baos.close();
            return baos.toString(ZIP_CHARSET);
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
        Object param = reqParamMap.get(TREE_PARAM);
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
            ByteArrayInputStream byteStream = new ByteArrayInputStream(s.getBytes(ZIP_CHARSET));
            InputStream decodedStream = Base64.getDecoder(byteStream);
            InputStream unzippedStream = new GZIPInputStream(decodedStream);
            ObjectInputStream ois = new MyFacesObjectInputStream(unzippedStream);
            Object obj = ois.readObject();
            ois.close();
            unzippedStream.close();
            decodedStream.close();
            byteStream.close();
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

}
