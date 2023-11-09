package net.braniumacademy.exercises515.ui.home.domain

import net.braniumacademy.exercises515.data.model.Student

interface Sorting {
    fun sortByNameASC(students: MutableList<Student>)
    fun sortByNameDESC(students: MutableList<Student>)
    fun sortByGpaASC(students: MutableList<Student>)
    fun sortByGpaDESC(students: MutableList<Student>)
    fun sortByIdASC(students: MutableList<Student>)
    fun sortByIdDESC(students: MutableList<Student>)
}