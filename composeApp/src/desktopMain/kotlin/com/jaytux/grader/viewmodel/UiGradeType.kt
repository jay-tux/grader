package com.jaytux.grader.viewmodel

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jaytux.grader.data.v2.BaseFeedback
import com.jaytux.grader.data.v2.CategoricGrade
import com.jaytux.grader.data.v2.CategoricGradeOption
import com.jaytux.grader.data.v2.Criterion
import com.jaytux.grader.data.v2.GradeType
import com.jaytux.grader.data.v2.NumericGrade
import com.jaytux.grader.maxN
import org.jetbrains.exposed.v1.core.Transaction

sealed class UiGradeType {
    object FreeText : UiGradeType()
    object Percentage : UiGradeType()
    data class Numeric(val grade: NumericGrade) : UiGradeType()
    data class Categoric(val options: List<CategoricGradeOption>, val grade: CategoricGrade) : UiGradeType()
}