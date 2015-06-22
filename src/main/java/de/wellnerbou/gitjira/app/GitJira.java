package de.wellnerbou.gitjira.app;

import com.google.common.base.Optional;
import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jgit.LatestTagFinder;
import de.wellnerbou.gitjira.jira.JiraFilterLinkCreator;
import de.wellnerbou.gitjira.jira.JiraTicketExtractor;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class GitJira {

	public static void main(String[] args) throws IOException {
		AppArgs appArgs = new AppArgs(args);
		GitJira gitJira = new GitJira();
		gitJira.run(appArgs);
	}

	private void run(final AppArgs appArgs) throws IOException {
		final File repo = new File(appArgs.getRepo());
		final FileRepositoryBuilder builder = new FileRepositoryBuilder();
		final Repository repository = builder.readEnvironment().findGitDir(repo).build();
		final GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());

		RevRange revRange = getRevRange(appArgs, repository);

		final Iterable<CommitDataModel> revs = gitLogBetween.getGitLogBetween(revRange.fromRev, revRange.toRev);
		final Collection<String> jiraTickets = new JiraTicketExtractor(appArgs.getJiraPrefixes()).extract(revs);

		System.out.println("-----\r\nJira-Tickets mentioned in commits between "+revRange.fromRev+" and "+revRange.toRev+":");
		System.out.println(new JiraFilterLinkCreator(appArgs.getJiraBaseUrl()).createFilterLink(jiraTickets));
		System.out.println("-----");
	}

	private RevRange getRevRange(AppArgs appArgs, Repository repository) {
		String fromRev = appArgs.getFromRev();
		String toRev = appArgs.getToRev();
		if (fromRev == null && toRev == null) {
			System.out.println("No revs given, searching automatically for latest released tags...");
			toRev = getLatestTag(null, repository);
			System.out.println("Found toRev tag " + toRev);
			fromRev = getLatestTag(toRev, repository);
			System.out.println("Found fromRev tag " + fromRev);
		} else if (fromRev == null) {
			System.out.println("Second rev not given, searching automatically for latest released tag as fromRev...");
			fromRev = getLatestTag(null, repository);
			System.out.println("Found tag " + fromRev);
		}
		return new RevRange(fromRev, toRev);
	}

	private String getLatestTag(final String beforeTag, final Repository repository) {
		Optional<Ref> latestTag = new LatestTagFinder(repository).startingFromTag(beforeTag).findLatestRef();
		if (latestTag.isPresent()) {
			return Repository.shortenRefName(latestTag.get().getName());
		} else {
			throw new RuntimeException("No revision found searching for latest tag" + (beforeTag != null ? " before tag " + beforeTag : "") + ".");
		}
	}
}
