# Advent of code #
This is a community driven Java template and utility API for the annual [Advent of Code](https://adventofcode.com) event.

# Setup #

1. Clone this repository from GitHub as an IntelliJ repository (untested on other IDE's)
2. Add the `AOC_SESSION_COOKIE` environment variable to your gradle run config and set it equal to that of your session cookie after logging in to your AoC account.
   * Alternatively you can set a system environment variable and restart the IDE/terminal.
3. Execute the gradle application -> run task.

# Creating and Running Solutions #

## Solution Packages
Create a `package-info.java` in the package which contains your solutions for a given year.
Make sure to annotate it with the `@AdventYear` annotation, so we can pull the correct inputs.

**Example**:
```java
@AdventYear(year=2024)
package org.togetherjava.aoc.solutions;

import org.togetherjava.aoc.core.annotations.AdventYear;
```

The package my contain any files or subpackages you wish, but as described next, the solutions need a specific structure.

---
## Solution Classes
Solution implementations must be under an `@AdventYear` package to be runnable.
Additionally, they must implement the `PuzzleSolution` interface, which provides methods
that give an input type directly.

A useful shortcut for this is `CTRL + I` in IntelliJ, which implements missing methods.

### Setting The Day

While `@AdventYear` explicitly defines the year, a solution can implicitly or explicitly 
define which day of the year.

### Explicit Day Value
To explicitly set which day your solution corresponds to, use the `@AdventDay` annotation to override and ignore auto-detection.

**Example**:
```java
@AdventDay(day = 1)
public class DayOne implements PuzzleSolution
```

### Implicit Day Auto-Detection
If no `@AdventDay` annotation is provided, a default auto-detection is used instead.
This will look for the *first* number in the class name in the range `[1, 31]`, ignoring leading zeros.
Therefore, the following examples would be parsed as:

| Class Name     | Implicit Day |
|----------------|--------------|
| `Day1`         | 1            |
| `Day02`        | 2            |
| `Day003`       | 3            |
| `AocDay4`      | 4            |
| `AocDay05`     | 5            |
| `Aoc2024Day6`  | 6            |
| `Aoc2024Day07` | 7            |
| `Day8Attempt2` | 8            |

---
If neither implicit nor explicit days can be resolved, an error is thrown at runtime.

### Puzzle Solution
The methods defined by `PuzzleSolution` return `Object`, which is because not all
AOC answers are `int` or `long`. You may still return an `int` or `long` type, and it will be autoboxed into an object.

Here is an example implementation

```java
public class Day01 implements PuzzleSolution {

	@Override
	public Object part1(PuzzleInput input) {
		return 123;
	}

	@Override
	public Object part2(PuzzleInput input) {
		return 456L;
	}
}
```

## Running Solutions
The `AocRunner` class provides static access to run your solution implementations.

There are 3 method names: `run`, `runPart1`, and `runPart2`, which each have the following overrides:

| Parameters                               | Description                  |
|------------------------------------------|------------------------------|
| `()`                                     | Run today                    |
| `(int year, int day)`                    | Run the given day            |
| `(PuzzleDate date)`                      | Run the given date           |
| `(Class<? extends PuzzleSolution> impl)` | Run the given implementation |


Below are examples of different runner invocations:

```java
AocRunner.run(); // Detect and run the current day's solution
AocRunner.run(2024, 1); // Detect and run the AOC 2024 Day 1 solution
AocRunner.run(Day01.class); // Run the solution implemented in Day01
AocRunner.run(Day01BruteForce.class); // Run the solution implemented in Day01BruteForce
```

### Multiple Solutions
If more than one class implements a solution for a given date, all of those
implementations are registered internally. When trying to run them without
a specific class reference (`e.g. run(Day1BruteForce.class)`) then the specific
implementation chosen is not well-defined, and is whichever is reflectively found first.

# Input Caching
Input files are fetched from the AOC web API to get your input data. To support AOC,
we automatically cache the input responses on your local computer, preventing redundant API
calls every time you run an implementation.

## Local File Cache
Cached input files are stored locally in a path relative to your OS platform. This is done
with the `user.home` system property. Relative to that, `/.together-java/aoc/inputs/YYYY-DD-puzzle-input.txt`
is where the file is stored.

* Windows: `%userprofile%/.together-java/aoc/inputs/`

# Session Cookie (`AOC_SESSION_COOKIE`)
## Getting your session cookie

In Chrome, or other Chromium browsers such as Opera, OperaGX etc
1. Hit `ctrl + shift + J` to open up developer tools
2. Navigate to the application tab
3. Click cookies
4. Copy the session cookie

![](/setup/4.png)

## Modifiying Gradle run configuration / env
1. Go to your Gradle tool window
   * `Tasks`
     * `application`
       * Right-click `run` → `Modify Run Configuration...`  

   ![](/setup/1.png)
2. Click this box to add an environment variable  
![](/setup/2.png)
3. Name the environment variable `AOC_SESSION_COOKIE` and then paste the value of your session cookie from your browser in to the value box.  
![](/setup/3.png)
4. Hit okay and then execute the Gradle `run` task
