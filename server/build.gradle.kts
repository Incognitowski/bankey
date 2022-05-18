plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":shared"))

    implementation("io.javalin:javalin-bundle:4.6.0")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testResources")