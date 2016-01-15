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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * {@link MainTest} unit tests.
 * 
 * @author Sebastien Le Callonnec
 */
public class MainTest {
  private Main main = new Main();

  @Test
  public void testParseArgsLocalPort() {
    String[] args = {"-localport", "1111"};

    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertEquals("1111", config.getLocalPort());
  }

  @Test
  public void testParseArgsRemoteHost() {
    String[] args = {"-remotehost", "localhost"};
    main.parseArgs(args);
    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertEquals("localhost", config.getRemoteHost());
  }

  @Test
  public void testParseArgsRemotePort() {
    String[] args = {"-remoteport", "443"};
    main.parseArgs(args);
    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertEquals("443", config.getRemotePort());
  }

  @Test
  public void testParseArgsAutoStart() {
    String[] args = {"-autostart"};
    main.parseArgs(args);
    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertTrue(config.isAutoStart());
  }

  @Test
  public void testParseArgsDebugLevel() {
    String[] args = {"-debuglevel", "2"};
    main.parseArgs(args);
    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertEquals(2, config.getDebugLevel());
  }

  @Test
  public void testParseArgsDebugLevelGreaterThanMaxLevel() {
    String[] args = {"-debuglevel", "42"};
    main.parseArgs(args);
    Configuration config = main.parseArgs(args);

    assertNotNull(config);
    assertEquals(Debug.FULL_DEBUG, config.getDebugLevel());
  }
}
