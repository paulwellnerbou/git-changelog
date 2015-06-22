package de.wellnerbou.gitjira.app;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.ArrayList;
import java.util.Collections;
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

		if(options.nonOptionArguments().size() == 1) {
			toRev = (String) options.nonOptionArguments().get(0);
		} else if(options.nonOptionArguments().size() >= 2) {
			toRev = (String) options.nonOptionArguments().get(1);
			fromRev = (String) options.nonOptionArguments().get(0);
		}

		jiraPrefixes = new ArrayList<>();
		for(String prefix : (List<String>) options.valuesOf("jiraprefix")) {
			Collections.addAll(jiraPrefixes, prefix.split(","));
		}
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
