plugins {
    id("com.github.vladislavsevruk.build-conventions")
    id("com.github.vladislavsevruk.test-conventions")
    id("com.github.vladislavsevruk.publishing-conventions")
}

dependencies {
    implementation(libs.jspecify)
    implementation(libs.jcip.annotations)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = "Token Parser Base"
                description = "This utility library sets base framework for parsing text input."
                url = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/token-parser-base"
            }
        }
    }
}