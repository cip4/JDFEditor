#!/bin/bash
#export JAVA_HOME=/opt/java/jdk1.5.0_22
export PATH=$JAVA_HOME/bin:$PATH
export DOCBOOK_XSL_HOME=~/Projects/JavaHelp/docbook-xsl-1.75.2
export OUTPUT_PATH=../src/help/org/cip4/help
export JAVAHELP_HOME=/opt/JavaSoft/jh-2.0.05

rm --verbose $OUTPUT_PATH/*
rm --verbose $OUTPUT_PATH/JavaHelpSearch/*
echo Generate help files...
xsltproc --stringparam base.dir $OUTPUT_PATH/ $DOCBOOK_XSL_HOME/javahelp/javahelp.xsl index.xml

cd $OUTPUT_PATH
echo Indexing help files...
$JAVAHELP_HOME/javahelp/bin/jhindexer .
