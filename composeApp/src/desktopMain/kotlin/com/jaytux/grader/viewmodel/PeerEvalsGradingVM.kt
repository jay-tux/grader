package com.jaytux.grader.viewmodel

import androidx.lifecycle.ViewModel
import com.jaytux.grader.data.v2.BaseAssignment
import com.jaytux.grader.data.v2.Course
import com.jaytux.grader.data.v2.Edition

class PeerEvalsGradingVM(val course: Course, val edition: Edition, val base: BaseAssignment) : ViewModel() {
}