package de.wellnerbou.gitjira.app;

import de.wellnerbou.gitjira.jgit.CommitDataModelMapper;
import de.wellnerbou.gitjira.jgit.GitLogBetween;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.assertj.core.api.Assertions;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class GitLogBetweenTest {

	@Test
	public void testGetGitLogBetween() throws Exception {
		Repository repository = openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository, new CommitDataModelMapper());
		final Iterable<CommitDataModel> res = gitLogBetween.getGitLogBetween("origin/test-branch", "origin/master");
		assertThat(res).isNotNull();
	}

	public static Repository openMyRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.readEnvironment().findGitDir().build();
	}
}
