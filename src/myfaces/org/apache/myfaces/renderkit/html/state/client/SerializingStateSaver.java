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

import net.sourceforge.myfaces.component.ext.UISaveState;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.state.ModelValueEntry;
import net.sourceforge.myfaces.renderkit.html.state.StateUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.Base64;
import net.sourceforge.myfaces.util.logging.LogUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    private static final Log log = LogFactory.getLog(SerializingStateRestorer.class);

    protected static final String SERIALIZED_TREE_CONTEXT_ATTR
        = SerializingStateSaver.class.getName() + ".SERIALIZED_TREE";

    protected static final String CURRENT_LOCALE_ATTR
        = SerializingStateSaver.class.getName() + ".CURRENT_LOCALE";
    protected static final String MODEL_VALUES_COLL_ATTR
        = SerializingStateSaver.class.getName() + ".MODEL_VALUES_COLL";

    protected static final String TREE_REQUEST_PARAM = "_tree";

    protected String getSerializedTree(FacesContext facesContext)
    {
        String serializedTree
            = (String)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(SERIALIZED_TREE_CONTEXT_ATTR);
        if (serializedTree == null)
        {
            Tree tree = facesContext.getTree();

            if (log.isTraceEnabled()) LogUtil.logTree(log, tree, "Tree to serialize");

            //discard internal attributes ("javax.*" and "net.sourceforge.*")
            StateUtils.discardInternalAttributes(facesContext, tree);

            //Save current locale in root component
            tree.getRoot().setAttribute(CURRENT_LOCALE_ATTR, facesContext.getLocale());

            //Save model values in root component
            saveModelValues(facesContext);

            //Serialize tree
            serializedTree = zipTree(tree);
            ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(SERIALIZED_TREE_CONTEXT_ATTR,
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
            OutputStream encStream = Base64.getEncoder(baos);
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
    }


    private void saveModelValues(FacesContext facesContext)
    {
        Collection modelValuesColl = null;

        //look for UISaveState components:
        Iterator it = TreeUtils.treeIterator(facesContext.getTree());
        while (it.hasNext())
        {
            UIComponent comp = (UIComponent)it.next();
            if (comp instanceof UISaveState)
            {
                String modelRef = ((UISaveState)comp).getValueRef();
                if (modelRef == null)
                {
                    log.error("UISaveState without value reference?!");
                }
                else
                {
                    if (modelValuesColl == null)
                    {
                        modelValuesColl = new ArrayList();
                    }
                    ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                    Object val = af.getApplication().getValueBinding(modelRef).getValue(facesContext);
                    if (val instanceof Serializable)
                    {
                        modelValuesColl.add(new ModelValueEntry(modelRef,
                                                                val,
                                                                ((UISaveState)comp).isGlobal()));
                    }
                    else
                    {
                        log.error("Value of model reference " + modelRef + " is not serializable, cannot save state of this value!");
                    }
                }
            }
        }

        if (modelValuesColl != null)
        {
            facesContext.getTree().getRoot().setAttribute(MODEL_VALUES_COLL_ATTR,
                                                          modelValuesColl);
        }
    }


}
