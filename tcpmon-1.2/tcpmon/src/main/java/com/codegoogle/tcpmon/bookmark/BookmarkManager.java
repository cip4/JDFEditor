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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.codegoogle.tcpmon.Debug;


/**
 * This class handles bookmarks by reading and writing a file on the filesystem.
 * <p/>
 * <p>This implementation does not try to be fancy, and read/write from/to FS
 * every time the bookmark list is accessed.  It currently is sufficient, but
 * can be revisited in the future.</p>
 *
 * @author Sebastien Le Callonnec
 */
public class BookmarkManager {
  private final String bookmarkFile;

  public BookmarkManager(String bookmarkFile) {
    this.bookmarkFile = bookmarkFile;
  }

  /**
   * reads the bookmarks from <code>bookmarkFile</code>, and returns a list
   * of {@link Bookmark}s.
   *
   * @return
   */
  public List<Bookmark> list() {
    List<Bookmark> bookmarks = new ArrayList<Bookmark>();
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(new File(bookmarkFile)));
      String currentLine;
      while ((currentLine = bufferedReader.readLine()) != null) {
        String[] values = currentLine.split("\\|");

        Bookmark bookmark = new Bookmark(values[0], values[1], values[2], values[3], Boolean.valueOf(values[4]));
        bookmarks.add(bookmark);
      }
    } catch (Exception e) {
      if (Debug.level >= Debug.MIN_DEBUG)
        Debug.println(e.getMessage());
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return bookmarks;
  }

  /**
   * adds a {@link Bookmark} to the list, and saves the file to the filesystem.
   *
   * @param bookmark
   */
  public void add(Bookmark bookmark) {
    List<Bookmark> existingBookmarks = list();
    existingBookmarks.add(bookmark);
    write(existingBookmarks);
  }

  private void write(List<Bookmark> bookmarks) {
    createTcpmonFolderIfNeeded();
    BufferedWriter bufferedWriter = null;
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(new File(bookmarkFile)));
      for (Bookmark bookmark : bookmarks) {
        bufferedWriter.write(String.format("%s|%s|%s|%s|%s",
            bookmark.getName(),
            bookmark.getLocalPort(),
            bookmark.getRemoteHost(),
            bookmark.getRemotePort(),
            bookmark.isSslServer()));
        bufferedWriter.newLine();
      }
    } catch (Exception e) {
      Debug.print(e);
    } finally {
      if (bufferedWriter != null) {
        try {
          bufferedWriter.close();
        } catch (IOException e) {
          Debug.print(e);
        }
      }
    }
  }

  private void createTcpmonFolderIfNeeded() {
    File actualBookmarkFile = new File(this.bookmarkFile);
    if (!actualBookmarkFile.getParentFile().exists()) {
      actualBookmarkFile.getParentFile().mkdirs();
    }
  }
}
