import org.gradle.jvm.tasks.Jar

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // https://mvnrepository.com/artifact/net.sourceforge.plantuml/plantuml
    implementation("net.sourceforge.plantuml:plantuml:1.2022.5")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

application {
    // Define the main class for the application.
    mainClass.set("com.github.binhtran432k.plantumlpreviewer.Runner")
}

tasks.withType<Jar>() {
    manifest { attributes["Main-Class"] = application.mainClass }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations["compileClasspath"].forEach { file: File -> from(zipTree(file.absoluteFile)) }
}

tasks.withType<AbstractArchiveTask> { setProperty("archiveBaseName", "plantuml-previewer") }

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
