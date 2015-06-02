package de.wellnerbou.gitjira.app;

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

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetGitLogBetween() throws Exception {
		Repository repository = openMyRepository();
		GitLogBetween gitLogBetween = new GitLogBetween(repository);
		final Iterable<RevCommit> res = gitLogBetween.getGitLogBetween("origin/470fb2e793c8f7974f3408e22d7b0322a8e5e702", "origin/60328b5bc789ef04eba5fb280e53a491bd8688d5");

		assertThat(res).isNotNull();
	}

	public static Repository openMyRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		return repository;
	}
}
