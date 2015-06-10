package de.wellnerbou.gitjira.jgit;

import com.google.common.base.Optional;
import org.assertj.core.api.Assertions;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LatestTagFinderTest {

	private LatestTagFinder latestTagFinder;

	@Before
	public void setUp() throws IOException {
		Repository repository = RepositoryHelper.openMyRepository();
		latestTagFinder = new LatestTagFinder(repository);
	}

	@Test
	public void findLatestTag() throws GitAPIException {
		Optional<Ref> ref = latestTagFinder.findRef();
		Assertions.assertThat(ref.isPresent()).isTrue();
		Assertions.assertThat(ref.get().getName()).endsWith("test-tag-newer");
	}

	@Test
	public void findLatestTagStartingFromOther() throws GitAPIException {
		Optional<Ref> ref = latestTagFinder.startingFrom("test-tag-newer").findRef();
		Assertions.assertThat(ref.get().getName()).endsWith("test-tag");
	}

	@Test
	public void findLatestTagStartingFromOldest() throws GitAPIException {
		Optional<Ref> ref = latestTagFinder.startingFrom("test-tag").findRef();
		Assertions.assertThat(ref.isPresent()).isFalse();
	}
}
