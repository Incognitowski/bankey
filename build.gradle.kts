import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val koinVersion : String by project
val ktormVersion : String by project

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

            implementation("com.h2database:h2:2.0.206")

            implementation("org.ktorm:ktorm-core:${ktormVersion}")

            implementation("io.insert-koin:koin-core:$koinVersion")

            // Testing
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