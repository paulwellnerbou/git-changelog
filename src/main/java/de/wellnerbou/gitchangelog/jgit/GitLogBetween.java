package de.wellnerbou.gitchangelog.jgit;

import de.wellnerbou.gitchangelog.model.CommitDataModel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

public class GitLogBetween {

	private final Repository repo;
	private final CommitDataModelMapper commitDataModelMapper;

	public GitLogBetween(final Repository repository, final CommitDataModelMapper commitDataModelMapper) {
		this.repo = repository;
		this.commitDataModelMapper = commitDataModelMapper;
	}

	public Iterable<CommitDataModel> getGitLogBetween(final String rev1, final String rev2) throws RuntimeException {
		try {
			return commitDataModelMapper.map(getJGitLogBetween(rev1, rev2));
		} catch (IOException | GitAPIException e) {
			throw new RuntimeException(e);
		}
	}

	private Iterable<RevCommit> getJGitLogBetween(final String rev1, final String rev2) throws IOException, GitAPIException {
		final ObjectId from = resolveRev(rev1);
		if(from == null) {
			throw new RuntimeException("Ref "+rev1+" not found.");
		}
		final ObjectId to = resolveRev(rev2);
		if(to == null) {
			throw new RuntimeException("Ref "+rev2+" not found.");
		}
		return new Git(repo).log().addRange(from, to).call();
	}

	private ObjectId resolveRev(String rev) throws IOException {
		Ref ref = repo.findRef(rev);
		if(ref == null) {
			return repo.resolve(rev);
		} else {
			return getActualRefObjectId(ref);
		}
	}

	private ObjectId getActualRefObjectId(Ref ref) {
		final Ref repoPeeled;
		try {
			repoPeeled = repo.getRefDatabase().peel(ref);
			if(repoPeeled.getPeeledObjectId() != null) {
				return repoPeeled.getPeeledObjectId();
			}
		} catch (IOException ignored) {
		}
		return ref.getObjectId();
	}
}
