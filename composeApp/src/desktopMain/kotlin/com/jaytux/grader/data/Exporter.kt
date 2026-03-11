package com.jaytux.grader.data

//class MdBuilder {
//    private val content = StringBuilder()
//
//    fun appendHeader(text: String, level: Int = 1) {
//        require(level in 1..6) { "Header level must be between 1 and 6" }
//        content.appendLine()
//        content.appendLine("#".repeat(level) + " $text")
//        content.appendLine()
//    }
//    fun appendMd(text: String) { content.appendLine(text) }
//    fun appendParagraph(text: String, bold: Boolean = false, italic: Boolean = false) {
//        val formattedText = buildString {
//            if (bold) append("**")
//            if (italic) append("_")
//            append(text)
//            if (italic) append("_")
//            if (bold) append("**")
//        }
//        content.appendLine(formattedText)
//        content.appendLine()
//    }
//
//    fun build(title: String, scheme: String = "dark"): String = "${prologue(title, scheme)}\n\n${content.toString()}"
//        private fun prologue(title: String, scheme: String = "dark") = """
//            ---
//            title: $title
//            date: ${Clock.System.now()}
//            header-includes:
//                - '<link rel="stylesheet" href="https://classless.de/classless-tiny.css" media="(prefers-color-scheme: $scheme)" />'
//                - '<link rel="stylesheet" href="https://classless.de/addons/themes.css" media="(prefers-color-scheme: light)" />'
//            ---
//        """.trimIndent()
//}
//
//object Exporter {
//    private fun MdBuilder.appendGroupFeedback(assignment: GroupAssignment, it: GroupAssignmentState.LocalGFeedback) {
//        appendHeader("${assignment.name} (group: ${it.group.name})", 1)
//        if (it.feedback.global != null && it.feedback.global.grade.isNotBlank()) {
//            val global = it.feedback.global.grade
//            appendParagraph("Overall grade: ${it.feedback.global.grade}", true, true)
//
//            it.individuals.forEach { (student, it) ->
//                val (_, data) = it
//                if (data.global != null && data.global.grade.isNotBlank() && data.global.grade != global) {
//                    appendParagraph("${student.name} grade: ${data.global.grade}", true, true)
//                }
//            }
//        }
//
//        fun appendFeedback(
//            heading: String,
//            group: GroupAssignmentState.FeedbackEntry?,
//            byStudent: List<Pair<Student, GroupAssignmentState.FeedbackEntry>>
//        ) {
//            if (group != null || byStudent.isNotEmpty()) {
//                appendHeader(heading, 2)
//                if (group != null) {
//                    if (group.grade.isNotBlank()) {
//                        appendParagraph("Group grade: ${group.grade}", true, true)
//                    }
//                    if (group.feedback.isNotBlank()) {
//                        appendMd(group.feedback)
//                    }
//                }
//
//                byStudent.forEach { (student, it) ->
//                    if (it.grade.isNotBlank() || it.feedback.isNotBlank()) appendHeader(student.name, 3)
//                    if (it.grade.isNotBlank()) {
//                        appendParagraph("Grade: ${it.grade}", true, true)
//                    }
//                    if (it.feedback.isNotBlank()) {
//                        appendMd(it.feedback)
//                    }
//                }
//            }
//        }
//
//        appendFeedback(
//            "Overall Feedback", it.feedback.global,
//            it.individuals.mapNotNull { ind -> ind.second.second.global?.let { g -> ind.first to g } }
//        )
//
//        val criteria = (it.feedback.byCriterion.map { (c, _) -> c } +
//                it.individuals.flatMap { (_, x) -> x.second.byCriterion.map { (c, _) -> c } }).distinctBy { x -> x.id.value }
//
//        criteria.forEach { c ->
//            appendFeedback(
//                c.name,
//                it.feedback.byCriterion.firstOrNull { it.criterion.id == c.id }?.entry,
//                it.individuals.mapNotNull { (student, s) ->
//                    val entry = s.second.byCriterion.firstOrNull { it.criterion.id == c.id }?.entry
//                    entry?.let { student to it }
//                }
//            )
//        }
//    }
//
//    private fun MdBuilder.outputTo(path: Path, title: String) {
//        val contents = build(title)
//        val buffer = Buffer()
//        buffer.write(contents.toByteArray())
//        SystemFileSystem.sink(path, false).write(buffer, buffer.size)
//    }
//
//    fun GroupAssignmentState.LocalGFeedback.exportTo(path: Path, assignment: GroupAssignment) {
//        val builder = MdBuilder()
//        builder.appendGroupFeedback(assignment, this)
//        builder.outputTo(path, "${assignment.name} (for group ${group.name})")
//    }
//
//    fun GroupAssignmentState.batchExport(dirPath: Path) {
//        feedback.entities.value.forEach { (_, it) ->
//            it.exportTo(dirPath / "${it.group.name} (${assignment.name}).md", assignment)
//        }
//    }
//}