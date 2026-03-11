package com.jaytux.grader.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jaytux.grader.app
import com.jaytux.grader.data.v2.*
import com.jaytux.grader.ui.AssignmentsTabHeader
import com.jaytux.grader.ui.GroupsTabHeader
import com.jaytux.grader.ui.StudentsTabHeader
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock

class EditionVM(val edition: Edition, val course: Course) : ViewModel() {
    data class GroupData(val group: Group, val members: List<Pair<Student, String?>>)
    data class CriterionData(val criterion: Criterion, val gradeType: UiGradeType) {
        companion object {
            context(trns: Transaction)
            fun from(c: Criterion) = CriterionData(c, when(c.gradeType) {
                GradeType.CATEGORIC -> UiGradeType.Categoric(c.categoricGrade!!.options.toList(), c.categoricGrade!!)
                GradeType.NUMERIC -> UiGradeType.Numeric(c.numericGrade!!)
                GradeType.PERCENTAGE -> UiGradeType.Percentage
                GradeType.NONE -> UiGradeType.FreeText
            })
        }
    }
    data class AssignmentData(val assignment: BaseAssignment, val global: CriterionData, val criteria: List<CriterionData>)
    data class GradeSummary(val assignment: BaseAssignment, val asMember: Group?, val overridden: Boolean, val grade: Grade?)
    enum class Tab(val renderTab: @Composable () -> Unit, val addText: String) {
        STUDENTS(::StudentsTabHeader, "Student"),
        GROUPS(::GroupsTabHeader, "Group"),
        ASSIGNMENTS(::AssignmentsTabHeader, "Assignment")
    }

    val studentList = RawDbState { edition.students.sortedBy { it.name }.toList() }
    val groupList = RawDbState {
        edition.groups.with(Group::students, GroupStudent::student).sortedBy { it.name }.map {
            GroupData(it, it.students.map { gs -> gs.student to gs.role }.sortedBy { it.first.name })
        }
    }
    val assignmentList = RawDbState {
        edition.assignments.sortedBy { it.number }.map {
            AssignmentData(it, CriterionData.from(it.globalCriterion), it.nonBaseCriteria.map { c ->
                CriterionData.from(c)
            })
        }
    }
    val usedRoles = RawDbState {
        GroupStudents.select(GroupStudents.role).mapNotNull { it[GroupStudents.role] }.distinct()
    }

    val categoricGrades = RawDbState {
        CategoricGrade.all().map {
            UiGradeType.Categoric(it.options.toList(), it)
        }
    }

    val numericGrades = RawDbState {
        NumericGrade.all().map { UiGradeType.Numeric(it) }
    }

    val studentGrades = RawDbFocusableState { st: Student ->
        val groupIds = st.groups.map { it.group.id }.toSet()

        edition.assignments.map { asg ->
            val (grade, memberOf, override) = when(asg.type) {
                AssignmentType.GROUP -> {
                    val asGroup = asg.globalCriterion.feedbacks.find { it.asGroupFeedback?.id in groupIds }
                    val solo = null // asg.globalCriterion.feedbacks.find { it.forStudentsOverrideIfGroup.any { over -> over.student == st } } // TODO
                    val gr = (solo ?: asGroup)?.let { Grade.fromAssignment(asg.globalCriterion, it) }
                    gr to asGroup?.asGroupFeedback app (solo != null)
                }
                AssignmentType.SOLO -> {
                    val gr = asg.globalCriterion.feedbacks.find { it.asSoloFeedback == st }
                        ?.let { Grade.fromAssignment(asg.globalCriterion, it) }
                    gr to null app false
                }
                AssignmentType.PEER_EVALUATION -> {
                    val asGroup = asg.globalCriterion.feedbacks.find { it.asPeerEvaluationFeedback?.id in groupIds }
                    val solo = asg.globalCriterion.feedbacks.find { it.forStudentsOverrideIfPeer.any { over -> over.student == st } }
                    val gr = (solo ?: asGroup)?.let { Grade.fromAssignment(asg.globalCriterion, it) }
                    gr to asGroup?.asPeerEvaluationFeedback app (solo != null)
                }
            }

            GradeSummary(asg, memberOf, override, grade)
        }
    }
    val studentGroups = RawDbFocusableState { st: Student ->
        st.groups.map { it.group to it.role }
    }

