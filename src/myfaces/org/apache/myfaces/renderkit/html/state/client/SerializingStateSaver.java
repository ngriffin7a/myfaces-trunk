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
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * StateSaver for the "client_serialized" state saving mode.
 * Tree is serialized, zipped and saved as url parameter or hidden input
 * (comparable to Sun's saving with saveStateInClient = true, but additionally
 * zips the parameter and has also support for url parameters).
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SerializingStateSaver
    extends ClientStateSaver
{
    protected static final String SERIALIZED_TREE_CONTEXT_ATTR
        = SerializingStateSaver.class.getName() + ".SERIALIZED_TREE";

    protected static final String CURRENT_LOCALE_ATTR
        = SerializingStateSaver.class.getName() + ".CURRENT_LOCALE";

    protected static final String TREE_REQUEST_PARAM = "_tree";

    protected String getSerializedTree(FacesContext facesContext)
    {
        String serializedTree
            = (String)facesContext.getServletRequest().getAttribute(SERIALIZED_TREE_CONTEXT_ATTR);
        if (serializedTree == null)
        {
            Tree tree = facesContext.getTree();

            LogUtil.printTreeToConsole(tree, "Tree to serialize");

            //Save current locale in root component
            tree.getRoot().setAttribute(CURRENT_LOCALE_ATTR, facesContext.getLocale());

            serializedTree = zipTree(tree);
            facesContext.getServletRequest().setAttribute(SERIALIZED_TREE_CONTEXT_ATTR,
                                                          serializedTree);
        }
        return serializedTree;
    }

    protected void writeUrlState(FacesContext facesContext, Writer writer) throws IOException
    {
        writer.write('&');  //we assume that there were previous parameters
        writer.write(TREE_REQUEST_PARAM);
        writer.write('=');
        writer.write(HTMLRenderer.urlEncode(getSerializedTree(facesContext)));
    }

    protected void writeHiddenInputsState(FacesContext facesContext, Writer writer) throws IOException
    {
        writer.write("\n<input type=\"hidden\" name=\"");
        writer.write(TREE_REQUEST_PARAM);
        writer.write("\" value=\"");
        writer.write(HTMLEncoder.encode(getSerializedTree(facesContext), false, false));
        writer.write("\">");
    }



    protected String zipTree(Tree tree)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = MimeUtility.encode(baos, ZipMinimizingStateSaver.ZIP_ENCODING);
            OutputStream zos = new GZIPOutputStream(encStream);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(tree);
            oos.close();
            zos.close();
            encStream.close();
            baos.close();

            String s = baos.toString(ZipMinimizingStateSaver.ZIP_CHARSET);
            return s;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

}
