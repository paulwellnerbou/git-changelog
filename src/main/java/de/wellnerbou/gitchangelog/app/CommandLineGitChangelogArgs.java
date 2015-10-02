package de.wellnerbou.gitchangelog.app;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.PrintStream;
import java.util.List;
import java.util.ServiceLoader;

public class CommandLineGitChangelogArgs extends GitChangelogArgs {
	private ServiceLoader<ChangelogProcessor> serviceLoader = ServiceLoader.load(ChangelogProcessor.class);
	private String[] args;

	public CommandLineGitChangelogArgs parse(final String... args) throws ParseException {
		this.args = args;
		final Options options = getOptions();

		CommandLine commandLine = new DefaultParser().parse(options, args, true);
		repo = commandLine.getOptionValue("repo", ".");
		if (commandLine.getArgList().size() == 1) {
			toRev = commandLine.getArgList().get(0);
		} else if (commandLine.getArgList().size() >= 2) {
			toRev = commandLine.getArgList().get(1);
			fromRev = commandLine.getArgList().get(0);
		}
		if(changelogProcessor != null) {
			changelogProcessor.parseOptions(commandLine);
		}
		return this;
	}

	public Options getOptions() throws ParseException {
		Options options = new Options();
		options.addOption(getProcessorOption());
		options.addOption(Option.builder().longOpt("repo").hasArg().required(false).argName("/path/to/repo").desc("Path to git repository to use, defaults to '.'.").build());

		changelogProcessor = getChangeLogProcessorFromArgs(args);
		if(changelogProcessor != null) {
			changelogProcessor.provideOptions(options);
		}
		return options;
	}

	private Option getProcessorOption() {
		return Option.builder().longOpt("processor").hasArg().required(true).argName("class name or id").desc("Available processors: " + listAvailableProcessors()).build();
	}

	private String listAvailableProcessors() {
		List<String> processors = Lists.newArrayList();
		for (final ChangelogProcessor next : serviceLoader) {
			processors.add(next.getId());
		}
		return Joiner.on(", ").join(processors);
	}

	protected ChangelogProcessor getChangeLogProcessorFromArgs(final String... args) {
		try {
			final Options options = new Options();
			options.addOption(getProcessorOption());
			return getChangeLogProcessor(new DefaultParser().parse(options, args, true).getOptionValue("processor"));
		} catch (ParseException e) {
			// e.printStackTrace();
			return null;
		}
	}

	private ChangelogProcessor getChangeLogProcessor(String className) {
		for (final ChangelogProcessor next : serviceLoader) {
			if (next.getClass().getSimpleName().equals(className) || next.getId().equals(className)) {
				return next;
			}
		}
		return null;
	}

	public void printHelp(final PrintStream out) throws ParseException {
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("git-changelog [OPTIONS] <revFrom> <revTo>", getOptions());
	}
}
