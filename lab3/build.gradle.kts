plugins {
	kotlin("jvm") version "1.4.21"
}

group = "com.hornedheck"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}
