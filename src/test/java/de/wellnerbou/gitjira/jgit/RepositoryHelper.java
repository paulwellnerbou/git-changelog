package de.wellnerbou.gitjira.jgit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

public class RepositoryHelper {

	public static Repository openMyRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.readEnvironment().findGitDir().build();
	}
}
