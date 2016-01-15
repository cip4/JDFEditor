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

/**
 * Configuration for a tunnel
 *
 * @author Inderjeet Singh
 */
public final class TunnelConfig {

  public TunnelConfig(String serverHost, int serverPort,
                      int localPort, boolean startTunnel, boolean ssl) {
    this.serverName = serverHost;
    this.serverPort = serverPort;
    this.localPort = localPort;
    this.startTunnel = startTunnel;
    this.ssl = ssl;
  }

  public TunnelConfig(String serverHost, String serverPort,
                      String localPort, boolean startTunnel, boolean ssl) {
    this.serverName = serverHost;
    try {
      this.serverPort = Integer.parseInt(serverPort);
      this.localPort = Integer.parseInt(localPort);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    this.startTunnel = startTunnel;
    this.ssl = ssl;
  }

  public String toString() {
    return localPort + ":" + serverName + ":" + serverPort + ":" + startTunnel + (ssl ? " SSL" : "");
  }

  public final String serverName;
  public final int serverPort;
  public final int localPort;
  public final boolean startTunnel;
  public final boolean ssl;
}
