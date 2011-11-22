<?xml version="1.0" encoding="iso-8859-1"?>
<!--

    Copyright (C) 2011 David Thomas Hume <dth@dthu.me>

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
