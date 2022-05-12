import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val koinVersion : String by project
val ktormVersion : String by project
val hikariCPVersion : String by project
val h2DatabaseVersion : String by project
val flywayVersion : String by project
val pgsqlConnectionVersion : String by project
val hopliteVersion : String by project

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
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        repositories {
            mavenLocal()
            mavenCentral()
        }

        dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

            // Config
            implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
            implementation("com.sksamuel.hoplite:hoplite-json:$hopliteVersion")

            // Database
            implementation("com.zaxxer:HikariCP:$hikariCPVersion")
            implementation("org.ktorm:ktorm-core:$ktormVersion")
            implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
            implementation("com.zaxxer:HikariCP:$hikariCPVersion")
            implementation("org.flywaydb:flyway-core:$flywayVersion")
            implementation("org.postgresql:postgresql:$pgsqlConnectionVersion")
            implementation("com.h2database:h2:$h2DatabaseVersion")

            // Koin
            implementation("io.insert-koin:koin-core:$koinVersion")

            /////// TEST DEPENDENCIES ///////
            testImplementation(kotlin("test"))

            testImplementation("io.insert-koin:koin-test:$koinVersion")
        }

        tasks.test {
            useJUnitPlatform()
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
