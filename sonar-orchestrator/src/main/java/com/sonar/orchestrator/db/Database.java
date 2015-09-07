/*
 * Orchestrator
 * Copyright (C) 2011 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.sonar.orchestrator.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface Database {

  DatabaseClient getClient();

  /**
   * Truncate a table. It does not fail if the table does not exist.
   */
  Database truncate(String tableName);

  /**
   * This is used only for SonarQube versions < 5.0.
   * Have to wait 5.0 becomes LTS before dropping this mechanism.
   */
  Database truncateInspectionTables();

  /**
   * Example : countSql("count(kee) from metrics")
   */
  int countSql(String sql);

  /**
   * Execute SQL request and return a list of rows. A row is a map of column name (in upper case) to string value.
   */
  List<Map<String, String>> executeSql(String sql);

  Connection openConnection();

  /**
   * clean other sql connection
   */
  void killOtherConnections();

  /**
   * Close the JDBC connection. Failures are logged with WARN level then are ignored.
   */
  Database closeQuietly(Connection connection);

  Map<String, String> getSonarProperties();

}
