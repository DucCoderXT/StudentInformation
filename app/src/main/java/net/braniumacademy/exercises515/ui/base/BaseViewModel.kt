package net.braniumacademy.exercises515.ui.base

import androidx.lifecycle.ViewModel
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.addition.domain.Filter
import net.braniumacademy.exercises515.ui.addition.domain.FilterImpl

open class BaseViewModel : ViewModel() {
    private val filter: Filter = FilterImpl()

    fun checkGpa(gpa: String): Boolean {
        return filter.isCorrectGpaFormat(gpa)
    }

    fun checkBirthDate(birthDate: String): Boolean {
        return filter.isCorrectDateFormat(birthDate)
    }

    fun checkStringIsEmpty(data: String): Boolean {
        return filter.isFieldEmpty(data)
    }

    fun checkStudentExisted(id: String, students: List<Student>): Boolean {
        return filter.isStudentExisted(id, students)
    }
}