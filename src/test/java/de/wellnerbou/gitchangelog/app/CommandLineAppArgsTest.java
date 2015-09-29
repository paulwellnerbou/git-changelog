package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.processors.jira.JiraFilterChangelogProcessor;
import joptsimple.OptionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CommandLineAppArgsTest {

	@Test
	public void testProcessorWithRepoFirst() {
		final CommandLineAppArgs args = new CommandLineAppArgs("--repo=/path/to/your/git/repo", "--processor=jirafilter");
		Assertions.assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
	}

	@Test
	public void testProcessorWithBaseUrl() {
		final CommandLineAppArgs args = new CommandLineAppArgs("--repo=/path/to/your/git/repo", "--processor=jirafilter", "--baseurl=http://example.com");
		Assertions.assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
		Assertions.assertThat(((JiraFilterChangelogProcessor) args.getChangelogProcessor()).getJiraBaseUrl()).isEqualTo("http://example.com");
	}

	@Test
	public void testProcessor() {
		final CommandLineAppArgs args = new CommandLineAppArgs("--processor=jirafilter");
		Assertions.assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
	}

	@Test(expected = OptionException.class)
	public void testRequireArgForRepo() {
		new CommandLineAppArgs("--repo");
	}

	@Test
	public void testDefaultRepo() {
		AppArgs appArgs = new CommandLineAppArgs("--processor=jirafilter", "rev1", "rev2");
		Assertions.assertThat(appArgs.getRepo()).isEqualTo(".");
	}

	@Test
	public void testCommitArgs() {
		AppArgs appArgs = new CommandLineAppArgs("--processor=jirafilter", "origin/branch1", "origin/branch2");
		Assertions.assertThat(appArgs.getFromRev()).isEqualTo("origin/branch1");
		Assertions.assertThat(appArgs.getToRev()).isEqualTo("origin/branch2");
	}
}
