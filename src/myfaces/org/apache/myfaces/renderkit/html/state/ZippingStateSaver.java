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
package net.sourceforge.myfaces.renderkit.html.state;

import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ZippingStateSaver
    extends StateSaver
{
    protected void writeHiddenInputsState(Writer writer, Map stateMap) throws IOException
    {
        writer.write("\n<input type=\"hidden\" name=\"");
        writer.write(ZippingStateRenderer.STATE_PARAM);
        writer.write("\" value=\"");
        writer.write(HTMLEncoder.encode(getZippedParams(stateMap), false, false));
        writer.write("\">");
    }

    protected void writeUrlState(Writer writer, Map stateMap) throws IOException
    {
        writer.write('&');  //we assume that there were previous parameters
        writer.write(ZippingStateRenderer.STATE_PARAM);
        writer.write('=');
        writer.write(HTMLRenderer.urlEncode(getZippedParams(stateMap)));
    }

    protected String getZippedParams(Map stateMap) throws IOException
    {
        //TODO: Cache!
        try
        {
            //OutputStream wos = new WriterOutputStream(origWriter, ZippingStateRenderer.ZIP_CHARSET);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = MimeUtility.encode(baos, ZippingStateRenderer.ZIP_ENCODING);
            OutputStream zos = new GZIPOutputStream(encStream);
            OutputStreamWriter writer = new OutputStreamWriter(zos, ZippingStateRenderer.ZIP_CHARSET);

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
                writer.write(URLEncoder.encode((String)entry.getValue(), ZippingStateRenderer.ZIP_CHARSET));
            }

            writer.close();
            zos.close();
            encStream.close();
            baos.close();

            String s = baos.toString(ZippingStateRenderer.ZIP_CHARSET);
            return s;
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
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

    /**
     * TODO: Move to a "ZipUtils" class
     * @param s
     * @return
     */
    protected static String zipString(String s)
    {
        try
        {
            //OutputStream wos = new WriterOutputStream(origWriter, ZippingStateRenderer.ZIP_CHARSET);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = MimeUtility.encode(baos, ZippingStateRenderer.ZIP_ENCODING);
            OutputStream zos = new GZIPOutputStream(encStream);
            OutputStreamWriter writer = new OutputStreamWriter(zos, ZippingStateRenderer.ZIP_CHARSET);

            writer.write(s);

            writer.close();
            zos.close();
            encStream.close();
            baos.close();

            return baos.toString(ZippingStateRenderer.ZIP_CHARSET);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
