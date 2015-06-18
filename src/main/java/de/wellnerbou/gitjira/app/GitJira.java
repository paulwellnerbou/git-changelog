package de.wellnerbou.gitjira.app;

import com.google.common.base.Optional;
import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jgit.LatestTagFinder;
import de.wellnerbou.gitjira.jira.JiraFilterLinkCreator;
import de.wellnerbou.gitjira.jira.JiraTicketExtractor;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class GitJira {

	public static void main(String[] args) throws IOException, GitAPIException {
		AppArgs appArgs = new AppArgs(args);
		final File repo = new File(appArgs.getRepo());
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		final Repository repository = builder.readEnvironment().findGitDir(repo).build();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());

		String fromRev = appArgs.getFromRev();
		if(fromRev == null) {
			System.out.println("toRev not given, searching automatically for latest released tag...");
			Optional<Ref> latestTag = new LatestTagFinder(repository).findRef();
			if(latestTag.isPresent()) {
				System.out.println("Found tag "+latestTag.get().toString());
				fromRev = latestTag.get().getName();
			} else {
				System.out.println("No tag found. Exiting.");
				return;
			}
		}

		final Iterable<CommitDataModel> revs = gitLogBetween.getGitLogBetween(fromRev, appArgs.getToRev());
		final Collection<String> jiraTickets = new JiraTicketExtractor(appArgs.getJiraPrefixes()).extract(revs);
		System.out.println(new JiraFilterLinkCreator(appArgs.getJiraBaseUrl()).createFilterLink(jiraTickets));
	}
}
