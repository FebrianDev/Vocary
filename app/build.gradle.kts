plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //  alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)

    id("kotlin-parcelize")
}

android {
    namespace = "com.febriandev.vocary"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.febriandev.vocary"
        minSdk = 26
        targetSdk = 35
        versionCode = 12
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            buildConfigField("Boolean", "DEBUG_MODE", "true")

            buildConfigField(
                "String",
                "BASE_URL_OPEN_AI",
                "\"https://febri-mat3z6f1-eastus2.cognitiveservices.azure.com/\""
            )

            buildConfigField(
                "String",
                "HEADER_TOKEN",
                "\"Bearer 8uFpTXInA8QqZC6wjXG3skbSD1Vnf5vhdZHe70svUhY8zD4mvjIwJQQJ99BEACHYHv6XJ3w3AAAAACOGTc95\""
            )

            buildConfigField(
                "String",
                "BASE_URL_DICTIONARY",
                "\"https://api.dictionaryapi.dev/\""
            )

            buildConfigField(
                "String",
                "BASE_URL_WORDS_API",
                "\"https://wordsapiv1.p.rapidapi.com/\""
            )

            buildConfigField(
                "String",
                "BASE_URL_TRANSLATE",
                "\"https://api.cognitive.microsofttranslator.com/\""
            )

            buildConfigField(
                "String",
                "API_TRANSLATE",
                "\"14MEi6cSKMwKlCDJifjNXPbhBebUIqo8JOV24Z7HoVDj1ngFAPemJQQJ99BFACqBBLyXJ3w3AAAbACOGOcQQ\""
            )

            buildConfigField(
                "String",
                "API_TEXT_TO_SPEECH",
                "\"EtayN2NikiVpU2zwy38KYFxlpRL5HRwofxAnK114eqtIAjP8J3jIJQQJ99BFACqBBLyXJ3w3AAAYACOGzZ9N\""
            )

            buildConfigField(
                "String",
                "API_WORDS_API",
                "\"357c17d8camsh689723147f1b925p127dc4jsnbbf4ae56f714\""
            )

            buildConfigField(
                "String",
                "API_ONESIGNAL",
                "\"os_v2_app_afapirsxwveqrijelnxvlycjtzhycsbxx3beyxufgml2fpjghldktyupims7q24etc3kfkxtx4nozfhhe5wd5dglbp7e7lgslz7tafi\""
            )

            buildConfigField(
                "String",
                "ONESIGNAL_APP_ID",
                "\"0140f446-57b5-4908-a124-5b6f55e0499e\""
            )

            buildConfigField(
                "String",
                "REVENUECAT_APP_ID",
                "\"goog_SDrghgQzcdXSqrZMAYWTJQzvGZn\""
            )

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = false
            buildConfigField("Boolean", "DEBUG_MODE", "false")

            buildConfigField(
                "String",
                "BASE_URL_OPEN_AI",
                "\"https://febri-mat3z6f1-eastus2.cognitiveservices.azure.com/\""
            )

            buildConfigField(
                "String",
                "HEADER_TOKEN",
                "\"Bearer 8uFpTXInA8QqZC6wjXG3skbSD1Vnf5vhdZHe70svUhY8zD4mvjIwJQQJ99BEACHYHv6XJ3w3AAAAACOGTc95\""
            )

            buildConfigField(
                "String",
                "BASE_URL_DICTIONARY",
                "\"https://api.dictionaryapi.dev/\""
            )

            buildConfigField(
                "String",
                "BASE_URL_WORDS_API",
                "\"https://wordsapiv1.p.rapidapi.com/\""
            )

            buildConfigField(
                "String",
                "HEADER_TOKEN",
                "\"Bearer 8uFpTXInA8QqZC6wjXG3skbSD1Vnf5vhdZHe70svUhY8zD4mvjIwJQQJ99BEACHYHv6XJ3w3AAAAACOGTc95\""
            )

            buildConfigField(
                "String",
                "BASE_URL_TRANSLATE",
                "\"https://api.cognitive.microsofttranslator.com/\""
            )

            buildConfigField(
                "String",
                "API_TRANSLATE",
                "\"14MEi6cSKMwKlCDJifjNXPbhBebUIqo8JOV24Z7HoVDj1ngFAPemJQQJ99BFACqBBLyXJ3w3AAAbACOGOcQQ\""
            )

            buildConfigField(
                "String",
                "API_TEXT_TO_SPEECH",
                "\"EtayN2NikiVpU2zwy38KYFxlpRL5HRwofxAnK114eqtIAjP8J3jIJQQJ99BFACqBBLyXJ3w3AAAYACOGzZ9N\""
            )

            buildConfigField(
                "String",
                "API_WORDS_API",
                "\"357c17d8camsh689723147f1b925p127dc4jsnbbf4ae56f714\""
            )

            buildConfigField(
                "String",
                "API_ONESIGNAL",
                "\"os_v2_app_afapirsxwveqrijelnxvlycjtzhycsbxx3beyxufgml2fpjghldktyupims7q24etc3kfkxtx4nozfhhe5wd5dglbp7e7lgslz7tafi\""
            )

            buildConfigField(
                "String",
                "ONESIGNAL_APP_ID",
                "\"0140f446-57b5-4908-a124-5b6f55e0499e\""
            )

            buildConfigField(
                "String",
                "REVENUECAT_APP_ID",
                "\"goog_SDrghgQzcdXSqrZMAYWTJQzvGZn\""
            )

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }

//        configurations.all {
//            resolutionStrategy {
//                force("com.google.guava:guava:31.1-android")
//            }
//        }
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
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        //extended-icons
        implementation(libs.androidx.material.icons.extended)
        implementation("com.composables:icons-lucide:1.0.0")

        //pager
        implementation(libs.accompanist.pager)

        //hilt
        implementation("com.google.dagger:hilt-android:2.48")
        ksp("com.google.dagger:hilt-android-compiler:2.48")
        implementation("androidx.hilt:hilt-work:1.0.0")
        implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
        ksp("androidx.hilt:hilt-compiler:1.0.0")

        //room
        implementation(libs.androidx.room.runtime)
        annotationProcessor(libs.androidx.room.compiler)
        ksp(libs.androidx.room.compiler)
        implementation(libs.androidx.room.ktx)
        implementation(libs.androidx.room.paging)

        //retrofit
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)

        //speech to text
        implementation("com.microsoft.cognitiveservices.speech:client-sdk:1.44.0")

        //work manager
        implementation("androidx.work:work-runtime-ktx:2.9.0")

        //auth
        implementation("com.google.android.gms:play-services-auth:21.4.0")

        //lottie
        implementation("com.airbnb.android:lottie-compose:6.1.0")

        //system ui
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

        //livedata
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
        implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

        //showcase
        implementation("io.github.jocoand:showcase-sequence:1.3.1")

        //lottie
        implementation("com.airbnb.android:lottie-compose:6.1.0")

        //revenue cat
        implementation("com.revenuecat.purchases:purchases:8.7.0")

        //one signal
        implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    }
}

dependencies {
    //firebase
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    //implementation(libs.googleid)
    implementation("com.google.guava:guava:32.1.3-android")
    implementation(libs.firebase.firestore.ktx)
}

