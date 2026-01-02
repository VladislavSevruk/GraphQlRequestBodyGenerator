plugins {
    id("com.github.vladislavsevruk.build-conventions")
    id("com.github.vladislavsevruk.publishing-conventions")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = "GraphQL Request Body Generator Annotations"
                description = "Annotations for utility library that helps to generate body for GraphQL request."
                url = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/tree/master/graphql-request-body-generator-annotation"
            }
        }
    }
}