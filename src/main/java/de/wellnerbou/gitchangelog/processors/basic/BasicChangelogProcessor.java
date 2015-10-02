package de.wellnerbou.gitchangelog.processors.basic;

import de.wellnerbou.gitchangelog.model.Changelog;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import de.wellnerbou.gitchangelog.model.RevRange;
import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

import java.io.PrintStream;

public class BasicChangelogProcessor implements ChangelogProcessor {

	@Override
	public String getId() {
		return "basic";
	}

	@Override
	public void provideOptions(final Options options) {

	}

	@Override
	public void parseOptions(final CommandLine commandLine) {

	}

	@Override
	public Changelog processChangelog(final RevRange revRange, final Iterable<CommitDataModel> revisions, final PrintStream out) {
		Changelog changelog = new Changelog(revRange.fromRev, revRange.toRev);
		out.println("Commits between " + revRange.fromRev + " and " + revRange.toRev + ":");
		for(final CommitDataModel commitDataModel : revisions) {
			changelog.addLines("* " + commitDataModel.getShortHash() + "\t" + commitDataModel.getFullMessage());
		}
		return changelog;
	}

	@Override
	public String generateOutput(final Changelog changelog, final PrintStream out) {
		StringBuilder stringBuilder = new StringBuilder();
		for(final String line : changelog.getLines()) {
			stringBuilder.append(line);
			out.print(line);
		}
		return stringBuilder.toString();
	}
}
