<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:db="http://docbook.org/ns/docbook"
  xmlns:dbx="http://www.dthume.com/ns/docbook/extension"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns:xslthl="http://xslthl.sf.net"

  version="1.0"
  exclude-result-prefixes="db dbx xslthl xhtml">

  <xsl:import href="urn:docbkx:stylesheet" />
  <xsl:import href="urn:docbkx:stylesheet/highlight.xsl" />

  <!-- Customisation Stylesheets -->
  <xsl:import href="db-xhtml-highlight.xsl" />

  <!-- Customisation Parameters -->
  <xsl:param name="abstract.notitle.enabled" select="1" />

  <xsl:param name="admon.graphics" select="1" />
  <xsl:param name="admon.graphics.path" select="'/resources/images/icons/simplicio-1.0/64x64/'" />
  <xsl:param name="admon.graphics.extension" select="'.png'" />
  <xsl:param name="admon.textlabel" select="1" />

  <!-- Put id's on containers -->
  <xsl:param name="generate.id.attributes" select="1" />
  <xsl:param name="generate.container.ids" select="1" />

  <xsl:param name="generate.meta.author" select="0" />

  <xsl:param name="generate.toc">
    appendix  nop
    article   nop
    book      toc,title,figure,table,example,equation
    chapter   toc
    part      nop
    preface   nop
    qandadiv  nop
    qandaset  nop
    reference toc,title
    section   nop
    set       toc
  </xsl:param>
  
  <xsl:param name="glossary.sort" select="1" />

  <xsl:param name="highlight.source" select="1"/>

  <xsl:param name="local.l10n.xml" select="document('local-gentext.gtx')"/>

  <!-- Section labeling -->
  <xsl:param name="section.autolabel" select="0" />
  <xsl:param name="section.autolabel.max.depth" select="2" />
  <xsl:param name="section.label.includes.component.label" select="1" />

  <!-- propagate @role to @class where possible -->
  <xsl:param name="emphasis.propagates.style" select="1" />
  <xsl:param name="entry.propagates.style" select="1" />
  <xsl:param name="para.propagates.style" select="1" />
  <xsl:param name="phrase.propagates.style" select="1" />
</xsl:stylesheet>