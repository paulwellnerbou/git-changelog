package de.wellnerbou.gitchangelog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Changelog {

	private final String from;
	private final String to;

	private Collection<String> lines = new ArrayList<>();

	public Changelog(final String from, final String to) {
		this.from = from;
		this.to = to;
	}

	public void addLines(String... tickets) {
		Collections.addAll(this.lines, tickets);
	}

	public void addLines(Collection<String> lines) {
		this.lines.addAll(lines);
	}

	public Collection<String> getLines() {
		return lines;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
}
