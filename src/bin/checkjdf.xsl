<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tbl="urn:TheLanguageTable" exclude-result-prefixes="tbl">
	<xsl:output method="html"/>
	<!-- A variable to hold the current language, default to English -->
	<xsl:param name="lang" select="//CheckOutput/@Language"/>
	<!-- here is the language table -->
	<tbl:lang>
		<tbl:loc key="title">
			<tbl:str lang="EN">CIP4 CheckJDF Output</tbl:str>
			<tbl:str lang="DE">CIP4 CheckJDF Output </tbl:str>
			<tbl:str lang="FR">CIP4 CheckJDF Output in French</tbl:str>
		</tbl:loc>
		<tbl:loc key="output">
			<tbl:str lang="EN">CheckJDF Validation Output</tbl:str>
			<tbl:str lang="DE">CheckJDF Validierungsoutput </tbl:str>
			<tbl:str lang="FR">CheckJDF Validation Output in French</tbl:str>
		</tbl:loc>
		<tbl:loc key="version">
			<tbl:str lang="EN">Checker Version: </tbl:str>
			<tbl:str lang="DE">Checker Version: </tbl:str>
			<tbl:str lang="FR">Checker Version: in French</tbl:str>
		</tbl:loc>
		<tbl:loc key="TestFile">
			<tbl:str lang="EN">Testing File: </tbl:str>
			<tbl:str lang="DE">Überprüfung der Datei: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="PrivateElement">
			<tbl:str lang="EN">Private Element: </tbl:str>
			<tbl:str lang="DE">Privates Element: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="PrivateContent">
			<tbl:str lang="EN">Private Element Content: </tbl:str>
			<tbl:str lang="DE">Privater ElementenInhalt: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="PrivateAttribute">
			<tbl:str lang="EN">Private Attribute: </tbl:str>
			<tbl:str lang="DE">Privates Attribut: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="at">
			<tbl:str lang="EN"> at: </tbl:str>
			<tbl:str lang="DE"> in: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="value">
			<tbl:str lang="EN"> value: </tbl:str>
			<tbl:str lang="DE"> Wert: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="UnknownElement">
			<tbl:str lang="EN"> Unknown Element: </tbl:str>
			<tbl:str lang="DE"> Unbekanntes Element: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="DeprecatedElement">
			<tbl:str lang="EN">Warning: Deprecated Element: </tbl:str>
			<tbl:str lang="DE"> Warnung: Veraltetes Element: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="PrereleaseElement">
			<tbl:str lang="EN">Prerelease Element: </tbl:str>
			<tbl:str lang="DE">Zu neues Element: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="SwapElement">
			<tbl:str lang="EN">Attribute written as Element: </tbl:str>
			<tbl:str lang="DE">Attribut wurde als Element geschrieben: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="MissLink">
			<tbl:str lang="EN">Missing ResourceLink: </tbl:str>
			<tbl:str lang="DE">Fehlender ResourceLink: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="MissProcUsage">
			<tbl:str lang="EN">Missing ProcessUsage for ResourceLink: </tbl:str>
			<tbl:str lang="DE">Fehlende ProcessUsage für ResourceLink: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="InvalidLink">
			<tbl:str lang="EN">Invalid ResourceLink: </tbl:str>
			<tbl:str lang="DE">Unzul㲳iger ResourceLink: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="MultiID">
			<tbl:str lang="EN"> refers to multiply defined ID: at: </tbl:str>
			<tbl:str lang="DE"> Bezieht sich auf mehrfach definierte ID: an: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="DangleLink">
			<tbl:str lang="EN">Dangling ResourceLink </tbl:str>
			<tbl:str lang="DE">Unverknüpfter ResourceLink </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="DanglePartLink">
			<tbl:str lang="EN">Dangling Partitioned ResourceLink </tbl:str>
			<tbl:str lang="DE">Unverknüpfter Partitionierter ResourceLink </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="DangleRefElem">
			<tbl:str lang="EN">Dangling RefElement </tbl:str>
			<tbl:str lang="DE">Unverknüpfter RefElement</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="DanglePartRefElem">
			<tbl:str lang="EN">Dangling Partitioned RefElement </tbl:str>
			<tbl:str lang="DE">Unverknüpfter Partitioniertes RefElement </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="InvResPosition">
			<tbl:str lang="EN">Invalid position of Resource and ResourceLink </tbl:str>
			<tbl:str lang="DE">Unzulässige Position der Ressource und des RecourceLinks </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="UnlinkedRes">
			<tbl:str lang="EN">Warning: Unlinked and Unreferenced Resource: </tbl:str>
			<tbl:str lang="DE">Warnung: Unverlinkte und Unreferenzierte Ressource: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="InvRefElem">
			<tbl:str lang="EN">Invalid RefElement: </tbl:str>
			<tbl:str lang="DE">Unzulässiges RefElement: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">General Invalid Element: </tbl:str>
			<tbl:str lang="DE">Allgemein unzulässiges Element: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Separation not defined in ColorPool: </tbl:str>
			<tbl:str lang="DE">Separation im ColorPool nicht definiert: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Separation defined in ColorPool but never referenced: </tbl:str>
			<tbl:str lang="DE">Separation im ColorPool definiert aber nie referenziert: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Multiple ID Attribute at: </tbl:str>
			<tbl:str lang="DE">Mehrfaches ID Attribut vorhanden in: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Prerelease Attribute </tbl:str>
			<tbl:str lang="DE">Zu neues Attribut </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Deprecated Attribute </tbl:str>
			<tbl:str lang="DE">Veraltetes Attribut </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Unknown Attribute </tbl:str>
			<tbl:str lang="DE">Unbekanntes Attribut </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Missing Required Attribute </tbl:str>
			<tbl:str lang="DE">Fehlendes erforderliches Attribut </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Element written as Attribute: </tbl:str>
			<tbl:str lang="DE">Element als Attribut gechrieben: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Tested Attribute </tbl:str>
			<tbl:str lang="DE">Getestetes Attribut </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Error Type: </tbl:str>
			<tbl:str lang="DE">Fehlertyp: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="message">
			<tbl:str lang="EN">Message: </tbl:str>
			<tbl:str lang="DE">Nachricht: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Unmatched element: TODO fix xslt for this</tbl:str>
			<tbl:str lang="DE">Unangepasstes Element: Aufgabe xsl reparieren</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Resource PartUsage: </tbl:str>
			<tbl:str lang="DE">Resource PartUsage: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="SchemaSuccess">
			<tbl:str lang="EN">XML Schema Validation Successful: </tbl:str>
			<tbl:str lang="DE">XML Validierung erfolgreich :</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="SchemaError">
			<tbl:str lang="EN">XML Schema Validation Error: </tbl:str>
			<tbl:str lang="DE">XML Validierungsfehler:</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="prefix">
			<tbl:str lang="EN">XML Namespace prefix: </tbl:str>
			<tbl:str lang="DE">XML Namespace präfix: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="SchemaOut">
			<tbl:str lang="EN">XML Schema Validation Output: </tbl:str>
			<tbl:str lang="DE">XML Schema Validierungs Output: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Schema Error: </tbl:str>
			<tbl:str lang="DE">Fehler im Schema: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Schema Fatal Error: </tbl:str>
			<tbl:str lang="DE">Fataler Fehler im Schema: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Schema Warning: </tbl:str>
			<tbl:str lang="DE">Schemawarnung: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">CheckJDF Validation Output: </tbl:str>
			<tbl:str lang="DE">Validierungsoutput der Überprüfung</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">CheckJDF Validation Successful: </tbl:str>
			<tbl:str lang="DE">Überprüfung der JDF Validation war erfolgreich: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">XML Schema Validation Output: </tbl:str>
			<tbl:str lang="DE">Schema Validierungs Output:</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc key="rawout">
			<tbl:str lang="EN">Raw XML Checker Output for: </tbl:str>
			<tbl:str lang="DE">CheckJDF Output als XML: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Checker Version:</tbl:str>
			<tbl:str lang="DE">Checkerversion</tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
		<tbl:loc>
			<tbl:str lang="EN">Last Valid Version: </tbl:str>
			<tbl:str lang="DE">Letzte gültige Version: </tbl:str>
			<tbl:str lang="FR">Testing File: in French </tbl:str>
		</tbl:loc>
	</tbl:lang>
	<!-- get a pointer to the language table -->
	<xsl:variable name="table" select="document('')/xsl:stylesheet/tbl:lang"/>
	<!-- a named template to do the gory localization stuff -->
	<xsl:template name="localize">
		<xsl:param name="string" select="''"/>
		<!--TOD move all definitions to the key method -->
		<!-- find the appropriate localizations for the key -->
		<xsl:variable name="loc1" select="$table/tbl:loc[@key=$string]"/>
		<!-- find the appropriate localizations for the English string -->
		<xsl:variable name="loc" select="$table/tbl:loc[tbl:str/@lang='EN' and tbl:str=$string]"/>
		<!-- get the localized string -->
		<xsl:value-of select="$loc/tbl:str[@lang=$lang]"/>
		<xsl:value-of select="$loc1/tbl:str[@lang=$lang]"/>
	</xsl:template>
	<!-- here are the processing templates -->
	<xsl:template match="CheckOutput">
		<LINK REL="stylesheet" HREF="http://www.cip4.org/css/styles_pc.css" TYPE="text/css"/>
		<head>
			<title>
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'title'"/>
				</xsl:call-template>
			</title>
		</head>
		<body bgcolor="#ccccff">
			<xsl:comment>#include virtual="/global/navigation/menue_switch.php?section=support" </xsl:comment>
			<HTML>
				<H1>
					<xsl:call-template name="localize">
						<xsl:with-param name="string" select="'output'"/>
					</xsl:call-template>
				</H1>
			</HTML>
			<br/>
			<hr/>
			<br/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'version'"/>
			</xsl:call-template>
			<xsl:value-of select="@Version"/>
			<br/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'rawout'"/>
			</xsl:call-template>
			<xsl:element name="A">
				<xsl:attribute name="href"><xsl:value-of select="@XMLUrl"/></xsl:attribute>
				<xsl:value-of select="@XMLFile"/>
			</xsl:element>
			<xsl:apply-templates/>
			<xsl:comment>#include virtual="/global/bottom_short.php" </xsl:comment>
		</body>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestFile">
		<hr/>
		<H2>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'TestFile'"/>
			</xsl:call-template>
			<xsl:value-of select="@FileName"/>
		</H2>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SchemaValidationOutput[@ValidationResult='Valid']">
		<hr/>
		<H3>
			<font color="#00ff00">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'SchemaSuccess'"/>
				</xsl:call-template>
			</font>
		</H3>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SchemaValidationOutput">
		<hr/>
		<H3>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'SchemaOut'"/>
			</xsl:call-template>
		</H3>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SchemaValidationOutput/Error">
		<font color="#ff3333">
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'SchemaError'"/>
			</xsl:call-template>
		</font>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SchemaValidationOutput/FatalError">
		<font color="#ff3333">Schema Fatal Error: </font>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SchemaValidationOutput/Warning">
		<font color="#aaaa00">Schema Warning: </font>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<xsl:template match="CheckJDFOutput[@IsValid='true']">
		<hr/>
		<H3>
			<font color="#00ff00">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'CheckJDF Validation Successful: '"/>
				</xsl:call-template>
			</font>
		</H3>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="CheckJDFOutput">
		<hr/>
		<H3>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'CheckJDF Validation Output: '"/>
			</xsl:call-template>
		</H3>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='InvalidElement']">
		<!-- ===============================================
