package com.jaytux.grader.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.GroupGrading
import com.jaytux.grader.SoloGrading
import com.jaytux.grader.viewmodel.GroupsGradingVM
import com.jaytux.grader.viewmodel.Navigator
import com.jaytux.grader.viewmodel.SolosGradingVM

@Composable
fun SolosGradingTitle(data: SoloGrading) = Text("Courses / ${data.course.name} / ${data.edition.name} / Individual Assignments / ${data.assignment.name} / Grading")

@Composable
fun SolosGradingView(data: SoloGrading, token: Navigator.NavToken) {
    val vm = viewModel<SolosGradingVM>(key = data.assignment.id.toString()) {
        SolosGradingVM(data.course, data.edition, data.assignment)
    }
}