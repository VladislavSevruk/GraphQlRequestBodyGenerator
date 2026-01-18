import org.gradle.api.internal.jvm.JavaVersionParser
import kotlin.String

plugins {
    `java-library`
    id("io.freefair.lombok")
}

val javaVersion: JavaVersion = JavaVersion.toVersion(if (project.hasProperty("javaVersion"))
    JavaVersionParser.parseMajorVersion(project.findProperty("javaVersion").toString())
else JavaVersionParser.parseCurrentMajorVersion())

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    withJavadocJar()
    withSourcesJar()
}

val oosrhUsername: String = project.findProperty("ossrhUsername")?.toString() ?: ""
val oosrhPassword: String = project.findProperty("ossrhPassword")?.toString() ?: ""
repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
            username = oosrhUsername
            password = oosrhPassword
        }
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        credentials {
            username = oosrhUsername
            password = oosrhPassword
        }
    }
}

val libs = versionCatalogs.named("libs")
dependencies {
    compileOnly(libs.findLibrary("lombok").get())
    annotationProcessor(libs.findLibrary("lombok").get())
    implementation(libs.findLibrary("log4j.api").get())
    implementation(libs.findLibrary("log4j.core").get())
}