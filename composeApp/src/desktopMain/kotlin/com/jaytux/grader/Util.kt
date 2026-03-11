package com.jaytux.grader

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.jaytux.grader.data.Database
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime
import java.util.prefs.Preferences

fun String.maxN(n: Int): String {
    return if (this.length > n) {
        this.substring(0, n - 3) + "..."
    } else {
        this
    }
}

suspend fun RichTextState.toClipboard(clip: ClipboardManager) {
    clip.setText(AnnotatedString(this.toMarkdown()))
}

suspend fun RichTextState.loadClipboard(clip: ClipboardManager, scope: CoroutineScope) {
    scope.launch { setMarkdown(clip.getText()?.text ?: "") }
}

object Preferences {
    private val _p = Preferences.userNodeForPackage(this::class.java)

    operator fun get(key: String): String? = _p.get(key, null)
    operator fun set(key: String, value: String) {
        _p.put(key, value)
    }

    var exportPath
        get() = get("exportPath") ?: System.getProperty("user.home") + "/grader_export"
        set(value) { set("exportPath", value) }
}

infix fun <T1, T2, T3> Pair<T1, T2>.app(x: T3) = Triple(first, second, x)
