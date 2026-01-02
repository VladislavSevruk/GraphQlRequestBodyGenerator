pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("com.gradle.develocity") version "4.3"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
    }
}

include(
    "graphql-request-body-generator-annotation",
    "graphql-request-body-generator",
    "graphql-model-generator-plugin"
)

rootProject.name = "graphql-request-body-generator-base"