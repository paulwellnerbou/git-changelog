package de.wellnerbou.gitjira.version;

public class Version {
	private int major;
	private int minor;
	private int increment;

	public Version(int major, int minor) {
		this(major, minor, 0);
	}

	public Version(final int major, final int minor, final int increment) {
		this.major = major;
		this.minor = minor;
		this.increment = increment;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getIncrement() {
		return increment;
	}
}
