plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.teamhelper.imsdk.netcore"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                arguments("-DANDROID_TOOLCHAIN=clang")
                cppFlags("-std=c++17")
            }
            ndkBuild {
                arguments("-j8")
            }
        }
        ndk {
            abiFilters.apply {
                add("armeabi-v7a")
                add("arm64-v8a")
                add("x86_64")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    ndkVersion = "25.2.9519653"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    api(project(":IMSDK:Protocol"))
    api(project(":IMSDK:RTC"))
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
}