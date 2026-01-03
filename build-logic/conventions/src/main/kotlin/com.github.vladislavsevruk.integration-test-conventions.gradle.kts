plugins {
    `java-library`
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    shouldRunAfter(tasks.named<Test>("test"))
    useJUnitPlatform()
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations["testImplementation"])
}
val integrationTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations["testRuntimeOnly"])
}

tasks.named("check") {
    dependsOn(tasks.named("integrationTest"))
}