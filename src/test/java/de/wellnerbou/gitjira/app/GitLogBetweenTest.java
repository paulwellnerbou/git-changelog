package de.wellnerbou.gitjira.app;

import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.jgit.RepositoryHelper;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GitLogBetweenTest {

	@Test
	public void testGetGitLogBetween() throws Exception {
		Repository repository = RepositoryHelper.openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("origin/test-branch", "origin/master");
		assertThat(res).isNotNull();
	}

}