<H4>

                                <xsl:call-template name="localize">
                                        <xsl:with-param name="string" select="Invalid Element: ''"/>
                                </xsl:call-template>
<xsl:value-of select="@NodeName"/> 
                                <xsl:call-template name="localize">
                                        <xsl:with-param name="string" select="'at'"/>
                                </xsl:call-template>
<xsl:value-of select="@XPath"/> 
</H4>
<xsl:text>Message: </xsl:text> <xsl:value-of select="@Message"/>
 =============================================== -->
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='PrivateElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'PrivateElement'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='MissingElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Missing Required Element: '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='UnknownElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'UnknownElement'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='DeprecatedElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'DeprecatedElement'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='PrereleaseElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'PrereleaseElement'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='SwapElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'SwapElement'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='MissingResourceLink']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'MissLink'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='UnknownResourceLink']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Missing ResourceLink: '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='MissingProcessUsage']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'MissProcUsage'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='InvalidResourceLink']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'InvalidLink'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='ResLinkMultipleID']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'ResourceLink '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'MultiID'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='DanglingResLink']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'DangleLink'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='DanglingPartResLink']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'DanglePartLink'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<br/>Resource PartUsage: <xsl:value-of select="@ResourcePartUsage"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='DanglingRefElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'DangleRefElem'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='DanglingPartRefElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'DanglePartRefElem'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<br/>Resource PartUsage: <xsl:value-of select="@ResourcePartUsage"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='InvalidPosition']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'InvResPosition'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='UnlinkedResource']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'UnlinkedRes'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='InvalidRefElement']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'InvRefElem'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestElement[@ErrorType='PrivateContents']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'PrivateContents'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H4>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- display partition keys -->
	<xsl:template match="TestElement/Part">
		<xsl:for-each select="@*">
			<br/>Partition key: <xsl:value-of select="name()"/> = <xsl:value-of select="."/>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="DeviceCapTest"/>
	<!-- =============================================== -->
	<xsl:template match="TestElement">
		<!-- =============================================== 
