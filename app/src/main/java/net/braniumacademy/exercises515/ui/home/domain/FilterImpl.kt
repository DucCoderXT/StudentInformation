package net.braniumacademy.exercises515.ui.home.domain

import net.braniumacademy.exercises515.data.model.Student
import java.util.regex.Pattern

class FilterImpl : Filter {
    override fun findByName(students: List<Student>, name: String): List<Student> {
        val result = mutableListOf<Student>()
        for (student in students) {
            if (isMatch(student.fullName.firstName, name)) {
                result.add(student)
            }
        }
        return result
    }

    private fun isMatch(firstName: String, key: String?): Boolean {
        val pattern = ".*$key.*"
        val pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
        val matcher = pat.matcher(firstName)
        return matcher.matches()
    }

    override fun findById(students: List<Student>, id: String): List<Student> {
        val result = mutableListOf<Student>()
        for (student in students) {
            if (isMatch(student.studentId, id)) {
                result.add(student)
            }
        }
        return result
    }

    override fun findByGpa(students: List<Student>, gpa: Float): List<Student> {
        val result = mutableListOf<Student>()
        for (student in students) {
            if (student.gpa == gpa) {
                result.add(student)
            }
        }
        return result
    }
}