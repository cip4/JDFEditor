#!/bin/sh
#Make sure prerequisite environment variables are set
if [ "JAVA_HOME" = "" ]
then
    echo "The JAVA_HOME environment variable is not defined"
    echo "This environment variable is needed to run this program"
fi

#Setup Environment variables for JDFEditor
# Set any jars you want to appear at the begining of the classpath here
# remember to terminate with a :

PRE_JDFEDITOR_CLASSPATH=

#Set any jars you want to appear at the end of the classpath here
#remember to terminate with a :

POST_JDFEDITOR_CLASSPATH=


LIB_DIR=../lib
CLASSPATH=.:$PRE_JDFEDITOR_CLASSPATH:

CLASSPATH=$CLASSPATH$LIB_DIR/activation-1.0.2.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/commons-lang-2.5.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/commons-logging-1.1.1.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/commons-io-2.0.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/log4j-1.2.8.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/mailapi.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/xercesImpl.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/xml-apis.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/rsyntaxtextarea.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/tcpmon.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/jhall.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/javax.servlet.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/org.mortbay.jetty.jar:

CLASSPATH=$CLASSPATH$LIB_DIR/JDFLibJ-2.1.4a.jar:
CLASSPATH=$CLASSPATH$LIB_DIR/jdfeditor.jar:

CLASSPATH=$CLASSPATH$POST_JDFEDITOR_CLASSPATH

#if using cygwin under windows we need to make the paths windows friendly

case "`uname`" in
CYGWIN*)
  if [ -n "CLASSPATH" ]
  then
   CLASSPATH=`cygpath -pw $CLASSPATH`
  fi
esac

$JAVA_HOME/bin/java -Dapple.laf.useScreenMenuBar=true -classpath $CLASSPATH org.cip4.jdfeditor.Editor $@
