plugins {
    org.sonarqube
    java
    jacoco
}

sonarqube {
    properties {
        property("sonar.projectKey", "VladislavSevruk_GraphQlRequestBodyGenerator")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.withType<Test>())
    executionData.setFrom(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))
    reports {
        xml.required = true
        csv.required = false
        html.required = true
    }
}

val mockitoAgent = configurations.create("mockitoAgent")
tasks.withType<Test>().configureEach {
    useJUnit()
    useJUnitPlatform()
    maxHeapSize = "64m"
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

val libs = versionCatalogs.named("libs")
dependencies {
    testCompileOnly(libs.findLibrary("lombok").get())
    testAnnotationProcessor(libs.findLibrary("lombok").get())
    testImplementation(libs.findLibrary("junit.jupiter.api").get())
    testImplementation(libs.findLibrary("junit.jupiter.params").get())
    testImplementation(libs.findLibrary("mockito.core").get())
    testImplementation(libs.findLibrary("mockito.junit.jupiter").get())
    testRuntimeOnly(libs.findLibrary("junit.platform.launcher").get())
    testRuntimeOnly(libs.findLibrary("junit.jupiter.engine").get())
    mockitoAgent(libs.findLibrary("mockito.core").get()) {
        isTransitive = false
    }
}