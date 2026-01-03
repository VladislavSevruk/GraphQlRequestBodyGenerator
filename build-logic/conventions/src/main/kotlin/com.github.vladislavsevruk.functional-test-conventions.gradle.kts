plugins {
    `java-library`
}

sourceSets {
    create("functionalTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

tasks.register<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"
    shouldRunAfter(tasks.named<Test>("test"))
    useJUnitPlatform()
}

val functionalTestImplementation by configurations.getting {
    extendsFrom(configurations["testImplementation"])
}
val functionalTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations["testRuntimeOnly"])
}

tasks.named("check") {
    dependsOn(tasks.named("functionalTest"))
}