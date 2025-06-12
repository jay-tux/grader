package com.jaytux.grader.data

import com.jaytux.grader.viewmodel.Assignment
import com.jaytux.grader.viewmodel.GroupAssignmentState
import io.github.vinceglb.filekit.PlatformFile

class MdBuilder {
    private val content = StringBuilder()

    fun appendHeader(text: String, level: Int = 1) {
        require(level in 1..6) { "Header level must be between 1 and 6" }
        content.appendLine()
        content.appendLine("#".repeat(level) + " $text")
        content.appendLine()
    }
    fun appendMd(text: String) { content.appendLine(text) }
    fun appendParagraph(text: String, bold: Boolean = false, italic: Boolean = false) {
        val formattedText = buildString {
            if (bold) append("**")
            if (italic) append("_")
            append(text)
            if (italic) append("_")
            if (bold) append("**")
        }
        content.appendLine(formattedText)
        content.appendLine()
    }

    fun build(): String = content.toString()
}

fun GroupAssignmentState.LocalGFeedback.exportTo(path: PlatformFile, assignment: GroupAssignment) {
    val builder = MdBuilder()
    builder.appendHeader("${assignment.name} Feedback for ${group.name}")
    if(feedback.global != null && feedback.global.grade.isNotBlank()) {
        val global = feedback.global.grade
        builder.appendParagraph("Overall grade: ${feedback.global.grade}", true, true)

        individuals.forEach { (student, it) ->
            val (_, data) = it
            if(data.global != null && data.global.grade.isNotBlank() && data.global.grade != global) {
                builder.appendParagraph("${student.name} grade: ${data.global.grade}", true, true)
            }
        }
    }

    fun appendFeedback(heading: String, group: GroupAssignmentState.FeedbackEntry?, byStudent: List<Pair<Student, GroupAssignmentState.FeedbackEntry>>) {
        if(group != null || byStudent.isNotEmpty()) {
            builder.appendHeader(heading, 2)
            if(group != null) {
                if(group.grade.isNotBlank()) {
                    builder.appendParagraph("Group grade: ${group.grade}", true, true)
                }
                if(group.feedback.isNotBlank()) {
                    builder.appendMd(group.feedback)
                }
            }

            byStudent.forEach { (student, it) ->
                if(it.grade.isNotBlank() || it.feedback.isNotBlank()) builder.appendHeader(student.name, 3)
                if(it.grade.isNotBlank()) {
                    builder.appendParagraph("Grade: ${it.grade}", true, true)
                }
                if(it.feedback.isNotBlank()) {
                    builder.appendMd(it.feedback)
                }
            }
        }
    }

    appendFeedback("Overall Feedback", feedback.global,
        individuals.mapNotNull { it.second.second.global?.let { g -> it.first to g } }
    )

    val criteria = (feedback.byCriterion.map { (c, _) -> c } +
        individuals.flatMap { (_, it) -> it.second.byCriterion.map { (c, _) -> c } }).distinctBy { it.id.value }

    criteria.forEach { c ->
        appendFeedback(
            c.name,
            feedback.byCriterion.firstOrNull { it.criterion.id == c.id }?.entry,
            individuals.mapNotNull { (student, it) ->
                val entry = it.second.byCriterion.firstOrNull { it.criterion.id == c.id }?.entry
                entry?.let { student to it }
            }
        )
    }

    path.file.writeText(builder.build())
}