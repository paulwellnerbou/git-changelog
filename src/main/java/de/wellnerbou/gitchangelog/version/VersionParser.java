package de.wellnerbou.gitchangelog.version;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VersionParser {

    public Version parse(final String versionString) {
        String[] versionPartStrings = versionString.split("\\.");
        Integer[] versionParts = convertToInts(versionPartStrings).toArray(new Integer[0]);

        if (versionParts.length >= 2) {
            return new Version(versionParts[0], versionParts[1], versionParts.length > 2 ? versionParts[2] : 0);
        } else {
            throw new RuntimeException("Unable to parse version string " + versionString);
        }
    }

    private List<Integer> convertToInts(final String[] versionPartStrings) {
        return Arrays.stream(versionPartStrings).map(input -> Integer.parseInt(input)).collect(Collectors.toList());
    }
}
