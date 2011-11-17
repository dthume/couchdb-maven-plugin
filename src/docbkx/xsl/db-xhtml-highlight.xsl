<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:xslthl="http://xslthl.sf.net"

  version="1.0"
  exclude-result-prefixes="xslthl">

  <xsl:template match="xslthl:attribute" mode="xslthl">
    <span class="hl-attribute"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:comment | xslthl:oneline-comment" mode="xslthl">
    <span class="hl-comment"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:directive" mode="xslthl">
    <span class="h1-directive"><xsl:value-of select="." /></span>
  </xsl:template>

  <xsl:template match="xslthl:keyword" mode="xslthl">
    <span class="hl-keyword"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:multiline-comment" mode="xslthl">
    <span class="hl-multiline-comment"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:string" mode="xslthl">
    <span class="hl-string"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:tag" mode="xslthl">
    <span class="hl-tag"><xsl:value-of select='.'/></span>
  </xsl:template>

  <xsl:template match="xslthl:value" mode="xslthl">
    <span class="hl-value"><xsl:value-of select='.'/></span>
  </xsl:template>
</xsl:stylesheet>
