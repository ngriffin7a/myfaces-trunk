<%@ page import="java.io.File,
                 java.io.InputStream,
                 java.io.FileInputStream,
                 java.io.OutputStream"
%><%@ page session="false"
%><%
    File file = (File)application.getAttribute("fileupload_file");
    if (file != null)
    {
        String contentType = (String)application.getAttribute("fileupload_type");
        response.setContentType(contentType);

        InputStream is = new FileInputStream(file);
        OutputStream os = response.getOutputStream();
        int b;
        while ((b = is.read()) != -1)
        {
            os.write(b);
        }
    }
%>