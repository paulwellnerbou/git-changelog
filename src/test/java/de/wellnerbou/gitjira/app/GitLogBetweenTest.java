package de.wellnerbou.gitjira.app;

import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jgit.RepositoryHelper;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class GitLogBetweenTest {

	@Test
	public void testJGitApiToDetectAnnotatedTags() throws IOException {
		final Repository repository = RepositoryHelper.openMyRepository();

	}

	@Test
	public void assureTagIsAnnotated() throws IOException {
		final Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("refs/tags/annotated-test-tag", "HEAD");
	}

	@Test
	public void testGetGitLogBetweenBranches() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("origin/test-branch", "origin/master");
		assertThat(res).isNotNull();
	}

	@Test
	public void testGetGitLogBetweenHeadAndTag() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("refs/tags/test-tag", "HEAD");
		assertThat(res).isNotNull();
	}

	@Test
	public void testGetGitLogBetweenHeadAndTagFazcore() throws Exception {
		Repository repository = RepositoryHelper.openRepository("/home/paul/src/fazcore");
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("refs/tags/fazcore-6.24.0", "HEAD");
		for(CommitDataModel commitDataModel : res) {
			System.out.println(commitDataModel);
		}
		assertThat(res).isNotNull();
	}
}
