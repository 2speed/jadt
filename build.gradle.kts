// ======================================================================
// JADT : Build
// ======================================================================

// Plugins
// ========================================

plugins {
    `java-library`
    `maven-publish`

    id("com.github.spotbugs") version "4.0.4"
}

// GAV
// ========================================

group       = "griz.jadt"
version     = "1.0.0-SNAPSHOT"
description = "Java ADTs"

// Java Version
// ========================================

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

// Dependency Management
// ========================================

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.1")
}

// Repositories
// ========================================

repositories {
    jcenter()
    mavenCentral()
}

// Code Quality
// ========================================

spotbugs {
    ignoreFailures.set(false)

    tasks.spotbugsMain {
        reports.create("html") {
            isEnabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }

    tasks.spotbugsTest {
        reports.create("html") {
            isEnabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }
}

// Tasks
// ========================================

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes(
            mapOf("Implementation-Title" to project.name,
                  "Implementation-Vendor" to "Matt Nicholls",
                  "Implementation-Version" to project.version)
        )
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
