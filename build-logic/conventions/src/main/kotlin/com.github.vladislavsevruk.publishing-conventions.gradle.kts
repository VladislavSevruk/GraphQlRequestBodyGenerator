plugins {
    `maven-publish`
    signing
}

group = project.group
version = project.version

val oosrhUsername: String = project.findProperty("ossrhUsername")?.toString() ?: ""
val oosrhPassword: String = project.findProperty("ossrhPassword")?.toString() ?: ""

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            artifactId = "${project.name}"
            pom {
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "uladzislau_seuruk"
                        name = "Uladzislau Seuruk"
                        email = "vladislavsevruk@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://VladislavSevruk/GraphQlRequestBodyGenerator.git"
                    developerConnection = "scm:git:ssh://VladislavSevruk/GraphQlRequestBodyGenerator.git"
                    url = "https://github.com/VladislavSevruk/GraphQlRequestBodyGenerator/tree/master"
                }
            }
        }
    }
    repositories {
        maven {
            val stagingRepoUrl = "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://central.sonatype.com/repository/maven-snapshots"
            url = uri(if (version.toString().endsWith("-SNAPSHOT")) snapshotsRepoUrl else stagingRepoUrl)
            credentials {
                username = oosrhUsername
                password = oosrhPassword
            }
        }
    }
}

signing {
    useGpgCmd()
    publishing.publications.withType<MavenPublication>().configureEach {
        sign(this)
    }
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(tasks.withType<Sign>())
}