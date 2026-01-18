plugins {
    java
    alias(libs.plugins.sonarqube)
    id("test-report-aggregation")
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

sonarqube {
    properties {
        property("sonar.projectKey", "VladislavSevruk_GraphQlRequestBodyGenerator")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.layout.buildDirectory.asFile.get()}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
        )
    }
}

val testReportData by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
    }
}

dependencies {
    testReportData(project(":token-parser-base"))
    testReportData(project(":graphql-request-body-generator"))
    testReportData(project(":graphql-model-generator-plugin"))
    jacocoAggregation(project(":token-parser-base"))
    jacocoAggregation(project(":graphql-request-body-generator"))
    jacocoAggregation(project(":graphql-model-generator-plugin"))
}

tasks.register<TestReport>("testSuiteAggregateTestReport") {
    group = "verification"
    description = "Aggregates tests suite reports from modules to root build directory"
    destinationDirectory = reporting.baseDirectory.dir("test-results")
    // Use test results from testReportData configuration
    testResults.from(testReportData)
}