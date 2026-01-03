plugins {
    java
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    jacocoAggregation(project(":graphql-request-body-generator"))
    jacocoAggregation(project(":graphql-model-generator-plugin"))
}