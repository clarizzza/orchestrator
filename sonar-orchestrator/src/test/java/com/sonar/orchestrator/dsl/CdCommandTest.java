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
package com.sonar.orchestrator.dsl;

import com.sonar.orchestrator.junit.PropertyFilterRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PropertyFilterRunner.class)
public class CdCommandTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void should_keep_path_in_context() throws IOException {
    File dir = temp.newFolder();

    CdCommand command = new CdCommand(dir.getAbsolutePath());
    assertThat(command.getPath()).isEqualTo(dir.getAbsolutePath());

    Dsl.Context context = new Dsl.Context();
    command.execute(context);

    assertThat(context.getState(CdCommand.PWD_KEY)).isEqualTo(dir);
  }

  @Test
  public void should_fail_if_dir_does_not_exist() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Directory does not exist: /does/not/exist");

    CdCommand command = new CdCommand("/does/not/exist");
    // do not fail during parsing
    assertThat(command.getPath()).isEqualTo("/does/not/exist");

    Dsl.Context context = new Dsl.Context();
    command.execute(context);
  }
}
