package org.togetherjava.aoc.util.command;

import org.togetherjava.aoc.util.command.arguments.ArgumentParser;

import java.util.Objects;

public abstract class AbstractCommand<T> {
	
	public abstract void execute(T sender, String commandName, ArgumentParser parser);
	public abstract String commandName();

	public String description() {
		return "";
	}

	@Override
	public int hashCode() {
		return Objects.hash(commandName(), description());
	}
}
