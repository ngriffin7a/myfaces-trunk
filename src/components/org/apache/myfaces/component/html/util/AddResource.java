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
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Revision 1.9  2004/12/02 22:26:23  svieujot
 * Simplify the AddResource methods
 *
 * Revision 1.8  2004/12/02 11:53:27  svieujot
 * Replace java 1.5 code by 1.4 version.
 *
 * Revision 1.7  2004/12/02 02:20:55  svieujot
 * Bugfix : render the head elements in the same order as they were added (use a LinkedHashSet).
 *
 * Revision 1.6  2004/12/02 02:07:22  svieujot
 * Make the Extensions filter work with resource hierarchies.
 * A small concession had to be made though :
 * The ExtensionsFilter must have the (additional) /faces/*
 *
 * Revision 1.5  2004/12/02 00:25:34  oros
 * i18n issues
 * some slight refactorings
 *
 * Revision 1.4  2004/12/01 22:12:51  svieujot
 * Add xml and xsl content types.
 *
 * Revision 1.3  2004/12/01 20:29:22  svieujot
 * Add javadoc.
 *
 * Revision 1.2  2004/12/01 20:25:10  svieujot
 * Make the Extensions filter support css and image resources.
 * Convert the popup calendar to use this new filter.
 *
 * Revision 1.1  2004/12/01 16:32:03  svieujot
 * Convert the Multipart filter in an ExtensionsFilter that provides an additional facility to include resources in a page.
 * Tested only with javascript resources right now, but should work fine with images too.
 * Some work to do to include css resources.
 * The popup component has been converted to use this new Filter.
 * 
 */
public class AddResource {
    private static final Log log = LogFactory.getLog(AddResource.class);
    
    private static final String COMPONENTS_PACKAGE = "org.apache.myfaces.custom.";
    
    private static final String RESOURCE_VIRUAL_PATH = "/faces/myFacesExtensionResource";
    
    private static final String ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME = "myFacesHeaderResource2Render";
    
    // Methodes to Add resources

