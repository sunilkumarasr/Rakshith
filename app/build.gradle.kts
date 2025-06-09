plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.village.villagevegetables"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.village.villagevegetables"
        minSdk = 25
        targetSdk = 34
        versionCode = 4
        versionName = "1.0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    email: sunilkumar.ece016@gmail.com
//    key0
//    Pwd: Sunil@9900

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //firebase
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation (libs.firebase.auth.ktx)


    implementation("de.hdodenhof:circleimageview:3.1.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    //banners
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //lottie
    implementation ("com.airbnb.android:lottie:6.0.0")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //images
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.razorpay:checkout:1.6.41")


}