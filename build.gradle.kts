plugins {
    kotlin("jvm") version "1.9.20"
    application
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(kotlin("stdlib-jdk8"))


}
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(20)
}