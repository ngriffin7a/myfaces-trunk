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
package net.sourceforge.myfaces.util;

import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.Base64Encoder;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for Base64 en/decoding.
 * We route all encoding through this methods, so that we are able to easily
 * replace the cos.jar lib anytime.
 * @author Manfred Geiler 
 * @version $Revision$ $Date$
 */
public class Base64
{
    private Base64() {}

    public static OutputStream getEncoder(OutputStream os)
    {
        /*
        try
        {
            return MimeUtility.encode(os, "base64");
        }
        catch (MessagingException e)
        {
            DebugUtils.getLogger().severe(e.getMessage());
            throw new RuntimeException(e);
        }
        */
        return new Base64Encoder(os);
    }

    public static InputStream getDecoder(InputStream is)
    {
        /*
        try
        {
            return MimeUtility.decode(is, "base64");
        }
        catch (MessagingException e)
        {
            DebugUtils.getLogger().severe(e.getMessage());
            throw new RuntimeException(e);
        }
        */
        return new Base64Decoder(is);
    }
}
