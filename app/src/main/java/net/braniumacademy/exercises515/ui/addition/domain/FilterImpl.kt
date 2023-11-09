package net.braniumacademy.exercises515.ui.addition.domain

import net.braniumacademy.exercises515.data.model.Student
import java.util.regex.Pattern

class FilterImpl : Filter {
    override fun isFieldEmpty(data: String): Boolean {
        return data.trim { it <= ' ' }.isEmpty()
    }

    override fun isCorrectDateFormat(data: String): Boolean {
        val format = "\\d{2}/\\d{2}/\\d{4}"
        val pattern = Pattern.compile(format)
        val matcher = data.let { pattern.matcher(it) }
        return matcher.matches()
    }

    override fun isCorrectGpaFormat(data: String): Boolean {
        val format = "\\d.\\d{1,2}"
        val pattern = Pattern.compile(format)
        val matcher = pattern.matcher(data)
        if (matcher.matches()) {
            val gpa = data.toFloat()
            return gpa in 0.0..4.0
        }
        return false
    }

    override fun isStudentExisted(id: String, students: List<Student>): Boolean {
        val student = Student(studentId = id)
        return students.contains(student)
    }
}