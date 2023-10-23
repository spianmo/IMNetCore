pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IMSDKProject"
include(":DemoApp")
include(":IMSDK")
include(":IMSDK:NetCore")
include(":IMSDK:NetHandler")
include(":IMUI")
include(":IMSDK:RTC")
