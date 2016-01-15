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
 */
package com.codegoogle.tcpmon;

/**
 * A callback interface
 *
 * @author Inderjeet Singh
 */
public interface CallBack {
  public void connectionFinished(CallBackData data);

  public static class CallBackData {
    private String srcHost;
    private String dstHost;
    private String state;
    private long time;
    private int localPort;
    private String forwardData;
    private String reverseData;

    public CallBackData(int localPort, String srcHost, String dstHost) {
      this.localPort = localPort;
      this.srcHost = srcHost;
      this.dstHost = dstHost;
      time = System.currentTimeMillis();
    }

    public void set(String forwardData, String reverseData) {
      this.forwardData = forwardData;
      this.reverseData = reverseData;
    }

    public void setState(String state) {
      this.state = state;
    }

    public String getSourceHost() {
      return srcHost;
    }

    public String getDestinationHost() {
      return dstHost;
    }

    public String getState() {
      return state;
    }

    public long getTime() {
      return time;
    }

    public int getLocalPort() {
      return localPort;
    }

    public String getForwardData() {
      return forwardData;
    }

    public String getReverseData() {
      return reverseData;
    }
  }
}