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
package com.sonar.orchestrator.util;

import com.sonar.orchestrator.junit.PropertyFilterRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PropertyFilterRunner.class)
public class NetworkUtilsTest {

  @Test
  public void shouldGetAvailablePortWithoutLockingHost() {
    for (int i = 0; i < 1000; i++) {
      /*
       * The Well Known Ports are those from 0 through 1023.
       * DCCP Well Known ports SHOULD NOT be used without IANA registration.
       */
      assertThat(NetworkUtils.getNextAvailablePort()).isGreaterThan(1023);
    }
  }

  @Test
  public void shouldGetRandomPort() {
    assertThat(NetworkUtils.getNextAvailablePort()).isNotEqualTo(NetworkUtils.getNextAvailablePort());
  }

  @Test
  public void shouldNotBeValidPorts() {
    assertThat(NetworkUtils.isValidPort(0)).isFalse();// <=1023
    assertThat(NetworkUtils.isValidPort(50)).isFalse();// <=1023
    assertThat(NetworkUtils.isValidPort(1023)).isFalse();// <=1023
    assertThat(NetworkUtils.isValidPort(2049)).isFalse();// NFS
    assertThat(NetworkUtils.isValidPort(4045)).isFalse();// lockd
  }

  @Test
  public void shouldBeValidPorts() {
    assertThat(NetworkUtils.isValidPort(1059)).isTrue();
  }
}
