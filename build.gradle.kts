plugins {
    id("com.palantir.git-version") version "3.1.0"
    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    kotlin("jvm") version "1.9.24"
    // https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij.platform") version "2.0.1"
}

val gitVersion: groovy.lang.Closure<String> by extra

group = "com.github.cloudcodec"
version = System.getenv("GITHUB_TAG") ?: gitVersion()

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "21"
        kotlinOptions.allWarningsAsErrors = false
    }
    withType<JavaCompile>().configureEach {
        targetCompatibility = "21"
    }
    buildSearchableOptions { enabled = false }
    test { useJUnitPlatform() }
}

intellijPlatform {
    pluginConfiguration {
        version.set(project.version.toString())
        ideaVersion {
            sinceBuild = "242"
            untilBuild = ""
        }
    }
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        instrumentationTools()
    }

    implementation("com.alibaba:druid:1.1.23")
    implementation("org.mybatis.generator:mybatis-generator-core:1.4.0")
    implementation("mysql:mysql-connector-java:5.1.25")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

