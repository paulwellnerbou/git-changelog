package de.wellnerbou.gitchangelog.app;

import de.wellnerbou.gitchangelog.processors.ChangelogProcessor;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.Iterator;
import java.util.ServiceLoader;

public class CommandLineAppArgs extends AppArgs {
	private ServiceLoader<ChangelogProcessor> serviceLoader = ServiceLoader.load(ChangelogProcessor.class);

	public CommandLineAppArgs(final String... args) {
		OptionParser parser = new OptionParser();
		parser.accepts("processor").withRequiredArg().defaultsTo("");
		parser.accepts("repo").withRequiredArg().defaultsTo(".");
		parser.nonOptions("git revision range").ofType(String.class).describedAs("git revisions");

		changelogProcessor = getChangeLogProcessorFromArgs(parser, args);
		changelogProcessor.provideOptions(parser);

		final OptionSet options = parser.parse(args);
		repo = (String) options.valueOf("repo");

		if (options.nonOptionArguments().size() == 1) {
			toRev = (String) options.nonOptionArguments().get(0);
		} else if (options.nonOptionArguments().size() >= 2) {
			toRev = (String) options.nonOptionArguments().get(1);
			fromRev = (String) options.nonOptionArguments().get(0);
		}

		changelogProcessor.parseOptions(this, options);
	}

	private ChangelogProcessor getChangeLogProcessorFromArgs(final OptionParser parser, final String... args) {
		parser.accepts("processor").withRequiredArg().required();
		parser.allowsUnrecognizedOptions();
		final OptionSet options = parser.parse(args);
		return getChangeLogProcessor((String) options.valueOf("processor"));
	}

	private ChangelogProcessor getChangeLogProcessor(String className) {
		Iterator<ChangelogProcessor> it = serviceLoader.iterator();
		while (it.hasNext()) {
			final ChangelogProcessor next = it.next();
			if(next.getClass().getSimpleName().equals(className) || next.getId().equals(className)) {
				return next;
			}
		}
		return null;
	}
}
