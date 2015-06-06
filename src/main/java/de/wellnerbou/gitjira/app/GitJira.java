package de.wellnerbou.gitjira.app;

import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jira.JiraFilterLinkCreator;
import de.wellnerbou.gitjira.jira.JiraTicketExtractor;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class GitJira {

	public static void main(String[] args) throws IOException {
		AppArgs appArgs = new AppArgs(args);

		final File repo = new File(appArgs.getRepo());
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		final Repository repository = builder.readEnvironment().findGitDir(repo).build();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());

		final Iterable<CommitDataModel> revs = gitLogBetween.getGitLogBetween(appArgs.getFromRev(), appArgs.getToRev());
		final Collection<String> jiraTickets = new JiraTicketExtractor(appArgs.getJiraPrefixes()).extract(revs);
		System.out.println(new JiraFilterLinkCreator(appArgs.getJiraBaseUrl()).createFilterLink(jiraTickets));
	}
}
