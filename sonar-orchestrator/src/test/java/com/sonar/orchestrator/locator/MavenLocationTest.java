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
package com.sonar.orchestrator.locator;

import com.sonar.orchestrator.junit.PropertyFilterRunner;
import com.sonar.orchestrator.version.Version;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PropertyFilterRunner.class)
public class MavenLocationTest {

  @Test
  public void testCreate() {
    MavenLocation location = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").build();
    assertThat(location.getGroupId()).isEqualTo("org.codehaus.sonar-plugins");
    assertThat(location.getArtifactId()).isEqualTo("sonar-clirr-plugin");
    assertThat(location.version()).isEqualTo(Version.create("1.1"));
    assertThat(location.getPackaging()).isEqualTo("jar");
    assertThat(location.getFilename()).isEqualTo("sonar-clirr-plugin-1.1.jar");
  }

  @Test
  public void testOf() {
    MavenLocation location = MavenLocation.of("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1");
    assertThat(location.getGroupId()).isEqualTo("org.codehaus.sonar-plugins");
    assertThat(location.getArtifactId()).isEqualTo("sonar-clirr-plugin");
    assertThat(location.version()).isEqualTo(Version.create("1.1"));
    assertThat(location.getPackaging()).isEqualTo("jar");
    assertThat(location.getFilename()).isEqualTo("sonar-clirr-plugin-1.1.jar");
  }

  @Test
  public void shouldOverrideFilename() {
    MavenLocation location = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").withFilename("clirr11.jar").build();
    assertThat(location.getGroupId()).isEqualTo("org.codehaus.sonar-plugins");
    assertThat(location.getArtifactId()).isEqualTo("sonar-clirr-plugin");
    assertThat(location.version()).isEqualTo(Version.create("1.1"));
    assertThat(location.getPackaging()).isEqualTo("jar");
    assertThat(location.getFilename()).isEqualTo("clirr11.jar");
  }

  @Test
  public void shouldUsePackagingAsSuffix() {
    MavenLocation location = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").withPackaging("zip").build();
    assertThat(location.getFilename()).isEqualTo("sonar-clirr-plugin-1.1.zip");
    assertThat(location.getPackaging()).isEqualTo("zip");
  }

  @Test
  public void shouldUseClassifier() {
    MavenLocation location = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").setClassifier("runtime").build();
    assertThat(location.getFilename()).isEqualTo("sonar-clirr-plugin-1.1-runtime.jar");
    assertThat(location.getClassifier()).isEqualTo("runtime");
  }

  @Test(expected = IllegalArgumentException.class)
  public void groupIdShouldBeMandatory() {
    MavenLocation.builder().setArtifactId("sonar-clirr-plugin").setVersion("1.1").build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void artifactIdShouldBeMandatory() {
    MavenLocation.builder().setGroupId("org.codehaus.sonar-plugins").setVersion("1.1").build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void versionShouldBeMandatory() {
    MavenLocation.builder().setGroupId("org.codehaus.sonar-plugins").setArtifactId("sonar-clirr-plugin").build();
  }

  @Test
  public void test_equals_and_hashCode() {
    MavenLocation location1 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").build();
    MavenLocation location2 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").build();
    assertThat(location1.equals(location2)).isTrue();
    assertThat(location2.equals(location1)).isTrue();
    assertThat(location1.hashCode()).isEqualTo(location1.hashCode());

    location1 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").build();
    location2 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.2").build();
    assertThat(location1.equals(location2)).isFalse();

    location1 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-pdfreport-plugin", "1.1").build();
    location2 = MavenLocation.builder().setKey("org.codehaus.sonar-plugins", "sonar-clirr-plugin", "1.1").build();
    assertThat(location1.equals(location2)).isFalse();
  }

  @Test
  public void test_toString() {
    MavenLocation location = MavenLocation.create("foo", "bar", "1.0");

    assertThat(location.toString()).isEqualTo("[foo:bar:1.0:jar]");
  }
}
