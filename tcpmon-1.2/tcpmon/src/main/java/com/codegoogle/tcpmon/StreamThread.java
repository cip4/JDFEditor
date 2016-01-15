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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NoRouteToHostException;
import java.net.SocketException;

/**
 * This class downloads data from a stream in a separate thread.
 *
 * @author Inderjeet Singh
 */
final class StreamThread extends Thread {
  /**
   * configuration parameter: the time to sleep (in Millis) if the
   * data is not present on the input stream yet.
   */
  private static final int DATA_ARRIVAL_WAIT_TIME = 50;

  /**
   * configuration parameter: the size of the data buffer.
   */
  private static final int BUF_SIZE = 8072;

  private static final int MAX_NUM_RETRIES = 15;

  public StreamThread(InputStream src, OutputStream d) {
    super("stream-" + Debug.getUniqueId());
    assert src != null;
    assert d != null;
    this.src = src;
    this.dst = new OutputStream[1];
    this.dst[0] = d;
  }

  public StreamThread(InputStream src, OutputStream[] dst) {
    super("stream-" + Debug.getUniqueId());
    assert src != null;
    for (int i = 0; i < dst.length; ++i)
      assert dst[i] != null;
    this.src = src;
    this.dst = dst;
  }

  public void closeConnections() {
    try {
      src.close();
    } catch (Exception e) {
      Debug.print(e);
    }
    for (int i = 0; i < dst.length; ++i) {
      try {
        dst[i].close();
      } catch (Exception e) {
        Debug.print(e);
      }
    }
  }

  public void run() {
    try {
      if (Debug.level >= Debug.MIN_DEBUG)
        Debug.println("Starting stream thread ...");
      int count = copyStream();
      if (Debug.level >= Debug.FULL_DEBUG)
        Debug.println("transferred " + count + " bytes.");
    } catch (EOFException eofe) {
      // Normal behavior. Ignore it.
    } catch (NoRouteToHostException nrthe) {
      System.err.println("No route to the other end of the tunnel!");
    } catch (SocketException ioe) {
      System.out.println("Socket closed: " + ioe.getClass().toString());
    } catch (IOException ioe) {
      Debug.print(ioe);
    } finally {
      closeConnections();
      if (Debug.level >= Debug.MIN_DEBUG)
        Debug.println("Ending stream thread ...");
    }
  }

  private int copyStreamByteByByte() throws IOException {
    int bytesRead = 0;
    int tmp = 0;
    while ((tmp = src.read()) != -1) {
      ++bytesRead;
      for (int i = 0; i < dst.length; ++i) {
        dst[i].write((char) tmp);
      }
    }
    return bytesRead;
  }

  /**
   * copy all the data present in the src to the dst.
   */
  private int copyStream() throws IOException {

    byte buf[] = new byte[BUF_SIZE];
    int bytesRead = 0;
    int total = 0;
    int numRetries = 0;
    do {
      if (src.available() == 0) {
        if (numRetries >= MAX_NUM_RETRIES)
          throw new IOException("StreamThread: data not available " +
              "on the connection");
        try {
          Thread.currentThread().sleep(DATA_ARRIVAL_WAIT_TIME, 0);
        } catch (InterruptedException ie) {
          Debug.print(ie);
        }
        ++numRetries;
        if (Debug.level >= Debug.FULL_DEBUG)
          Debug.println("NumRetries: " + numRetries);
      }
      bytesRead = src.read(buf);
      if (bytesRead > 0) {
        numRetries = 0;
        for (int i = 0; i < dst.length; ++i) {
          dst[i].write(buf, 0, bytesRead);
        }
        total += bytesRead;
      }
    } while (bytesRead != -1);
    return total;
  }

  private InputStream src;
  private OutputStream[] dst;
}
