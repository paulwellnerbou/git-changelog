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
}
