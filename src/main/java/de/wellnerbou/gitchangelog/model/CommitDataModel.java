package de.wellnerbou.gitchangelog.model;

/**
 * Creating another commit object for clean data mapping.
 *
 * Using JGit's RevCommit introduces a dependency in this internal code
 * to JGit and makes it very hard to test, as RevCommit contains some
 * final methods.
 */
public class CommitDataModel {

	private final String hash;
	private final String fullMessage;

	public CommitDataModel(final int commitTime, final String hash, final String fullMessage) {
		this.hash = hash;
		this.fullMessage = fullMessage;
	}

	public String getHash() {
		return hash;
	}

	public String getShortHash() {
		return hash.substring(0, 7);
	}

	public String getFullMessage() {
		return fullMessage;
	}
}