    val groupAvailableStudents = RawDbFocusableState { grp: Group ->
        val exclude = grp.students.map { it.student.id }.toSet()
        edition.students.filterNot { it.id in exclude }
    }
    val groupGrades = RawDbFocusableState { g: Group ->
        edition.assignments.filter{ it.type != AssignmentType.SOLO }.map { asg ->
            val grade = when(asg.type) {
                AssignmentType.GROUP -> {
                    val asGroup = asg.globalCriterion.feedbacks.find { it.asGroupFeedback?.id == g.id }
                    asGroup?.let { Grade.fromAssignment(asg.globalCriterion, it) }
                }
                AssignmentType.PEER_EVALUATION -> {
                    val asGroup = asg.globalCriterion.feedbacks.find { it.asPeerEvaluationFeedback?.id == g.id }
                    asGroup?.let { Grade.fromAssignment(asg.globalCriterion, it) }
                }
                else -> null
            }

            asg to grade
        }
    }

    private val _selectedTab = mutableStateOf(Tab.STUDENTS)
    private val _focusIndex = mutableStateOf(-1)
    val selectedTab = _selectedTab.immutable()
    val focusIndex = _focusIndex.immutable()

    fun switchTo(tab: Tab) {
        _selectedTab.value = tab
        _focusIndex.value = -1
    }

    fun focus(idx: Int) {
        _focusIndex.value = idx

        when(_selectedTab.value) {
            Tab.STUDENTS -> {
                val st = studentList.entities.value[idx]
                studentGrades.focus(st)
                studentGroups.focus(st)
            }
            Tab.GROUPS -> {
                val grp = groupList.entities.value[idx].group
                groupAvailableStudents.focus(grp)
                groupGrades.focus(grp)
            }
            Tab.ASSIGNMENTS -> {}
        }
    }

    fun focus(group: Group) {
        val idx = groupList.entities.value.indexOfFirst { it.group.id == group.id }
        if(idx != -1) {
            switchTo(Tab.GROUPS)
            focus(idx)
        }
    }

    fun focus(student: Student) {
        val idx = studentList.entities.value.indexOfFirst { it.id == student.id }
        if(idx != -1) {
            switchTo(Tab.STUDENTS)
            focus(idx)
        }
    }

    fun unfocus() {
        _focusIndex.value = -1

        studentGrades.unfocus()
        studentGroups.unfocus()
    }

    fun mkStudent(name: String, contact: String, note: String) {
        transaction {
            val s = Student.new {
                this.name = name
                this.contact = contact
                this.note = note
            }
            EditionStudents.insert {
                it[EditionStudents.editionId] = edition.id
                it[EditionStudents.studentId] = s.id
            }
        }
        unfocus()
        studentList.refresh()
    }

    fun modStudent(student: Student, name: String?, contact: String?, note: String?) {
        transaction {
            student.name = name ?: student.name
            student.contact = contact ?: student.contact
            student.note = note ?: student.note
        }
        studentList.refresh()
        studentGroups.refresh()
        studentGrades.refresh()
    }

    fun rmStudent(student: Student) {
        transaction {
            student.delete()
        }
        unfocus()
        studentList.refresh()
    }

    fun mkGroup(name: String) {
        transaction {
            Group.new {
                this.name = name
                this.edition = this@EditionVM.edition
            }
        }
        unfocus()
        groupList.refresh()
    }

    fun modGroup(group: Group, name: String?) {
        transaction {
            group.name = name ?: group.name
        }
        groupList.refresh()
    }

    fun addStudentToGroup(student: Student, group: Group, role: String?) {
        transaction {
            GroupStudent.new {
                this.student = student
                this.group = group
                this.role = role
            }
        }
        groupList.refresh()
        studentGroups.refresh()
        groupAvailableStudents.refresh()
    }

    fun setStudentRole(student: Student, group: Group, role: String?) {
        transaction {
            GroupStudent.find { (GroupStudents.studentId eq student.id) and (GroupStudents.groupId eq group.id) }.firstOrNull()?.let {
                it.role = role
            }
        }
        groupList.refresh()
        groupAvailableStudents.refresh()
        usedRoles.refresh()
    }

    fun rmStudentFromGroup(student: Student, group: Group) {
        transaction {
            GroupStudent.find { (GroupStudents.studentId eq student.id) and (GroupStudents.groupId eq group.id) }.firstOrNull()?.delete()
        }
        groupList.refresh()
        groupAvailableStudents.refresh()
    }

    fun rmGroup(group: Group) {
        transaction {
            group.delete()
        }
        unfocus()
        groupList.refresh()
    }

