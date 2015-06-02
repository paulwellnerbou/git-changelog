package de.wellnerbou.gitjira.app;

import java.util.Collection;
import java.util.stream.Collectors;

public class JiraFilterLinkCreator {

	private String jiraBaseUrl;

	public JiraFilterLinkCreator(final String jiraBaseUrl) {
		this.jiraBaseUrl = jiraBaseUrl + (jiraBaseUrl.endsWith("/") ? "" : "/");
	}

	public String createFilterLink(Collection<String> tickets) {
		return jiraBaseUrl + "issues/?jql=key%20in%20(" + createjoinedTickets(tickets) + ")";
	}

	protected String createjoinedTickets(Collection<String> tickets) {
		return tickets.stream().map(i -> i).collect(Collectors.joining(","));
	}
}
