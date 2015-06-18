package de.wellnerbou.gitjira.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class GitJiraTest {

	@Test
	public void testMain() throws Exception {
		final String[] args = new String[] { "--repo", "/home/paul/src/fazcore", "--jirabaseurl", "http://jira.faz.net", "--jiraprefix=SUPPORT", "--jiraprefix=FTK", "origin/release/6.24", "origin/develop" };
		GitJira.main(args);
	}

	@Test
	public void testMainWithOneRevGiven() throws Exception {
		final String[] args = new String[] { "--repo", "/home/paul/src/fazcore", "--jirabaseurl", "http://jira.faz.net", "--jiraprefix=SUPPORT", "--jiraprefix=FTK", "origin/develop" };
		GitJira.main(args);
	}
}
