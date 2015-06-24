package de.wellnerbou.gitjira.app;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jgit.LatestTagFinder;
import de.wellnerbou.gitjira.jira.JiraFilterLinkCreator;
import de.wellnerbou.gitjira.jira.JiraTicketExtractor;
import de.wellnerbou.gitjira.model.Changelog;
import de.wellnerbou.gitjira.model.CommitDataModel;
import de.wellnerbou.gitjira.model.RevRange;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

/**
 * Entry point for this library which is used for command line as well.
 */
public class GitJira {

	public static void main(String[] args) throws IOException {
		GitJira gitJira = new GitJira(new AppArgs(args));
		final Changelog changelog = gitJira.changelog();
		gitJira.jiraFilterUrl(changelog.getTickets());
	}

	private final AppArgs appArgs;
	private final PrintStream out;

	/**
	 * Creates a new GitJira instance with the given AppArgs.
	 * {@see AppArgs} for more information about the arguments.
	 * <p/>
	 * This will call GitJira(appArgs, System.out), so STDOUT is
	 * used as default Prinstream for logging.
	 *
	 * @param appArgs Arguments for this GitJira instance
	 */
	public GitJira(final AppArgs appArgs) {
		this(appArgs, System.out);
	}

	/**
	 * Creates a new GitJira instance with the given AppArgs
	 * and the given PrintStream for logging.
	 *
	 * @param appArgs Arguments for this GitJira instance
	 * @param out     The printstream used for logging and error output.
	 */
	public GitJira(final AppArgs appArgs, PrintStream out) {
		this.appArgs = appArgs;
		this.out = out;
	}

	/**
	 * Calculates the changelog between the two revisions given in AppArgs (or the automatically
	 * calculated tags if no or only one revision is given).
	 * <p/>
	 * Only commits containing a project key given in AppArgs' projects ({@link AppArgs#setJiraProjectPrefixes})
	 * are considered.
	 * <p/>
	 * If no valid git repository is found, JGit will throw an java.lang.IllegalArgumentException: One of setGitDir or setWorkTree must be called.
	 *
	 * @return the changelog object containing the {@link Changelog#getFrom()} and {@link Changelog#getTo()}
	 * revisions and the list of jira tickets found in the commit messages between.
	 * @throws IOException
	 */
	public Changelog changelog() throws IOException {
		if (appArgs.getJiraProjectPrefixes().isEmpty()) {
			out.println("No jira project prefixes given, won't return anything.");
		}

		final File repo = new File(appArgs.getRepo());
		final FileRepositoryBuilder builder = new FileRepositoryBuilder();
		final Repository repository = builder.readEnvironment().findGitDir(repo).build();
		final GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());

		RevRange revRange = getRevRange(appArgs, repository);

		final Iterable<CommitDataModel> revs = gitLogBetween.getGitLogBetween(revRange.fromRev, revRange.toRev);

		final Collection<String> jiraTickets = new JiraTicketExtractor(appArgs.getJiraProjectPrefixes()).extract(revs);
		final Changelog changelog = new Changelog(revRange.fromRev, revRange.toRev);
		changelog.addTickets(jiraTickets);

		out.println("Jira-Tickets mentioned in commits between " + revRange.fromRev + " and " + revRange.toRev + ":");
		out.println(Joiner.on(",").join(changelog.getTickets()));

		return changelog;
	}

	/**
	 * Creates the JIRA filter URL prefixed with the JIRA base url ({@link AppArgs#getJiraBaseUrl()}), if any.
	 *
	 * @param jiraTickets A collection of (distinct) JIRA tickets, e.g. "PROJ1-123", "PROJ2-234", ...
	 * @return the escaped url string to the JIRA filter in the format http://jira.base.url/issues/?jql=key%20in%20%28PROJ1-123,PROJ2-234%29"
	 */
	public String jiraFilterUrl(final Collection<String> jiraTickets) {
		final String filterLink = new JiraFilterLinkCreator(appArgs.getJiraBaseUrl()).createFilterLink(jiraTickets);
		out.println(filterLink);
		return filterLink;
	}

	private RevRange getRevRange(AppArgs appArgs, Repository repository) {
		String fromRev = appArgs.getFromRev();
		String toRev = appArgs.getToRev();
		if (fromRev == null && toRev == null) {
			out.println("No revs given, searching automatically for latest released tags...");
			toRev = getLatestTag(null, repository);
			out.println("Found toRev tag " + toRev);
			fromRev = getLatestTag(toRev, repository);
			out.println("Found fromRev tag " + fromRev);
		} else if (fromRev == null) {
			out.println("Second rev not given, searching automatically for latest released tag as fromRev...");
			fromRev = getLatestTag(null, repository);
			out.println("Found tag " + fromRev);
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
