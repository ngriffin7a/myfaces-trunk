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

    <!--
    <xsl:template match="/">
        <xsl:processing-instruction name="xml-stylesheet">href="book.css" type="text/css"</xsl:processing-instruction>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*">
      <xsl:element name="{name()}">
        <xsl:for-each select = "./@*" >
          <xsl:attribute name="{name()}"><xsl:value-of select="."></xsl:value-of></xsl:attribute>
        </xsl:for-each>
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:template>
    -->

</xsl:stylesheet>