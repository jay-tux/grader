package com.jaytux.grader.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.GroupGrading
import com.jaytux.grader.PeerEvalGrading
import com.jaytux.grader.viewmodel.GroupsGradingVM
import com.jaytux.grader.viewmodel.Navigator
import com.jaytux.grader.viewmodel.PeerEvalsGradingVM

@Composable
fun PeerEvalsGradingTitle(data: PeerEvalGrading) = Text("Courses / ${data.course.name} / ${data.edition.name} / Peer Evaluations / ${data.assignment.name} / Grading")

@Composable
fun PeerEvalsGradingView(data: PeerEvalGrading, token: Navigator.NavToken) {
    val vm = viewModel<PeerEvalsGradingVM>(key = data.assignment.id.toString()) {
        PeerEvalsGradingVM(data.course, data.edition, data.assignment)
    }
}