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
package com.sonar.orchestrator.config;

import com.sonar.orchestrator.locator.Location;
import com.sonar.orchestrator.locator.Locators;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileSystem {

  private static final Logger LOG = LoggerFactory.getLogger(FileSystem.class);

  private File mavenLocalRepository;
  private URL nexusUrl;
  private String nexusRepository;
  private File mavenHome;
  private File antHome;
  private File javaHome;
  private File workspace;
  private File sonarInstallsDir;
  private final Configuration config;
  private final Locators locators;

  public FileSystem(Configuration config) {
    this.config = config;
    this.locators = new Locators(config);
    initWorkspace();
    initMavenHome();
    initAntHome();
    initUserHome();
    initJavaHome();
    initMavenLocalRepository();
    initMavenRemoteRepository();
  }

  private void initWorkspace() {
    String value = config.getString("orchestrator.workspaceDir", "target");
    workspace = new File(value);
  }

  private void initMavenLocalRepository() {
    String value = config.getStringByKeys("maven.localRepository", "MAVEN_LOCAL_REPOSITORY");
    if (StringUtils.isNotBlank(value)) {
      mavenLocalRepository = new File(value);
      if (!mavenLocalRepository.isDirectory() || !mavenLocalRepository.exists()) {
        throw new IllegalArgumentException("Maven local repository is not valid: " + value);
      }
    }
  }

  private void initMavenRemoteRepository() {
    String value = config.getStringByKeys("maven.nexusUrl", "MAVEN_NEXUS_URL");
    if (StringUtils.isBlank(value)) {
      LOG.warn("Nexus remote repository url is not set. Please define " + "maven.nexusUrl");
      return;
    }
    try {
      nexusUrl = new URL(value);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Maven remote repository is not valid: " + value, e);
    }
    nexusRepository = config.getStringByKeys("maven.nexusRepository", "MAVEN_NEXUS_REPOSITORY");
    if (StringUtils.isBlank(nexusRepository)) {
      LOG.warn("Nexus remote repository name is not set. Please define " + "maven.nexusRepository");
    }
  }

  private void initJavaHome() {
    String value = config.getStringByKeys("java.home", "JAVA_HOME");
    if (StringUtils.isNotBlank(value)) {
      javaHome = new File(value);
      if (!javaHome.isDirectory() || !javaHome.exists()) {
        throw new IllegalArgumentException("Java home is not valid: " + value);
      }
    }
    if (javaHome == null) {
      LoggerFactory.getLogger(FileSystem.class).warn("Java home is not set. Please set the property java.home or the env variable JAVA_HOME");
    }
  }

  private void initUserHome() {
    String value = config.getString("orchestrator.sonarInstallsDir");
    if (StringUtils.isNotBlank(value)) {
      sonarInstallsDir = new File(value);
    } else {
      String sonarUserHome = System.getenv("SONAR_USER_HOME");
      if (StringUtils.isNotBlank(sonarUserHome)) {
        sonarInstallsDir = new File(sonarUserHome, "installs");
      } else {
        File userHome = new File(System.getProperty("user.home"));
        sonarInstallsDir = new File(userHome, ".sonar/installs");
      }
    }
  }

  private void initMavenHome() {
    String value = config.getStringByKeys("maven.home", "MAVEN_HOME");
    if (StringUtils.isBlank(value)) {
      value = config.getStringByKeys("maven.home", "M2_HOME");
    }
    if (StringUtils.isNotBlank(value)) {
      mavenHome = new File(value);
      if (!mavenHome.isDirectory() || !mavenHome.exists()) {
        throw new IllegalArgumentException("Maven home is not valid: " + value);
      }
    }
  }

  private void initAntHome() {
    String value = config.getStringByKeys("ant.home", "ANT_HOME");
    if (StringUtils.isNotBlank(value)) {
      antHome = new File(value);
      if (!antHome.isDirectory() || !antHome.exists()) {
        throw new IllegalArgumentException("Ant home is not valid: " + value);
      }
    }
  }

  public File mavenLocalRepository() {
    return mavenLocalRepository;
  }

  public URL mavenNexusUrl() {
    return nexusUrl;
  }

  public String mavenNexusRepository() {
    return nexusRepository;
  }

  public File mavenHome() {
    return mavenHome;
  }

  public File antHome() {
    return antHome;
  }

  public File javaHome() {
    return javaHome;
  }

  public File workspace() {
    return workspace;
  }

  public File sonarInstallsDir() {
    return sonarInstallsDir;
  }

  public File locate(Location location) {
    return locators.locate(location);
  }

  public InputStream openInputStream(Location location) {
    return locators.openInputStream(location);
  }

  public File copyToDirectory(Location location, File toDir) {
    return locators.copyToDirectory(location, toDir);
  }

  public File copyToFile(Location location, File toFile) {
    return locators.copyToFile(location, toFile);
  }
}
