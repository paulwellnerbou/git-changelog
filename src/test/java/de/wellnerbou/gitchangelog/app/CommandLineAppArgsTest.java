package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import de.wellnerbou.gitchangelog.processors.jira.JiraFilterChangelogProcessor;
import org.apache.commons.cli.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandLineAppArgsTest {

	@Test
	@Ignore("This does not work for now, the order matters, is this a commons cli bug? It works switching order of arguments")
	public void testProcessorWithRepoFirst() throws ParseException {
		final CommandLineAppArgs args = new CommandLineAppArgs().parse("--repo=/path/to/your/git/repo", "--processor=jirafilter");
		assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
	}

	@Test
	public void testPrintHelpWithoutProcessor() throws ParseException {
		new CommandLineAppArgs().printHelp(System.out);
	}

	@Test
	public void testGetChangeLogProcessorFromArgs() {
		final ChangelogProcessor actual = new CommandLineAppArgs().getChangeLogProcessorFromArgs("--processor=jirafilter");
		assertThat(actual).isNotNull();
	}

	@Test
	public void testGetChangeLogProcessorFromArgs_withOtherArgs() {
		final ChangelogProcessor actual = new CommandLineAppArgs().getChangeLogProcessorFromArgs("--processor=jirafilter", "--baseurl=aaa");
		assertThat(actual).isNotNull();
	}

	@Test
	public void testPrintHelpWithProcessor() throws ParseException {
		new CommandLineAppArgs().parse("--processor=jirafilter", "--baseurl=", "--projects=").printHelp(System.out);
	}

	@Test
	public void testProcessorWithBaseUrl() throws ParseException {
		final CommandLineAppArgs args = new CommandLineAppArgs().parse("--processor=jirafilter", "--repo=/path/to/your/git/repo", "--baseurl=http://example.com", "--projects=");
		assertThat(args.getChangelogProcessor()).isInstanceOf(JiraFilterChangelogProcessor.class);
		assertThat(((JiraFilterChangelogProcessor) args.getChangelogProcessor()).getJiraBaseUrl()).isEqualTo("http://example.com");
	}

	@Test(expected = ParseException.class)
	public void testRequireArgForRepo() throws ParseException {
		new CommandLineAppArgs().parse("--repo");
	}

	@Test
	public void testDefaultRepo() throws ParseException {
		AppArgs appArgs = new CommandLineAppArgs().parse("--processor=basic", "rev1", "rev2");
		assertThat(appArgs.getRepo()).isEqualTo(".");
	}

	@Test
	public void testCommitArgs() throws ParseException {
		AppArgs appArgs = new CommandLineAppArgs().parse("--processor=basic", "origin/branch1", "origin/branch2");
		assertThat(appArgs.getFromRev()).isEqualTo("origin/branch1");
		assertThat(appArgs.getToRev()).isEqualTo("origin/branch2");
	}
}
