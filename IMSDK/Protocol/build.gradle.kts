import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.teamhelper.imsdk.nethandler"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
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

    sourceSets {
        getByName("main") {
            java {
                srcDir("build/generated/source/proto/main/java")
            }
            kotlin {
                srcDir("build/generated/source/proto/main/kotlin")
            }
            proto {
                srcDir("src/main/proto")
            }
        }
    }

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.27.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("java") { }
                id("kotlin") { }
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    api("com.google.code.gson:gson:2.10.1")
    api("com.google.protobuf:protobuf-java:4.27.1")
    api("com.google.protobuf:protobuf-kotlin:4.27.1")
}

