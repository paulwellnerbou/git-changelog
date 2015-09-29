package de.wellnerbou.gitchangelog.processors;

import de.wellnerbou.gitchangelog.app.AppArgs;
import de.wellnerbou.gitchangelog.model.Changelog;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import de.wellnerbou.gitchangelog.model.RevRange;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.PrintStream;

public interface ChangelogProcessor {

	String getId();
	void provideOptions(OptionParser optionParser);
	void parseOptions(AppArgs appArgs, OptionSet optionSet);
	Changelog processChangelog(RevRange revRange, Iterable<CommitDataModel> revisions, PrintStream out);
	String generateOutput(final Changelog changelog, PrintStream out);
}
