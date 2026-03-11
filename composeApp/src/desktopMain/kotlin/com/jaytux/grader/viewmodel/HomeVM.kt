package com.jaytux.grader.viewmodel

import androidx.lifecycle.ViewModel
import com.jaytux.grader.data.v2.BaseAssignment
import com.jaytux.grader.data.v2.Course
import com.jaytux.grader.data.v2.Edition
import com.jaytux.grader.data.v2.Group
import com.jaytux.grader.data.v2.Student
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class HomeVM : ViewModel() {
    data class EditionData(val edition: Edition, val students: List<Student>, val groups: List<Group>, val assignments: List<BaseAssignment>)
    data class CourseData(val course: Course, val editions: List<EditionData>, val archived: List<EditionData>)

    val courses = RawDbState {
        Course.all().with(Course::editions, Edition::students, Edition::groups, Edition::assignments).map {
            val mkEditionData = { e: Edition ->
                EditionData(e, e.students.toList(), e.groups.toList(), e.assignments.toList())
            }

            CourseData(it, it.editions.filter { e -> !e.archived }.map(mkEditionData), it.editions.filter { e -> e.archived }.map(mkEditionData))
        }
    }

    fun mkCourse(name: String) {
        transaction {
            Course.new { this.name = name }
        }
        courses.refresh()
    }

    fun rmCourse(course: Course) {
        transaction {
            course.delete()
        }
        courses.refresh()
    }

    fun mkEdition(course: Course, name: String) {
        transaction {
            Edition.new {
                this.course = course
                this.name = name
            }
        }
        courses.refresh()
    }

    fun rmEdition(edition: Edition) {
        transaction {
            edition.delete()
        }
        courses.refresh()
    }

    fun archiveEdition(edition: Edition) {
        transaction {
            edition.archived = true
        }
        courses.refresh()
    }

    fun unarchiveEdition(edition: Edition) {
        transaction {
            edition.archived = false
        }
        courses.refresh()
    }
}