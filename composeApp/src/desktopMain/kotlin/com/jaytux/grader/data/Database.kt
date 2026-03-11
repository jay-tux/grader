package com.jaytux.grader.data

import com.jaytux.grader.app
import com.jaytux.grader.data.v2.CategoricGrade
import com.jaytux.grader.data.v2.CategoricGradeOption
import com.jaytux.grader.data.v2.CategoricGradeOptions
import com.jaytux.grader.data.v2.CategoricGrades
import com.jaytux.grader.data.v2.Courses
import com.jaytux.grader.data.v2.NumericGrade
import com.jaytux.grader.data.v2.v2Tables
import dev.dirs.ProjectDirectories
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.getValue
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager

object Database {
    val dataDir: String = ProjectDirectories.from("com", "jaytux", "grader").dataDir.also {
        val path = Path(it)
        if(!path.exists()) path.createDirectories()
    }
    val db by lazy {
        val actual = Database.connect("jdbc:sqlite:file:${dataDir}/grader.db", "org.sqlite.JDBC")
        transaction(actual) {
            SchemaUtils.create(*v2Tables)
        }

        actual
    }

    fun init() {
        TransactionManager.defaultDatabase = db
        transaction {
            if(CategoricGrade.count() == 0L) {
                val (pf, af) = CategoricGrades.batchInsert(listOf("Pass/Fail", "Default A-F"), shouldReturnGeneratedValues = true) {
                    this[CategoricGrades.name] = it
                }.map {
                    it[CategoricGrades.id]
                }

                CategoricGradeOptions.batchInsert(
                    listOf("Pass", "Fail").mapIndexed { idx, it -> it to pf app idx } +
                            listOf("A (Excellent)", "B (Good)", "C (Satisfactory)", "D (Poor)", "F (Fail)").mapIndexed { idx, it -> it to af app idx }
                ) {
                    this[CategoricGradeOptions.option] = it.first
                    this[CategoricGradeOptions.gradeId] = it.second
                    this[CategoricGradeOptions.index] = it.third
                }
            }

            if(NumericGrade.count() == 0L) {
                NumericGrade.new {
                    name = "Max-20"
                    max = 20.0
                }
            }
        }
    }
}