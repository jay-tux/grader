package com.jaytux.grader.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.EditionDetail
import com.jaytux.grader.data.v2.BaseAssignment
import com.jaytux.grader.data.v2.Student
import com.jaytux.grader.viewmodel.EditionVM
import com.jaytux.grader.viewmodel.Navigator

@Composable
fun EditionTitle(data: EditionDetail) = Text("Courses / ${data.course.name} / ${data.ed.name}")

@Composable
fun EditionView(data: EditionDetail, token: Navigator.NavToken) {
    val vm = viewModel<EditionVM>(key = data.ed.id.toString()) { EditionVM(data.ed, data.course) }
    val tab by vm.selectedTab
    var adding by remember { mutableStateOf(false) }

    val groups by vm.groupList.entities
    val assignments by vm.assignmentList.entities


    Column(Modifier.padding(10.dp)) {
        Row {
            Text("${vm.course.name} - ${vm.edition.name}", Modifier.weight(1f), style = MaterialTheme.typography.headlineMedium)
            Button({ adding = true }) {
                Icon(CirclePlus, "Add ${tab.addText}")
                Spacer(Modifier.width(5.dp))
                Text("Add ${tab.addText}")
            }
        }
        Spacer(Modifier.height(5.dp))
        PrimaryScrollableTabRow(tab.ordinal, edgePadding = 10.dp) {
            EditionVM.Tab.entries.forEach {
                Tab(tab == it, onClick = { vm.switchTo(it) }, modifier = Modifier.padding(horizontal = 5.dp)) { it.renderTab() }
            }
        }
        Box(Modifier.weight(1f)) {
            when (tab) {
                EditionVM.Tab.STUDENTS -> StudentsView(vm)
                EditionVM.Tab.GROUPS -> GroupsView(vm)
                EditionVM.Tab.ASSIGNMENTS -> AssignmentsView(vm, token)
            }
        }
    }

    if(adding) {
        when(tab) {
            EditionVM.Tab.STUDENTS ->
                AddStringDialog("Student Name", listOf(), { adding = false }, "") { vm.mkStudent(it, "", "") }
            EditionVM.Tab.GROUPS ->
                AddStringDialog("Group Name", groups.map { it.group.name }, { adding = false }, "") { vm.mkGroup(it) }
            EditionVM.Tab.ASSIGNMENTS ->
                AddAssignmentDialog("Assignment Name", assignments.map { it.assignment.name }, { adding = false }, "") { name, type -> vm.mkAssignment(name, type) }
        }
    }
}

@Composable
fun StudentsTabHeader() = Row(Modifier.padding(all = 5.dp)) {
    Icon(UserIcon, "Students")
    Spacer(Modifier.width(5.dp))
    Text("Students")
}

@Composable
fun GroupsTabHeader() = Row(Modifier.padding(all = 5.dp)) {
    Icon(UserGroupIcon, "Groups")
    Spacer(Modifier.width(5.dp))
    Text("Groups")
}

@Composable
fun AssignmentsTabHeader() = Row(Modifier.padding(all = 5.dp)) {
    Icon(AssignmentIcon, "Assignments")
    Spacer(Modifier.width(5.dp))
    Text("Assignments")
}
