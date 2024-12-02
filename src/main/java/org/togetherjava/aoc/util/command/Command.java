package org.togetherjava.aoc.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * Returns the aliases this certain {@code Command} has relating to it.
	 * 
	 * @return the aliases
	 */
	String[] aliases();

	/**
	 * Returns the description of what this {@code Command} executes.
	 * 
	 * @return the description
	 */
	String description() default "";

	/**
	 * Returns the syntax of how this {@code Command} should be used.
	 * 
	 * @return the syntax
	 */
	String syntax() default "";
}