    private fun Transaction.mkBaseAssignment(name: String, type: AssignmentType): BaseAssignment {
        val asg = BaseAssignment.new {
            this.name = name
            this.assignment = ""
            this.deadline = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            this.number = assignmentList.entities.value.size
            this.edition = this@EditionVM.edition
            this.type = type
        }

        val crit = Criterion.new {
            this.assignment = asg
            this.name = "(Default Criterion)"
            this.desc = "Default criterion for assignment ${asg.name}"
            this.gradeType = GradeType.NONE
        }

        asg.globalCriterion = crit
        return asg
    }

    private fun postCreateAsg() {
        focus(assignmentList.entities.value.size)
        assignmentList.refresh()
    }

    fun mkGroupAssignment(name: String) {
        transaction {
            val asg = mkBaseAssignment(name, AssignmentType.GROUP)
            GroupAssignment.new { this.base = asg }
        }
        postCreateAsg()
    }

    fun mkSoloAssignment(name: String) {
        transaction {
            val asg = mkBaseAssignment(name, AssignmentType.SOLO)
            SoloAssignment.new { this.base = asg }
        }
        postCreateAsg()
    }

    fun mkPeerEvaluation(name: String) {
        transaction {
            val asg = mkBaseAssignment(name, AssignmentType.PEER_EVALUATION)
            PeerEvaluation.new { this.base = asg }
        }
        postCreateAsg()
    }

    fun mkAssignment(name: String, type: AssignmentType) {
        when(type) {
            AssignmentType.GROUP -> mkGroupAssignment(name)
            AssignmentType.SOLO -> mkSoloAssignment(name)
            AssignmentType.PEER_EVALUATION -> mkPeerEvaluation(name)
        }
    }

    fun modAssignment(assignment: BaseAssignment, name: String?, deadline: LocalDateTime?) {
        transaction {
            assignment.name = name ?: assignment.name
            assignment.deadline = deadline ?: assignment.deadline
        }
        assignmentList.refresh()
    }

    fun setDesc(assignment: AssignmentData, desc: String) {
        transaction {
            assignment.global.criterion.desc = desc
        }
        assignmentList.refresh()
    }

    fun mkCriterion(assignment: BaseAssignment, name: String, desc: String, gradeType: UiGradeType) {
        transaction {
            val crit = Criterion.new {
                this.assignment = assignment
                this.name = name
                this.desc = desc
                this.gradeType = when(gradeType) {
                    is UiGradeType.Categoric -> GradeType.CATEGORIC
                    is UiGradeType.Numeric -> GradeType.NUMERIC
                    UiGradeType.Percentage -> GradeType.PERCENTAGE
                    UiGradeType.FreeText -> GradeType.NONE
                }
            }

            when(gradeType) {
                is UiGradeType.Categoric -> crit.categoricGrade = gradeType.grade
                is UiGradeType.Numeric -> crit.numericGrade = gradeType.grade
                else -> {}
            }
        }
        assignmentList.refresh()
    }

    fun modCriterion(crit: Criterion, name: String?, desc: String?, gradeType: UiGradeType?) {
        transaction {
            crit.name = name ?: crit.name
            crit.desc = desc ?: crit.desc
            crit.gradeType = when(gradeType) {
                null -> crit.gradeType
                is UiGradeType.Categoric -> GradeType.CATEGORIC
                is UiGradeType.Numeric -> GradeType.NUMERIC
                UiGradeType.Percentage -> GradeType.PERCENTAGE
                UiGradeType.FreeText -> GradeType.NONE
            }

            when(gradeType) {
                is UiGradeType.Categoric -> crit.categoricGrade = gradeType.grade
                is UiGradeType.Numeric -> crit.numericGrade = gradeType.grade
                else -> {}
            }
        }
        assignmentList.refresh()
    }

    fun mkScale(name: String, options: List<String>) {
        transaction {
            val grade = CategoricGrade.new { this.name = name }
            options.forEachIndexed { idx, opt ->
                CategoricGradeOption.new {
                    this.grade = grade
                    this.option = opt
                    this.index = idx
                }
            }
        }
        categoricGrades.refresh()
    }

    fun mkNumericScale(name: String, max: Double) {
        transaction {
            NumericGrade.new {
                this.name = name
                this.max = max
            }
        }
        numericGrades.refresh()
    }

    fun rmAssignment(assignment: BaseAssignment) {
        transaction {
            assignment.delete()
            (assignment.asPeerEvaluation ?: assignment.asGroupAssignment ?: assignment.asSoloAssignment)?.delete()
        }
        unfocus()
        assignmentList.refresh()
    }
}