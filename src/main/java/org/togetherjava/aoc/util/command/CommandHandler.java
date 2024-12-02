package org.togetherjava.aoc.util.command;

import org.togetherjava.aoc.util.command.arguments.ArgumentParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

	private static final Pattern PARSE_QUOTES = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
	
	private final HashMap<String, AbstractCommand<?>> commands = new HashMap<>();

	/**
	 * see {@link CommandHandler#execute(T, String, String[])}
	 */
	public <T> void execute(T executor, String rawCommand) throws CommandNotFoundException {
		Matcher matcher = PARSE_QUOTES.matcher(rawCommand);
		String commandName = "";
		List<String> arguments = new ArrayList<>();
		if (matcher.find()) {
			commandName = matcher.group(1);
		}
		while(matcher.find()) {
			arguments.add(matcher.group(1).replace("\"", ""));
		}
		execute(executor, commandName, arguments.toArray(new String[0]));
	}

	public <T> void execute(T executor, String commandName, String[] args) throws CommandNotFoundException {
		AbstractCommand<T> command = (AbstractCommand<T>) commands.get(commandName);
		if(command == null) {
			throw new CommandNotFoundException(commandName);
		}
		ArgumentParser parser = new ArgumentParser(args);
		command.execute(executor, commandName, parser);
	}
	
	public <T> void registerCommand(AbstractCommand<T> command) {
		commands.putIfAbsent(command.commandName(), command);
	}

	public void unregisterCommand(String commandName) {
		commands.remove(commandName);
	}

	public <T> void unregisterCommand(AbstractCommand<T> command) {
		unregisterCommand(command.commandName());
	}

	public void unregisterCommandListener(Object listener) {
		for(Method m : listener.getClass().getMethods()) {
			m.setAccessible(true);
			if(m.isAnnotationPresent(Command.class)) {
				Command command = m.getAnnotation(Command.class);
				for(String cmd : command.aliases()) {
					unregisterCommand(cmd);
				}
			}
		}
	}
	
	public void registerCommandListener(Object listener) {
		for(Method m : listener.getClass().getMethods()) {
			if(m.isAnnotationPresent(Command.class)) {
				Class<?>[] params = m.getParameterTypes();
				if(params.length != 3 || !Object.class.isAssignableFrom(params[0]) || !params[1].equals(String.class) || !params[2].equals(ArgumentParser.class)) {
					throw new IllegalArgumentException(m.getName() + " must have 3 parameters (Sender, String, Arguments)");
				}
				Command command = m.getAnnotation(Command.class);
				for(String alias : command.aliases()) {
					CommandMethod<?> method = new CommandMethod<>(alias, m, command, listener);
					registerCommand(method);
				}
			}
		}
	}
}