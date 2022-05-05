import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("idea")
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.4.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    groups.set(emptyList())
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(projectDir.resolve(".qodana").canonicalPath)
    reportPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
    saveReport.set(true)
    showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

tasks {
    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                projectDir.resolve("README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider {
            changelog.run {
                getOrNull(properties("pluginVersion")) ?: getLatest()
            }.toHTML()
        })

        this.finalizedBy(buildPlugin)
        this.finalizedBy(publishPlugin)
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    signPlugin {
        certificateChain.set("""-----BEGIN CERTIFICATE-----
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
-----END CERTIFICATE-----""".trimIndent())

        privateKey.set("""-----BEGIN ENCRYPTED PRIVATE KEY-----
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
-----END ENCRYPTED PRIVATE KEY-----""".trimIndent())
        password.set("pass")
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set("perm:MjkxMjA2.OTItNTkyNw==.I2QjiUdbYHDySJGJ04jO53tYrhaqQR")
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
