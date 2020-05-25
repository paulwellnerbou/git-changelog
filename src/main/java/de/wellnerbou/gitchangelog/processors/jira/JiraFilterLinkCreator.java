package de.wellnerbou.gitchangelog.processors.jira;

import java.util.Collection;

public class JiraFilterLinkCreator {

    private String jiraBaseUrl;

    public JiraFilterLinkCreator(final String jiraBaseUrl) {
        this.jiraBaseUrl = jiraBaseUrl + (jiraBaseUrl != null && jiraBaseUrl.endsWith("/") ? "" : "/");
    }

    public String createFilterLink(Collection<String> tickets) {
        return jiraBaseUrl + "issues/?jql=key%20in%20%28" + createjoinedTickets(tickets) + "%29";
    }

    protected String createjoinedTickets(Collection<String> tickets) {
        return String.join(",", tickets);
    }
}
