<head>
  <meta HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
  <title>MyFaces - a free JSF Implementation</title>
  <Link rel="stylesheet" type="text/css" href="css/basic.css">
</head>
<%
    String pageLayout = (String)application.getAttribute("pageLayout");
    if (pageLayout == null)
    {
        pageLayout = "classic";
        //pageLayout = "navigationRight";
        application.setAttribute("pageLayout", pageLayout);
    }

    String msgBundle = "net.sourceforge.myfaces.examples.resource.example_messages";
%>