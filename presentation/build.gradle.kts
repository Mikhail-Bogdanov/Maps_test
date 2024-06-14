plugins {
    id(Dependencies.Android.Android)
    id(Dependencies.Kotlin.Kotlin)
    id(Dependencies.Ksp.Ksp)
}

android {
    namespace = "${Settings.PackageName}.presentation"
    compileSdk = Settings.CompileSDK

    defaultConfig {
        minSdk = Settings.MinSDK
    }

    ksp {
        arg(
            Dependencies.Navigation.ComposeDestinationsMode,
            Dependencies.Navigation.ModeDestinations
        )
    }

    buildTypes {
        release {
            consumerProguardFiles(
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Dependencies.Jvm.CompileJavaVersion
        targetCompatibility = Dependencies.Jvm.CompileJavaVersion
    }
    kotlinOptions {
        jvmTarget = Dependencies.Jvm.CompileJavaVersion.toString()
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.ComposeCompilerVersion
    }
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation(project(Dependencies.Modules.Core))
    implementation(project(Dependencies.Modules.LocalData))

    implementation(Dependencies.Compose.ComposeMaterial3)
    implementation(Dependencies.Compose.ComposeIcons)
    implementation(Dependencies.Compose.ComposeViewModel)
    implementation(Dependencies.Compose.ComposeLifecycle)
    implementation(Dependencies.Navigation.ComposeDestinationsCore)
    implementation(Dependencies.Compose.ComposePermissionsAccompanist)
    ksp(Dependencies.Navigation.ComposeDestinationsKsp)
    implementation(platform(Dependencies.Compose.ComposeBOM))

    implementation(platform(Dependencies.Kotlin.KotlinBOM))

    implementation(Dependencies.Maps.OSM)
    implementation(Dependencies.Maps.OSMBonus)
    implementation(Dependencies.Maps.LocationServices)

    implementation(Dependencies.Koin.KoinAndroid)
    implementation(Dependencies.Koin.KoinCompose)

    implementation(Dependencies.Orbit.OrbitCompose)
    implementation(Dependencies.Orbit.OrbitAndroid)
}