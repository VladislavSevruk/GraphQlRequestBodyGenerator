plugins {
    java
    jacoco
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
    reports.html.required = false
    useJUnit()
    useJUnitPlatform()
    maxHeapSize = "64m"
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

configurations.create("binaryTestResultsElements") {
    isCanBeResolved = false
    isCanBeConsumed = true
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
    }
    outgoing.artifact(tasks.test.map { task -> task.getBinaryResultsDirectory().get() })
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