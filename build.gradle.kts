import java.nio.file.Files
import java.nio.file.Paths
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