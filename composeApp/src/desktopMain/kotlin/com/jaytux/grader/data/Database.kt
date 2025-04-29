package com.jaytux.grader.data

import MigrationUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Database {
    val db by lazy {
        val actual = Database.connect("jdbc:sqlite:file:./grader.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(
                Courses, Editions, Groups,
                Students, GroupStudents, EditionStudents,
                GroupAssignments, SoloAssignments, GroupAssignmentCriteria, SoloAssignmentCriteria,
                GroupFeedbacks, IndividualFeedbacks, SoloFeedbacks,
                PeerEvaluations, PeerEvaluationContents, StudentToStudentEvaluation,
                StudentToGroupEvaluation
            )

            val migrate = MigrationUtils.statementsRequiredForDatabaseMigration(
                Courses, Editions, Groups,
                Students, GroupStudents, EditionStudents,
                GroupAssignments, SoloAssignments, GroupAssignmentCriteria, SoloAssignmentCriteria,
                GroupFeedbacks, IndividualFeedbacks, SoloFeedbacks,
                PeerEvaluations, PeerEvaluationContents, StudentToStudentEvaluation,
                StudentToGroupEvaluation,
                withLogs = true
            )

            println(" --- Migration --- ")
            migrate.forEach { println(it); exec(it) }
            println(" --- End migration --- ")
        }
        actual
    }

    fun init() { db }
}