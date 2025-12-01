import java.nio.file.Files
import java.nio.file.Paths
import java.util.Calendar
import kotlin.io.path.listDirectoryEntries

plugins {
    id("java")
    id("java-library")
    id("application")
    id("me.champeau.jmh") version "0.7.2"
    id("maven-publish")
}

group = "org.togetherjava"
version = "1.0.1-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    api("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    api("org.reflections:reflections:0.10.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

application {
    mainClass = "org.togetherjava.aoc.AOC"
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

jmh {
    warmupIterations = 2
    iterations = 2
    fork = 2
}

tasks.register("deleteCachedInputs") {
    group = "AOC"
    description = "Deletes all cached AOC inputs"
    doLast {
        val inputFolder = Paths.get(System.getProperty("user.home"), ".together-java", "aoc", "inputs")
        inputFolder.listDirectoryEntries().forEach { f ->
            if(Files.deleteIfExists(f)) {
                println("Sucessfully deleted " + f.toAbsolutePath().toString())
            }
        }
    }
}

tasks.register("generateAocYear") {
    group = "AOC"
    description = "Creates a new AOC solution package for a given year."

    var year = Calendar.getInstance().get(Calendar.YEAR)

    val aocYear: String = project.findProperty("aocYear") as String? ?: year.toString()

    doLast {
        val baseDir = "src/main/java/org/togetherjava/aoc/solutions/y$aocYear"
        val packageDir = file(baseDir)

        if (!packageDir.exists()) {
            packageDir.mkdirs()
        }

        // --- Generate package-info.java ---
        val packageInfoFile = file("$baseDir/package-info.java")
        packageInfoFile.writeText("""
            @AdventYear(year=$aocYear)
            package org.togetherjava.aoc.solutions.y$aocYear;

            import org.togetherjava.aoc.internal.puzzle.AdventYear;
            """.trimIndent() + "\n")

        // --- Generate DayX.java files ---
        for (day in 1..25) {
            val dayFormatted = String.format("%02d", day)
            val classFile = file("$baseDir/Day$dayFormatted.java")


            if (!classFile.exists()) {
                classFile.writeText(
                    """
                    package org.togetherjava.aoc.solutions.y$aocYear;

                    import org.togetherjava.aoc.internal.puzzle.AdventDay;
                    import org.togetherjava.aoc.internal.puzzle.PuzzleInput;
                    import org.togetherjava.aoc.internal.puzzle.PuzzleSolution;

                    @AdventDay(day=$day)
                    public class Day$dayFormatted implements PuzzleSolution {

                        @Override
                        public Object part1(PuzzleInput input) {
                            return null;
                        }

                        @Override
                        public Object part2(PuzzleInput input) {
                            return null;
                        }
                    }
                    """.trimIndent() + "\n"
                )
            }
        }

        println("Generated Advent of Code package for year $aocYear")
    }
}