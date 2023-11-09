package net.braniumacademy.exercises515.data.repository

import net.braniumacademy.exercises515.data.model.Student

interface StudentRepository {
    fun loadStudent(): List<Student>
}