<?xml version="1.0" encoding="iso-8859-1"?>
<!--

    Copyright (C) 2011 David Thomas Hume <dth at dthu.me>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
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