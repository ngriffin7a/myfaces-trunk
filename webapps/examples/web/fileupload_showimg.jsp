<%@ page import="java.io.File,
                 java.io.InputStream,
                 java.io.FileInputStream,
                 java.io.OutputStream"
%><%@ page session="false"
%><%
	response.setHeader("pragma", "no-cache");
	response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
	response.setHeader("Expires", "01 Apr 1995 01:10:10 GMT");
	
    byte[] bytes = (byte[])application.getAttribute("fileupload_bytes");
    if (bytes != null)
    {
        String contentType = (String)application.getAttribute("fileupload_type");
        response.setContentType(contentType);

        response.getOutputStream().write(bytes);
    }
%>