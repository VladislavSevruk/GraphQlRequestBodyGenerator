pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "build-logic"

include("conventions")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}