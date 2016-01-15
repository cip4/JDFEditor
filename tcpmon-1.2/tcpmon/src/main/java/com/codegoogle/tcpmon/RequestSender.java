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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * A class to send requests on a thread
 *
 * @author Inderjeet Singh
 */
public class RequestSender extends Thread {

  public RequestSender(String data, TunnelConfig config, CallBack callback) {
    this(data.getBytes(), config, callback);
  }

  public RequestSender(byte[] data, TunnelConfig config, CallBack callback) {
    this.data = data;
    this.config = config;
    this.callback = callback;
    this.start();
  }

  public void run() {
    Socket s = null;
    CallBack.CallBackData callbackdata =
        new CallBack.CallBackData(config.localPort, REPORTED_CLIENT_NAME, config.serverName);
    ByteArrayOutputStream reverseData = new ByteArrayOutputStream();
    try {

      if (config.ssl) {
        s = SSLSocketFactory.getDefault().createSocket(config.serverName, config.serverPort);
      } else {
        s = new Socket(config.serverName, config.serverPort);
      }
      InputStream forwardData = new ByteArrayInputStream(data);
      Thread forward = new StreamThread(forwardData, s.getOutputStream());
      forward.start();

      // Reuse the current thread for one of the streams            
      Thread reverse = new StreamThread(s.getInputStream(), reverseData);
      //reverse.start();
      reverse.run();

      forward.join();
      //reverse.join();
    } catch (Exception e) {
      reportException(e);
    } finally {
      try {
        if (s != null) {
          s.close();
        }
      } catch (Exception e) {
        reportException(e);
      }
      // System.out.println(reverseData.toString());
      callbackdata.set(new String(data), reverseData.toString());
      callbackdata.setState(Tunnel.FINISHED);
      callback.connectionFinished(callbackdata);
    }
  }

  private void reportException(Exception e) {
    String stackTrace = Utils.extractStackTrace(e);
    CallBack.CallBackData callbackdata =
        new CallBack.CallBackData(config.localPort, REPORTED_CLIENT_NAME, config.serverName);
    callbackdata.set(stackTrace, "");
    callbackdata.setState(Tunnel.FAILED);
    callback.connectionFinished(callbackdata);
  }


  private byte[] data;
  private TunnelConfig config;
  private CallBack callback;
  private static final String REPORTED_CLIENT_NAME = "tcpmon-localhost";
}
