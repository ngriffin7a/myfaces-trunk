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
package org.apache.myfaces.component.html.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;

/**
 * This is a utility class to render link to resources used by custom components.
 * Mostly used to avoid having to include <script src="..."></script>
 * in the head of the pages before using a component.
 *  
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/12/01 16:32:03  svieujot
 * Convert the Multipart filter in an ExtensionsFilter that provides an additional facility to include resources in a page.
 * Tested only with javascript resources right now, but should work fine with images too.
 * Some work to do to include css resources.
 * The popup component has been converted to use this new Filter.
 *
 * 
 */
public class AddResource {
    private static final Log log = LogFactory.getLog(AddResource.class);
    
    private static final String COMPONENTS_PACKAGE = "org.apache.myfaces.custom.";
    
    private static final String RESOURCE_MAP_PATH = "/myFacesExtensionResource";
    
    public static void addJavaScript(Class componentClass, String resourceFileName, FacesContext context) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_LANGUAGE_ATTR,HTML.SCRIPT_LANGUAGE_JAVASCRIPT,null);
        writer.writeURIAttribute(HTML.SRC_ATTR,
                getResourceMappedPath(componentClass,resourceFileName, context),
                null);

        writer.endElement(HTML.SCRIPT_ELEM);
    }
    
    public static void addJavaScriptOncePerPage(Class componentClass, String resourceFileName, FacesContext context) throws IOException{
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String javascriptRenderedAttributeName = "myFacesResourceRendered"+(componentClass.hashCode()+resourceFileName.hashCode());
        
        if( request.getAttribute(javascriptRenderedAttributeName) == null ){
            addJavaScript(componentClass, resourceFileName, context);
            request.setAttribute(javascriptRenderedAttributeName, Boolean.TRUE);
        }
    }
    
    private static String getResourceMappedPath(Class componentClass, String ressourceFileName, FacesContext context){
        String contextPath = ((HttpServletRequest)context.getExternalContext().getRequest()).getContextPath(); 
        return contextPath+getAddRessourceMaping()+"?component="+getComponentName(componentClass)
        		+"&resource="+ressourceFileName;
    }
    
    private static String getAddRessourceMaping(){
        return RESOURCE_MAP_PATH+".jsf";	// TODO Make this compliant with other mapings (/faces/* for example).
    }
    
    public static boolean isResourceMappedPath(HttpServletRequest request){
        return request.getRequestURI().endsWith( getAddRessourceMaping() )
        	&& request.getParameter("component") != null
        	&& request.getParameter("resource") != null;
    }
    
    private static String getComponentName(Class componentClass){
        String name = componentClass.getName();
        if( ! name.startsWith(COMPONENTS_PACKAGE) ){
            log.error("getComponentName called for non extension component : "+name+"\n"+
                    "For security reasons, only components member of the "+COMPONENTS_PACKAGE+" are allowed to add ressources.");
            return null;
        }
        
        name = name.substring( COMPONENTS_PACKAGE.length() );
        
        /*
        int posFirstDot = name.indexOf('.');
        if( posFirstDot > 0 )
            name = name.substring(0,posFirstDot);
            */
        return name;
    }
    
    static Class getComponent(String componentName) throws ClassNotFoundException{
        return Class.forName( COMPONENTS_PACKAGE+componentName );
    }
    
    static private InputStream getResource(String componentName, String resourceFileName) {
        Class component;
        try {
            component = getComponent(componentName);
        } catch (ClassNotFoundException e) {
            log.error("Class not found for component "+componentName);
			return null;
        }
        while( resourceFileName.startsWith(".") || resourceFileName.startsWith("/") || resourceFileName.startsWith("\\") )
                resourceFileName = resourceFileName.substring(1);

        return component.getResourceAsStream( "resource/"+resourceFileName );
    }
    
    static public void serveResource(HttpServletRequest request, ServletResponse response) throws IOException{
        String componentName = request.getParameter("component");
        String resourceFileName = request.getParameter("resource");
        
        if( resourceFileName.endsWith(".js") )
            response.setContentType("text/javascript");
        
        InputStream is = getResource(componentName, resourceFileName);
        if( is == null ){
            throw new IOException("Unable to find resource "+resourceFileName+" for component "+componentName);
        }
        OutputStream os = response.getOutputStream();
        int c;
        while ((c = is.read()) != -1)
            os.write(c);

        os.close();
    }
}