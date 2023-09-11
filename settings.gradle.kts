rootProject.name = "jeopardy"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            plugin("ktor", "io.ktor.plugin")
                .versionRef("ktor")

            library(
                "kotlinx-coroutines-core",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core"
            ).versionRef("coroutines")

            bundle("kotlin-coroutines", listOf("kotlinx-coroutines-core"))

            library(
                "kotlinx-serialization-json",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-json"
            ).versionRef("serialization")

            bundle("kotlin-serialization", listOf("kotlinx-serialization-json"))

            library(
                "ktor-server-core",
                "io.ktor",
                "ktor-server-core"
            ).versionRef("ktor")

            library(
                "ktor-server-sessions",
                "io.ktor",
                "ktor-server-sessions"
            ).versionRef("ktor")

            library(
                "ktor-server-auth",
                "io.ktor",
                "ktor-server-auth"
            ).versionRef("ktor")

            library(
                "ktor-server-content-negotiation",
                "io.ktor",
                "ktor-server-content-negotiation"
            ).versionRef("ktor")

            library(
                "ktor-serialization-kotlinx-json",
                "io.ktor",
                "ktor-serialization-kotlinx-json"
            ).versionRef("ktor")

            library(
                "ktor-server-call-logging",
                "io.ktor",
                "ktor-server-call-logging"
            ).versionRef("ktor")

            library(
                "ktor-server-cio",
                "io.ktor",
                "ktor-server-cio"
            ).versionRef("ktor")

            library(
                "ktor-client-cio",
                "io.ktor",
                "ktor-client-cio"
            ).versionRef("ktor")

            library(
                "ktor-client-content-negotiation",
                "io.ktor",
                "ktor-client-content-negotiation"
            ).versionRef("ktor")

            library(
                "ktor-html",
                "io.ktor",
                "ktor-server-html-builder"
            ).versionRef("ktor")

            library(
                "ktor-server-webjars",
                "io.ktor",
                "ktor-server-webjars"
            ).versionRef("ktor")

            library(
                "ktor-server-websockets",
                "io.ktor",
                "ktor-server-websockets"
            ).versionRef("ktor")

            library(
                "ktor-server-freemarker",
                "io.ktor",
                "ktor-server-freemarker"
            ).versionRef("ktor")

            bundle(
                "ktor", listOf(
                    "ktor-server-auth",
                    "ktor-server-call-logging",
                    "ktor-server-cio",
                    "ktor-server-content-negotiation",
                    "ktor-server-core",
                    "ktor-server-freemarker",
                    "ktor-server-sessions",
                    "ktor-server-webjars",
                    "ktor-server-websockets",
                    "ktor-serialization-kotlinx-json",
                    "ktor-client-cio",
                    "ktor-client-content-negotiation"
                )
            )

            library(
                "log4j-slf4j2-impl",
                "org.apache.logging.log4j",
                "log4j-slf4j2-impl"
            ).versionRef("log4j2")

            bundle("logging", listOf("log4j-slf4j2-impl"))

            library(
                "koin-core",
                "io.insert-koin",
                "koin-core"
            ).versionRef("koin")

            library(
                "koin-ktor",
                "io.insert-koin",
                "koin-ktor"
            ).versionRef("koin_ktor")

            library(
                "koin-logger-slf4j",
                "io.insert-koin",
                "koin-logger-slf4j"
            ).versionRef("koin_ktor")

            bundle(
                "koin", listOf(
                    "koin-core",
                    "koin-ktor",
                    "koin-logger-slf4j"
                )
            )

            library(
                "koin-test",
                "io.insert-koin",
                "koin-test"
            ).versionRef("koin_ktor")

            library(
                "koin-test-junit5",
                "io.insert-koin",
                "koin-test-junit5"
            ).versionRef("koin_ktor")

            bundle(
                "koin-test", listOf(
                    "koin-test",
                    "koin-test-junit5",
                )
            )

            library(
                "ktor-server-tests-jvm",
                "io.ktor",
                "ktor-server-tests-jvm"
            ).versionRef("ktor")

            library(
                "kotlin-test-junit",
                "org.jetbrains.kotlin",
                "kotlin-test-junit"
            ).versionRef("kotlin")

            bundle(
                "ktor-testing", listOf(
                    "ktor-server-tests-jvm",
                    "kotlin-test-junit"
                )
            )

            library(
                "ktor-client-mock",
                "io.ktor",
                "ktor-client-mock"
            ).versionRef("ktor")

            bundle(
                "ktor-testing-client", listOf(
                    "ktor-client-mock"
                )
            )
        }
    }
}