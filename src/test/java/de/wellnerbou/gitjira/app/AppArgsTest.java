package de.wellnerbou.gitjira.app;

import joptsimple.OptionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class AppArgsTest {

	@Test(expected = OptionException.class)
	public void testRequireArgForRepo() {
		new AppArgs("--repo");
	}

	@Test
	public void testDefaultRepo() {
		AppArgs appArgs = new AppArgs("rev1", "rev2");
		Assertions.assertThat(appArgs.getRepo()).isEqualTo(".");
	}

	@Test
	public void testJiraProjectPrefix() {
		AppArgs appArgs = new AppArgs("--projects", "PROJ1", "rev1", "rev2");
		Assertions.assertThat(appArgs.getJiraProjectPrefixes()).contains("PROJ1");
	}

	@Test
	public void testJiraProjectPrefixes() {
		AppArgs appArgs = new AppArgs("--projects", "PROJ1", "--projects", "PROJ2", "rev1", "rev2");
		Assertions.assertThat(appArgs.getJiraProjectPrefixes()).contains("PROJ1", "PROJ2");
	}

	@Test
	public void testJiraProjectPrefixesCommaSeparated() {
		AppArgs appArgs = new AppArgs("--projects", "PROJ1,PROJ2;PROJ3");
		Assertions.assertThat(appArgs.getJiraProjectPrefixes()).contains("PROJ1", "PROJ2", "PROJ3");
	}

	@Test
	public void testCommitArgs() {
		AppArgs appArgs = new AppArgs("origin/branch1", "origin/branch2");
		Assertions.assertThat(appArgs.getFromRev()).isEqualTo("origin/branch1");
		Assertions.assertThat(appArgs.getToRev()).isEqualTo("origin/branch2");
	}
}
