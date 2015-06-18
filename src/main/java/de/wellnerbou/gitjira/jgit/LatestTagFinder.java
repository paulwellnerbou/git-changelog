package de.wellnerbou.gitjira.jgit;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LatestTagFinder {

	private Repository repository;
	private String tagStartingFrom;

	public LatestTagFinder(final Repository repository) {
		this.repository = repository;
	}

	public Optional<Ref> findRef() throws GitAPIException {
		final RevWalk walk = new RevWalk(repository);
		List<Ref> call = new Git(repository).tagList().call();
		if(!call.isEmpty()) {
			Collections.sort(call, new RefLatestFirstComparator(walk));
			if(this.tagStartingFrom != null) {
				call = findFirstBefore(call, this.tagStartingFrom);
				if(call.isEmpty()) {
					return Optional.absent();
				}
			}
			return Optional.fromNullable(call.get(0));
		} else {
			return Optional.absent();
		}
	}

	private List<Ref> findFirstBefore(final List<Ref> refs, final String tagName) {
		return FluentIterable.from(refs).filter(new Predicate<Ref>() {
			boolean refFound = false;
			@Override
			public boolean apply(final Ref ref) {
				if(refFound) {
					return true;
				} else {
					refFound = ref.getName().equals("refs/tags/"+tagName);
					return false;
				}
			}
		}).toList();
	}

	public LatestTagFinder startingFrom(final String tagStartingFrom) {
		this.tagStartingFrom = tagStartingFrom;
		return this;
	}

	public class RefLatestFirstComparator implements Comparator<Ref> {

		private RevWalk walk;

		public RefLatestFirstComparator(RevWalk walk) {
			this.walk = walk;
		}

		public int compare(Ref o1, Ref o2) {
			Integer d1;
			Integer d2;
			try {
				d1 = walk.parseCommit(o1.getObjectId()).getCommitTime();
				d2 = walk.parseCommit(o2.getObjectId()).getCommitTime();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return d2.compareTo(d1);
		}
	}
}