<H4>

                                <xsl:call-template name="localize">
                                        <xsl:with-param name="string" select="'General Invalid Element: '"/>
                                </xsl:call-template>
<xsl:value-of select="@NodeName"/> 
 at: <xsl:value-of select="@XPath"/></H4>
<xsl:text>Error Type: </xsl:text> <xsl:value-of select="@ErrorType"/>
<br/>
<xsl:text>Message: </xsl:text> <xsl:value-of select="@Message"/>
 =============================================== 
-->
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestFile/Error">
		<H3>
			<font color="#ff0000">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'General Error: '"/>
				</xsl:call-template>
			</font>
		</H3>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'Error Type: '"/>
		</xsl:call-template>
		<xsl:value-of select="@ErrorType"/>
		<br/>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SeparationPool">
		<H3>
			<font color="#000000">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'Warning: Inconsistent Separations: '"/>
				</xsl:call-template>
			</font>
		</H3>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SeparationPool/Warning[@ErrorType='MissingSeparation']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Separation not defined in ColorPool: '"/>
			</xsl:call-template>
			<xsl:value-of select="@Separation"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="SeparationPool/Warning[@ErrorType='UnreferencedSeparation']">
		<H4>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Separation defined in ColorPool but never referenced: '"/>
			</xsl:call-template>
			<xsl:value-of select="@Separation"/>
		</H4>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- Attributes below here -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='MultipleID']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Multiple ID Attribute at: '"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<br/>
		<xsl:text>ID: </xsl:text>
		<xsl:value-of select="@Value"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='PreReleaseAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Prerelease Attribute '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/> at: <xsl:value-of select="@XPath"/>
		</H5> First Valid Version:
                        <xsl:value-of select="@FirstVersion"/>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='DeprecatedAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Deprecated Attribute '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'Last Valid Version: '"/>
		</xsl:call-template>
		<xsl:value-of select="@LastVersion"/>
		<br/>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='PrivateAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'PrivateAttribute'"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'prefix'"/>
		</xsl:call-template>
		<xsl:value-of select="@NSPrefix"/>
		<BR/>
		<xsl:text>Namespace URI: </xsl:text>
		<xsl:value-of select="@NSURI"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='UnknownAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Unknown Attribute '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='MissingAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Missing Required Attribute '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='InvalidAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Invalid Attribute Value '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/> at: <xsl:value-of select="@XPath"/>
		</H5>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>: <xsl:value-of select="@Value"/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute[@ErrorType='SwapAttribute']">
		<H5>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'Element written as Attribute: '"/>
			</xsl:call-template>
			<xsl:value-of select="@NodeName"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'value'"/>
			</xsl:call-template>
			<xsl:value-of select="@Value"/>
		</H5>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<xsl:template match="TestAttribute">
		<H5>
			<font color="#ff0000">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'Tested Attribute'"/>
				</xsl:call-template>
				<xsl:value-of select="@NodeName"/>
			</font>
			<xsl:call-template name="localize">
				<xsl:with-param name="string" select="'at'"/>
			</xsl:call-template>
			<xsl:value-of select="@XPath"/>
		</H5>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'Error Type: '"/>
		</xsl:call-template>
		<xsl:value-of select="@ErrorType"/>
		<br/>
		<xsl:call-template name="localize">
			<xsl:with-param name="string" select="'message'"/>
		</xsl:call-template>
		<xsl:value-of select="@Message"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<!-- =============================================== -->
	<xsl:template match="*">
		<H4>
			<Font color="#ff3333">
				<xsl:call-template name="localize">
					<xsl:with-param name="string" select="'Unmatched element: TODO fix xslt for this'"/>
				</xsl:call-template>
			</Font>
		</H4>
		<xsl:value-of select="name()"/>
		<br/>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================== -->
</xsl:stylesheet>
