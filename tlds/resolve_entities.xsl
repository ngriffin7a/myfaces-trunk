<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml"
                encoding="ISO-8859-1"
                doctype-public="-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN"
                doctype-system="http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd"
        />

    <xsl:template match="@*|node()">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:template>

</xsl:stylesheet>