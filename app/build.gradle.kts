import java.util.Properties
import java.io.StringReader

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val mapsApiKey = providers.fileContents(
    rootProject.layout.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["MAPS_API_KEY"]?.toString() ?: ""
}.getOrElse("")

android {
    namespace = "com.loom.app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.loom.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.ext.work)
    implementation(libs.coil.kt)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.hilt.ext.compiler)
    implementation(libs.hilt.core)
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":sync:work"))
    implementation(project(":feature:explore:api"))
    implementation(project(":feature:explore:impl"))
    implementation(project(":feature:auth:api"))
    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:home:impl"))
    implementation(project(":feature:post-editor:api"))
    implementation(project(":feature:post-editor:impl"))
    implementation(project(":feature:profile:api"))
    implementation(project(":feature:profile:impl"))
    implementation(project(":feature:settings:api"))
    implementation(project(":feature:settings:impl"))
    implementation(project(":feature:comments:api"))
    implementation(project(":feature:comments:impl"))
    implementation(project(":feature:events:api"))
    implementation(project(":feature:events:impl"))
    implementation(project(":feature:event-detail:api"))
    implementation(project(":feature:event-detail:impl"))
    implementation(project(":feature:event-editor:api"))
    implementation(project(":feature:event-editor:impl"))
    implementation(project(":feature:notifications:api"))
    implementation(project(":feature:notifications:impl"))

}
