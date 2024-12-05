# Advent of code #
This is a community driven Java template and utility API for the annual [Advent of Code](https://adventofcode.com) event.

# Setup #

1. Clone this repository from GitHub as an IntelliJ repository (untested on other IDE's)
2. Add the `AOC_SESSION_COOKIE` environment variable to your gradle run config and set it equal to that of your session cookie after logging in to your AoC account.
3. Execute the gradle application -> run task.

# Creating and running a solution #

Create a `package-info.java` in the package which contains your solutions for a given year.
Make sure to annotate it with the `@AdventYear` annotation.
Example:

```java
@AdventYear(year=2024)
package org.togetherjava.aoc.solutions;
import org.togetherjava.aoc.core.annotations.AdventYear;
```

Create an implementation of the `PuzzleSolution` interface and annotate it with the `@AdventDay` annotation

```java
import org.togetherjava.aoc.core.annotations.AdventDay;
import org.togetherjava.aoc.core.puzzle.PuzzleInput;
import org.togetherjava.aoc.core.puzzle.PuzzleSolution;

@AdventDay(day = 1)
public class Day01 implements PuzzleSolution {

	@Override
	public Object part1(PuzzleInput input) {
		return 0L;
	}

	@Override
	public Object part2(PuzzleInput input) {
		return 0L;
	}
}
```

Then you can run your solutions with the static `AocRunner` class.

```java
AocRunner.run(); //this will detect and invoke the current day's problems
AocRunner.run(2024, 1); //this will detect and invoke a day for a given year/day
AocRunner.run(Day01.class); //Invoke a specific class
```

# Getting your session cookie #

In Chrome, or other Chromium browsers such as Opera, OperaGX etc
1. Hit ctrl + shift + J to open up developer tools
2. Navigate to the application tab
3. Click cookies
4. Copy the session cookie

![](/setup/4.png)

# Modifiying Gradle run configuration #
1. Go to your Gradle tasks and go to application -> run and right click to modify the run configurations
![](/setup/1.png)
2. Click this box to add an environment variable
![](/setup/2.png)
3. Name the environment variable `AOC_SESSION_COOKIE` and then paste the value of your session cookie from your browser in to the value box.
![](/setup/3.png)
4. Hit okay and then execute the Gradle run task
