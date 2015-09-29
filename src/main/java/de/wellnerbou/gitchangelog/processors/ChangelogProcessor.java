package de.wellnerbou.gitchangelog.processors;

import de.wellnerbou.gitchangelog.model.Changelog;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import de.wellnerbou.gitchangelog.model.RevRange;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

import java.io.PrintStream;

public interface ChangelogProcessor {

	String getId();
	void provideOptions(final Options options);
	void parseOptions(CommandLine commandLine);
	Changelog processChangelog(RevRange revRange, Iterable<CommitDataModel> revisions, PrintStream out);
	String generateOutput(final Changelog changelog, PrintStream out);
}
