package de.wellnerbou.gitjira.app;

import com.google.common.collect.Lists;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppArgs {

	private String repo;
	private String fromRev;
	private String toRev;
	private List<String> jiraProjectPrefixes;
	private String jiraBaseUrl = "";

	public AppArgs(final String... args) {
		OptionParser parser = new OptionParser();
		parser.accepts("repo").withRequiredArg().defaultsTo(".");
		parser.accepts("projects").withRequiredArg();
		parser.accepts("baseurl").withRequiredArg().defaultsTo("");
		parser.nonOptions("git revision range").ofType(String.class).describedAs("git revisions");

		final OptionSet options = parser.parse(args);
		repo = (String) options.valueOf("repo");

		if (options.nonOptionArguments().size() == 1) {
			toRev = (String) options.nonOptionArguments().get(0);
		} else if (options.nonOptionArguments().size() >= 2) {
			toRev = (String) options.nonOptionArguments().get(1);
			fromRev = (String) options.nonOptionArguments().get(0);
		}

		splitAndSetJiraPrefixes((List<String>) options.valuesOf("projects"));
		jiraBaseUrl = (String) options.valueOf("baseurl");
	}

	private void splitAndSetJiraPrefixes(final Collection<String> jiraprefix) {
		jiraProjectPrefixes = new ArrayList<>();
		for (String prefix : jiraprefix) {
			Collections.addAll(jiraProjectPrefixes, prefix.split("[\\W]"));
		}
	}

	public void setFromRev(final String fromRev) {
		this.fromRev = fromRev;
	}

	public void setJiraBaseUrl(final String jiraBaseUrl) {
		this.jiraBaseUrl = jiraBaseUrl;
	}

	public void setJiraProjectPrefixes(final String jiraProjectPrefixes) {
		this.splitAndSetJiraPrefixes(Lists.newArrayList(jiraProjectPrefixes));
	}

	public void setJiraProjectPrefixes(final Collection<String> jiraProjectPrefixes) {
		this.splitAndSetJiraPrefixes(jiraProjectPrefixes);
	}

	public void setRepo(final String repo) {
		this.repo = repo;
	}

	public void setToRev(final String toRev) {
		this.toRev = toRev;
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

	public List<String> getJiraProjectPrefixes() {
		return jiraProjectPrefixes;
	}

	public String getJiraBaseUrl() {
		return jiraBaseUrl;
	}
}
