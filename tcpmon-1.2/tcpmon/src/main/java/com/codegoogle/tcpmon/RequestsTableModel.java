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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Model data for requests table
 *
 * @author Inderjeet Singh
 */
public final class RequestsTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    return entries.size();
  }

  private String[] columnNames = {"State", "Time", "Request Host", "Target Host", "Request"};

  public String getColumnName(int col) {
    return columnNames[col].toString();
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public Object getValueAt(int row, int col) {
    String[] data = (String[]) entries.get(row);
    return data[col];
  }

  public String getForwardData(int row) {
    String[] data = (String[]) entries.get(row);
    return data[5];
  }

  public String getReverseData(int row) {
    String[] data = (String[]) entries.get(row);
    return data[6];
  }

  public void addEntry(String state, long time, String requestHost, String targetHost,
                       String request, String forwardData, String reverseData) {
    String[] entry = new String[7];
    entry[0] = state;
    entry[1] = (new Date(time)).toString();
    entry[2] = requestHost;
    entry[3] = targetHost;
    entry[4] = request;
    entry[5] = forwardData;
    entry[6] = reverseData;
    entries.add(0, entry);
    fireTableDataChanged();
  }

  public void deleteRow(int i) {
    entries.remove(i);
    fireTableDataChanged();
  }

  public void deleteAllRows() {
    entries.clear();
    fireTableDataChanged();
  }

  private List entries = new LinkedList();
};