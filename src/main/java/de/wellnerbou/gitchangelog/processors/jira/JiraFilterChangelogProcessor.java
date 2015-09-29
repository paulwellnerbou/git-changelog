package de.wellnerbou.gitchangelog.processors.jira;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import de.wellnerbou.gitchangelog.model.Changelog;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import de.wellnerbou.gitchangelog.model.RevRange;
import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class JiraFilterChangelogProcessor implements ChangelogProcessor {

	private List<String> jiraProjectPrefixes;
	public String jiraBaseUrl = "";

	@Override
	public String getId() {
		return "jirafilter";
	}

	@Override
	public void provideOptions(final Options options) {
		options.addOption(Option.builder().longOpt("projects").hasArg().required().argName("JIRA project keys").desc("Keys of JIRA project which shall be included in the filter url separated by commas.").build());
		options.addOption(Option.builder().longOpt("baseurl").hasArg().required().argName("JIRA URL").desc("URL to JIRA instance, e.g. 'https://issues.jenkins.org/").build());
	}

	@Override
	public void parseOptions(final CommandLine commandLine) {
		jiraBaseUrl = commandLine.getOptionValue("baseurl");
		setJiraProjectPrefixes(commandLine.getOptionValue("projects"));
	}

	@Override
	public Changelog processChangelog(final RevRange revRange, final Iterable<CommitDataModel> revisions, final PrintStream out) {
		final Collection<String> jiraTickets = new JiraTicketExtractor(jiraProjectPrefixes).extract(revisions);
		final Changelog changelog = new Changelog(revRange.fromRev, revRange.toRev);
		changelog.addLines(jiraTickets);
		out.println("Jira-Tickets mentioned in commits between " + revRange.fromRev + " and " + revRange.toRev + ":");
		out.println(Joiner.on(",").join(changelog.getLines()));
		return changelog;
	}

	@Override
	public String generateOutput(final Changelog changelog, final PrintStream out) {
		final String jiraFilterUrl = jiraFilterUrl(changelog.getLines());
		out.println(jiraFilterUrl);
		return jiraFilterUrl;
	}


	/**
	 * Creates the JIRA filter URL prefixed with the JIRA base url, if any.
	 *
	 * @param jiraTickets A collection of (distinct) JIRA tickets, e.g. "PROJ1-123", "PROJ2-234", ...
	 * @return the escaped url string to the JIRA filter in the format http://jira.base.url/issues/?jql=key%20in%20%28PROJ1-123,PROJ2-234%29"
	 */
	public String jiraFilterUrl(final Collection<String> jiraTickets) {
		return new JiraFilterLinkCreator(jiraBaseUrl).createFilterLink(jiraTickets);
	}

	@VisibleForTesting
	public String getJiraBaseUrl() {
		return jiraBaseUrl;
	}

	public void setJiraBaseUrl(final String jiraBaseUrl) {
		this.jiraBaseUrl = jiraBaseUrl;
	}

	public void setJiraProjectPrefixes(final String jiraProjectPrefixes) {
		setJiraProjectPrefixes(Splitter.on(",").split(jiraProjectPrefixes));
	}

	public void setJiraProjectPrefixes(final Iterable<String> jiraProjectPrefixes) {
		this.jiraProjectPrefixes = Lists.newArrayList(jiraProjectPrefixes);
	}
}
