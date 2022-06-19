pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
rootProject.name = "demo"
include ("application_modules")
include("application_modules:api")
findProject(":application_modules:api")?.name = "api"
