package de.wellnerbou.gitjira.app;

public class RevRange {
	public final String fromRev;
	public final String toRev;

	public RevRange(final String fromRev, final String toRev) {
		this.fromRev = fromRev;
		this.toRev = toRev;
	}
}
