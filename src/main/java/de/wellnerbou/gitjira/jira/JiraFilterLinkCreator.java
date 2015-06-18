package de.wellnerbou.gitjira.jira;

import com.google.common.base.Joiner;

import java.util.Collection;

public class JiraFilterLinkCreator {

	private String jiraBaseUrl;

	public JiraFilterLinkCreator(final String jiraBaseUrl) {
		this.jiraBaseUrl = jiraBaseUrl + (jiraBaseUrl != null && jiraBaseUrl.endsWith("/") ? "" : "/");
	}

	public String createFilterLink(Collection<String> tickets) {
		return jiraBaseUrl + "issues/?jql=key%20in%20(" + createjoinedTickets(tickets) + ")";
	}

	protected String createjoinedTickets(Collection<String> tickets) {
		return Joiner.on(",").join(tickets);
	}
}
