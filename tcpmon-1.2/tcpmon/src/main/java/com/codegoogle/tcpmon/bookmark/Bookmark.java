/*
 * Copyright (c) 2004-2011 tcpmon authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codegoogle.tcpmon.bookmark;

/**
 * This class holds the details of a tcpmon bookmark.
 *
 * @author Sebastien Le Callonnec
 */
public final class Bookmark {
  private final String name;
  private final String localPort;
  private final String remoteHost;
  private final String remotePort;
  private final boolean sslServer;

  public Bookmark(String name, String localPort, String remoteHost, String remotePort, boolean sslServer) {
    this.name = name;
    this.localPort = localPort;
    this.remoteHost = remoteHost;
    this.remotePort = remotePort;
    this.sslServer = sslServer;
  }

  public String getLocalPort() {
    return localPort;
  }

  public String getName() {
    return name;
  }

  public String getRemoteHost() {
    return remoteHost;
  }

  public String getRemotePort() {
    return remotePort;
  }

  public boolean isSslServer() {
    return sslServer;
  }
}
