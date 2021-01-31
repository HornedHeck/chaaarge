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
	
	testImplementation(platform("org.junit:junit-bom:5.7.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}