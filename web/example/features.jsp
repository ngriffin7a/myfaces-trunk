<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%>
<%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="f"
%>
<%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
%><html>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
//-->

<body>

<f:use_faces>

    <table border="1"><tr>
        <td valign="top" width="140"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640">

<h4>MyFaces supports two ways of saving state information:</h4>
<ul>
<li>"On the fly"<br>
    State info is encoded within HREFs and FORMs at once.
    Drawback: The state of dynamic request time attributes that
              are calculated during JSP processing will not be
              saved correctly. Also the state of dynamically added
              or removed UIComponents will not be saved correctly.</li>
<li>"Tokens"<br>
    State info is encoded within HREFs and FORMs as tokens (placeholders)
    that are replaced by the real state info after the JSP processing
    of MyFaces tags has finished.
    Drawback: The usefaces tag must buffer all produced HTML code
              within it's body (see javax.servlet.jsp.tagext.BodyContent)
              and must do a search'n'replace before writing the
              content out to the response stream.</li>
</ul>

<h4>MyFaces supports two methods of encoding the state information:</h4>
<ul>
<li>"Normal"<br>
    All state values are encoded as normal URL parameters.
    i.e. Query parameters in HREFs and hidden inputs in FORMs</li>
<li>"Zipped"<br>
    All state values are encoded to a String of key/value pairs
    that is zipped (GZIP) and encoded to allowed characters (Base64).
    This String is then written as one query parameter or hidden
    form input.</li>
</ul>

        </td>
    </tr></table>

</f:use_faces>

</body>

</html>