plugins {
    kotlin("jvm") version "1.9.10"
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

group = "de.lanrena"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation(kotlin("stdlib"))
    implementation("com.intellij:annotations:12.0")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // frontend
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.webjars:knockout:3.5.1")

    // backend
    implementation("org.webjars:jquery:3.6.0")
    implementation("org.webjars:tether:1.4.0")
    implementation("org.webjars:bootstrap:5.1.1")
}
