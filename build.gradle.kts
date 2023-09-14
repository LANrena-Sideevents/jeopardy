import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    alias(libs.plugins.ktor)
}

group = "de.lanrena"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.koin)
    implementation(libs.ktor.html)

    // frontend
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.webjars:knockout:3.5.1")

    // backend
    implementation("org.webjars:jquery:3.6.0")
    implementation("org.webjars:tether:1.4.0")
    implementation("org.webjars:bootstrap:5.1.1")

    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "2.0"
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

extensions.configure<KotlinTopLevelExtension> {
    jvmToolchain(11)
}

extensions.configure<KotlinProjectExtension> {
    sourceSets.all {
        languageSettings.optIn("kotlin.ExperimentalStdlibApi")
        languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
        languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }
}
