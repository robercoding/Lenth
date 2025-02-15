import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
}

// Workaround: https://stackoverflow.com/a/79082144/9420348
ksp {
    arg("room.schemaLocation", "${projectDir}/schemas")
}


kotlin {
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview) // Needed for Previews on android source set.

            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.material3.android)
            implementation(libs.androidx.startup.runtime)

            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.room.runtime.android)

            implementation(libs.slf4j.simple)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.material3)

            implementation(compose.components.resources)
            // This does not work if you don't have JVM target... But already working on Intellij EAP https://kotlinlang.slack.com/archives/CJLTWPH7S/p1737654194601389.
            // Soon will be available on stable version. (Check on 2025.12.01). For now use Previews on Android source set.
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.androidx.navigation.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.compose.ktor) // Needed otherwise error shows up on runtime: Unable to create a fetcher that supports: (Also need darwin and ktor http for each platform)

            // Local database
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            // Network
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)

            // Logger
            implementation(libs.touchlab.kermit)

            val nav_version = "2.8.0-alpha11"
            implementation("org.jetbrains.androidx.navigation:navigation-compose:$nav_version")
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}


dependencies {
    // Workaround: https://stackoverflow.com/a/79082144/9420348
    // KSP support for Room Compiler. Don't add kspCommonMetadataOnly
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}


android {
    namespace = "app.lenth"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.lenth"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

buildkonfig {
    packageName = "app.lenth"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "API_MAPS", "${project.property("lenth.maps.apikey")}")
    }
}