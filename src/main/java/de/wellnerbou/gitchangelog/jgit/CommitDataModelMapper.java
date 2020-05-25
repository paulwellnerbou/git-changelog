package de.wellnerbou.gitchangelog.jgit;

import de.wellnerbou.gitchangelog.model.CommitDataModel;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CommitDataModelMapper {

    public Iterable<CommitDataModel> map(final Iterable<RevCommit> jGitLogBetween) {

        return StreamSupport.stream(jGitLogBetween.spliterator(), false).map(
                input -> new CommitDataModel(input.getCommitTime(), input.getName(), input.getFullMessage())
        ).collect(Collectors.toList());
    }
}
