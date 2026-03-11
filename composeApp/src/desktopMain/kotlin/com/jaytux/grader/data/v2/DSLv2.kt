package com.jaytux.grader.data.v2

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.datetime

object Courses : UUIDTable("courses") {
    val name = varchar("name", 50).uniqueIndex()
}

object Editions : UUIDTable("editions") {
    val courseId = reference("course_id", Courses.id)
    val name = varchar("name", 50)
    val archived = bool("archived").default(false)

    init {
        uniqueIndex(courseId, name)
    }
}

object Groups : UUIDTable("groups") {
    val editionId = reference("edition_id", Editions.id)
    val name = varchar("name", 50)

    init {
        uniqueIndex(editionId, name)
    }
}

object Students : UUIDTable("students") {
    val name = varchar("name", 50)
    val contact = varchar("contact", 50)
    val note = text("note")
}

object GroupStudents : UUIDTable("grpStudents") {
    val groupId = reference("group_id", Groups.id)
    val studentId = reference("student_id", Students.id)
    val role = varchar("role", 50).nullable()

    init {
        uniqueIndex(groupId, studentId)
    }
}

object EditionStudents : CompositeIdTable("editionStudents") {
    val editionId = reference("edition_id", Editions.id)
    val studentId = reference("student_id", Students.id)

    override val primaryKey = PrimaryKey(editionId, studentId)
}

object BaseAssignments : UUIDTable("baseAssgmts") {
    val editionId = reference("edition_id", Editions.id)
    val name = varchar("name", 50)
    val assignment = text("assignment")
    val globalCriterion = reference("global_crit", Criteria.id)
    val deadline = datetime("deadline")
    val number = integer("number").nullable()
    val type = enumerationByName("type", 20, AssignmentType::class)
}

object Criteria : UUIDTable("criteria") {
    val assignmentId = reference("assignment_id", BaseAssignments.id)
    val name = varchar("name", 50)
    val desc = text("desc")
    val gradeType = enumerationByName("grade_type", 20, GradeType::class)
    val categoricGrade = reference("categoric_grade_id", CategoricGrades.id).nullable()
    val numericGrade = reference("numeric_grade_id", NumericGrades.id).nullable()
}

object GroupAssignments : UUIDTable("grpAssgmts") {
    val baseAssignmentId = reference("base_assignment_id", BaseAssignments.id).uniqueIndex()
}

object SoloAssignments : UUIDTable("soloAssgmts") {
    val baseAssignmentId = reference("base_assignment_id", BaseAssignments.id).uniqueIndex()
}

object BaseFeedbacks : UUIDTable("baseFeedbacks") {
    val criterionId = reference("criterion_id", Criteria.id)
    val feedback = text("feedback")
    val gradeFreeText = varchar("grade_text", 32).nullable()
    val gradeCategoric = reference("grade_categoric", CategoricGradeOptions.id).nullable()
    val gradeNumeric = double("grade_numeric").nullable()
}

object GroupFeedbacks : CompositeIdTable("grpFdbks") {
    val groupId = reference("group_id", Groups.id)
    val feedbackId = reference("feedback_id", BaseFeedbacks.id)

    override val primaryKey = PrimaryKey(groupId, feedbackId)
}

object StudentOverrideFeedbacks : UUIDTable("studOvrFdbks") {
    val groupId = reference("group_id", Groups.id)
    val studentId = reference("student_id", Students.id)
    val feedbackId = reference("feedback_id", BaseFeedbacks.id)
    val overrides = reference("overrides", GroupFeedbacks.feedbackId)
}

object SoloFeedbacks : CompositeIdTable("soloFdbks") {
    val studentId = reference("student_id", Students.id)
    val feedbackId = reference("feedback_id", BaseFeedbacks.id)

    override val primaryKey = PrimaryKey(studentId, feedbackId)
}

object PeerEvaluations : UUIDTable("peerEvals") {
    val baseAssignmentId = reference("base_assignment_id", BaseAssignments.id).uniqueIndex()
}

object PeerEvaluationFeedbacks : CompositeIdTable("peerEvalFdbks") {
    val groupId = reference("group_id", Groups.id)
    val feedbackId = reference("feedback_id", BaseFeedbacks.id)

    override val primaryKey = PrimaryKey(groupId, feedbackId)
}

object PeerEvaluationStudentOverrideFeedbacks : UUIDTable("peerEvalStudOvrFdbks") {
    val groupId = reference("group_id", Groups.id)
    val studentId = reference("student_id", Students.id)
    val feedbackId = reference("feedback_id", BaseFeedbacks.id)
    val overrides = reference("overrides", BaseFeedbacks.id)
}

object PeerEvaluationS2GEvaluations : UUIDTable("peerEvalS2GEvals") {
    val peerEvalId = reference("peer_eval_id", PeerEvaluations.id)
    val studentId = reference("student_id", Students.id)
    val groupId = reference("group_id", Groups.id)
    val evaluationId = reference("evaluation_id", BaseFeedbacks.id)

    init {
        uniqueIndex(peerEvalId, groupId, studentId)
    }
}

object PeerEvaluationS2SEvaluations : UUIDTable("peerEvalS2SEvals") {
    val peerEvalId = reference("peer_eval_id", PeerEvaluations.id)
    val studentId = reference("student_id", Students.id)
    val evaluatedStudentId = reference("evaluated_student_id", Students.id)
    val evaluationId = reference("evaluation_id", BaseFeedbacks.id)

    init {
        uniqueIndex(peerEvalId, studentId, evaluatedStudentId)
    }
}

object CategoricGrades : UUIDTable("categoricGrades") {
    val name = varchar("name", 50).uniqueIndex()
}

object CategoricGradeOptions : UUIDTable("categoricGradeOpts") {
    val gradeId = reference("grade_id", CategoricGrades.id)
    val option = varchar("option", 50)
    val index = integer("index")

    init {
        uniqueIndex(gradeId, option)
    }
}

object NumericGrades : UUIDTable("numericGrades") {
    val name = varchar("name", 50).uniqueIndex()
    val max = double("max")
}

enum class GradeType {
    CATEGORIC, NUMERIC, PERCENTAGE, NONE
}

enum class AssignmentType(val display: String) {
    GROUP("Group Assignment"), SOLO("Individual Assignment"), PEER_EVALUATION("Peer Evaluation")
}

val v2Tables = arrayOf(
    Courses, Editions, Groups, Students, GroupStudents, EditionStudents, BaseAssignments, Criteria, GroupAssignments,
    SoloAssignments, BaseFeedbacks, GroupFeedbacks, StudentOverrideFeedbacks, SoloFeedbacks, PeerEvaluations,
    PeerEvaluationFeedbacks, PeerEvaluationStudentOverrideFeedbacks, PeerEvaluationS2GEvaluations,
    PeerEvaluationS2SEvaluations, CategoricGrades, CategoricGradeOptions, NumericGrades
)