plugins {
    id("com.github.vladislavsevruk.build-conventions")
    id("com.github.vladislavsevruk.integration-test-conventions")
    id("com.github.vladislavsevruk.functional-test-conventions")
    id("com.github.vladislavsevruk.test-conventions")
    id("com.github.vladislavsevruk.publishing-conventions")
    alias(libs.plugins.gradle.plugin.publish)
}

if (version.toString().endsWith("-SNAPSHOT")) {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    name = "GraphQL Model Generator Plugin"
                    description =
                        "This plugin helps to generate java POJO models for GraphQL Request Body Generator library."
                    url =
                        "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/tree/master/graphql-model-generator-plugin"
                }
            }
        }
    }
} else {
    gradlePlugin {
        website = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/tree/master/graphql-model-generator-plugin"
        vcsUrl = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator"
        plugins {
            register("graphQlModelGeneratorPlugin") {
                id = "${group}.${project.name}"
                displayName = "GraphQL Model Generator Plugin"
                description = "This plugin helps to generate java POJO models for GraphQL Request Body Generator library."
                implementationClass = "com.github.vladislavsevruk.generator.model.graphql.GqlModelGeneratorPlugin"
                tags = listOf("graphql-client", "graphql", "generator", "annotations", "request-body", "graphql-annotations")
            }
        }
    }
}

dependencies {
    implementation(libs.java.clazz.generator)
    functionalTestImplementation(platform(libs.spock.bom))
    functionalTestImplementation(libs.spock.core)
}

tasks.named<Test>("functionalTest") {
    shouldRunAfter(tasks.named<Test>("integrationTest"))
}