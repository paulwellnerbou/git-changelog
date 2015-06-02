package de.wellnerbou.gitjira.app;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

public class GitLogBetween {

	private Repository repo;

	public GitLogBetween(final Repository repository) {
		this.repo = repository;
	}

	public Iterable<RevCommit> getGitLogBetween(final String rev1, final String rev2) throws IOException, GitAPIException {
		Ref refFrom = repo.getRef(rev1);
		Ref refTo = repo.getRef(rev2);
		return new Git(repo).log().addRange(refFrom.getObjectId(), refTo.getObjectId()).call();
	}
}
