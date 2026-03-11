package com.jaytux.grader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.jaytux.grader.maxN
import com.jaytux.grader.viewmodel.Grade
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import kotlin.time.toJavaInstant

@Composable
fun CancelSaveRow(canSave: Boolean, onCancel: () -> Unit, cancelText: String = "Cancel", saveText: String = "Save", onSave: () -> Unit) {
    Row {
        Button({ onCancel() }, Modifier.weight(0.45f)) { Text(cancelText) }
        Spacer(Modifier.weight(0.1f))
        Button({ onSave() }, Modifier.weight(0.45f), enabled = canSave) { Text(saveText) }
    }
}

@Composable
fun AddStringDialog(label: String, taken: List<String>, onClose: () -> Unit, current: String = "", onSave: (String) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(400.dp, 300.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            var name by remember(current) { mutableStateOf(current) }
            Column(Modifier.align(Alignment.Center)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().focusRequester(focus), label = { Text(label) }, isError = name in taken)
                CancelSaveRow(name.isNotBlank() && name !in taken, onClose) {
                    onSave(name)
                    onClose()
                }
            }
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }
}

@Composable
fun ConfirmDeleteDialog(
    deleteAWhat: String,
    onExit: () -> Unit,
    onDelete: () -> Unit,
    render: @Composable () -> Unit
) = DialogWindow(
    onCloseRequest = onExit,
    state = rememberDialogState(size = DpSize(400.dp, 300.dp), position = WindowPosition(Alignment.Center))
) {
    Surface(Modifier.width(400.dp).height(300.dp), tonalElevation = 5.dp) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column(Modifier.align(Alignment.Center)) {
                Text("You are about to delete $deleteAWhat.", Modifier.padding(10.dp))
                render()
                CancelSaveRow(true, onExit, "Cancel", "Delete") {
                    onDelete()
                    onExit()
                }
            }
        }
    }
}

@Composable
fun <T> ListOrEmpty(
    data: List<T>,
    onEmpty: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    item: @Composable LazyItemScope.(idx: Int, it: T) -> Unit
) {
    if(data.isEmpty()) {
        Box(modifier) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.align(Alignment.Center)) {
                    onEmpty()
                }
            }
        }
    }
    else {
        Column(modifier) {
            LazyColumn(Modifier.weight(1f)) {
                itemsIndexed(data) { idx, it ->
                    item(idx, it)
                }
            }
        }
    }
}

@Composable
fun FromTo(size: Dp) {
    var w by remember { mutableStateOf(0) }
    var h by remember { mutableStateOf(0) }
    Box(Modifier.width(size).height(size).onGloballyPositioned {
        w = it.size.width
        h = it.size.height
    }) {
        Box(Modifier.align(Alignment.BottomStart)) {
            Text("Evaluator", fontWeight = FontWeight.Bold)
        }

        Box {
            Text("Evaluated", Modifier.graphicsLayer {
                rotationZ = -90f
                translationX = w - 15f
                translationY = h - 15f
                transformOrigin = TransformOrigin(0f, 0.5f)
            }, fontWeight = FontWeight.Bold)
        }
    }
}

//@Composable
//fun PEGradeWidget(
//    grade: PeerEvaluationState.Student2StudentEntry?,
//    onSelect: () -> Unit, onDeselect: () -> Unit,
//    isSelected: Boolean,
//    modifier: Modifier = Modifier
//) = Box(modifier.padding(2.dp)) {
//    Selectable(isSelected, onSelect, onDeselect) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text(grade?.let { if(it.grade.isNotBlank()) it.grade else if(it.feedback.isNotBlank()) "(other)" else null } ?: "none")
//        }
//    }
//}

@Composable
fun VLine(width: Dp = 1.dp, color: Color = Color.Black) = Spacer(Modifier.fillMaxHeight().width(width).background(color))

@Composable
fun MeasuredLazyItemScope.HLine(height: Dp = 1.dp, color: Color = Color.Black) {
    val width by measuredWidth()
    Spacer(Modifier.width(width).height(height).background(color))
}

