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
 * This class provides some debugging support.
 *
 * @author Inderjeet Singh
 */
final public class Debug {
  public static final int NO_DEBUG = 0;
  public static final int DEBUG_EXCEPTIONS = 1;
  public static final int MIN_DEBUG = 2;
  public static final int FULL_DEBUG = 3;
  public static int level = DEBUG_EXCEPTIONS;
  //    public static final int level = MIN_DEBUG;
  //    public static final int level = FULL_DEBUG;

  public static void print(String str) {
    if (level >= MIN_DEBUG)
      System.err.print(Thread.currentThread().getName() +
          ": " + str);
  }

  public static void println(String str) {
    if (level >= MIN_DEBUG)
      System.err.println(Thread.currentThread().getName() +
          ": " + str);
  }

  public static void print(Exception e) {
    if (level >= DEBUG_EXCEPTIONS) {
      System.err.print(Thread.currentThread().getName() + ": ");
      e.printStackTrace();
    }
  }


  public static String getUniqueId() {
    // No need to wrap this call in a synchronized block since java
    // language guarantees the int increment to be thread-safe.
    return String.valueOf(uniqueId++);
  }

  private static int uniqueId = 0;
}
