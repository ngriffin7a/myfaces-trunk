<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="graphicImage example">
<p>
<f:view>
    This is a wbmp image:
    <wap:graphicImage url="./myfaces_header_logo.wbmp" alt="Apache MyFaces" />
    <wap:form>
	    <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>
</f:view>
</p>
</card>
</wml>

