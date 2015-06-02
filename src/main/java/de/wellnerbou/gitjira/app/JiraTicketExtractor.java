package de.wellnerbou.gitjira.app;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

	public Collection<String> extract(Iterable<RevCommit> revs) {
		Collection<String> result = new ArrayList<>();
		for(RevCommit revCommit : revs) {
			result.addAll(extract(revCommit.getFullMessage()));
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
