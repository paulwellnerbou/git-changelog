package de.wellnerbou.gitjira.jgit;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import de.wellnerbou.gitjira.model.CommitDataModel;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * @author Paul Wellner Bou <pwb@faz.net>
 */
public class CommitDataModelMapper {

	public Iterable<CommitDataModel> map(final Iterable<RevCommit> jGitLogBetween) {
		return FluentIterable.from(jGitLogBetween).transform(new Function<RevCommit, CommitDataModel>() {
			@Override
			public CommitDataModel apply(final RevCommit input) {
				return new CommitDataModel(input.getName(), input.getFullMessage());
			}
		});
	}
}
