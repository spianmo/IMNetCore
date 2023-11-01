pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
}

rootProject.name = "IMSDKProject"
include(":DemoApp")
include(":IMSDK")
include(":IMSDK:NetCore")
include(":IMSDK:Protocol")
include(":IMUI")
include(":IMSDK:RTC")
