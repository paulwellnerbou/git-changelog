package de.wellnerbou.gitchangelog.processors.jira;

import de.wellnerbou.gitchangelog.model.CommitDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiraTicketExtractor {
	private final Collection<String> validJiraProjectPrefixes;

	public JiraTicketExtractor(final String... validJiraProjectPrefixes) {
		this(Arrays.asList(validJiraProjectPrefixes));
	}

	public JiraTicketExtractor(final Collection<String> validJiraProjectPrefixes) {
		this.validJiraProjectPrefixes = validJiraProjectPrefixes;
	}

	public Collection<String> extract(Iterable<CommitDataModel> commits) {
		Set<String> result = new HashSet<>();
		for(CommitDataModel commit : commits) {
			result.addAll(extract(commit.getFullMessage()));
		}
		return result;
	}

	protected Collection<String> extract(String commitMessage) {
		Collection<String> result = new ArrayList<>();
		for(String jiraProject : this.validJiraProjectPrefixes) {
			Pattern pattern = Pattern.compile(jiraProject+"(-[0-9]+)");
			Matcher matcher = pattern.matcher(commitMessage);
			while(matcher.find()) {
				result.add(matcher.group());
			}
		}
		return result;
	}
}
