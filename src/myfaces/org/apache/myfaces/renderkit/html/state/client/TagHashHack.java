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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.util.zip.ZipUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Sun saves a special Map in the root component, when there are common without an id.
 * They calculate a hashKey from the nested tags and store the corresponding component with
 * that key. During {@link javax.faces.webapp.FacesTag#findComponent} they use this map
 * to lookup and identify common that are already in the tree.
 * We must save and restore this Map, because otherwise the findComponent method would always
 * add a component that has no id again to our restored tree.
 * Since this is a heavy object and we do not want to drag the whole bunch of common, we use
 * this little trick:
 * Before we save the map by serializing, we replace each component by it's clientId. And then
 * when we have restored the map again, we do the other way round and replace each clientId by
 * the (hopefully found again) component.
 * And if we are in production and have jspInfoApplicationCaching = true, we can even abandon
 * serializing, and easily save the clientId-Map in the parsed tree's root.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TagHashHack
{
    private static final String TAG_HASH_ATTR = "tagHash";
    private static final boolean SERIALIZED_MAP = false;
    private static final boolean ZIP_STRING = true;

    private TagHashHack() {}

    public static boolean isTagHashAttribute(UIComponent uiComponent,
                                             String attrName)
    {
        return (uiComponent.getParent() == null &&
                attrName.equals(TAG_HASH_ATTR));
    }

    /**
     * @param facesContext
     * @return  null, if there is no need to save
     */
    public static String getAsStringToBeSaved(FacesContext facesContext,
                                              Map tagHash)
    {
        Tree parsedTree = JspInfo.getTree(facesContext,
                                          facesContext.getTree().getTreeId());

        Map saveTagHash = (Map)parsedTree.getRoot().getAttribute(TAG_HASH_ATTR);
        boolean newMap = false;
        if (saveTagHash == null)
        {
            saveTagHash = new HashMap();
            newMap = true;
        }

        //Remap each tagKey to the uniqueId instead of the component
        for (Iterator it = tagHash.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            if (newMap || !saveTagHash.containsKey(entry.getKey()))
            {
                UIComponent comp = (UIComponent)entry.getValue();
                if (comp != null)
                {
                    saveTagHash.put(entry.getKey(),
                                    UIComponentUtils.getUniqueComponentId(comp));
                }
                else
                {
                    LogUtil.getLogger().severe("Component not found for entry " + entry.getKey());
                }
            }
        }

        if (MyFacesConfig.isJspInfoCaching(facesContext.getServletContext()))
        {
            //Save tagHash in parsed tree and do not save in client
            //FIXME: If there is already a tagHash in the parsed tree, we should
            //rather merge new entries into the existing map, than replacing the
            //whole map!
            parsedTree.getRoot().setAttribute(TAG_HASH_ATTR, saveTagHash);
            return null;
        }

        if (SERIALIZED_MAP)
        {
            return ConverterUtils.serializeAndEncodeBase64(saveTagHash);
        }
        else
        {
            //Alternative: generate a comma-separated key-value list
            StringBuffer buf = new StringBuffer();
            for (Iterator it = saveTagHash.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                if (buf.length() > 0)
                {
                    buf.append(',');
                }
                buf.append((String)entry.getKey());
                buf.append('=');
                buf.append((String)entry.getValue());
            }
            if (ZIP_STRING)
            {
                return ZipUtils.zipString(buf.toString());
            }
            else
            {
                return buf.toString();
            }
        }
    }


    public static Object getAsObjectFromSaved(String attrValue)
    {
        if (SERIALIZED_MAP)
        {
            return ConverterUtils.deserializeAndDecodeBase64(attrValue);
        }
        else
        {
            if (ZIP_STRING)
            {
                attrValue = ZipUtils.unzipString(attrValue);
            }
            HashMap map = new HashMap();
            StringTokenizer st = new StringTokenizer(attrValue, ",");
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                int equalIdx = token.indexOf('=');
                if (equalIdx < 0)
                {
                    throw new IllegalArgumentException("Invalid tagHash String!");
                }
                map.put(token.substring(0, equalIdx),
                        token.substring(equalIdx + 1));
            }
            return map;
        }
    }


    public static void convertUniqueIdsBackToComponents(Tree restoredTree)
    {
        UIComponent root = restoredTree.getRoot();
        Map restoredTagHash = (Map)root.getAttribute(TAG_HASH_ATTR);
        if (restoredTagHash != null)
        {
            Map realTagHash = new HashMap();
            for (Iterator tagHashIt = restoredTagHash.entrySet().iterator(); tagHashIt.hasNext();)
            {
                Map.Entry tagHashEntry = (Map.Entry)tagHashIt.next();
                String uniqueId = (String)tagHashEntry.getValue();
                UIComponent comp = UIComponentUtils.findComponentByUniqueId(restoredTree,
                                                                   uniqueId);
                if (comp == null)
                {
                    LogUtil.getLogger().severe("Could not find component by uniqueId " + uniqueId);
                }
                realTagHash.put(tagHashEntry.getKey(), comp);
            }
            root.setAttribute(TAG_HASH_ATTR, realTagHash);
        }
    }

}
