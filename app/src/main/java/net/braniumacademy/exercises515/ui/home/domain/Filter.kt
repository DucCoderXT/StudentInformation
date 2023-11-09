package net.braniumacademy.exercises515.ui.home.domain

import net.braniumacademy.exercises515.data.model.Student

interface Filter {
    fun findByName(students: List<Student>, name: String): List<Student>
    fun findById(students: List<Student>, id: String): List<Student>
    fun findByGpa(students: List<Student>, gpa: Float): List<Student>
}