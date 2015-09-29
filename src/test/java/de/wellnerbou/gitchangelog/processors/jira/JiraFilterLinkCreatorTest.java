package de.wellnerbou.gitchangelog.processors.jira;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JiraFilterLinkCreatorTest {

	JiraFilterLinkCreator jiraFilterLinkCreator = new JiraFilterLinkCreator("https://jira.example.com/");

	@Test
	public void testCreateFilterLink() {
		final List<String> ticketList = Arrays.asList("XXX");
		final String actual = jiraFilterLinkCreator.createFilterLink(ticketList);
		assertThat(actual).isEqualTo("https://jira.example.com/issues/?jql=key%20in%20%28XXX%29");
	}

	@Test
	public void testCreatejoinedTickets() {
		final List<String> ticketList = Arrays.asList("TEST-1", "TEST-2", "ANOTHERPROJECT-123");
		final String actual = jiraFilterLinkCreator.createjoinedTickets(ticketList);
		assertThat(actual).isEqualTo("TEST-1,TEST-2,ANOTHERPROJECT-123");
	}

	@Test
	public void testCreatejoinedTickets_oneTicketOnly() {
		final List<String> ticketList = Arrays.asList("XXX");
		final String actual = jiraFilterLinkCreator.createjoinedTickets(ticketList);
		assertThat(actual).isEqualTo("XXX");
	}
}
