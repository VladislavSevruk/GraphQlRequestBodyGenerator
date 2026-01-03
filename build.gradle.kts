plugins {
    java
    id("test-report-aggregation")
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

val testReportData by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
    }
}

dependencies {
    testReportData(project(":graphql-request-body-generator"))
    testReportData(project(":graphql-model-generator-plugin"))
    jacocoAggregation(project(":graphql-request-body-generator"))
    jacocoAggregation(project(":graphql-model-generator-plugin"))
}

tasks.register<TestReport>("testSuiteAggregateTestReport") {
    destinationDirectory = reporting.baseDirectory.dir("test-results")
    // Use test results from testReportData configuration
    testResults.from(testReportData)
}