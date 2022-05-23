import org.gradle.jvm.tasks.Jar

val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

group = "com.github.binhtran432k.plantumlpreviewer"

description = "PlantUML Previewer"

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

tasks.compileJava {
    if (JavaVersion.current().isJava8) {
        java.targetCompatibility = JavaVersion.VERSION_1_8
    } else {
        options.release.set(Integer.parseInt(javacRelease))
    }
}

tasks.withType<Jar>() {
    manifest {
        attributes["Main-Class"] = application.mainClass
        attributes["Implementation-Version"] = archiveVersion
        attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val fullJar by
        tasks.registering(Jar::class) {
            from(sourceSets.main.get().output)

            dependsOn(configurations.runtimeClasspath)
            from({
                configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map {
                    zipTree(it)
                }
            })
        }

val noPlantUmlJar by
        tasks.registering(Jar::class) {
            archiveClassifier.set("noplantuml")

            from(sourceSets.main.get().output)

            dependsOn(configurations.runtimeClasspath)

            manifest { attributes["Class-Path"] = ". plantuml.jar" }
        }

val buildJars by tasks.registering { dependsOn(fullJar, noPlantUmlJar) }

tasks.withType<AbstractArchiveTask> { setProperty("archiveBaseName", "plantuml-previewer") }

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
