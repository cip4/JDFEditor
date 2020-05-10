/**
 * The CIP4 Software License, Version 1.0
 *
 * Copyright (c) 2001-2020 The International Cooperation for the Integration of Processes in Prepress, Press and Postpress (CIP4). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must include the following acknowledgment: "This product includes software developed by the The International Cooperation for
 * the Integration of Processes in Prepress, Press and Postpress (www.cip4.org)" Alternately, this acknowledgment may appear in the software itself, if and wherever such third-party acknowledgments
 * normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of Processes in Prepress, Press and Postpress" must not be used to endorse or promote products derived from this software
 * without prior written permission. For written permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4", nor may "CIP4" appear in their name, without prior written permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE. ====================================================================
 *
 * This software consists of voluntary contributions made by many individuals on behalf of the The International Cooperation for the Integration of Processes in Prepress, Press and Postpress and was
 * originally based on software copyright (c) 1999-2001, Heidelberger Druckmaschinen AG copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the Integration of Processes in Prepress, Press and Postpress , please see <http://www.cip4.org/>.
 *
 *
 */
package org.cip4.tools.jdfeditor.transport;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.util.StringUtil;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * standalone jetty server wrapper
 *
 * @author rainer prosi
 * @date Dec 9, 2010
 */
public abstract class JettyServer
{

    protected int thePort;
    protected int sslPort;
    protected SslContextFactory sslFactory;
    protected String context;
    protected Server server;
    protected final Log log;
    protected static JettyServer theServer;

    /**
     * @param context
     * @param port
     *
     */
    public JettyServer(String context, final int port)
    {
        super();
        log = LogFactory.getLog(getClass());
        setPort(port);
        context = setContext(context);
        log.info("creating JettyServer at context: " + context + " port: " + port);
        sslPort = -1;
    }

    protected String setContext(String context)
    {
        if (context == null)
            context = "";
        else if (!context.startsWith("/"))
            context = "/" + context;
        this.context = context;
        return context;
    }

    /**
     * set the port
     *
     * @param port the port to set
     */
    public void setPort(final int port)
    {
        thePort = port;
    }

    /**
     *
     * @param port the ssl port
     * @param keystorePath
     * @return may be used for additional setup
     */
    public SslContextFactory setSSLPort(final int port, String keystorePath)
    {
        sslPort = port;
        if (port <= 0)
        {
            sslFactory = null;
        }
        else
        {
            sslFactory = new SslContextFactory();

            if (keystorePath == null)
            {
                keystorePath = getDefaultKeyStore();
                sslFactory.setKeyStorePassword("changeit");
            }
            sslFactory.setKeyStorePath(keystorePath);
        }
        return sslFactory;
    }

    /**
     *
     * @return
     */
    public String getDefaultKeyStore()
    {

        final File loc = SystemUtils.getJavaHome();
        if (loc != null)
        {
            final File f = new File(loc, "lib/security/cacerts");
            if (f.canRead())
            {
                return f.getAbsolutePath();
            }
            else
            {
                log.warn("Cannot read " + f.getAbsolutePath());
            }
        }
        return null;
    }

    /**
     *
     *
     *
     */
    public JettyServer()
    {
        super();
        log = LogFactory.getLog(getClass());
        context = "";
        thePort = getDefaultPort();
    }

    /**
     *
     * the doing routine to run a jetty server
     *
     * it is generally a bad idea to overwrite this routine - it is not final to allow an empty null server
     *
     * @throws Exception
     * @throws InterruptedException
     */
    public void runServer() throws Exception, InterruptedException
    {
        if (server != null)
        {
            log.error("server already existing - whazzup");
        }
        log.info("creating new server at port: " + thePort + context);
        server = new Server(thePort);
        updateSSL();
        final HandlerList handlers = createHandlerList();
        final HandlerCollection handlerbase = createBaseCollection(handlers);
        server.setHandler(handlerbase);
        server.start();
        log.info("completed starting new server at port: " + thePort + context);
    }

