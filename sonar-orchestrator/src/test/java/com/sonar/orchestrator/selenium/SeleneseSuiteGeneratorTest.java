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
package com.sonar.orchestrator.selenium;

import com.sonar.orchestrator.junit.PropertyFilterRunner;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PropertyFilterRunner.class)
public class SeleneseSuiteGeneratorTest {
  SeleneseSuiteGenerator generator = new SeleneseSuiteGenerator();

  @Test
  public void shouldCreateTargetDirectoryAndCopyTestFiles() {
    File toDir = new File("target/test-tmp/SeleneseSuiteGeneratorTest/shouldCreateTargetDirectoryAndCopyTestFiles");
    FileUtils.deleteQuietly(toDir);

    File suite = generator.generateSuite("shouldCreateTargetDirectoryAndCopyTestFiles", toDir,
        getTestFile("shouldCreateTargetDirectoryAndCopyTestFiles/test1.html"),
        getTestFile("shouldCreateTargetDirectoryAndCopyTestFiles/test2.html"));

    assertThat(suite).exists();
    assertThat(suite.getName()).isEqualTo("shouldCreateTargetDirectoryAndCopyTestFiles.html");
    assertThat(suite.getParentFile()).isEqualTo(toDir);
    assertThat(FileUtils.listFiles(toDir, new String[] {"html"}, false)).hasSize(3);
    assertThat(new File(suite.getParentFile(), "test1.html")).exists();
    assertThat(new File(suite.getParentFile(), "test2.html")).exists();
  }

  @Test
  public void shouldCreateHtmlTable() throws IOException {
    File toDir = new File("target/test-tmp/SeleneseSuiteGeneratorTest/shouldCreateHtmlTable");

    File suite = generator.generateSuite("shouldCreateHtmlTable", toDir,
        getTestFile("shouldCreateHtmlTable/test2.html"),
        getTestFile("shouldCreateHtmlTable/test1.html"));

    assertThat(suite).exists();
    String html = FileUtils.readFileToString(suite);
    assertThat(html)
        .startsWith("<!-- Generated by Orchestrator --><html><body>")
        .endsWith("</body></html>")
        .contains("<tr><td><a href=\"./test2.html\">test2.html</a></td></tr><tr><td><a href=\"./test1.html\">test1.html</a></td></tr>");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailIfNoTests() {
    File toDir = new File("target/test-tmp/SeleneseSuiteGeneratorTest/shouldFailIfNoTests");

    generator.generateSuite("shouldFailIfNoTestst", toDir);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailIfTestNotExists() {
    File toDir = new File("target/test-tmp/SeleneseSuiteGeneratorTest/shouldFailIfTestNotExists");

    generator.generateSuite("shouldFailIfTestNotExists", toDir, new File("unknown/file.html"));
  }

  @Test
  public void should_rename_suite_with_same_name_as_test_file() {
    File toDir = new File("target/test-tmp/SeleneseSuiteGeneratorTest/shouldCreateHtmlTable");

    File suite = generator.generateSuite("test1", toDir, getTestFile("shouldCreateHtmlTable/test1.html"));

    assertThat(suite).exists();
    assertThat(suite.getName()).isEqualTo("test1-suite.html");
  }

  private File getTestFile(String path) {
    return FileUtils.toFile(getClass().getResource("/com/sonar/orchestrator/selenium/SeleneseSuiteGeneratorTest/" + path));
  }
}
