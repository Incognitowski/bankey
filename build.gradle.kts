import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val koinVersion : String by project
val ktormVersion : String by project
val hikariCPVersion : String by project
val h2DatabaseVersion : String by project
val flywayVersion : String by project
val pgsqlConnectionVersion : String by project

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "io.learn"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

subprojects {
    afterEvaluate {
        dependencies {
            implementation("com.zaxxer:HikariCP:$hikariCPVersion")
            implementation("org.ktorm:ktorm-core:$ktormVersion")
            implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
            implementation("com.zaxxer:HikariCP:$hikariCPVersion")
            implementation("org.postgresql:postgresql:$pgsqlConnectionVersion")
            implementation("org.flywaydb:flyway-core:$flywayVersion")

            implementation("io.insert-koin:koin-core:$koinVersion")

            // Testing
            testImplementation("com.h2database:h2:$h2DatabaseVersion")
            testImplementation("io.insert-koin:koin-test:$koinVersion")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}