    /**
     * Adds the given Javascript resource to the document body.
     */
    public static void addJavaScriptHere(Class componentClass, String resourceFileName, FacesContext context) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_LANGUAGE_ATTR,HTML.SCRIPT_LANGUAGE_JAVASCRIPT,null);
        writer.writeURIAttribute(HTML.SRC_ATTR,
                getResourceMappedPath(componentClass,resourceFileName, context),
                null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script is already has already been referenced, it's added only once. 
     */
    public static void addJavaScriptToHeader(Class componentClass, String resourceFileName, FacesContext context){
        AdditionalHeaderInfoToRender jsInfo = new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_JS, componentClass, resourceFileName);
        getAdditionalHeaderInfoToRender(context).add( jsInfo );
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet is already has already been referenced, it's added only once.
     */
    public static void addStyleSheet(Class componentClass, String resourceFileName, FacesContext context){
        AdditionalHeaderInfoToRender cssInfo = new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_CSS, componentClass, resourceFileName);
        getAdditionalHeaderInfoToRender(context).add( cssInfo );
    }
    
    /**
     * Get the Path used to retrieve an internal resource for a custom component.
     * Example : You can use this to initialize javascript scripts so that they know the path of some other resources
     * (image, css, ...).
     * <code>
     * 	AddResource.addJavaScriptOncePerPage(HtmlCalendarRenderer.class, "popcalendar.js", facesContext,
     * 		"jscalendarSetImageDirectory("+AddResource.getResourceMappedPath(HtmlCalendarRenderer.class, "DB", facesContext)+")");
     * </code>
     */
    public static String getResourceMappedPath(Class componentClass, String resourceFileName, FacesContext context){
        return getResourceMappedPath(
                getComponentName(componentClass),
                resourceFileName,
                (HttpServletRequest)context.getExternalContext().getRequest());
    }
    
    private static String getResourceMappedPath(String componentName, String resourceFileName, HttpServletRequest request){
        String contextPath = request.getContextPath(); 
        return contextPath+RESOURCE_VIRUAL_PATH+"/"+componentName+'/'+resourceFileName;
    }
    
    public static boolean isResourceMappedPath(HttpServletRequest request){
        return request.getRequestURI().indexOf( RESOURCE_VIRUAL_PATH ) != -1;
    }
    
    /**
     * Decodes the path to return the requested componentName & resourceFileName
     * String[0] == componentName
     * String[1] == resourceFileName
     */
    private static String[] getResourceInfoFromPath(HttpServletRequest request){
        String uri = request.getRequestURI();
        String componentNameStartsAfter = RESOURCE_VIRUAL_PATH+'/';

        int posStartComponentName = uri.indexOf( componentNameStartsAfter )+componentNameStartsAfter.length();
        int posEndComponentName = uri.indexOf("/", posStartComponentName);
        String componentName = uri.substring(posStartComponentName, posEndComponentName);
        
        String resourceFileName = uri.substring(posEndComponentName+1);
        
        return new String[]{componentName, resourceFileName};
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
        String[] resourceInfo = getResourceInfoFromPath(request);
        String componentName = resourceInfo[0];
        String resourceFileName = resourceInfo[1];
        
        log.debug("Serving resource "+resourceFileName+" for component "+componentName);
        
        String lcResourceFileName = resourceFileName.toLowerCase();
        
        if( lcResourceFileName.endsWith(".js") )
            response.setContentType("text/javascript");
        else if( lcResourceFileName.endsWith(".css") )
            response.setContentType("text/css");
        else if( lcResourceFileName.endsWith(".gif") )
            response.setContentType("image/gif");
        else if( lcResourceFileName.endsWith(".png") )
            response.setContentType("image/png");
        else if( lcResourceFileName.endsWith(".jpg") || lcResourceFileName.endsWith(".jpeg") )
            response.setContentType("image/jpeg");
        else if( lcResourceFileName.endsWith(".xml") )
            response.setContentType("text/xml");
        else if( lcResourceFileName.endsWith(".xsl") )
            response.setContentType("text/xsl");
        
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
    
    // Header stuffs
    
    static public boolean hasAdditionalHeaderInfoToRender(HttpServletRequest request){
        boolean test = request.getAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME) != null;
		return test;
    }
    
    private static Set getAdditionalHeaderInfoToRender(FacesContext context){
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return getAdditionalHeaderInfoToRender( request );
    }
    
    private static Set getAdditionalHeaderInfoToRender(HttpServletRequest request){
        Set set = (Set) request.getAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME);
        if( set == null ){
            set = new LinkedHashSet();
            request.setAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME, set);
        }
        
        return set;
    }
    
    static public void writeWithFullHeader(HttpServletRequest request,
            ExtensionsResponseWrapper responseWrapper,
            HttpServletResponse response) throws IOException{
        
        String originalResponse = responseWrapper.toString();

        // try the most common cases first
        boolean addHeaderTags = false;
        int insertPosition = originalResponse.indexOf( "</head>" );
        
        if( insertPosition < 0 ){
			insertPosition = originalResponse.indexOf( "</HEAD>" );
			
	        if( insertPosition < 0 ){
	            insertPosition = originalResponse.indexOf( "<body" );
	            addHeaderTags = true;
	        
		        if( insertPosition < 0 ){
		        	insertPosition = originalResponse.indexOf( "<BODY" );
		            addHeaderTags = true;
		
			        if( insertPosition < 0 ){
			 	       // the two most common cases head/HEAD and body/BODY did not work, so we try it with lowercase
			           String lowerCase = originalResponse.toLowerCase(response.getLocale());
			           insertPosition = lowerCase.indexOf( "</head>" );
			
			           if( insertPosition < 0 ){
			    	       insertPosition = lowerCase.indexOf( "<body" );
			               addHeaderTags = true;
			           }
			        }
		        }
	        }
	
	        if( insertPosition < 0 ){
	            log.warn("Response has no <head> or <body> tag.");
	            insertPosition = 0;
	        }
        }
        
        PrintWriter writer = response.getWriter();
        
        if( insertPosition > 0 )
            writer.write( originalResponse.substring(0, insertPosition) );
        if( addHeaderTags )
            writer.write("<head>");
        
        for(Iterator i = getAdditionalHeaderInfoToRender(request).iterator(); i.hasNext() ;){
            AdditionalHeaderInfoToRender headerInfo = (AdditionalHeaderInfoToRender) i.next();
            writer.write( headerInfo.getString(request) );
        }
        
        if( addHeaderTags )
            writer.write("</head>");
        
        writer.write( originalResponse.substring(insertPosition) );
    }
    
    private static class AdditionalHeaderInfoToRender{
        public static final int TYPE_CSS = 0;
        public static final int TYPE_JS = 1;
        public String componentName;
        public String resourceFileName;
        public int type;
        
        public AdditionalHeaderInfoToRender(int infoType, String componentName, String resourceFileName) {
            this.componentName = componentName;
            this.resourceFileName = resourceFileName;
            this.type = infoType;
        }
        
        public AdditionalHeaderInfoToRender(int infoType, Class componentClass, String resourceFileName) {
            this.componentName = getComponentName(componentClass);
            this.resourceFileName = resourceFileName;
            this.type = infoType;
        }
        
        public int hashCode() {
            return (componentName+((char)7)+resourceFileName+((char)7)+type).hashCode();
        }
        
        public String getString(HttpServletRequest request){
            switch (type) {
            case TYPE_CSS:
                return "<link rel=\"stylesheet\" "
                	+"href=\""+getResourceMappedPath(componentName, resourceFileName, request)+"\" "
                	+"type=\"text/css\"/>\n";
           case TYPE_JS:
                    return "<script language=\"JavaScript\" "
                        +"src=\""+getResourceMappedPath(componentName, resourceFileName, request)+"\" type=\"text/javascript\"></script>\n";
            default:
                log.warn("Unknown type:"+type);
                return "<link href=\""+"\"/>\n";
            }
        }
    }
}