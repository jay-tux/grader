package com.jaytux.grader.viewmodel

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jaytux.grader.data.v2.BaseFeedback
import com.jaytux.grader.data.v2.CategoricGrade
import com.jaytux.grader.data.v2.CategoricGradeOption
import com.jaytux.grader.data.v2.CategoricGradeOptions
import com.jaytux.grader.data.v2.Criterion
import com.jaytux.grader.data.v2.GradeType
import com.jaytux.grader.data.v2.NumericGrade
import com.jaytux.grader.maxN
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

sealed class Grade {
    data class FreeText(val text: String) : Grade()
    data class Percentage(val percentage: Double) : Grade()
    data class Numeric(val value: Double, val grade: NumericGrade) : Grade()
    data class Categoric(val value: CategoricGradeOption, val options: List<CategoricGradeOption>, val grade: CategoricGrade) : Grade()

    @Composable
    fun render(modifier: Modifier = Modifier) = when(this) {
        is FreeText -> Text(text.maxN(15), modifier)
        is Categoric -> Text(value.option, modifier)
        is Numeric -> Text("$value / ${grade.max}", modifier)
        is Percentage -> Text("$percentage%", modifier)
    }

    companion object {
        context(trns: Transaction)
        fun fromAssignment(asg: Criterion, fdb: BaseFeedback): Grade = when(asg.gradeType) {
            GradeType.CATEGORIC ->
                Categoric(fdb.gradeCategoric!!, asg.categoricGrade!!.options.toList(), asg.categoricGrade!!)

            GradeType.NUMERIC -> Numeric(fdb.gradeNumeric!!, asg.numericGrade!!)
            GradeType.PERCENTAGE -> Percentage(fdb.gradeNumeric!!)
            GradeType.NONE -> FreeText(fdb.gradeFreeText!!)
        }

        fun defaultFreeText() = FreeText("")
        fun defaultPercentage() = Percentage(0.0)
        fun defaultNumeric(grade: NumericGrade) = Numeric(0.0, grade)
        fun defaultCategoric(grade: CategoricGrade, options: List<CategoricGradeOption>) = Categoric(options.first(), options, grade)

        fun default(type: GradeType, cat: CategoricGrade?, num: NumericGrade?) = when(type) {
            GradeType.CATEGORIC -> transaction {
                cat!!
                defaultCategoric(cat, cat.options.toList())
            }
            GradeType.NUMERIC -> defaultNumeric(num!!)
            GradeType.PERCENTAGE -> defaultPercentage()
            GradeType.NONE -> defaultFreeText()
        }
    }
}