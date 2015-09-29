package de.wellnerbou.gitchangelog.app;

import com.google.common.base.Optional;
import de.wellnerbou.gitchangelog.jgit.CommitDataModelMapper;
import de.wellnerbou.gitchangelog.jgit.GitLogBetween;
import de.wellnerbou.gitchangelog.jgit.LatestTagFinder;
import de.wellnerbou.gitchangelog.model.Changelog;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import de.wellnerbou.gitchangelog.model.RevRange;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Entry point for this library which is used for command line as well.
 */
public class GitChangelog {

	public static void main(String[] args) throws IOException {
		GitChangelog gitChangelog = new GitChangelog(new CommandLineAppArgs(args));
		final Changelog changelog = gitChangelog.changelog();
		gitChangelog.print(changelog);
	}

	private final AppArgs appArgs;
	private final PrintStream out;

	/**
	 * Creates a new GitChangelog instance with the given AppArgs.
	 * {@see AppArgs} for more information about the arguments.
	 * <p/>
	 * This will call GitChangelog(appArgs, System.out), so STDOUT is
	 * used as default Prinstream for logging.
	 *
	 * @param appArgs Arguments for this GitChangelog instance
	 */
	public GitChangelog(final AppArgs appArgs) {
		this(appArgs, System.out);
	}

	/**
	 * Creates a new GitChangelog instance with the given AppArgs
	 * and the given PrintStream for logging.
	 *
	 * @param appArgs Arguments for this GitChangelog instance
	 * @param out     The printstream used for logging and error output.
	 */
	public GitChangelog(final AppArgs appArgs, PrintStream out) {
		this.appArgs = appArgs;
		this.out = out;
	}

	/**
	 * Calculates the changelog between the two revisions given in AppArgs (or the automatically
	 * calculated tags if no or only one revision is given).
	 * If no valid git repository is found, JGit will throw an java.lang.IllegalArgumentException: One of setGitDir or setWorkTree must be called.
	 *
	 * @return the changelog object containing the {@link Changelog#getFrom()} and {@link Changelog#getTo()}
	 * revisions and the list of jira tickets found in the commit messages between.
	 * @throws IOException
	 */
	public Changelog changelog() throws IOException {
		final File repo = new File(appArgs.getRepo());
		final FileRepositoryBuilder builder = new FileRepositoryBuilder();
		final Repository repository = builder.readEnvironment().findGitDir(repo).build();
		final GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());

		RevRange revRange = getRevRange(appArgs, repository);
		final Iterable<CommitDataModel> revs = gitLogBetween.getGitLogBetween(revRange.fromRev, revRange.toRev);

		return appArgs.getChangelogProcessor().processChangelog(revRange, revs, out);
	}

	public String print(final Changelog changelog) {
		return appArgs.getChangelogProcessor().generateOutput(changelog, out);
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
