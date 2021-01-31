plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "com.hornedheck"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
