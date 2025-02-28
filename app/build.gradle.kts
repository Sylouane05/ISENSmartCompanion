plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.com.google.devtools.ksp.gradle.plugin)
    alias(libs.plugins.com.google.android.libraries.mapsplatform.secrets.gradle.plugin.gradle.plugin) // ✅ Version correcte
}

android {
    namespace = "fr.isen.moussahmboumbe.isensmartcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.isen.moussahmboumbe.isensmartcompanion"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Retrofit pour les requêtes API
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // ✅ Google AI Client SDK (Gemini)
    implementation(libs.generativeai)

    // Ajout de Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.core.ktx.v1101)
    implementation(libs.androidx.datastore.preferences)

    // Coroutine pour exécuter Room en arrière-plan
    implementation(libs.kotlinx.coroutines.android)

    // 🔴 SUPPRESSION des dépendances Firebase Vertex AI (elles ne sont PAS nécessaires)

    // Test et Debug
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// 🔴 SUPPRESSION du plugin Firebase Google Services (inutile ici)
