package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import de.wellnerbou.gitchangelog.processors.jira.JiraFilterChangelogProcessor;
import org.apache.commons.cli.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandLineGitChangelogArgsTest {

	@Test
	@Ignore("This does not work for now, the order matters, is this a commons cli bug? It works switching order of arguments")
	public void testProcessorWithRepoFirst() throws ParseException {
		final CommandLineGitChangelogArgs args = new CommandLineGitChangelogArgs().parse("--repo=/path/to/your/git/repo", "--processor=jirafilter");
		assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
	}

	@Test
	public void testPrintHelpWithoutProcessor() throws ParseException {
		new CommandLineGitChangelogArgs().printHelp(System.out);
	}

	@Test
	public void testGetChangeLogProcessorWithoutFromArgs() {
		final ChangelogProcessor actual = new CommandLineGitChangelogArgs().getChangeLogProcessorFromArgs("--processor=jirafilter");
		assertThat(actual).isNotNull();
	}

	@Test
	public void testGetChangeLogProcessorWithFromArgs() throws ParseException {
		final CommandLineGitChangelogArgs args = new CommandLineGitChangelogArgs().parse("--processor=basic", "from", "to");
	}

	@Test
	public void testGetChangeLogProcessorFromArgs_withOtherArgs() {
		final ChangelogProcessor actual = new CommandLineGitChangelogArgs().getChangeLogProcessorFromArgs("--processor=jirafilter", "--baseurl=aaa");
		assertThat(actual).isNotNull();
	}

	@Test
	public void testPrintHelpWithProcessor() throws ParseException {
		new CommandLineGitChangelogArgs().parse("--processor=jirafilter", "--baseurl=", "--projects=").printHelp(System.out);
	}

	@Test
	public void testProcessorWithBaseUrl() throws ParseException {
		final CommandLineGitChangelogArgs args = new CommandLineGitChangelogArgs().parse("--processor=jirafilter", "--repo=/path/to/your/git/repo", "--baseurl=http://example.com", "--projects=");
		assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
		assertThat(((JiraFilterChangelogProcessor) args.getChangelogProcessor()).getJiraBaseUrl()).isEqualTo("http://example.com");
	}

	@Test(expected = ParseException.class)
	public void testRequireArgForRepo() throws ParseException {
		new CommandLineGitChangelogArgs().parse("--repo");
	}

	@Test
	public void testDefaultRepo() throws ParseException {
		GitChangelogArgs gitChangelogArgs = new CommandLineGitChangelogArgs().parse("--processor=basic", "rev1", "rev2");
		assertThat(gitChangelogArgs.getRepo()).isEqualTo(".");
	}

	@Test
	public void testCommitArgs() throws ParseException {
		GitChangelogArgs gitChangelogArgs = new CommandLineGitChangelogArgs().parse("--processor=basic", "origin/branch1", "origin/branch2");
		assertThat(gitChangelogArgs.getFromRev()).isEqualTo("origin/branch1");
		assertThat(gitChangelogArgs.getToRev()).isEqualTo("origin/branch2");
	}
}
