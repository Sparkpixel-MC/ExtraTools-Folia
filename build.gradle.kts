import org.gradle.jvm.tasks.Jar

plugins {
    java
    alias(libs.plugins.shadow)
}

group = "me.sfiguz7"
version = "1.1.0-Folia"

java {
    toolchain {
        // Paper/Folia 26.3 requires Java 25
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }

    maven("https://jitpack.io") {
        name = "jitpack"
    }
}

dependencies {
    compileOnly(libs.folia.api)
    compileOnly(libs.slimefun4)
    compileOnly(libs.guizhanlib.plugin)

    implementation(libs.bstats.bukkit)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
    }

    processResources {
        val props = mapOf(
            "version" to project.version.toString()
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    named<Jar>("jar") {
        archiveClassifier = "unshaded"
    }

    shadowJar {
        archiveClassifier = ""
        minimize()

        relocate("org.bstats", "me.sfiguz7.extratools.bstats")

        // Strip signature / license metadata pulled in from shaded dependencies
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        exclude("META-INF/LICENSE*", "META-INF/NOTICE*", "META-INF/versions/*/module-info.class")
    }

    build {
        dependsOn(shadowJar)
    }
}