@Composable
fun RolePicker(used: List<String>, curr: String?, onClose: () -> Unit, onSave: (String?) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(400.dp, 500.dp), position = WindowPosition(Alignment.Center))
) {
    Surface(Modifier.fillMaxSize().padding(10.dp)) {
        Box(Modifier.fillMaxSize()) {
            var role by remember { mutableStateOf(curr ?: "") }
            Column {
                Text("Used roles:")
                LazyColumn(Modifier.weight(1.0f).padding(5.dp)) {
                    items(used) {
                        Surface(Modifier.fillMaxWidth().clickable { role = it }, tonalElevation = 5.dp) {
                            Text(it, Modifier.padding(5.dp))
                        }
                        Spacer(Modifier.height(5.dp))
                    }
                }
                OutlinedTextField(role, { role = it }, Modifier.fillMaxWidth())
                CancelSaveRow(true, onClose) {
                    onSave(role.ifBlank { null })
                    onClose()
                }
            }
        }
    }
}

@Composable
fun GradePicker(grade: Grade, modifier: Modifier = Modifier, key: Any = Unit, onUpdate: (Grade) -> Unit) = Row(modifier) { // TODO: fix UI to remove save-buttons (instead wait fo end of editing)
    Text("Grade: ", Modifier.align(Alignment.CenterVertically))

    when(grade) {
        is Grade.Categoric -> {
            if(grade.options.size <= 5) {
                Column {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        grade.options.forEachIndexed { idx, opt ->
                            println("Rendering opt ${opt.option} (index $idx) ~ current value: ${grade.value.option})")
                            SegmentedButton(
                                grade.value.option == opt.option, { onUpdate(Grade.Categoric(opt, grade.options, grade.grade)) },
                                shape = SegmentedButtonDefaults.itemShape(idx, grade.options.size)
                            ) { Text(opt.option.maxN(15)) }
                        }
                    }
                    Row {
                        Spacer(Modifier.weight(1f))
                        Text(grade.value.option, fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else {
                var slider by remember(grade, key) { mutableStateOf(maxOf(0, grade.options.indexOfFirst { it.option == grade.value.option }))  }
                Row {
                    Column(Modifier.weight(1f)) {
                        Slider(
                            slider.toFloat(),
                            onValueChange = { onUpdate(grade.copy(value = grade.options[slider])) },
                            steps = grade.options.size,
                            valueRange = 0f..(grade.options.size - 1).toFloat()
                        )
                        Row {
                            Spacer(Modifier.weight(1f))
                            Text(grade.options[slider].option, fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        is Grade.FreeText -> {
            var text by remember(grade, key) { mutableStateOf(grade.text) }

            OutlinedTextField(grade.text, { onUpdate(grade.copy(text = it)) }, Modifier.weight(1f), singleLine = true)
            Button({ onUpdate(Grade.FreeText(text)) }, enabled = text != grade.text) {
                Text("Save")
            }
        }
        is Grade.Numeric -> {
            var num by remember(grade, key) { mutableStateOf(grade.value.toString()) }

            OutlinedTextField(
                num, { num = it.filter { c -> c.isDigit() || c == '.' || c == ',' }.ifEmpty { "0" } },
                Modifier.weight(1f), singleLine = true, isError = (num.toDoubleOrNull() ?: 0.0) > grade.grade.max
            )
            Button({ onUpdate(Grade.Numeric(num.toDoubleOrNull() ?: 0.0, grade.grade)) }, enabled = (num.toDoubleOrNull() ?: 0.0) <= grade.grade.max) {
                Text("Save")
            }
        }
        is Grade.Percentage -> {
            var perc by remember(grade, key) { mutableStateOf(grade.percentage.toString()) }

            OutlinedTextField("$perc%", { perc = it.filter { c -> c.isDigit() || c == '.' || c == ',' }.ifEmpty { "0" } }, Modifier.weight(1f), singleLine = true)
            Button({ onUpdate(Grade.Percentage(perc.toDoubleOrNull() ?: 0.0)) }) {
                Text("Save")
            }
        }
    }
}