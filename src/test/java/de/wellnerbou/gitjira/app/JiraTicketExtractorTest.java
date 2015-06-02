package de.wellnerbou.gitjira.app;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JiraTicketExtractorTest {

	private JiraTicketExtractor jiraTicketExtractor;

	@Before
	public void setUp() throws Exception {
		jiraTicketExtractor = new JiraTicketExtractor("TEST", "ANOTHERPROJECT");
	}

	@Test
	public void testExtractSingleString_oneTicket() throws Exception {
		final String teststr = "[TEST-1234] Fixed Bugs.";
		final Collection<String> res = jiraTicketExtractor.extract(teststr);
		assertThat(res).contains("TEST-1234");
	}

	@Test
	public void testExtractSingleString_twoTickets() throws Exception {
		final String teststr = "[TEST-1234] Fixed Bugs and did something else for TEST-345";
		final Collection<String> res = jiraTicketExtractor.extract(teststr);
		assertThat(res).contains("TEST-1234");
		assertThat(res).contains("TEST-345");
	}

	@Test
	public void testExtractSingleString_twoTicketsFromDifferentProjects() throws Exception {
		final String teststr = "[TEST-1234] Fixed Bugs and did something else for ANOTHERPROJECT-345";
		final Collection<String> res = jiraTicketExtractor.extract(teststr);
		assertThat(res).contains("TEST-1234");
		assertThat(res).contains("ANOTHERPROJECT-345");
	}
}
