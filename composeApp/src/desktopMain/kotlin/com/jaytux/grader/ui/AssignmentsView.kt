package com.jaytux.grader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.jaytux.grader.GroupGrading
import com.jaytux.grader.PeerEvalGrading
import com.jaytux.grader.SoloGrading
import com.jaytux.grader.data.v2.AssignmentType
import com.jaytux.grader.viewmodel.EditionVM
import com.jaytux.grader.viewmodel.Navigator
import com.jaytux.grader.viewmodel.UiGradeType
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlin.time.Instant

@Composable
fun AssignmentsView(vm: EditionVM, token: Navigator.NavToken) = Row(Modifier.fillMaxSize()) {
    val assignments by vm.assignmentList.entities
    val focus by vm.focusIndex
    val scope = rememberCoroutineScope()

    val descRtf = rememberRichTextState()
    val assignment = remember(assignments, focus) {
        assignments.getOrNull(focus)?.also {
            scope.launch { descRtf.setMarkdown(it.global.criterion.desc) }
        }
    }

    var updatingDeadline by remember { mutableStateOf(false) }
    var addingRubric by remember { mutableStateOf(false) }
    var editingRubric by remember { mutableStateOf(-1) }
    var updatingGrade by remember { mutableStateOf(false) }

    val navToGrading = lambda@{
        if(assignment == null) return@lambda
        when(assignment.assignment.type) {
            AssignmentType.GROUP -> token.navTo(GroupGrading(vm.course, vm.edition, assignment.assignment))
            AssignmentType.SOLO -> token.navTo(SoloGrading(vm.course, vm.edition, assignment.assignment))
            AssignmentType.PEER_EVALUATION -> token.navTo(PeerEvalGrading(vm.course, vm.edition, assignment.assignment))
        }
    }

    Surface(Modifier.weight(0.25f).fillMaxHeight(), tonalElevation = 7.dp) {
        ListOrEmpty(assignments, { Text("No groups yet.") }) { idx, it ->
            QuickAssignment(idx, it, vm)
        }
    }

    Surface(Modifier.weight(0.75f).fillMaxHeight(), tonalElevation = 1.dp) {
        if (assignment == null) {
            Box(Modifier.fillMaxSize()) {
                Text("Select an assignment to see details.", Modifier.padding(10.dp).align(Alignment.Center), fontStyle = FontStyle.Italic)
            }
        } else {
            Column(Modifier.padding(10.dp)) {
                Text(assignment.assignment.name, style = MaterialTheme.typography.headlineMedium)
                Text("Deadline: ${assignment.assignment.deadline.format(fmt)}", Modifier.padding(top = 5.dp).clickable { updatingDeadline = true }, fontStyle = FontStyle.Italic)
                Row {
                    Text("${assignment.assignment.type.display} using grading ", Modifier.align(Alignment.CenterVertically))
                    Surface(shape = MaterialTheme.shapes.small, tonalElevation = 10.dp) {
                        Box(Modifier.clickable { updatingGrade = true }.padding(3.dp)) {
                            Text(when(val t = assignment.global.gradeType){
                                is UiGradeType.Categoric -> t.grade.name
                                UiGradeType.FreeText -> "by free-form grades"
                                is UiGradeType.Numeric -> t.grade.name
                                UiGradeType.Percentage -> "by percentages"
                            })
                        }
                    }
                }
                Row {
                    Column(Modifier.weight(0.75f)) {
                        Row {
                            Text("Description:", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 10.dp).weight(1f))
                            Button({ vm.setDesc(assignment, descRtf.toMarkdown()) }) {
                                Text("Update")
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        RichTextField(descRtf)
                    }
                    Spacer(Modifier.width(10.dp))
                    Surface(Modifier.weight(0.25f), color = Color.White) {
                        Column(Modifier.padding(15.dp)) {
                            Row {
                                Text("Grading Rubrics", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall)
                                IconButton({ addingRubric = true }) {
                                    Icon(CirclePlus, "Add grading rubric")
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            LazyColumn(Modifier.weight(1f)) {
                                itemsIndexed(assignment.criteria) { idx, it ->
                                    Row(Modifier.padding(5.dp)) {
                                        Column(Modifier.weight(1f)) {
                                            Text(it.criterion.name)
                                            Text(it.criterion.desc, Modifier.padding(start = 10.dp), fontStyle = FontStyle.Italic)
                                        }
                                        IconButton({ editingRubric = idx }, Modifier.align(Alignment.Top)) {
                                            Icon(Edit, "Edit grading rubric")
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Button({ navToGrading() }, Modifier.fillMaxWidth()) {
                                Text("Go to grading")
                            }
                        }
                    }
                }
            }
        }
    }

    if(updatingDeadline) {
        if(assignment == null) updatingDeadline = false
        else {
            DeadlinePicker(assignment.assignment.deadline, { updatingDeadline = false }) {
                vm.modAssignment(assignment.assignment, null, it)
            }
        }
    }

    if(addingRubric) {
        if(assignment == null) addingRubric = false
        else {
            AddCriterionDialog(null, vm, assignment.criteria.map { it.criterion.name }, { addingRubric = false }) { name, desc, type ->
                vm.mkCriterion(assignment.assignment, name, desc, type)
            }
        }
    }

    if(editingRubric != -1) {
        if(assignment == null) editingRubric = -1
        else {
            AddCriterionDialog(assignment.criteria[editingRubric], vm, assignment.criteria.map { it.criterion.name }, { editingRubric = -1 }) { name, desc, type ->
                vm.modCriterion(assignment.criteria[editingRubric].criterion, name, desc, type)
            }
        }
    }

    if(updatingGrade) {
        if(assignment == null) updatingGrade = false
        else {
            SetGradingDialog(assignment.assignment.name, assignment.global.gradeType, vm, { updatingGrade = false }) { type ->
                vm.modCriterion(assignment.global.criterion, null, null, type)
            }
        }
    }
}

val fmt = LocalDateTime.Format {
    date(LocalDate.Format {
        day(); char(' '); monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
    })
    char(' ')
    time(LocalTime.Format {
        amPmHour(); char(':'); minute(); char(' '); amPmMarker("AM", "PM")
    })
}

@Composable
fun QuickAssignment(idx: Int, assignment: EditionVM.AssignmentData, vm: EditionVM) {
    val focus by vm.focusIndex
    Surface(tonalElevation = if(focus == idx) 15.dp else 0.dp, shape = MaterialTheme.shapes.small) {
        Column(Modifier.fillMaxWidth().clickable { vm.focus(idx) }.padding(10.dp)) {
            Text(assignment.assignment.name, fontWeight = FontWeight.Bold)
            Text("Deadline: ${assignment.assignment.deadline.format(fmt)}", Modifier.padding(start = 10.dp), fontStyle = FontStyle.Italic)
        }
    }
}

@Composable
fun AddAssignmentDialog(label: String, taken: List<String>, onClose: () -> Unit, current: String = "", onSave: (String, AssignmentType) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(750.dp, 300.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            var type by remember { mutableStateOf(AssignmentType.SOLO) }
            var name by remember(current) { mutableStateOf(current) }
            Column(Modifier.align(Alignment.Center)) {
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    AssignmentType.entries.forEachIndexed { idx, it ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(idx, AssignmentType.entries.size),
                            selected = type == it,
                            onClick = { type = it }
                        ) { Text(it.display) }
                    }
                }
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().focusRequester(focus), label = { Text(label) }, isError = name in taken)
                CancelSaveRow(name.isNotBlank() && name !in taken, onClose) {
                    onSave(name, type)
                    onClose()
                }
            }
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(deadline: LocalDateTime, onDismiss: () -> Unit, onSave: (LocalDateTime) -> Unit) {
    val state = rememberDatePickerState(deadline.date.toJavaLocalDate())
    val (h, m) = deadline.time.let { it.hour to it.minute }
    val time = rememberTimePickerState(h, m)

    val reconstruct = {
        val inst = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
        val date = inst.toLocalDateTime(TimeZone.currentSystemDefault())
        LocalDateTime(date.date, LocalTime(time.hour, time.minute))
    }

    Dialog(onDismiss, DialogProperties()) {
        Surface(tonalElevation = 5.dp, shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.padding(15.dp)) {
                DatePicker(state, Modifier.fillMaxWidth())
                TimeInput(time, Modifier.fillMaxWidth())
                Row {
                    Spacer(Modifier.weight(1f))
                    Button(onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(10.dp))
                    Button({ onSave(reconstruct()); onDismiss() }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AddCriterionDialog(current: EditionVM.CriterionData?, vm: EditionVM, taken: List<String>, onClose: () -> Unit, onSave: (name: String, desc: String, type: UiGradeType) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(750.dp, 600.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }
    var type by remember(current) { mutableStateOf(current?.gradeType ?: UiGradeType.FreeText) }
    var name by remember(current) { mutableStateOf(current?.criterion?.name ?: "") }
    var desc by remember(current) { mutableStateOf(current?.criterion?.desc ?: "") }
    val categories by vm.categoricGrades.entities
    val numeric by vm.numericGrades.entities

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column(Modifier.align(Alignment.Center)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().focusRequester(focus), label = { Text("Criterion Name") }, isError = name in taken, singleLine = true)
                OutlinedTextField(desc, { desc = it }, Modifier.fillMaxWidth(), label = { Text("Short Description") }, singleLine = true)
                Surface(shape = MaterialTheme.shapes.small, color = Color.White, modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                    Column {
                        GradeTypePicker(type, categories, numeric, { n, o -> vm.mkScale(n, o) }, { n, m -> vm.mkNumericScale(n, m) }, Modifier.weight(1f)) { type = it }

                        CancelSaveRow(name.isNotBlank() && (name !in taken || name == current?.criterion?.name), onClose) {
                            onSave(name, desc, type)
                            onClose()
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(current) { focus.requestFocus() }
}

@Composable
fun SetGradingDialog(name: String, current: UiGradeType, vm: EditionVM, onClose: () -> Unit, onSave: (type: UiGradeType) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(750.dp, 600.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }
    val categories by vm.categoricGrades.entities
    val numeric by vm.numericGrades.entities
    var type by remember(current) { mutableStateOf(current) }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column(Modifier.align(Alignment.Center)) {
                Text("Select a grading scale for $name", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 10.dp))
                Surface(shape = MaterialTheme.shapes.small, color = Color.White, modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                    Column {
                        GradeTypePicker(type, categories, numeric, { n, o -> vm.mkScale(n, o) }, { n, m -> vm.mkNumericScale(n, m) }, Modifier.weight(1f)) { type = it }

                        CancelSaveRow(true, onClose) {
                            onSave(type)
                            onClose()
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(current) { focus.requestFocus() }
}

@Composable
fun GradeTypePicker(
    type: UiGradeType, categories: List<UiGradeType.Categoric>, numeric: List<UiGradeType.Numeric>,
    mkCat: (String, List<String>) -> Unit, mkNum: (String, Double) -> Unit,
    modifier: Modifier = Modifier,
    onUpdate: (UiGradeType) -> Unit
) = Column(modifier) {
    var selectedCategory by remember(categories) {
        mutableStateOf(
            if(type is UiGradeType.Categoric) categories.indexOfFirst { it.grade.id == type.grade.id }
            else -1
        )
    }
    var selectedNumeric by remember(numeric) {
        mutableStateOf(
            if(type is UiGradeType.Numeric) numeric.indexOfFirst { it.grade.id == type.grade.id }
            else -1
        )
    }
    var adding by remember { mutableStateOf(false) }

    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        SegmentedButton(
            type is UiGradeType.FreeText, onClick = { onUpdate(UiGradeType.FreeText) },
            shape = SegmentedButtonDefaults.itemShape(0, 4)
        ) {
            Text("Free-form grade")
        }
        SegmentedButton(
            type is UiGradeType.Percentage, onClick = { onUpdate(UiGradeType.Percentage) },
            shape = SegmentedButtonDefaults.itemShape(1, 4)
        ) {
            Text("Percentage")
        }
        SegmentedButton(
            type is UiGradeType.Categoric, onClick = { onUpdate(categories[maxOf(selectedCategory, 0)]) },
            shape = SegmentedButtonDefaults.itemShape(2, 4)
        ) {
            Text("Grading System")
        }
        SegmentedButton(
            type is UiGradeType.Numeric, onClick = { onUpdate(numeric[maxOf(selectedNumeric, 0)]) },
            shape = SegmentedButtonDefaults.itemShape(3, 4)
        ) {
            Text("Numeric Grade")
        }
    }
    (type as? UiGradeType.Categoric)?.let {
        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(categories) { idx, it ->
                Surface(
                    tonalElevation = if (selectedCategory == idx) 15.dp else 0.dp,
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(Modifier.fillMaxWidth().clickable { selectedCategory = idx; onUpdate(it) }.padding(10.dp)) {
                        Text(it.grade.name, fontWeight = FontWeight.Bold)
                        Text(
                            "(${it.options.size} options)",
                            Modifier.padding(start = 10.dp),
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            item {
                Button({ adding = true }, Modifier.fillMaxWidth()) {
                    Text("Add grading system")
                }
            }
        }
    } ?: (type as? UiGradeType.Numeric)?.let {
        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(numeric) { idx, it ->
                Surface(
                    tonalElevation = if (selectedNumeric == idx) 15.dp else 0.dp,
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(Modifier.fillMaxWidth().clickable { selectedNumeric = idx; onUpdate(it) }.padding(10.dp)) {
                        Text(it.grade.name, fontWeight = FontWeight.Bold)
                        Text(
                            "(graded as X/${it.grade.max})",
                            Modifier.padding(start = 10.dp),
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            item {
                Button({ adding = true }, Modifier.fillMaxWidth()) {
                    Text("Add numeric system")
                }
            }
        }
    } ?: Spacer(Modifier.weight(1f))

    if(adding) {
        when(type) {
            is UiGradeType.Categoric -> AddCatScaleDialog(categories.map { it.grade.name }, { adding = false }) { name, options ->
                mkCat(name, options)
            }
            is UiGradeType.Numeric -> AddNumScaleDialog(numeric.map { it.grade.name }, { adding = false }) { name, max ->
                mkNum(name, max)
            }
            else -> adding = false
        }
    }
}

@Composable
fun AddCatScaleDialog(taken: List<String>, onClose: () -> Unit, onSave: (String, List<String>) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(750.dp, 600.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }
    var name by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf<String>()) }
    var adding by remember { mutableStateOf("") }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column(Modifier.align(Alignment.Center)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().focusRequester(focus), label = { Text("Grading system name") }, isError = name in taken, singleLine = true)
                Text("Grade options:", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 10.dp))
                LazyColumn(Modifier.weight(1f)) {
                    itemsIndexed(options) { idx, it ->
                        Row(Modifier.fillMaxWidth().padding(5.dp)) {
                            Text(it, Modifier.weight(1f))
                            IconButton({ options = options.filterNot { o -> o == it } }) {
                                Icon(Delete, "Delete grading option")
                            }
                        }
                    }
                    item {
                        Row {
                            OutlinedTextField(adding, { adding = it }, Modifier.weight(1f).align(Alignment.CenterVertically).padding(5.dp), label = { Text("New option") }, isError = adding in options, singleLine = true)
                            Button({ options = options + adding; adding = "" }, Modifier.align(Alignment.CenterVertically).padding(5.dp), enabled = adding.isNotBlank() && adding !in options) {
                                Text("Add")
                            }
                        }
                    }
                }
                CancelSaveRow(name.isNotBlank() && name !in taken, onClose) {
                    onSave(name, options)
                    onClose()
                }
            }
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }
}

@Composable
fun AddNumScaleDialog(taken: List<String>, onClose: () -> Unit, onSave: (String, Double) -> Unit) = DialogWindow(
    onCloseRequest = onClose,
    state = rememberDialogState(size = DpSize(750.dp, 300.dp), position = WindowPosition(Alignment.Center))
) {
    val focus = remember { FocusRequester() }
    var name by remember { mutableStateOf("") }
    var maxStr by remember { mutableStateOf("0") }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column(Modifier.align(Alignment.Center)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().focusRequester(focus), label = { Text("Grading system name") }, isError = name in taken, singleLine = true)
                OutlinedTextField(maxStr, { maxStr = it.toDoubleOrNull()?.let { _ -> it } ?: "0" }, Modifier.fillMaxWidth(), label = { Text("Maximum grade") }, singleLine = true)

                CancelSaveRow(name.isNotBlank() && name !in taken && (maxStr.toDoubleOrNull() ?: 0.0) > 0.0, onClose) {
                    onSave(name, maxStr.toDoubleOrNull()!!)
                    onClose()
                }
            }
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }
}