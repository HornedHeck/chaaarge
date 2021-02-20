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
	
	implementation("io.ktor:ktor-client-core:1.5.1")
	implementation("io.ktor:ktor-client-cio:1.5.1")
}
