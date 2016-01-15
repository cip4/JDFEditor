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
package com.codegoogle.tcpmon;

import java.security.AccessControlException;

/**
 * Class holding the configuration details for the tcpmon app.
 * 
 * @author Sebastien Le Callonnec
 */
public class Configuration {
  public final static String DEFAULT_LOCAL_PORT = "8080";
  public final static String DEFAULT_REMOTE_HOST = "127.0.0.1";
  public final static String DEFAULT_REMOTE_PORT = "80";

  public final static String TCPMON_CONF_DIR = ".tcpmon";
  public final static String BOOKMARK_FILE = "bookmarks.txt";

  private String localPort = DEFAULT_LOCAL_PORT;
  private String remoteHost = DEFAULT_REMOTE_HOST;
  private String remotePort = DEFAULT_REMOTE_PORT;
  private boolean autoStart;
  private int debugLevel = Debug.NO_DEBUG;

  public String getLocalPort() {
    return localPort;
  }

  public void setLocalPort(String localPort) {
    this.localPort = localPort;
  }

  public String getRemoteHost() {
    return remoteHost;
  }

  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  public String getRemotePort() {
    return remotePort;
  }

  public void setRemotePort(String remotePort) {
    this.remotePort = remotePort;
  }

  public boolean isAutoStart() {
    return autoStart;
  }

  public void setAutoStart(boolean autoStart) {
    this.autoStart = autoStart;
  }

  public int getDebugLevel() {
    return debugLevel;
  }

  public void setDebugLevel(int debugLevel) {
    this.debugLevel = debugLevel;
  }

  public String getBookmarkLocation() {
    try {
      return System.getProperty("user.home")
          + System.getProperty("file.separator")
          + Configuration.TCPMON_CONF_DIR
          + System.getProperty("file.separator")
          + Configuration.BOOKMARK_FILE;
    } catch (SecurityException e) {
      Debug.print(e);
    }
    
    return null;
  }
}
