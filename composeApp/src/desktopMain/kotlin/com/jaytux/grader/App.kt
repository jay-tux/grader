package com.jaytux.grader

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.jaytux.grader.data.v2.BaseAssignment
import com.jaytux.grader.data.v2.Course
import com.jaytux.grader.data.v2.Edition
import com.jaytux.grader.ui.EditionTitle
import com.jaytux.grader.ui.EditionView
import com.jaytux.grader.ui.GroupsGradingTitle
import com.jaytux.grader.ui.GroupsGradingView
import com.jaytux.grader.ui.HomeTitle
import com.jaytux.grader.ui.HomeView
import com.jaytux.grader.ui.PeerEvalsGradingTitle
import com.jaytux.grader.ui.PeerEvalsGradingView
import com.jaytux.grader.ui.SolosGradingTitle
import com.jaytux.grader.ui.SolosGradingView
import com.jaytux.grader.viewmodel.Navigator

object Home : Navigator.IDestination
data class EditionDetail(val ed: Edition, val course: Course) : Navigator.IDestination
data class GroupGrading(val course: Course, val edition: Edition, val assignment: BaseAssignment) : Navigator.IDestination
data class SoloGrading(val course: Course, val edition: Edition, val assignment: BaseAssignment) : Navigator.IDestination
data class PeerEvalGrading(val course: Course, val edition: Edition, val assignment: BaseAssignment) : Navigator.IDestination

@Composable
fun App() {
    MaterialTheme {
        Navigator.NavHost(Home) {
            composable<Home>({ HomeTitle() }) { _, token -> HomeView(token) }
            composable<EditionDetail>({ EditionTitle(it) }) { data, token -> EditionView(data, token) }
            composable<GroupGrading>({ GroupsGradingTitle(it) }) { data, token -> GroupsGradingView(data, token) }
            composable<SoloGrading>({ SolosGradingTitle(it) }) { data, token -> SolosGradingView(data, token) }
            composable<PeerEvalGrading>({ PeerEvalsGradingTitle(it) }) { data, token -> PeerEvalsGradingView(data, token) }
        }
    }
}