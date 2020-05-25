package de.wellnerbou.gitchangelog.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LatestTagFinder {

    private final Repository repository;
    private String tagStartingFrom;

    public LatestTagFinder(final Repository repository) {
        this.repository = repository;
    }

    public Optional<Ref> findLatestRef() throws RuntimeException {
        final RevWalk walk = new RevWalk(repository);
        Optional<Ref> returnValue = Optional.empty();
        List<Ref> call;
        try {
            call = new Git(repository).tagList().call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        if (!call.isEmpty()) {
            call.sort(new RefLatestFirstComparator(walk));
            if (this.tagStartingFrom != null) {
                call = findLatestBefore(call, this.tagStartingFrom);
            }
            if (!call.isEmpty()) {
                returnValue = Optional.ofNullable(call.get(0));
            }
        }
        walk.dispose();
        return returnValue;
    }

    private List<Ref> findLatestBefore(final List<Ref> refs, final String tagName) {
        return refs.stream().filter(new Predicate<Ref>() {
            boolean refFound = false;

            @Override
            public boolean test(final Ref ref) {
                if (refFound) {
                    return true;
                } else {
                    refFound = ref.getName().equals("refs/tags/" + tagName);
                    return false;
                }
            }
        }).collect(Collectors.toList());
    }

    public LatestTagFinder startingFromTag(final String tagStartingFrom) {
        this.tagStartingFrom = tagStartingFrom;
        return this;
    }

    public static class RefLatestFirstComparator implements Comparator<Ref> {

        private final RevWalk walk;

        public RefLatestFirstComparator(RevWalk walk) {
            this.walk = walk;
        }

        public int compare(Ref o1, Ref o2) {
            Integer d1, d2;
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
