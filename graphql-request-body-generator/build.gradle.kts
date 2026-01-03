plugins {
    id("com.github.vladislavsevruk.build-conventions")
    id("com.github.vladislavsevruk.test-conventions")
    id("com.github.vladislavsevruk.publishing-conventions")
}

dependencies {
    implementation(project(":graphql-request-body-generator-annotation"))
    implementation(libs.jackson.databind)
    api(libs.type.resolver)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = "GraphQL Request Body Generator"
                description = "This utility library helps to generate body for GraphQL request using POJO and annotations."
                url = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/graphql-request-body-generator"
            }
        }
    }
}