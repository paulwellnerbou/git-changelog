package de.wellnerbou.gitjira.jira;

import com.google.common.collect.Lists;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
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
	public void testExtractRevCommitList() {
		Iterable<CommitDataModel> commits = Lists.newArrayList(
				new CommitDataModel("hash1", "[TEST-1234] Fixed Bugs and did something else for TEST-345"),
				new CommitDataModel("hash2", "ANOTHERPROJECT-345")
		);
		final Collection<String> res = jiraTicketExtractor.extract(commits);
		assertThat(res).contains("ANOTHERPROJECT-345");
		assertThat(res).contains("TEST-1234");
		assertThat(res).contains("TEST-345");
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

	private class StubRevCommit extends RevCommit {
		private String fullCommitMessage;

		StubRevCommit(final String fullCommitMessage) {
			super(new AnyObjectId() {
				@Override
				public ObjectId toObjectId() {
					return ObjectId.zeroId();
				}
			});
			this.fullCommitMessage = fullCommitMessage;
		}


	}
}
