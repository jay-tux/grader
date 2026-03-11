package com.jaytux.grader.data.v2

import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

class Course(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Course>(Courses)

    var name by Courses.name

    val editions by Edition referrersOn Editions.courseId orderBy Editions.name
}

class Edition(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, Edition>(Editions)

    var course by Course referencedOn Editions.courseId
    var name by Editions.name
    var archived by Editions.archived

    val students by Student via EditionStudents orderBy Students.name
    val groups by Group referrersOn Groups.editionId orderBy Groups.name
    val assignments by BaseAssignment referrersOn BaseAssignments.editionId orderBy BaseAssignments.number
}

class Group(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, Group>(Groups)

    var edition by Edition referencedOn Groups.editionId
    var name by Groups.name

    val students by GroupStudent referrersOn GroupStudents.groupId
    val feedbacks by BaseFeedback via GroupFeedbacks
}

class Student(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, Student>(Students)

    var name by Students.name
    var note by Students.note
    var contact by Students.contact

    val editions by Edition via EditionStudents orderBy Editions.name
    val groups by GroupStudent referrersOn GroupStudents.studentId
}

class GroupStudent(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, GroupStudent>(GroupStudents)

    var student by Student referencedOn GroupStudents.studentId
    var group by Group referencedOn GroupStudents.groupId
    var role by GroupStudents.role
}

class BaseAssignment(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, BaseAssignment>(BaseAssignments)

    var name by BaseAssignments.name
    var assignment by BaseAssignments.assignment
    var globalCriterion by Criterion referencedOn BaseAssignments.globalCriterion
    var deadline by BaseAssignments.deadline
    var number by BaseAssignments.number
    var edition by Edition referencedOn BaseAssignments.editionId
    var type by BaseAssignments.type

    private val _asGroupAssignment by GroupAssignment referrersOn GroupAssignments.baseAssignmentId
    private val _asSoloAssignment by SoloAssignment referrersOn SoloAssignments.baseAssignmentId
    private val _asPeerEvaluation by PeerEvaluation referrersOn PeerEvaluations.baseAssignmentId
    val asGroupAssignment get() = _asGroupAssignment.singleOrNull()
    val asSoloAssignment get() = _asSoloAssignment.singleOrNull()
    val asPeerEvaluation get() = _asPeerEvaluation.singleOrNull()

    val criteria by Criterion referrersOn Criteria.assignmentId orderBy Criteria.name
    val nonBaseCriteria get() = criteria.filterNot { it.id.value == globalCriterion.id.value }
}

class GroupAssignment(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, GroupAssignment>(GroupAssignments)

    var base by BaseAssignment referencedOn GroupAssignments.baseAssignmentId
}

class SoloAssignment(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, SoloAssignment>(SoloAssignments)

    var base by BaseAssignment referencedOn SoloAssignments.baseAssignmentId
}

class PeerEvaluation(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, PeerEvaluation>(PeerEvaluations)

    var base by BaseAssignment referencedOn PeerEvaluations.baseAssignmentId
}

class CategoricGrade(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, CategoricGrade>(CategoricGrades)

    var name by CategoricGrades.name

    val options by CategoricGradeOption referrersOn CategoricGradeOptions.gradeId orderBy CategoricGradeOptions.index
}

class CategoricGradeOption(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, CategoricGradeOption>(CategoricGradeOptions)

    var grade by CategoricGrade referencedOn CategoricGradeOptions.gradeId
    var option by CategoricGradeOptions.option
    var index by CategoricGradeOptions.index
}

class NumericGrade(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, NumericGrade>(NumericGrades)

    var name by NumericGrades.name
    var max by NumericGrades.max
}

class Criterion(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, Criterion>(Criteria)

    var assignment by BaseAssignment referencedOn Criteria.assignmentId
    var name by Criteria.name
    var desc by Criteria.desc
    var gradeType by Criteria.gradeType
    var categoricGrade by CategoricGrade optionalReferencedOn Criteria.categoricGrade
    var numericGrade by NumericGrade optionalReferencedOn Criteria.numericGrade

    val feedbacks by BaseFeedback referrersOn BaseFeedbacks.criterionId
}

class BaseFeedback(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, BaseFeedback>(BaseFeedbacks)

    var criterion by Criterion referencedOn BaseFeedbacks.criterionId
    var feedback by BaseFeedbacks.feedback
    var gradeFreeText by BaseFeedbacks.gradeFreeText
    var gradeCategoric by CategoricGradeOption optionalReferencedOn BaseFeedbacks.gradeCategoric
    var gradeNumeric by BaseFeedbacks.gradeNumeric

    private val _forStudentIfSolo by Student via SoloFeedbacks
    private val _forGroupIfGroup by Group via GroupFeedbacks
    private val _forGroupIfPeer by Group via PeerEvaluationFeedbacks

    val asSoloFeedback get() = _forStudentIfSolo.singleOrNull()
    val asGroupFeedback get() = _forGroupIfGroup.singleOrNull()
    val asPeerEvaluationFeedback get() = _forGroupIfPeer.singleOrNull()

    val forStudentsOverrideIfGroup by StudentOverrideFeedback referrersOn StudentOverrideFeedbacks.overrides
    val forStudentsOverrideIfPeer by PeerEvaluationStudentOverrideFeedback referrersOn PeerEvaluationStudentOverrideFeedbacks.overrides
}

class StudentOverrideFeedback(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, StudentOverrideFeedback>(StudentOverrideFeedbacks)

    var group by Group referencedOn StudentOverrideFeedbacks.groupId
    var student by Student referencedOn StudentOverrideFeedbacks.studentId
    var feedback by BaseFeedback referencedOn StudentOverrideFeedbacks.feedbackId
    var overrides by BaseFeedback referencedOn StudentOverrideFeedbacks.overrides
}

class PeerEvaluationStudentOverrideFeedback(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, PeerEvaluationStudentOverrideFeedback>(PeerEvaluationStudentOverrideFeedbacks)

    var group by Group referencedOn PeerEvaluationStudentOverrideFeedbacks.groupId
    var student by Student referencedOn PeerEvaluationStudentOverrideFeedbacks.studentId
    var feedback by BaseFeedback referencedOn PeerEvaluationStudentOverrideFeedbacks.feedbackId
    var overrides by BaseFeedback referencedOn PeerEvaluationStudentOverrideFeedbacks.overrides
}

class PeerEvaluationS2G(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, PeerEvaluationS2G>(PeerEvaluationS2GEvaluations)

    var peerEvaluation by PeerEvaluation referencedOn PeerEvaluationS2GEvaluations.peerEvalId
    var student by Student referencedOn PeerEvaluationS2GEvaluations.studentId
    var group by Group referencedOn PeerEvaluationS2GEvaluations.groupId
    var evaluation by BaseFeedback referencedOn PeerEvaluationS2GEvaluations.evaluationId
}

class PeerEvaluationS2S(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, PeerEvaluationS2S>(PeerEvaluationS2SEvaluations)

    var peerEvaluation by PeerEvaluation referencedOn PeerEvaluationS2SEvaluations.peerEvalId
    var student by Student referencedOn PeerEvaluationS2SEvaluations.studentId
    var evaluatedStudent by Student referencedOn PeerEvaluationS2SEvaluations.evaluatedStudentId
    var evaluation by BaseFeedback referencedOn PeerEvaluationS2SEvaluations.evaluationId
}