package de.wellnerbou.gitchangelog.version;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import java.util.Arrays;

public class VersionParser {

	public Version parse(final String versionString) {
		String[] versionPartStrings = versionString.split("\\.");
		Integer[] versionParts = convertToInts(versionPartStrings);

		if(versionParts.length >= 2) {
			return new Version(versionParts[0], versionParts[1], versionParts.length > 2 ? versionParts[2] : 0);
		} else {
			throw new RuntimeException("Unable to parse version string "+versionString);
		}
	}

	private Integer[] convertToInts(final String[] versionPartStrings) {
		return FluentIterable.from(Arrays.asList(versionPartStrings)).transform(new Function<String, Integer>() {
			@Override
			public Integer apply(final String input) {
				return Integer.parseInt(input);
			}
		}).toArray(Integer.class);
	}
}
