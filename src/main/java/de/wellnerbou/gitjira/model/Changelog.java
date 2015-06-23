package de.wellnerbou.gitjira.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Changelog {

	private final String from;
	private final String to;

	private Collection<String> tickets = new ArrayList<>();

	public Changelog(final String from, final String to) {
		this.from = from;
		this.to = to;
	}

	public void addTickets(String... tickets) {
		Collections.addAll(this.tickets, tickets);
	}

	public void addTickets(Collection<String> tickets) {
		this.tickets.addAll(tickets);
	}

	public Collection<String> getTickets() {
		return tickets;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
}
