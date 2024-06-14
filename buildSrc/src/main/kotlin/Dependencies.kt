import org.gradle.api.JavaVersion

object Dependencies {

    object Modules {
        const val LocalData = ":localData"
        const val Presentation = ":presentation"
        const val AppEntryPoint = ":appEntryPoint"
        const val Core = ":core"
    }

    object Android {
        const val ApplicationVersion = "8.1.0"
        const val Application = "com.android.application"
        const val AppCompatVersion = "1.6.1"

        const val AppCompat = "androidx.appcompat:appcompat:$AppCompatVersion"
        const val Android = "com.android.library"
    }

    object Compose {
        const val AccompanistVersion = "0.31.3-beta"
        const val ComposeCompilerVersion = "1.5.4"
        const val ComposeBomVersion = "2024.05.00"
        const val ComposeActivityVersion = "1.8.2"
        const val ComposeLifecycleVersion = "2.7.0"

        const val ComposeBOM = "androidx.compose:compose-bom:$ComposeBomVersion"
        const val ComposeActivity = "androidx.activity:activity-compose:$ComposeActivityVersion"
        const val ComposeMaterial3 = "androidx.compose.material3:material3"
        const val ComposeIcons = "androidx.compose.material:material-icons-extended"
        const val ComposeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$ComposeLifecycleVersion"
        const val ComposePermissionsAccompanist = "com.google.accompanist:accompanist-permissions:$AccompanistVersion"
        const val ComposeLifecycle = "androidx.lifecycle:lifecycle-runtime-compose:$ComposeLifecycleVersion"
    }

    object Navigation {

        const val ComposeNavigationVersion = "2.6.0"
        const val ComposeDestinationsVersion = "1.9.55"

        const val ComposeNavigation = "androidx.navigation:navigation-compose:$ComposeNavigationVersion"
        const val ComposeDestinationsCore = "io.github.raamcosta.compose-destinations:core:$ComposeDestinationsVersion"
        const val ComposeDestinationsKsp = "io.github.raamcosta.compose-destinations:ksp:$ComposeDestinationsVersion"
        const val ComposeDestinationsAnimationsCore = "io.github.raamcosta.compose-destinations:animations-core:$ComposeDestinationsVersion"

        const val ComposeDestinationsMode = "compose-destinations.mode"
        const val ModeDestinations = "destinations"
    }

    object Maps {
        const val OSMVersion = "6.1.18"
        const val OSMBonusVersion = "6.9.0"
        const val LocationServicesVersion = "21.3.0"

        const val OSM = "org.osmdroid:osmdroid-android:$OSMVersion"
        const val OSMBonus = "com.github.MKergall:osmbonuspack:$OSMBonusVersion"
        const val LocationServices = "com.google.android.gms:play-services-location:$LocationServicesVersion"
    }

    object Jvm {
        const val JvmVersion = "1.8.0"
        val CompileJavaVersion = JavaVersion.VERSION_17

        const val Jvm = "org.jetbrains.kotlin.jvm"
    }

    object Kotlin {
        const val KotlinVersion = "1.9.20"

        const val KotlinBOM = "org.jetbrains.kotlin:kotlin-bom:$KotlinVersion"
        const val Kotlin = "org.jetbrains.kotlin.android"
    }

    object Room {
        const val RoomVersion = "2.5.2"

        const val RoomRuntime = "androidx.room:room-runtime:$RoomVersion"
        const val RoomKotlin = "androidx.room:room-ktx:$RoomVersion"
        const val RoomCompiler = "androidx.room:room-compiler:$RoomVersion"
    }

    object Koin {
        const val KoinVersion = "3.4.3"
        const val KoinComposeVersion = "3.4.6"

        const val KoinAndroid = "io.insert-koin:koin-android:$KoinVersion"
        const val KoinCore = "io.insert-koin:koin-core:$KoinVersion"
        const val KoinCompose = "io.insert-koin:koin-androidx-compose:$KoinComposeVersion"
    }

    object Ksp {
        const val KspVersion = "1.9.20-1.0.14"

        const val KspGradlePlugin = "com.google.devtools.ksp"
        const val Ksp = "com.google.devtools.ksp"
    }

    object Orbit {
        const val OrbitVersion = "6.0.0"

        const val OrbitAndroid = "org.orbit-mvi:orbit-viewmodel:$OrbitVersion"
        const val OrbitCompose = "org.orbit-mvi:orbit-compose:$OrbitVersion"
    }
}