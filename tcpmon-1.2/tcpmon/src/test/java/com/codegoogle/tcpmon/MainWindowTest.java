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
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codegoogle.tcpmon.bookmark.Bookmark;
import com.codegoogle.tcpmon.bookmark.BookmarkManager;

/**
 * {@link MainWindow} unit tests.
 *
 * @author Sebastien Le Callonnec
 */
@RunWith(MockitoJUnitRunner.class)
public class MainWindowTest {

  @Mock
  private BookmarkManager bookmarkManager;
  private MainWindow mainWindow;
  private List<Bookmark> bookmarks;

  @Before
  public void setUp() {
    // For some weird reason, @InjectMocks doesn't work for the following line?!
    mainWindow = new MainWindow(bookmarkManager, new Configuration());

    // Sample bookmarks.
    bookmarks = new ArrayList<Bookmark>();
    bookmarks.add(new Bookmark("First", "9988", "localhost", "8080", false));
    bookmarks.add(new Bookmark("Second", "9999", "localhost", "8081", true));
  }

  @Test
  public void testCreateMenuFromBookmarksWithNullList() {
    when(bookmarkManager.list()).thenReturn(null);
    assertNoBookmark();
  }

  @Test
  public void testCreateMenuFromBookmarksWithEmptyList() {
    when(bookmarkManager.list()).thenReturn(new ArrayList<Bookmark>());
    assertNoBookmark();
  }

  private void assertNoBookmark() {
    JMenu bookmarkMenu = mainWindow.createMenuFromBookmarks();

    assertNotNull(bookmarkMenu);
    assertEquals("No bookmark", bookmarkMenu.getItem(1).getText());
  }

  @Test
  public void testCreateMenuFromBookmarks() {
    when(bookmarkManager.list()).thenReturn(bookmarks);

    JMenu bookmarkMenu = mainWindow.createMenuFromBookmarks();

    assertNotNull(bookmarkMenu);
    assertEquals("First", bookmarkMenu.getItem(1).getText());
    assertEquals("Second", bookmarkMenu.getItem(2).getText());
  }

// TODO(sebastien): figure out how to unit test menu item clicks
//  @Test
//  public void testLoadBookmarkEntry() throws Exception {
//    when(bookmarkManager.list()).thenReturn(bookmarks);
//
//    JMenu bookmarkMenu = mainWindow.createMenuFromBookmarks();
//    JMenuItem secondItem = bookmarkMenu.getItem(1);
//
//    MouseEvent event = new MouseEvent(secondItem, MouseEvent.MOUSE_CLICKED, 0,
//        MouseEvent.BUTTON1_MASK, 10, 10, 1, false);
//    Method process = Component.class.getDeclaredMethod(
//        "processEvent", AWTEvent.class);
//    process.setAccessible(true);
//    process.invoke(secondItem, event);
//
//    assertEquals("9988", mainWindow.getTfLocalPort().getText());
//    assertEquals("localhost", mainWindow.getTfRemoteHost().getText());
//    assertEquals("8080", mainWindow.getTfRemotePort().getText());
//    assertTrue(mainWindow.getCbSsl().isSelected());
//  }
}
