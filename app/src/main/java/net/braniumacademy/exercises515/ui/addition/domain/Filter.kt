package net.braniumacademy.exercises515.ui.addition.domain

import net.braniumacademy.exercises515.data.model.Student

interface Filter {
    fun isFieldEmpty(data: String): Boolean
    fun isCorrectDateFormat(data: String): Boolean
    fun isCorrectGpaFormat(data: String): Boolean
    fun isStudentExisted(id: String, students: List<Student>): Boolean
}