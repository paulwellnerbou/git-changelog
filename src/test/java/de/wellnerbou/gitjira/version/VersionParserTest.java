package de.wellnerbou.gitjira.version;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VersionParserTest {

	@Test
	public void testVersionParsing_majorMinor_without_increment() {
		VersionParser versionParser = new VersionParser();
		Version version = versionParser.parse("1.0");
		Assertions.assertThat(version.getMajor()).isEqualTo(1);
		Assertions.assertThat(version.getMinor()).isEqualTo(0);
		Assertions.assertThat(version.getIncrement()).isEqualTo(0);
	}

	@Test
	public void testVersionParsing_majorMinor_with_increment() {
		VersionParser versionParser = new VersionParser();
		Version version = versionParser.parse("1.0.1");
		Assertions.assertThat(version.getMajor()).isEqualTo(1);
		Assertions.assertThat(version.getMinor()).isEqualTo(0);
		Assertions.assertThat(version.getIncrement()).isEqualTo(1);
	}
}
