plugins {
    id("java")
    id("java-library")
    id("application")
    id("me.champeau.jmh") version "0.7.2"
}

group = "org.togetherjava"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.reflections:reflections:0.10.2")
}

application {
    mainClass = "org.togetherjava.aoc.Aoc"
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

jmh {
    warmupIterations = 2
    iterations = 2
    fork = 2
}