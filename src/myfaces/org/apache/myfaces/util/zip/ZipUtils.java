/**
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
package net.sourceforge.myfaces.util.zip;

import net.sourceforge.myfaces.util.Base64;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ZipUtils
{
    public static final String ZIP_CHARSET = "ISO-8859-1";


    private ZipUtils() {}


    /**
     * @param s
     * @return
     */
    public static String unzipString(String s)
    {
        try
        {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(s.getBytes(ZIP_CHARSET));
            InputStream decodedStream = Base64.getDecoder(byteStream);
            InputStream unzippedStream = new GZIPInputStream(decodedStream);

            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = unzippedStream.read()) != -1)
            {
                buf.append((char)c);
            }

            unzippedStream.close();
            decodedStream.close();
            byteStream.close();

            return buf.toString();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param s
     * @return
     */
    public static String zipString(String s)
    {
        try
        {
            //OutputStream wos = new WriterOutputStream(origWriter, ZippingStateRenderer.ZIP_CHARSET);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = Base64.getEncoder(baos);
            OutputStream zos = new GZIPOutputStream(encStream);
            OutputStreamWriter writer = new OutputStreamWriter(zos, ZIP_CHARSET);

            writer.write(s);

            writer.close();
            zos.close();
            encStream.close();
            baos.close();

            return baos.toString(ZIP_CHARSET);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


}
