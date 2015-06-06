package de.wellnerbou.gitjira.app;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.List;

public class AppArgs {

	private String repo;
	private String fromRev;
	private String toRev;
	private List<String> jiraPrefixes;
	private String jiraBaseUrl;

	public AppArgs(final String... args) {
		OptionParser parser = new OptionParser();
		parser.accepts("repo").withRequiredArg().defaultsTo(".");
		parser.accepts("jiraprefix").withRequiredArg();
		parser.accepts("jirabaseurl").withRequiredArg();
		parser.nonOptions("git revision range").ofType(String.class).describedAs("git revisions").isRequired();

		final OptionSet options = parser.parse(args);
		repo = (String) options.valueOf("repo");

		if (options.nonOptionArguments().size() != 2) {
			throw new RuntimeException("Two git paths/revisions expected");
		}
		fromRev = (String) options.nonOptionArguments().get(0);
		toRev = (String) options.nonOptionArguments().get(1);
		jiraPrefixes = (List<String>) options.valuesOf("jiraprefix");
		jiraBaseUrl = (String) options.valueOf("jirabaseurl");
	}

	public String getRepo() {
		return repo;
	}

	public String getFromRev() {
		return fromRev;
	}

	public String getToRev() {
		return toRev;
	}

	public List<String> getJiraPrefixes() {
		return jiraPrefixes;
	}

	public String getJiraBaseUrl() {
		return jiraBaseUrl;
	}
}
