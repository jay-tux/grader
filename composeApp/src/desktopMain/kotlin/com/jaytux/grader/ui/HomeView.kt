package com.jaytux.grader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.EditionDetail
import com.jaytux.grader.data.v2.Edition
import com.jaytux.grader.viewmodel.HomeVM
import com.jaytux.grader.viewmodel.Navigator

@Composable
fun HomeTitle() = Text("Grader")

@Composable
fun HomeView(token: Navigator.NavToken) {
    val vm = viewModel<HomeVM>()
    val courses by vm.courses.entities
    var addingCourse by remember { mutableStateOf(false) }

    LazyColumn(Modifier.padding(15.dp)) {
        item {
            Row {
                Text("Courses Overview", Modifier.weight(0.8f), style = MaterialTheme.typography.headlineMedium)
                Button({ addingCourse = true }) {
                    Icon(CirclePlus, "Add course")
                    Spacer(Modifier.width(5.dp))
                    Text("Add course")
                }
            }
        }

        items(courses) {
            CourseCard(it, vm) { e -> token.navTo(EditionDetail(e, it.course)) }
        }
    }

    if(addingCourse) {
        AddStringDialog("Course Name", courses.map { it.course.name }, { addingCourse = false }, "") {
            vm.mkCourse(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(course: HomeVM.CourseData, vm: HomeVM, onOpenEdition: (Edition) -> Unit) {
    var addingEdition by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 2.dp, shadowElevation = 5.dp, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        Column(Modifier.padding(8.dp)) {
            Row {
                Text(course.course.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                IconButton({ deleting = true }) { Icon(Delete, "Delete course") }
            }

            Row {
                Text("Editions", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                Button({ addingEdition = true }) {
                    Icon(CirclePlus, "Add edition")
                    Spacer(Modifier.width(5.dp))
                    Text("Add edition")
                }
            }

            FlowRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                course.editions.forEach { EditionCard(course.course.name, it, vm, onOpenEdition) }
            }

            if(course.archived.isNotEmpty()) {
                Text("Archived editions", style = MaterialTheme.typography.headlineSmall)
                FlowRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                    course.archived.forEach { EditionCard(course.course.name, it, vm, onOpenEdition) }
                }
            }
        }
    }

    if(addingEdition) {
        AddStringDialog("Edition Name (in ${course.course.name})", course.editions.map { it.edition.name }, { addingEdition = false }, "") {
            vm.mkEdition(course.course, it)
        }
    }

    if(deleting) {
        ConfirmDeleteDialog("a course", { deleting = false }, { vm.rmCourse(course.course) }) {
            Text(course.course.name)
        }
    }
}

@Composable
fun EditionCard(courseName: String, edition: HomeVM.EditionData, vm: HomeVM, onOpen: (Edition) -> Unit) {
    val type = if(edition.edition.archived) "Archived" else "Active"
    var deleting by remember { mutableStateOf(false) }

    Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 2.dp, shadowElevation = 5.dp, modifier = Modifier.padding(10.dp).clickable { onOpen(edition.edition) }) {
        Column(Modifier.padding(10.dp).width(IntrinsicSize.Min)) {
            Column(Modifier.width(IntrinsicSize.Max)) {
                Text(edition.edition.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    "$type\n${edition.students.size} student(s) • ${edition.groups.size} group(s) • ${edition.assignments.size} assignment(s)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(5.dp))
            Row {
                if(edition.edition.archived) {
                    Button({ vm.unarchiveEdition(edition.edition) }, Modifier.weight(0.5f)) {
                        Icon(Unarchive, "Unarchive edition")
                        Spacer(Modifier.width(5.dp))
                        Text("Unarchive edition")
                    }
                }
                else {
                    Button({ vm.archiveEdition(edition.edition) }, Modifier.weight(0.5f)) {
                        Icon(Archive, "Archive edition")
                        Spacer(Modifier.width(5.dp))
                        Text("Archive edition")
                    }
                }
                Spacer(Modifier.width(10.dp))
                Button({ deleting = true }, Modifier.weight(0.5f)) {
                    Icon(Delete, "Archive edition")
                    Spacer(Modifier.width(5.dp))
                    Text("Delete edition")
                }
            }
        }
    }

    if(deleting) {
        ConfirmDeleteDialog("an edition", { deleting = false }, { vm.rmEdition(edition.edition) }) {
            Column {
                Text(edition.edition.name, Modifier.align(Alignment.CenterHorizontally))
                Text("Edition in course $courseName", Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
