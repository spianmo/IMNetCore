pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://jcenter.bintray.com/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://jcenter.bintray.com/")
    }
}

rootProject.name = "IMSDKProject"
include(":DemoApp")
include(":IMSDK")
include(":IMSDK:YukiReflection")
include(":IMSDK:EpicHook")
include(":IMSDK:NetCore")
include(":IMSDK:Protocol")
include(":IMUI")
include(":IMSDK:RTC")
