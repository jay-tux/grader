package com.jaytux.grader.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jaytux.grader.data.v2.BaseAssignment
import com.jaytux.grader.data.v2.BaseFeedback
import com.jaytux.grader.data.v2.BaseFeedbacks
import com.jaytux.grader.data.v2.CategoricGrade
import com.jaytux.grader.data.v2.Course
import com.jaytux.grader.data.v2.Criterion
import com.jaytux.grader.data.v2.Edition
import com.jaytux.grader.data.v2.GradeType
import com.jaytux.grader.data.v2.Group
import com.jaytux.grader.data.v2.GroupAssignment
import com.jaytux.grader.data.v2.GroupFeedbacks
import com.jaytux.grader.data.v2.GroupStudent
import com.jaytux.grader.data.v2.NumericGrade
import com.jaytux.grader.data.v2.Student
import com.jaytux.grader.data.v2.StudentOverrideFeedback
import com.jaytux.grader.data.v2.StudentOverrideFeedbacks
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsertReturning

class GroupsGradingVM(val course: Course, val edition: Edition, val base: BaseAssignment) : ViewModel() {
    data class GroupData(val group: Group, val students: List<Pair<Student, String?>>)
    data class FeedbackItem(val base: BaseFeedback, val grade: Grade, val feedback: String) {
        companion object {
            context(trns: Transaction)
            fun fromDb(f: BaseFeedback): FeedbackItem = when(f.criterion.gradeType) {
                GradeType.CATEGORIC -> {
                    val categoric = f.criterion.categoricGrade!!
                    val options = categoric.options.toList()
                    Grade.Categoric(f.gradeCategoric ?: options.first(), options, categoric)
                }
                GradeType.NUMERIC -> Grade.Numeric(f.gradeNumeric ?: 0.0, f.criterion.numericGrade!!)
                GradeType.PERCENTAGE -> Grade.Percentage(f.gradeNumeric ?: 0.0)
                GradeType.NONE -> Grade.FreeText(f.gradeFreeText ?: "")
            }.let { FeedbackItem(f, it, f.feedback) }
        }
    }
    data class FeedbackData(val groupLevel: FeedbackItem?, val overrides: List<Pair<Student, FeedbackItem?>>)
    data class CritData(val criterion: Criterion, val cat: CategoricGrade?, val num: NumericGrade?) {
        companion object {
            context(trns: Transaction)
            fun fromDb(c: Criterion) = CritData(c, c.categoricGrade, c.numericGrade)
        }
    }

    private val _focus = mutableStateOf(-1)
    val focus = _focus.immutable()

    val asGroup = transaction { base.asGroupAssignment!! }
    val global = transaction { CritData.fromDb(base.globalCriterion) }
    val groupList = RawDbState {
        edition.groups.with(Group::students, GroupStudent::student).map { group ->
            GroupData(group, group.students.map { Pair(it.student, it.role) })
        }
    }

    val globalGrade = RawDbFocusableSingleState { group: Group ->
        val g = base.globalCriterion.feedbacks.find { it.asGroupFeedback?.id == group.id }?.let { FeedbackItem.fromDb(it) }
        val overrides = g?.let { gl -> getOverrides(group, gl.base) } ?: group.students.map { it.student to null }
        FeedbackData(g, overrides)
    }

    val gradeList = RawDbFocusableState { group: Group ->
        base.nonBaseCriteria.map { crit ->
            val groupLevel = crit.feedbacks.find { it.asGroupFeedback?.id == group.id }?.let { FeedbackItem.fromDb(it) }
            val overrides = groupLevel?.let { gl -> getOverrides(group, gl.base) } ?: group.students.map { it.student to null }

            CritData.fromDb(crit) to FeedbackData(groupLevel, overrides)
        }
    }

    private fun Transaction.getOverrides(group: Group, fd: BaseFeedback): List<Pair<Student, FeedbackItem?>> {
//        val feedbacks = fd.forStudentsOverrideIfGroup.filter { it.group.id == group.id }.associateBy { it.student.id }
//
//        return group.students.map {
//            it.student to feedbacks[it.student.id]?.let { sof -> FeedbackItem.fromDb(sof.feedback) }
//        }
        return listOf() // TODO!!!
    }

    fun focusGroup(idx: Int) {
        _focus.value = idx
        val group = groupList.entities.value[idx].group
        globalGrade.focus(group)
        gradeList.focus(group)
    }

    fun focusPrev() {
        if (focus.value > 0) {
            focusGroup(focus.value - 1)
        }
    }

    fun focusNext() {
        if (focus.value < groupList.entities.value.size - 1) {
            focusGroup(focus.value + 1)
        }
    }

    context(trns: Transaction)
    private fun BaseFeedback.set(grade: Grade, feedback: String) {
        this.feedback = feedback
        when(grade) {
            is Grade.Categoric -> this.gradeCategoric = grade.value
            is Grade.FreeText -> this.gradeFreeText = grade.text
            is Grade.Numeric -> this.gradeNumeric = grade.value
            is Grade.Percentage -> this.gradeNumeric = grade.percentage
        }
    }

    fun modGroupFeedback(crit: Criterion, group: Group, grade: Grade, feedback: String) {
        transaction {
            val existing = group.feedbacks.find { f -> f.criterion.id == crit.id }
            if(existing != null) {
                existing.set(grade, feedback)
            }
            else {
                val fdb = BaseFeedback.new {
                    criterion = crit
                    set(grade, feedback)
                }
                GroupFeedbacks.insert {
                    it[GroupFeedbacks.feedbackId] = fdb.id
                    it[GroupFeedbacks.groupId] = group.id
                }
            }
        }
        globalGrade.refresh()
        gradeList.refresh()
    }

    fun modOverrideFeedback(crit: Criterion, group: Group, student: Student, groupLevel: FeedbackItem, grade: Grade, feedback: String) {
        transaction {
            val existing = groupLevel.base.forStudentsOverrideIfGroup.find { it.student.id == student.id }
            if(existing != null) {
                existing.feedback.set(grade, feedback)
            }
            else {
                val fdb = BaseFeedback.new {
                    criterion = crit
                    set(grade, feedback)
                }
                StudentOverrideFeedback.new {
                    this.group = group
                    this.student = student
                    this.feedback = fdb
                    this.overrides = groupLevel.base
                }
            }
        }
        globalGrade.refresh()
        gradeList.refresh()
    }

    fun rmOverrideFeedback(crit: Criterion, group: Group, student: Student) {
        transaction {
            crit.feedbacks.find {
                it.asGroupFeedback!!.id == group.id // find relevant group-level feedback
            }?.forStudentsOverrideIfGroup?.find {
                it.student.id == student.id // find override for the student
            }?.delete()
        }
        globalGrade.refresh()
        gradeList.refresh()
    }
}
