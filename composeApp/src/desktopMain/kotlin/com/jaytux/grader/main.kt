package com.jaytux.grader

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jaytux.grader.App
import com.jaytux.grader.data.Database
import io.github.vinceglb.filekit.FileKit

fun main(){
    Database.init()
    application {
        FileKit.init(appId = "com.jaytux.grader")

        Window(
            onCloseRequest = ::exitApplication,
            title = "Grader",
        ) {
            App()
        }
    }
}