package com.jaytux.grader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.GroupGrading
import com.jaytux.grader.app
import com.jaytux.grader.data.v2.CategoricGrade
import com.jaytux.grader.data.v2.Criterion
import com.jaytux.grader.data.v2.GradeType
import com.jaytux.grader.data.v2.Group
import com.jaytux.grader.data.v2.Student
import com.jaytux.grader.viewmodel.Grade
import com.jaytux.grader.viewmodel.GroupsGradingVM
import com.jaytux.grader.viewmodel.Navigator
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

@Composable
fun GroupsGradingTitle(data: GroupGrading) = Text("Courses / ${data.course.name} / ${data.edition.name} / Group Assignments / ${data.assignment.name} / Grading")

@Composable
fun GroupsGradingView(data: GroupGrading, token: Navigator.NavToken) {
    val vm = viewModel<GroupsGradingVM>(key = data.assignment.id.toString()) {
        GroupsGradingVM(data.course, data.edition, data.assignment)
    }
    val groups by vm.groupList.entities
    val focus by vm.focus

    val selectedGroup = remember(focus, groups) { groups.getOrNull(focus) }

    Column(Modifier.padding(10.dp)) {
        Text("Grading ${vm.base.name}", Modifier.weight(1f), style = MaterialTheme.typography.headlineMedium)
        Text("Group assignment in ${vm.course.name} - ${vm.edition.name}")
        Spacer(Modifier.height(5.dp))
        Row(Modifier.fillMaxSize()) {
            Surface(Modifier.weight(0.25f).fillMaxHeight(), tonalElevation = 7.dp) {
                ListOrEmpty(groups, { Text("No groups yet.") }) { idx, it ->
                    QuickAGroup(idx == focus, { vm.focusGroup(idx) }, it)
                }
            }

            Surface(Modifier.weight(0.75f).fillMaxHeight(), tonalElevation = 1.dp) {
                if (focus == -1 || selectedGroup == null) {
                    Box(Modifier.weight(0.75f).fillMaxHeight()) {
                        Text("Select a group to start grading.", Modifier.align(Alignment.Center))
                    }
                } else {
                    Column(Modifier.weight(0.75f).padding(15.dp)) {
                        Row {
                            IconButton({ vm.focusPrev() }, Modifier.align(Alignment.CenterVertically), enabled = focus > 0) {
                                Icon(DoubleBack, "Previous group")
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(selectedGroup.group.name, Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.weight(1f))
                            IconButton({ vm.focusNext() }, Modifier.align(Alignment.CenterVertically), enabled = focus < groups.size - 1) {
                                Icon(DoubleForward, "Next group")
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        val global by vm.globalGrade.entity
                        val byCriteria by vm.gradeList.entities

                        Surface(Modifier.fillMaxSize(), color = Color.White, shape = MaterialTheme.shapes.medium) {
                            LazyColumn {
                                items(byCriteria ?: listOf()) { (crit, fdbk) ->
                                    var isOpen by remember(selectedGroup) { mutableStateOf(false) }
                                    Column(Modifier.padding(5.dp)) {
                                        GFWidget(crit, selectedGroup.group, fdbk, vm, global to byCriteria, isOpen) { isOpen = !isOpen }
                                        Spacer(Modifier.height(5.dp))
                                    }
                                }
                                global?.let { fdbk ->
                                    item {
                                        Box(Modifier.padding(5.dp)) {
                                            GFWidget(
                                                vm.global, selectedGroup.group, fdbk, vm, global to byCriteria, true,
                                                (byCriteria ?: listOf()).flatMap { (_, it) ->
                                                    it.overrides.mapNotNull { o ->
                                                        o.second?.let { _ -> o.first.id.value }
                                                    }
                                                }.toSet()
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAGroup(isFocus: Boolean, onFocus: () -> Unit, group: GroupsGradingVM.GroupData) {
    Surface(tonalElevation = if(isFocus) 15.dp else 0.dp, shape = MaterialTheme.shapes.small) {
        Column(Modifier.fillMaxWidth().clickable { onFocus() }.padding(10.dp)) {
            Text(group.group.name, fontWeight = FontWeight.Bold)
            Text("${group.students.size} student(s)", Modifier.padding(start = 10.dp), fontStyle = FontStyle.Italic)
        }
    }
}

private fun gradeState(crit: GroupsGradingVM.CritData, current: Grade?): Grade = transaction {
    if(current == null) Grade.default(crit.criterion.gradeType, crit.cat, crit.num)
    when(crit.criterion.gradeType) {
        GradeType.CATEGORIC ->
            if(current is Grade.Categoric && current.grade.id == crit.criterion.categoricGrade?.id) current
            else Grade.default(GradeType.CATEGORIC, crit.cat, crit.num)
        GradeType.NUMERIC ->
            if(current is Grade.Numeric && current.grade.id == crit.criterion.numericGrade?.id) current
            else Grade.default(GradeType.NUMERIC, crit.cat, crit.num)
        GradeType.PERCENTAGE ->
            current as? Grade.Percentage ?: Grade.default(GradeType.PERCENTAGE, crit.cat, crit.num)
        GradeType.NONE ->
            current as? Grade.FreeText ?: Grade.default(GradeType.NONE, crit.cat, crit.num)
    }
}

@Composable
fun GFWidget(crit: GroupsGradingVM.CritData, gr: Group, feedback: GroupsGradingVM.FeedbackData, vm: GroupsGradingVM, key: Any, isOpen: Boolean, markOverridden: Set<UUID> = setOf(), onToggle: () -> Unit) = Surface(
    Modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    shadowElevation = 3.dp
) {
    Column {
        Surface(tonalElevation = 5.dp) {
            Row(Modifier.fillMaxWidth().clickable { onToggle() }.padding(10.dp)) {
                Icon(if(isOpen) ChevronDown else ChevronRight, "Toggle criterion detail grading", Modifier.align(Alignment.CenterVertically))
                Spacer(Modifier.width(5.dp))
                Text(crit.criterion.name, Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.width(5.dp))
                feedback.groupLevel?.grade?.let {
                    Row(Modifier.align(Alignment.Bottom)) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text("(Grade: ")
                            it.render()
                            Text(")")
                        }
                    }
                }
            }
        }

        if(isOpen) {
            Row(Modifier.padding(10.dp)) {
                var grade by remember(key, feedback) { mutableStateOf(gradeState(crit, feedback.groupLevel?.grade)) }
                var text by remember(key, feedback) { mutableStateOf(feedback.groupLevel?.feedback ?: "") }
                Column(Modifier.weight(0.5f).height(IntrinsicSize.Min)) {

                    GradePicker(grade, key = crit to gr) { grade = it }
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(text, { text = it }, label = { Text("Feedback") }, singleLine = false, minLines = 5, modifier = Modifier.fillMaxWidth().weight(1f))
                    Spacer(Modifier.height(5.dp))
                    Button({ vm.modGroupFeedback(crit.criterion, gr, grade, text) }) {
                        Text("Save grade and feedback")
                    }
                }

                feedback.groupLevel?.let { groupLevel ->
                    Spacer(Modifier.width(10.dp))

                    Surface(Modifier.weight(0.5f).height(IntrinsicSize.Min), tonalElevation = 10.dp, shape = MaterialTheme.shapes.small) {
                        Column(Modifier.padding(10.dp)) {
                            Text("Individual overrides", style = MaterialTheme.typography.bodyLarge)
                            feedback.overrides.forEach { (student, it) ->
                                var enable by remember(key, it) { mutableStateOf(false) }
                                var maybeRemoving by remember(key, it) { mutableStateOf(false) }
                                var sGrade by remember(key, it) { mutableStateOf(gradeState(crit, it?.grade ?: grade)) }
                                var sText by remember(key, it) { mutableStateOf(it?.feedback ?: "") }

                                Column {
                                    Row {
                                        Checkbox(enable, { if(it) { enable = true } else { maybeRemoving = true } })
                                        Spacer(Modifier.width(5.dp))
                                        Text(student.name, Modifier.align(Alignment.CenterVertically))
                                        if(student.id.value in markOverridden) {
                                            Spacer(Modifier.width(5.dp))
                                            Text("(Overridden)", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = Color.Red)
                                        }
                                    }

                                    if(enable) Row {
                                        Spacer(Modifier.width(15.dp))
                                        Surface(color = Color.White, shape = MaterialTheme.shapes.small) {
                                            Column(Modifier.padding(10.dp)) {
                                                Spacer(Modifier.height(5.dp))
                                                GradePicker(sGrade, key = crit to gr app student) { sGrade = it }
                                                Spacer(Modifier.height(5.dp))
                                                OutlinedTextField(sText, { sText = it }, label = { Text("Feedback") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                                                Spacer(Modifier.height(5.dp))
                                                Button({ vm.modOverrideFeedback(crit.criterion, gr, student, groupLevel, sGrade, sText) }) {
                                                    Text("Save override")
                                                }
                                            }
                                        }
                                    }
                                }

                                if(maybeRemoving) {
                                    ConfirmDeleteDialog("the individual grade for ${student.name}", { maybeRemoving = false }, {
                                        maybeRemoving = false
                                        enable = false
                                        vm.rmOverrideFeedback(crit.criterion, gr, student)
                                    }) {
                                        Column {
                                            Row {
                                                Text("Grade:")
                                                sGrade.render()
                                            }
                                            Row {
                                                Text("Feedback:")
                                                if(sText.isBlank()) Text("No feedback", fontStyle = FontStyle.Italic)
                                                else Text(sText)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}