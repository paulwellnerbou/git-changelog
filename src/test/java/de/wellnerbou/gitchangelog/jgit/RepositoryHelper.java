package de.wellnerbou.gitchangelog.jgit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class RepositoryHelper {

	public static Repository openMyRepository() throws IOException {
		final FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.readEnvironment().findGitDir().build();
	}

	public static Repository openRepository(String path) throws IOException {
		final File repo = new File(path);
		final FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.readEnvironment().findGitDir(repo).build();
	}
}
