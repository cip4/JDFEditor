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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link BookmarkManager} unit tests.
 *
 * @author Sebastien Le Callonnec
 */
public class BookmarkManagerTest {
  private BookmarkManager manager;
  
  @Before
  public void setUp() {
    File bookmarkStore = new File("src/test/resources", "bookmarks.txt");
    this.manager = new BookmarkManager(bookmarkStore.getAbsolutePath());
  }
  
  @Test
  public void testGetBookmarks() {
    List<Bookmark> bookmarks = this.manager.list();
    assertNotNull(bookmarks);
    assertEquals(2, bookmarks.size());
  }
  
  @Test
  public void testAddBookmark() throws Exception {
    File tmpBookmarkStore = File.createTempFile("bookmarks", "txt");
    BookmarkManager localManager = new BookmarkManager(tmpBookmarkStore.getAbsolutePath());
    localManager.add(new Bookmark("foobar", "1", "host", "2", false));
    List<Bookmark> bookmarks = localManager.list();
    
    assertNotNull(bookmarks);
    assertEquals(1, bookmarks.size());
  }
}
