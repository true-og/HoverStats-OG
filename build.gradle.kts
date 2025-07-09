plugins {
    id("java") // Tell gradle this is a java project.
    id("java-library") // Import helper for source-based libraries.
    kotlin("jvm") version
        "2.1.21" // Import kotlin jvm plugin for kotlin/java integration (Required for DiamondBank-OG API)
    id("com.diffplug.spotless") version "7.0.4" // Import auto-formatter.
    id("com.gradleup.shadow") version "8.3.6" // Import shadow API.
    eclipse // Import eclipse plugin for IDE integration.
}

java {
    sourceCompatibility = JavaVersion.VERSION_17 // Compile with JDK 17 compatibility.

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Use JDK 17.
        vendor.set(JvmVendorSpec.GRAAL_VM) // Use GraalVM CE.
    }
}

kotlin { jvmToolchain(17) }

group = "me.Brand0n_" // Declare bundle identifier.

version = "5.6" // Declare plugin version (will be in .jar).

val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version, "apiVersion" to apiVersion)

    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") { expand(props) }
    from("LICENSE") { into("/") } // Bundle license into .jars.
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://repo.purpurmc.org/snapshots") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("file://${System.getProperty("user.home")}/.m2/repository") }
    val customMavenLocal = System.getProperty("SELF_MAVEN_LOCAL_REPO")
    if (customMavenLocal != null) {
        val mavenLocalDir = file(customMavenLocal)
        if (mavenLocalDir.isDirectory) {
            println("Using SELF_MAVEN_LOCAL_REPO at: $customMavenLocal")
            maven { url = uri("file://${mavenLocalDir.absolutePath}") }
        } else {
            logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 for mavenLocal()")
        }
    } else {
        logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 for mavenLocal()")
    }
}

dependencies {
    compileOnly(
        "org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT"
    ) // Declare Purpur API version to be packaged.        compileOnly("me.clip:placeholderapi:2.11.6") // Import
    // Placeholder API.
    compileOnly("me.clip:placeholderapi:2.11.6") // Import PlaceholderAPI.
    compileOnlyApi(project(":libs:Utilities-OG")) // Import TrueOG Network Utilities-OG API.
    compileOnlyApi(
        "net.trueog.diamondbank-og:diamondbank-og:1.19-190fb1dc21"
    ) // Import TrueOG Network DiamondBank-OG API.
}

tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible .jars
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    exclude("me.clip.placeholderapi.*") // Exclude the Placeholders package from being shadowed.
    archiveClassifier.set("") // Use empty string instead of null.
    minimize()
}

tasks.build {
    dependsOn(tasks.spotlessApply)
    dependsOn(tasks.shadowJar)
}

tasks.jar { archiveClassifier.set("part") }

tasks.withType<JavaCompile>().configureEach {
    dependsOn(":libs:DiamondBank-OG:publishToMavenLocal")
    options.compilerArgs.addAll(listOf("-parameters", "-Xlint:deprecation")) // Triggers deprecation warning messages.
    options.encoding = "UTF-8"
    options.isFork = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(":libs:DiamondBank-OG:publishToMavenLocal")
}

spotless {
    java {
        removeUnusedImports()
        palantirJavaFormat()
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }
        target("build.gradle.kts", "settings.gradle.kts")
    }
}
