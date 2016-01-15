/*
 * Copyright (c) 2004-2011 tcpmon authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * $Id$
 */
package com.codegoogle.tcpmon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * A tcp tunnel between two end points
 *
 * @author Inderjeet Singh
 */
public final class Tunnel extends Thread {
  /**
   * One of the possible states of the tunnel
   */
  public static final String FAILED = "Failed";
  public static final String CONNNECTED = "Connected";
  /**
   * One of the possible states of the tunnel
   */
  public static final String FINISHED = "Finished";

  public Tunnel(TunnelConfig config, CallBack callback) {
    super("tunnel-" + Debug.getUniqueId());
    this.config = config;
    this.callback = callback;
    this.start();
  }

  public void run() {
    if (!config.startTunnel)
      return;
    Socket client = null;
    try {
      ServerSocket server = new ServerSocket(config.localPort, MaxBackLog);
      while (!stopNow) {
        client = server.accept();
        (new ConnectionHandler(client)).start();
      }
    } catch (IOException ioe) {
      String clientName = "";
      if (client != null) {
        clientName = client.getInetAddress().getHostName();
      }
      reportException(ioe, clientName);
    }
  }

  private void reportException(Exception e, String clientName) {
    String stackTrace = Utils.extractStackTrace(e);
    CallBack.CallBackData callbackdata = new CallBack.CallBackData(
        config.localPort, clientName, config.serverName);
    callbackdata.set(stackTrace, "");
    callbackdata.setState(FAILED);
    callback.connectionFinished(callbackdata);
  }

  public void stopNow() {
    stopNow = true;
  }

  private boolean stopNow = false;

  public String toString() {
    return "(" + config.localPort + ", " + config.serverName +
        ":" + config.serverPort + ")";
  }

  private TunnelConfig config;
  private CallBack callback;
  private static final int MaxBackLog = 5;

  private final class ConnectionHandler extends Thread {
    ConnectionHandler(Socket client) {
      super("conn-" + Debug.getUniqueId());
      this.client = client;
    }

    public void run() {
      Socket server = null;
      InetAddress clientAddr = client.getInetAddress();
      CallBack.CallBackData callbackdata = new CallBack.CallBackData(config.localPort, clientAddr.getHostName(), config.serverName);
      ByteArrayOutputStream forwardData = new ByteArrayOutputStream();
      ByteArrayOutputStream reverseData = new ByteArrayOutputStream();
      try {
        if (Debug.level >= Debug.MIN_DEBUG)
          Debug.println("Starting connection thread for " +
              Tunnel.this.getName() + " ...");
        if (Debug.level >= Debug.FULL_DEBUG)
          Debug.println("Connecting " + Tunnel.this + "...");
        if (config.ssl) {
          server = SSLSocketFactory.getDefault().createSocket(config.serverName, config.serverPort);
        } else {
          server = new Socket(config.serverName, config.serverPort);
        }

        // TBD: Reuse the current thread to run one of the streams
        OutputStream[] fdst = new OutputStream[2];
        fdst[0] = server.getOutputStream();
        fdst[1] = forwardData;
        Thread forward = new StreamThread(client.getInputStream(), fdst);
        forward.start();

        // Run the reverse stream in its own thread
        OutputStream[] rdst = new OutputStream[2];
        rdst[0] = client.getOutputStream();
        rdst[1] = reverseData;
        Thread reverse = new StreamThread(server.getInputStream(), rdst);
        //reverse.start();
        reverse.run();

        forward.join();
        //reverse.join();
      } catch (Exception e) {
        e.printStackTrace(new PrintStream(forwardData));
        try {
          if (client != null)
            client.close();
        } catch (Exception ee) {
          ee.printStackTrace(new PrintStream(forwardData));
        }
        try {
          if (server != null)
            server.close();
        } catch (Exception ee) {
          ee.printStackTrace(new PrintStream(forwardData));
        }
      } finally {
        if (Debug.level >= Debug.MIN_DEBUG)
          Debug.println("Ending connection thread for " + Tunnel.this.getName() + " ...");
        callbackdata.set(forwardData.toString(), reverseData.toString());
        callbackdata.setState(FINISHED);
        callback.connectionFinished(callbackdata);
      }
    }

    private Socket client;
  }
}
