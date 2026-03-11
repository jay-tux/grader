package com.jaytux.grader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.jaytux.grader.data.v2.Edition
import com.jaytux.grader.data.v2.Student
import com.jaytux.grader.viewmodel.EditionVM

@Composable
fun StudentsView(vm: EditionVM) = Row(Modifier.fillMaxSize()) {
    val students by vm.studentList.entities
    val focus by vm.focusIndex

    Surface(Modifier.weight(0.25f).fillMaxHeight(), tonalElevation = 7.dp) {
        ListOrEmpty(students, { Text("No students yet.") }) { idx, it ->
            QuickStudent(idx, it, vm)
        }
    }

    Surface(Modifier.weight(0.75f).fillMaxHeight(), tonalElevation = 1.dp) {
        if(focus == -1) {
            Box(Modifier.weight(0.75f).fillMaxHeight()) {
                Text("Select a student to view details.", Modifier.align(Alignment.Center))
            }
        }
        else {
            val groups by vm.studentGroups.entities
            val grades by vm.studentGrades.entities

            Column(Modifier.weight(0.75f).padding(15.dp)) {
                Surface(Modifier.padding(10.dp).fillMaxWidth(), tonalElevation = 10.dp, shadowElevation = 2.dp, shape = MaterialTheme.shapes.medium) {
                    Column(Modifier.padding(10.dp)) {
                        Text(students[focus].name, style = MaterialTheme.typography.headlineSmall)
                        Row {
                            var editing by remember { mutableStateOf(false) }

                            Text("Contact: ", Modifier.align(Alignment.CenterVertically).padding(start = 15.dp))
                            if(!editing) {
                                if (students[focus].contact.isBlank()) Text(
                                    "No contact info.",
                                    Modifier.padding(start = 5.dp),
                                    fontStyle = FontStyle.Italic,
                                    color = LocalTextStyle.current.color.copy(alpha = 0.5f)
                                )
                                else Text(students[focus].contact, Modifier.padding(start = 5.dp))
                                Spacer(Modifier.width(5.dp))
                                Icon(Edit, "Edit contact info", Modifier.clickable { editing = true })
                            }
                            else {
                                var mod by remember(focus, students[focus].contact, students[focus].id.value) { mutableStateOf(students[focus].contact) }
                                OutlinedTextField(mod, { mod = it })
                                Spacer(Modifier.width(5.dp))
                                Icon(Check, "Confirm edit", Modifier.align(Alignment.CenterVertically).clickable {
                                    vm.modStudent(students[focus], null, mod, null)
                                    editing = false
                                })
                                Spacer(Modifier.width(5.dp))
                                Icon(Close, "Cancel edit", Modifier.align(Alignment.CenterVertically).clickable { editing = false })
                            }
                        }

                        Column {
                            Text("Groups:", style = MaterialTheme.typography.headlineSmall)
                            groups?.let { gList ->
                                if(gList.isEmpty()) null
                                else {
                                    FlowRow(Modifier.padding(start = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        gList.forEach { group ->
                                            Surface(tonalElevation = 15.dp, shadowElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                                                Box(Modifier.padding(5.dp).clickable { vm.focus(group.first) }) {
                                                    Text("${group.first.name} (${group.second ?: "no role"})", Modifier.padding(5.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            } ?: Text("Not a member of any group.", Modifier.padding(start = 15.dp), fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                        }
                    }
                }
                Row {
                    Column(Modifier.weight(0.33f)) {
                        var mod by remember(focus, students[focus].note, students[focus].id.value) { mutableStateOf(students[focus].note) }

                        Text("Internal Note:")
                        OutlinedTextField(
                            mod,
                            { mod = it },
                            singleLine = false,
                            minLines = 5,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if(mod != students[focus].note) {
                            Row {
                                Spacer(Modifier.weight(1f))
                                Button({ vm.modStudent(students[focus], null, null, mod) }) {
                                    Text("Update note")
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(0.66f)) {
                        Text("Grade Summary: ", style = MaterialTheme.typography.headlineSmall)
                        Surface(shape = MaterialTheme.shapes.medium, color = Color.White) {
                            LazyColumn {
                                item {
                                    Surface(tonalElevation = 15.dp) {
                                        Row(Modifier.padding(10.dp)) {
                                            Text("Assignment", Modifier.weight(0.66f))
                                            Text("Grade", Modifier.weight(0.33f))
                                        }
                                    }
                                }

                                items(grades ?: listOf()) {
                                    Column(Modifier.padding(10.dp)) {
                                        Row {
                                            Text(it.assignment.name, Modifier.weight(0.66f))
                                            it.grade?.render(Modifier.weight(0.33f))
                                                ?: Text("---", Modifier.weight(0.33f), color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                                        }

                                        it.asMember?.let { g ->
                                            Row(Modifier.padding(start = 10.dp)) {
                                                Text("As member of ${g.name}", fontStyle = FontStyle.Italic)
                                                if (it.overridden) Text(" (overridden)", fontStyle = FontStyle.Italic)
                                            }
                                        }
                                    }
                                }

                                if((grades ?: listOf()).isEmpty()) {
                                    item {
                                        Box(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                                            Text("No grades yet.", Modifier.align(Alignment.Center), fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
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
fun QuickStudent(idx: Int, student: Student, vm: EditionVM) {
    val focus by vm.focusIndex
    Surface(tonalElevation = if(focus == idx) 15.dp else 0.dp, shape = MaterialTheme.shapes.small) {
        Column(Modifier.fillMaxWidth().clickable { vm.focus(idx) }.padding(10.dp)) {
            Text(student.name, fontWeight = FontWeight.Bold)
            if(student.contact.isBlank())
                Text("No contact info.", fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
            else Text(student.contact)
        }
    }
}