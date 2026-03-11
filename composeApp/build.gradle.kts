import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    jvm("desktop") {}
    
    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.material3.core)
            implementation(libs.sl4j)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.exposed.core)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.migration)
            implementation(libs.exposed.migration.jdbc)
            implementation(libs.exposed.kotlin.datetime)
            implementation(libs.sqlite)
            implementation(libs.material3.desktop)
            implementation(libs.rtfield)
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.coil)
            implementation(libs.directories)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.compose.backhandler)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.jaytux.grader.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.jaytux.grader"
            mainClass = "com.jaytux.grader.MainKt"
            packageVersion = "1.0.0"
            includeAllModules = true

            linux {
                modules("jdk.security.auth")
            }
        }
    }
}
