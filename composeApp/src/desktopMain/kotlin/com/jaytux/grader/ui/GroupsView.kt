package com.jaytux.grader.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jaytux.grader.data.v2.Group
import com.jaytux.grader.data.v2.Student
import com.jaytux.grader.viewmodel.EditionVM
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.util.UUID

@Composable
fun GroupsView(vm: EditionVM) = Row(Modifier.fillMaxSize()) {
    val groups by vm.groupList.entities
    val focus by vm.focusIndex
    var swappingRole by remember { mutableStateOf(-1) }

    val group = remember(groups, focus) { if(focus != -1) groups[focus] else null }
    val grades by vm.groupGrades.entities

    Surface(Modifier.weight(0.25f).fillMaxHeight(), tonalElevation = 7.dp) {
        ListOrEmpty(groups, { Text("No groups yet.") }) { idx, it ->
            QuickGroup(idx, it, vm)
        }
    }

    Surface(Modifier.weight(0.75f).fillMaxHeight(), tonalElevation = 1.dp) {
        if(group == null) {
            Box(Modifier.weight(0.75f).fillMaxHeight()) {
                Text("Select a group to view details.", Modifier.align(Alignment.Center))
            }
        }
        else {
            Column(Modifier.padding(10.dp)) {
                Text(group.group.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(5.dp))
                Row(Modifier.padding(5.dp)) {
                    var showTargetBorder by remember { mutableStateOf(false) }
                    val ddTarget = remember {
                        DDTarget({ showTargetBorder = true }, { showTargetBorder = false }, { DDTarget.extractStudent(it) }) {
                            vm.addStudentToGroup(it, group.group, null)
                        }
                    }

                    Column(Modifier.weight(0.75f)) {
                        Surface(
                            Modifier.weight(0.5f).then(if(showTargetBorder) Modifier.border(BorderStroke(3.dp, Color.Black)) else Modifier)
                                .dragAndDropTarget({ true }, target = ddTarget),
                            shape = MaterialTheme.shapes.medium, color = Color.White, shadowElevation = 1.dp) {
                            LazyColumn {
                                item {
                                    Surface(tonalElevation = 15.dp) {
                                        Row(Modifier.fillMaxWidth().padding(10.dp)) {
                                            Text("Members", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(10.dp))
                                        }
                                    }
                                }

                                itemsIndexed(group.members) { idx, (student, role) ->
                                    Row(Modifier.clickable { vm.focus(student) }.padding(10.dp)) {
                                        Column(Modifier.weight(1f)) {
                                            Text(student.name, fontWeight = FontWeight.Bold)
                                            if(student.contact.isEmpty())
                                                Text("No contact info.", fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                                            else Text(student.contact)
                                        }
                                        if(role != null) {
                                            Surface(Modifier.align(Alignment.CenterVertically), tonalElevation = 5.dp, shape = MaterialTheme.shapes.small) {
                                                Box(Modifier.clickable { swappingRole = -1 }.clickable { swappingRole = idx }) {
                                                    Text(role, Modifier.padding(horizontal = 5.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium)
                                                }
                                            }
                                        }
                                        else {
                                            Text("No role", Modifier.align(Alignment.CenterVertically).clickable { swappingRole = idx }, fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                                        }
                                        IconButton({ vm.rmStudentFromGroup(student, group.group) }, Modifier.align(Alignment.CenterVertically)) {
                                            Icon(PersonMinus, "Remove ${student.name} from group")
                                        }
                                    }
                                }

                                if(group.members.isEmpty()) {
                                    item {
                                        Box(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                                            Text("No members yet.", Modifier.align(Alignment.Center), fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Column(Modifier.weight(0.5f)) {
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
                                            Text(it.first.name, Modifier.weight(0.66f))
                                            it.second?.render(Modifier.weight(0.33f))
                                                ?: Text("---", Modifier.weight(0.33f), color = LocalTextStyle.current.color.copy(alpha = 0.5f))
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

                    Spacer(Modifier.width(10.dp))

                    val available by vm.groupAvailableStudents.entities
                    Surface(Modifier.weight(0.25f), shape = MaterialTheme.shapes.medium, color = Color.White, shadowElevation = 1.dp) {
                        LazyColumn {
                            item {
                                Surface(tonalElevation = 15.dp) {
                                    Row(Modifier.fillMaxWidth().padding(10.dp)) {
                                        Text("Available Students", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(10.dp))
                                    }
                                }
                            }

                            items(available ?: listOf()) { student ->
                                AvailableStudent(student, group.group, vm)
                            }

                            if((available ?: listOf()).isEmpty()) {
                                item {
                                    Box(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                                        Text("No available students.", Modifier.align(Alignment.Center), fontStyle = FontStyle.Italic, color = LocalTextStyle.current.color.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if(swappingRole != -1) {
        if(group != null) {
            val roles by vm.usedRoles.entities
            RolePicker(roles, group.members[swappingRole].second, { swappingRole = -1 }) { upd ->
                vm.setStudentRole(group.members[swappingRole].first, group.group, upd)
            }
        }
        else {
            swappingRole = -1
        }
    }
}

private class DDTarget<T>(val onStart: () -> Unit, val onEnd: () -> Unit, val validator: (Transferable) -> T?, val handle: (T) -> Unit) : DragAndDropTarget {
    override fun onStarted(event: DragAndDropEvent) {
        onStart()
        super.onStarted(event)
    }

    override fun onEnded(event: DragAndDropEvent) {
        onEnd()
        super.onEnded(event)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDrop(event: DragAndDropEvent): Boolean {
        println("Action at the target: ${event.action}")

        return validator(event.awtTransferable)?.let {
            handle(it)
            true
        } ?: false
    }

    companion object {
        @OptIn(ExperimentalComposeUiApi::class)
        fun mkStudentTransferable(student: Student) = DragAndDropTransferable(StringSelection("com.jaytux.grader:student:${student.id.value}"))

        fun extractStudent(transf: Transferable): Student? {
            if(transf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                val raw = transf.getTransferData(DataFlavor.stringFlavor) as String
                val prefix = "com.jaytux.grader:student:"
                if(raw.startsWith(prefix)) {
                    val id = UUID.fromString(raw.removePrefix(prefix))
                    return transaction { Student.findById(id) }
                }
            }
            return null
        }
    }
}

@Composable
fun QuickGroup(idx: Int, group: EditionVM.GroupData, vm: EditionVM) {
    val focus by vm.focusIndex
    Surface(tonalElevation = if(focus == idx) 15.dp else 0.dp, shape = MaterialTheme.shapes.small) {
        Column(Modifier.fillMaxWidth().clickable { vm.focus(idx) }.padding(10.dp)) {
            Text(group.group.name, fontWeight = FontWeight.Bold)
            Text("${group.members.size} member(s)", Modifier.padding(start = 10.dp), fontStyle = FontStyle.Italic)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AvailableStudent(student: Student, group: Group, vm: EditionVM) {
    Row(Modifier.padding(10.dp).dragAndDropSource(
        drawDragDecoration = {},
    ) {
        DragAndDropTransferData(
            transferable = DDTarget.mkStudentTransferable(student),
            supportedActions = listOf(DragAndDropTransferAction.Move),
            dragDecorationOffset = it,
            onTransferCompleted = { act -> println("Source action: $act") }
        )
    }) {
        Text(student.name, Modifier.align(Alignment.CenterVertically).weight(1f), fontWeight = FontWeight.Bold)
        IconButton({ vm.addStudentToGroup(student, group, null) }) {
            Icon(CirclePlus, "Add ${student.name} to group")
        }
    }
}
