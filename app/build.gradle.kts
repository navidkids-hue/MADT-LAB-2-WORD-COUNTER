plugins {
    alias(libs.plugins.android.application)
    jacoco
}

android {
    namespace = "com.example.wordcounter"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.wordcounter"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
        debug {
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

// ─── JaCoCo coverage report task ──────────────────────────────────────────────
tasks.register<JacocoReport>("jacocoUnitTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    // Classes produced by the debug unit-test compilation
    val fileFilter = listOf(
        // Excluded classes
        "**/MainActivity*",
        "**/HistoryAdapter*",
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*"
    )

    val debugTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(debugTree)

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))

    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
            include("jacoco/testDebugUnitTest.exec")
        }
    )
}