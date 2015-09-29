package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;

public class AppArgs {
	protected String repo;
	protected String fromRev;
	protected String toRev;
	protected ChangelogProcessor changelogProcessor;

	public String getRepo() {
		return repo;
	}

	public String getFromRev() {
		return fromRev;
	}

	public String getToRev() {
		return toRev;
	}

	public ChangelogProcessor getChangelogProcessor() {
		return changelogProcessor;
	}

	public void setChangelogProcessor(final ChangelogProcessor changelogProcessor) {
		this.changelogProcessor = changelogProcessor;
	}

	public void setFromRev(final String fromRev) {
		this.fromRev = fromRev;
	}

	public void setRepo(final String repo) {
		this.repo = repo;
	}

	public void setToRev(final String toRev) {
		this.toRev = toRev;
	}
}
