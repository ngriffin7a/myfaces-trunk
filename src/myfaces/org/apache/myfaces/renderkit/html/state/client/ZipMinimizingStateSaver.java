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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.Base64;

import javax.faces.context.FacesContext;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * StateSaver for the "client_minimized_zipped" state saving mode.
 * Like "client_minimal", but additionally state info is zipped (GZIP),
 * encoded to allowed characters (Base64) and written as one query parameter
 * or hidden form input.
 * Meant for production environments, where high HTTP traffic "costs" more than
 * zipping und unzipping would slow down the webserver.
 * i.e. When running a fast server together with (slow) clients connected over
 * Internet (and not LAN).
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ZipMinimizingStateSaver
    extends MinimizingStateSaver
{
    public static final String STATE_PARAM = "zState";

    public static final String ZIP_CHARSET = "ISO-8859-1";
    public static final String ZIP_ENCODING = "base64";

    private static final String ZIPPED_PARAMS_ATTR
        = ZipMinimizingStateSaver.class.getName() + ".ZIPPED_PARAMS";


    protected void writeHiddenInputsState(FacesContext facesContext, Writer writer) throws IOException
    {
        Map stateMap = getStateMap(facesContext);
        writer.write("\n<input type=\"hidden\" name=\"");
        writer.write(STATE_PARAM);
        writer.write("\" value=\"");
        writer.write(HTMLEncoder.encode(getZippedParams(facesContext, stateMap), false, false));
        writer.write("\">");
    }

    protected void writeUrlState(FacesContext facesContext, Writer writer) throws IOException
    {
        Map stateMap = getStateMap(facesContext);
        writer.write('&');  //we assume that there were previous parameters
        writer.write(STATE_PARAM);
        writer.write('=');
        writer.write(HTMLRenderer.urlEncode(getZippedParams(facesContext, stateMap)));
    }

    protected String getZippedParams(FacesContext facesContext, Map stateMap) throws IOException
    {
        String s = (String)facesContext.getServletRequest().getAttribute(ZIPPED_PARAMS_ATTR);
        if (s != null)
        {
            return s;
        }

        //OutputStream wos = new WriterOutputStream(origWriter, ZippingStateRenderer.ZIP_CHARSET);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream encStream = Base64.getEncoder(baos);
        OutputStream zos = new GZIPOutputStream(encStream);
        OutputStreamWriter writer = new OutputStreamWriter(zos, ZIP_CHARSET);

        boolean first = true;
        for (Iterator entries = stateMap.entrySet().iterator(); entries.hasNext();)
        {
            Map.Entry entry = (Map.Entry)entries.next();
            if (first)
            {
                first = false;
            }
            else
            {
                writer.write('&');
            }
            writer.write((String)entry.getKey());
            writer.write('=');
            writer.write(URLEncoder.encode((String)entry.getValue(), ZIP_CHARSET));
        }

        writer.close();
        zos.close();
        encStream.close();
        baos.close();

        s = baos.toString(ZIP_CHARSET);
        facesContext.getServletRequest().setAttribute(ZIPPED_PARAMS_ATTR, s);
        return s;
    }

    /*
    private static class WriterOutputStream
        extends OutputStream
    {
        private Writer _writer;
        private String _encoding;

        public WriterOutputStream(Writer writer, String encoding)
        {
            _writer = writer;
            _encoding = encoding;
        }

        private byte[] buf = new byte[1];
        public void write(int b) throws IOException
        {
            buf[0] = (byte)b;
            _writer.write(new String(buf, _encoding));
        }

        public void write(byte b[]) throws IOException
        {
            _writer.write(new String(b, _encoding));
        }

        public void write(byte b[], int off, int len) throws IOException
        {
            _writer.write(new String(b, off, len, _encoding));
        }
    }
    */

}