    /**
     *
     */
    protected void updateSSL()
    {
        if (sslPort > 0)
        {
            log.info("Updating ssl port to: " + sslPort);
            final HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());
            final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslFactory, "http/1.1");
            final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(https);
            final ServerConnector sslConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
            sslConnector.setPort(sslPort);
            server.addConnector(sslConnector);
        }

    }

    /**
     *
     *
     * @return
     */
    protected HandlerList createHandlerList()
    {
        final HandlerList handlers = new HandlerList();
        // the resource handler is always first
        final ResourceHandler resourceHandler = createResourceHandler();
        handlers.addHandler(resourceHandler);

        addMoreHandlers(handlers);

        final ServletContextHandler context = createServletHandler();
        context.getContextPath();

        handlers.addHandler(context);
        handlers.addHandler(new RedirectHandler());
        return handlers;
    }

    /**
     *
     * create the base handler collection that contains all handlers to be processed multiple times
     *
     * @param handlers
     * @return
     */
    protected HandlerCollection createBaseCollection(final HandlerList handlers)
    {
        final HandlerCollection handlerbase = new HandlerCollection();
        handlerbase.addHandler(handlers);
        addHttpLogger(handlerbase);
        return handlerbase;
    }

    /**
     * hook to add more handlers if required
     *
     * @param handlers
     */
    protected void addMoreHandlers(final HandlerList handlers)
    {
        // nop
    }

    /**
     * hook to add http loggers or other post handling handlers, if required
     *
     * @param handlerbase
     */
    protected void addHttpLogger(final HandlerCollection handlerbase)
    {
        final RequestLog requestLog = createRequestLog();
        if (requestLog != null)
        {
            final RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setRequestLog(requestLog);
            handlerbase.addHandler(requestLogHandler);
            log.info("adding http log handler");
        }
    }

    /**
     * create a request log - the default method does not
     *
     * @return
     */
    protected RequestLog createRequestLog()
    {
        return null;
    }

    /**
     * get the base url of this server
     *
     * @return
     */
    public String getBaseURL()
    {
        try
        {
            return "http://" + InetAddress.getLocalHost().getHostName() + ":" + thePort + context;
        }
        catch (final UnknownHostException e)
        {
            log.fatal("the network looks real bad...", e);
            return null;
        }
    }

    /**
     *
     * simple resource (file) handler that tweeks the url to match the context, thus allowing servlets to emulate a war file without actually requiring the war file
     *
     * @author rainer prosi
     * @date Dec 10, 2010
     */
    public class MyResourceHandler extends ResourceHandler
    {

        public MyResourceHandler(final String strip)
        {
            super();
            this.strip = StringUtil.getNonEmpty(strip);
        }

        private final String strip;

        @Override
        public Resource getResource(String url)
        {

            if (strip != null && url.startsWith(strip) && (url.length() == strip.length() || url.charAt(strip.length()) == '/'))
            {
                url = url.substring(strip.length());
            }
            if ("".equals(url) || "/".equals(url))
            {
                return super.getResource(getHome());
            }
            return super.getResource(url);
        }

        @Override
        public String toString()
        {
            return "MyResourceHandler [strip=" + strip + ", getResourceBase()=" + getResourceBase() + "]";
        }
    }

    /**
     *
     * @author rainer prosi
     *
     */
    private class RedirectHandler extends AbstractHandler
    {
        /**
         * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        @Override
        public void handle(final String pathInContext, final Request arg1, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
        {
            response.sendRedirect(getHome());
            arg1.setHandled(true);
        }
    }

    /**
     *
     * @return
     */
    protected ResourceHandler createResourceHandler()
    {
        final ResourceHandler resourceHandler = new MyResourceHandler(context);
        resourceHandler.setResourceBase(".");
        return resourceHandler;
    }

    /**
     * @return the home url, e.g. "."+context+"/overview"
     */
    protected abstract String getHome();

    /**
     *
     * @return
     */
    protected abstract int getDefaultPort();

    /**
     *
     * @return
     */
    protected abstract ServletContextHandler createServletHandler();

    /**
     *
     * @see Server start()
     */
    public void start()
    {
        if (server == null)
        {
            try
            {
                if (sslPort > 0)
                {
                    theServer.setSSLPort(sslPort, null);
                }

                runServer();
            }
            catch (final Throwable e1)
            {
                log.error("Snafu creating server at Port: " + thePort + context, e1);
            }
        }
        try
        {
            server.start();
        }
        catch (final Throwable e)
        {
            log.error("Snafu starting server: ", e);
        }
        log.info("finished starting server");
    }

    /**
     *
     * @see Server stop()
     */
    public void stop()
    {
        if (server != null)
        {
            log.info("Stopping server");
            try
            {
                server.stop();
            }
            catch (final Throwable e)
            {
                log.error("Snafu stopping server: ", e);
            }
            log.info("Stopped server");
        }
        else
        {
            log.warn("Stopping null server");
        }
    }

    /**
     *
     * @see Server destroy()
     */
    public void destroy()
    {
        if (server != null)
            server.destroy();
    }

    /**
     * get the port number the port is always a singleton in a jetty environment
     *
     * @return
     */
    public int getPort()
    {
        return thePort;
    }

    public boolean isRunning()
    {
        return server != null && server.isRunning();
    }

    public boolean isStarted()
    {
        return server != null && server.isStarted();
    }

    public boolean isStarting()
    {
        return server != null && server.isStarting();
    }

    public boolean isStopped()
    {
        return server == null || server.isStopped();
    }

    public boolean isStopping()
    {
        return server != null && server.isStopping();
    }

    /**
     * @return
     */
    public static JettyServer getServer()
    {
        return theServer;
    }

    /**
     *
     */
    public static void shutdown()
    {
        if (theServer != null)
        {
            theServer.stop();
            theServer = null;
        }
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getServerType() + " [context=" + context + ":" + thePort + "ssl=" + sslPort + "]";
    }

    /**
     * get the server type as a string
     *
     * @return
     */
    public String getServerType()
    {
        return getClass().getSimpleName();
    }

    /**
     *
     * @param server
     */
    public static void setServer(final JettyServer server)
    {
        theServer = server;
    }

}