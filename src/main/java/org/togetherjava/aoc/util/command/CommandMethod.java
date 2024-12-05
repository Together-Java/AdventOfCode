package org.togetherjava.aoc.util.command;

import lombok.RequiredArgsConstructor;
import org.togetherjava.aoc.util.command.arguments.ArgumentParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class CommandMethod<T> extends AbstractCommand<T> {
	
	private final String commandName;
	private final Method method;
	private final Command command;
	private final Object listener;

	@Override
	public void execute(T sender, String commandName, ArgumentParser parser) {
		try {
			method.invoke(listener, sender, commandName, parser);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String commandName() {
		return commandName;
	}

	@Override
	public String description() {
		return command.description();
	}
}
