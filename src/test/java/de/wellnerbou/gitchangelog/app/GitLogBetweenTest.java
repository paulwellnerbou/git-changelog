package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.jgit.CommitDataModelMapper;
import de.wellnerbou.gitchangelog.jgit.GitLogBetween;
import de.wellnerbou.gitchangelog.jgit.RepositoryHelper;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GitLogBetweenTest {

	public static final String ANNOTATED_TEST_TAG = "refs/tags/annotated-test-tag";
	public static final String TEST_BRANCH = "origin/test-branch";

	@Test
	public void testGetGitLogBetweenBranches() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween(TEST_BRANCH, "origin/master");
		assertThat(res).isNotNull();
	}

	@Test
	public void testGetGitLogBetweenTagAndHead() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("refs/tags/test-tag", "HEAD");
		assertThat(res).isNotNull();
	}

	@Test
	public void testGetGitLogBetweenAnnotatedTagAndHead() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween(ANNOTATED_TEST_TAG, "HEAD");
		assertThat(res).isNotNull();
	}

	@Test
	public void testGetGitLogBetweenTwoCommitHashes() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("f16d2dde1a3dd8d058e07ae2f2b4ae9964191b8f", "1f3f068ebc67153506ae8360cc6ea68aab159248");
		assertThat(res).isNotNull();
		for (CommitDataModel commitDataModel : res) {
			System.out.println(commitDataModel.getHash() + ": " + commitDataModel.getFullMessage());
		}
	}
}
